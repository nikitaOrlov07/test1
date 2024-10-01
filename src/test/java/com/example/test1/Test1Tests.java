package com.example.test1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;



@SpringBootTest
public class Test1Tests {
    @Test
    public void testRunWithPrintOperation() {
        // Setting
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Execution
        String[] args = {"print"};
        Test1Application.main(args); // call Test1Application main method
        System.setOut(originalOut);

        // Assertions
        Assertions.assertNotNull(outContent.toString().trim());
    }
    @Test
    public void testRunWithFindMaxOperation() {
        // Setting
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Execution
        String[] args = {"findMax"};
        Test1Application.main(args); // call Test1Application main method
        System.setOut(originalOut);

        // Get last String from terminal
        String[] outputLines = outContent.toString().split(System.lineSeparator());
        String actualResult = outputLines[outputLines.length - 1].trim();

        // Assertion
        String expectedResult = "vegetables -> carrot -> red: 21";
        Assertions.assertEquals(expectedResult, actualResult);
    }

}
