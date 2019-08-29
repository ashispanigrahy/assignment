package com.me.ice.fileutil;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class Problem2Test {

    private static Logger logger = Logger.getLogger(Problem2.class);

    private static String filePath1 = "problem2File1.txt";
    private static String filePath2 = "problem2File2.txt";
    private static String mergedFilePath = "mergedFile.txt";


    private String newLine = System.getProperty("line.separator");

    @Before
    public void setUp() {

        long startTime = System.currentTimeMillis();
        FileOutputStream op1 = null;
        FileOutputStream op2 = null;
        DataOutputStream dos1 = null;
        DataOutputStream dos2 = null;
        try {
            op1 = new FileOutputStream(filePath1);
            op2 = new FileOutputStream(filePath2);
            dos1 = new DataOutputStream(op1);
            dos2 = new DataOutputStream(op2);
            writeToFile(dos1, "A");
            writeToFile(dos1, "D");
            writeToFile(dos1, "E");
            writeToFile(dos1, "F");
            writeToFile(dos1, "J");
            writeToFile(dos2, "B");
            writeToFile(dos2, "C");
            writeToFile(dos2, "G");
            writeToFile(dos2, "G");
            writeToFile(dos2, "H");
            writeToFile(dos2, "I");

        } catch (FileNotFoundException e) {
            logger.error("File not found.", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        } finally {
            try {
                if (op1 != null) {
                    op1.close();
                }
                if (op2 != null) {
                    op2.close();
                }
                if (dos1 != null) {
                    dos1.close();
                }
                if (dos2 != null) {
                    dos2.close();
                }
            } catch (IOException e) {
                logger.error("Exception while closing IO objects.");
            }
        }
    }

    @After
    public void burnDown() {
        deleteFile(filePath1);
        deleteFile(filePath2);
    }

    private void deleteFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            logger.error("Exception while deleting the file.", e);
        }
    }

    private void writeToFile(DataOutputStream dataOutputStream, String stringToWrite) throws IOException {
        byte[] data = stringToWrite.getBytes(StandardCharsets.UTF_8);
        dataOutputStream.write(data);
        dataOutputStream.writeBytes(newLine);
    }

    @Test
    public void checkAndProcessFileTest() {

        Problem2 problem2 = new Problem2();
        String resultFilePath = "";
        FileInputStream fisFirstFile = null;
        Scanner scannerFirst = null;
        try {
            resultFilePath = problem2.checkAndProcessFile(filePath1, filePath2);
            fisFirstFile = new FileInputStream(resultFilePath);
            scannerFirst = new Scanner(fisFirstFile, StandardCharsets.UTF_8);
            int i = 0;
            while (scannerFirst.hasNextLine()) {
                String nextLine = scannerFirst.nextLine();
                if (i == 2) {
                    Assert.assertEquals("C", nextLine);
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            logger.error("File not found", e);
        } finally {
            try {
                if (fisFirstFile != null) {
                    fisFirstFile.close();
                }
                if (scannerFirst != null) {
                    scannerFirst.close();
                }
            } catch (IOException e) {
                logger.error("Exception while closing IO objects.", e);
            }
            deleteFile(resultFilePath);
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void checkAndProcessFileTestException() throws FileNotFoundException {
        Problem2 problem2 = new Problem2();
        problem2.checkAndProcessFile("test1.txt", "test2.txt");
    }
}
