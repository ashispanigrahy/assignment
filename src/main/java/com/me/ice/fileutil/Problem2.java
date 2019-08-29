package com.me.ice.fileutil;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.apache.log4j.Logger;

/**
 * <h1>Problem2.java</h1>
 * The problem2.java program takes the paths of two files multiple lines if strings in a sorted array.
 * <p>
 * Once the program detects the files exists then it will read line by line without
 * loading the entire file to memory.
 * <p>
 * Which will help to read a bigger file with smaller memory.
 * <p>
 *
 * <h2>Problem Statement</h2>
 * <p>
 * Given two sorted files, write a Java program to merge them to preserve sort order. You can assume you can compare two lines of a file as strings.
 * DO NOT assume either of these files can fit in memory!
 * </p>
 *
 * <h2>Solution Ideas!!!</h2>
 * <p>
 * Reading 1st lines of each file and compare them after null check.
 * If 1st file has value and 2nd doesn't then the new file will have value from 1st file.
 * If 2nd file has value and 1st doesn't then the new file will have value from 2nd file.
 * <p>
 * If both file have value then the comparator method of string is used to detect which string should come 1st.
 * If the value is less than or equal then 1st file's string will be written to the new file else the data from 2nd file.
 *
 *
 *
 * </p>
 *
 * @author ashis
 * Created on 28th August 2019
 */
public class Problem2 {

    private static Logger logger = Logger.getLogger(Problem2.class);
    private String mergedFilePath = "mergedFile.txt";
    private String newLine = System.getProperty("line.separator");


    public String checkAndProcessFile(String firstFilePath, String secondFilePath) throws FileNotFoundException {

        if (!new File(firstFilePath).exists()) {
            throw new FileNotFoundException("File \"" + firstFilePath + "\" not found.");
        }
        if (!new File(secondFilePath).exists()) {
            throw new FileNotFoundException("File \"" + secondFilePath + "\" not found.");
        }

        return this.compareAndMergeFiles(firstFilePath, secondFilePath);
    }

    private String compareAndMergeFiles(String firstFilePath, String secondFilePath) {

        FileInputStream fisFirstFile = null;
        FileInputStream fisSecondFile = null;
        Scanner scannerFirst = null;
        Scanner scannerSecond = null;
        FileOutputStream op = null;
        DataOutputStream dos = null;
        try {
            fisFirstFile = new FileInputStream(firstFilePath);
            fisSecondFile = new FileInputStream(secondFilePath);
            scannerFirst = new Scanner(fisFirstFile, StandardCharsets.UTF_8);
            scannerSecond = new Scanner(fisSecondFile, StandardCharsets.UTF_8);

            op = new FileOutputStream(mergedFilePath);
            dos = new DataOutputStream(op);

            String lineFromFirstFile = scannerFirst.nextLine();
            String lineFromSecondFile = scannerSecond.nextLine();

            while (lineFromFirstFile != null || lineFromSecondFile != null) {

                if (lineFromSecondFile == null) {
                    writeToResultFile(dos, lineFromFirstFile);
                    lineFromFirstFile = readNextLine(scannerFirst);
                } else if (lineFromFirstFile == null) {
                    writeToResultFile(dos, lineFromSecondFile);
                    lineFromSecondFile = readNextLine(scannerSecond);
                } else if (lineFromFirstFile.compareTo(lineFromSecondFile) <= 0) {
                    writeToResultFile(dos, lineFromFirstFile);
                    lineFromFirstFile = readNextLine(scannerFirst);
                } else {
                    writeToResultFile(dos, lineFromSecondFile);
                    lineFromSecondFile = readNextLine(scannerSecond);
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("File not found error.", e);
        } catch (IOException e) {
            logger.error("Exception while processing the file.", e);
        } finally {
            try {
                if (fisFirstFile != null) {
                    fisFirstFile.close();
                }
                if (fisSecondFile != null) {
                    fisSecondFile.close();
                }
                if (scannerFirst != null) {
                    scannerFirst.close();
                }
                if (scannerSecond != null) {
                    scannerSecond.close();
                }
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
        return mergedFilePath;
    }

    private void writeToResultFile(DataOutputStream dos, String lineFromSecondFile) throws IOException {
        byte[] data = lineFromSecondFile.getBytes(StandardCharsets.UTF_8);

        dos.write(data);
        dos.writeBytes(newLine);
    }

    private static String readNextLine(Scanner scanner) {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        } else {
            return null;
        }
    }


}
