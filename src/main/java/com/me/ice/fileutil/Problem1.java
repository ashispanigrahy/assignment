package com.me.ice.fileutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


/**
 * <h1>Problem1.java</h1>
 * The problem1.java program takes the path of to a file with the required formatted.
 * <p>
 * Once the program detects the file is exists then it will read line by line without
 * loading the file to memory.
 * <p>
 * Which will help to read a bigger file with smaller memory.
 *
 * <h2>Problem Statement</h2>
 * <p>
 * You are given a file formatted like this:
 * <p>
 * CUSIP1
 * Price
 * Price
 * Price
 * …
 * CUSIP2
 * Price
 * Price
 * CUSIP
 * Price
 * Price
 * Price
 * …
 * Price
 * CUSIP3
 * Price
 * …
 * <p>
 * Think of it as a file of price ticks for a set of bonds identified by their CUSIPs.
 * You can assume a CUSIP is just an 8-character alphanumeric string.
 * Each CUSIP may have any number of prices (e.g., 95.752, 101.255) following it in
 * sequence, one per line.
 * The prices can be considered to be ordered by time in ascending order, earliest to latest.
 * <p>
 * Write me a Java program that will print the closing (or latest) price for each CUSIP in the file.
 * DO NOT assume the entire file can fit in memory!
 *
 * </p>
 *
 * <h2>Solution Ideas!!!</h2>
 * <p>
 * If the value of a line is not converted to BigDecimal type then I assume it is a CUSTOMER ID.
 * If the value of a line is converted to BigDecimal then it is a price.
 * <p>
 * Once we get a ID, then it is saved to a lastId value which will be used to map all subsequent price values.
 * All price are not saved to the HashMap as we are only concerned about the latest price.
 *
 * @author ashis
 * Created on 28th August 2019
 */
public class Problem1 {

    private static Logger logger = Logger.getLogger(Problem1.class);

    public void checkAndProcessFile(String filePath) throws FileNotFoundException {
        if (new File(filePath).exists()) {
            this.readFile(filePath);
            this.readFileNew(filePath);
        } else {
            throw new FileNotFoundException("File not found");
        }
    }

    public void readFileNew(String filePath) {
        Instant startTime = Instant.now();
        Map<String, BigDecimal> closingDetails = new HashMap<>();

        AtomicReference<String> lastId = new AtomicReference<>();
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.forEachOrdered(line -> {
                if (checkIfValueIsId(line)) {
                    lastId.set(line);
                } else {
                    closingDetails.put(lastId.get(), new BigDecimal(line));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String cusId : closingDetails.keySet()) {
            System.out.println("cusId = " + cusId + " Closing price = " + closingDetails.get(cusId));
        }
        Instant endTime = Instant.now();
        System.out.println("Duration.between(startTime,endTime).toMillis() = " + Duration.between(startTime, endTime).toMillis());

    }

    @Deprecated
    public void readFile(String filePath) {
        FileInputStream fileInputStream = null;
        Scanner scanner = null;
        Map<String, BigDecimal> closingDetails = new HashMap<>();
        String lastId = "";
        try {
            long startTime = System.currentTimeMillis();
            fileInputStream = new FileInputStream(filePath);
            scanner = new Scanner(fileInputStream, StandardCharsets.UTF_8);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (checkIfValueIsId(line)) {
                    lastId = line;
                } else {
                    closingDetails.put(lastId, new BigDecimal(line));
                }
            }

            for (String cusId : closingDetails.keySet()) {
                System.out.println("cusId = " + cusId + " Closing price = " + closingDetails.get(cusId));
            }

            if (scanner.ioException() != null) {
                throw scanner.ioException();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("TotalTime to process the file = " + (endTime - startTime) + "ms");

        } catch (FileNotFoundException e) {
            logger.error("File not found error.", e);
        } catch (IOException e) {
            logger.error("Exception while processing the file.", e);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (scanner != null) {
                    scanner.close();
                }
            } catch (IOException e) {
                logger.error("Exception while closing IO objects.");
            }

        }

    }

    private static void insertIntoMap(Map<String, BigDecimal> closingDetails, String lastId, BigDecimal bigDecimal) {
        closingDetails.put(lastId, bigDecimal);
    }

    private static boolean checkIfValueIsId(String line) {
        try {
            BigDecimal bigDecimal = new BigDecimal(line);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
