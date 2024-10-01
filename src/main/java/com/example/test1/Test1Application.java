package com.example.test1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.io.File;
import java.io.IOException;


@SpringBootApplication
@Slf4j
public class Test1Application {
    public static void main(String[] args) {
        SpringApplication.run(Test1Application.class, args);
    }
    @Bean
    public CommandLineRunner run()
    {
     return args -> {                  // take args from main method
         // check arguments not empty
         if(args.length == 0)
         {
             System.out.println("You need to specify operation");
             return;
         }
         String operation  = args[0];  // operation can be print or findMax
         String path = "project.json";
         // Check operation value
         if(operation == null || (!operation.equals("print")) && !operation.equals("findMax"))
         {
             System.out.println("Operation must be \"read\" or  \"findMax\"");
             return; // exit from program
         }
         ObjectMapper mapper = new ObjectMapper();
         JsonNode jsonFile = readFile("project.json",mapper); // Json tree
         // Check if returned JsonNode object is null
         if(jsonFile == null)
         {
             System.out.println("File with path " + path+ "was not found");
             return ; // exit from program
         }
         switch (operation)
         {
             case "print":
                 log.info("print operation is executed");
                 printFile(jsonFile,"");
                 break;
             case "findMax":
                 log.info("findMax operation is executed");
                 findMax(jsonFile);
                 break;
         }
     };
    }

    // Method for reading json file
    private JsonNode readFile(String filePath, ObjectMapper mapper) {
        try {
            log.info("Reading file with path : " + filePath);
            return mapper.readTree(new File(filePath));
        } catch (IOException e) {
            System.out.println("Error loading JSON file: " + e.getMessage());
            return null;
        }
    }
    // Method for printing JSON file
    private void printFile(JsonNode node, String indent) {
        if (node.isObject()) {
            node.fields().forEachRemaining(entry -> {
                System.out.println(indent + entry.getKey());
                printFile(entry.getValue(), indent + "..");
            });
        } else if (node.isArray()) {
            for (JsonNode element : node) {
                printFile(element, indent + "..");
            }
        }
    }
    // Method for finding Max value

    private void findMax(JsonNode node) {
        MaxValue maxValue = new MaxValue();
        findMaxRecursive(node, "", maxValue);
        if (maxValue.value != Integer.MIN_VALUE) {
            System.out.println(maxValue.category + " -> " + maxValue.item + " -> " + maxValue.color + ": " + maxValue.value);
        } else {
            System.out.println("No maximum value found.");
        }
    }

    private void findMaxRecursive(JsonNode node, String path, MaxValue maxValue) {
        if (node.isObject()) {
            node.fields().forEachRemaining(entry -> {
                String newPath = path.isEmpty() ? entry.getKey() : path + " -> " + entry.getKey();
                findMaxRecursive(entry.getValue(), newPath, maxValue);
            });
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                findMaxRecursive(node.get(i), path + "[" + i + "]", maxValue);
            }
        } else if (node.isInt()) {
            int value = node.asInt();
            if (value > maxValue.value) {
                String[] pathParts = path.split(" -> ");
                maxValue.category = pathParts[0];
                maxValue.color = pathParts[pathParts.length - 1];
                maxValue.item = pathParts.length > 1 ? pathParts[1] : "";
                maxValue.value = value;
            }
        }
    }

}
