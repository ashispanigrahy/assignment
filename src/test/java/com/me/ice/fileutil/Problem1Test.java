package com.me.ice.fileutil;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class Problem1Test {

    private static Logger logger = Logger.getLogger(Problem1.class);

    private static String filePath = "problem1.txt";

    @Before
    public void setUp() {

        long startTime = System.currentTimeMillis();
        FileOutputStream op = null;
        DataOutputStream dos = null;
        try {
            op = new FileOutputStream(filePath);
            dos = new DataOutputStream(op);
            List<String> ids = new ArrayList<>();
            String newLine = System.getProperty("line.separator");
            for (int i = 0; i < 10; i++) {
                ids.add(RandomStringUtils.random(8, true, false));
            }
            for (int i = 0, k = 0; k < 10; i++) {
                byte[] data = ids.get(i).getBytes(StandardCharsets.UTF_8);
                dos.write(data);
                dos.writeBytes(newLine);
                for (int j = 0; j < 10; j++) {
                    String val = RandomStringUtils.random(5, false, true);
                    byte[] price = val.getBytes(StandardCharsets.UTF_8);
                    dos.write(price);
                    dos.writeBytes(newLine);
                }
                if (i >= 9) {
                    i = 0;
                    k++;
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("File not found.", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        } finally {
            try {
                if (op != null) {
                    op.close();
                }
                if (dos != null) {
                    dos.close();
                }
            } catch (IOException e) {
                logger.error("Exception while closing IO objects.");
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("TotalTime to create a file = " + (endTime - startTime) + "ms");


    }

    @After
    public void burnDown() {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            logger.error("Exception while deleting the file.", e);
        }
    }

    @Test
    public void readFileTest() {

        Problem1 problem1 = new Problem1();
        try {
            problem1.checkAndProcessFile(filePath);
        } catch (FileNotFoundException e) {
            logger.error("File not found", e);
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void readFileTestForException() throws FileNotFoundException {
        Problem1 problem1 = new Problem1();
        problem1.checkAndProcessFile("test.txt");
    }
}
