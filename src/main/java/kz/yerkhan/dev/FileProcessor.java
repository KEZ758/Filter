package kz.yerkhan.dev;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class FileProcessor {
    private final List<String> inputFiles;
    private final String outputDir;
    private final String prefix;
    private final boolean append;

    private final List<BigInteger> integers = new ArrayList<>();
    private final List<Double> floats = new ArrayList<>();
    private final List<String> strings = new ArrayList<>();

    public FileProcessor(List<String> inputFiles, String outputDir, String prefix, boolean append) {
        this.inputFiles = inputFiles;
        this.outputDir = outputDir;
        this.prefix = prefix;
        this.append = append;
    }

    public void processFiles() {
        if (append) {
            loadPreviousResultsIfNeeded();
        }
        for (String fileName : inputFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    if (DataClassifier.isInteger(line)) {
                        integers.add(new BigInteger(line));
                    } else if (DataClassifier.isFloat(line)) {
                        floats.add(Double.parseDouble(line));
                    } else {
                        strings.add(line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка при чтении файла: " + fileName + " — " + e.getMessage());
            }
        }

        saveResults();
    }

    private void saveResults() {
        if (!integers.isEmpty()) {
            writeLines("integers.txt", integers.stream().map(Object::toString).collect(Collectors.toList()));
        }
        if (!floats.isEmpty()) {
            writeLines("floats.txt", floats.stream().map(Object::toString).collect(Collectors.toList()));

        }
        if (!strings.isEmpty()) {
            writeLines("strings.txt", strings);
        }
    }

    private void writeLines(String filenameSuffix, List<String> lines) {
        String fullPath = Paths.get(outputDir, prefix + filenameSuffix).toString();
        try (PrintWriter writer = new PrintWriter(new FileWriter(fullPath, append))) {
            for (String line : lines) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при записи файла: " + fullPath + " — " + e.getMessage());
        }
    }

    private void loadPreviousResultsIfNeeded() {
        loadFileIfExists("integers.txt", line -> {
            if (DataClassifier.isInteger(line)) {
                integers.add(new BigInteger(line));
            }
        });

        loadFileIfExists("floats.txt", line -> {
            if (DataClassifier.isFloat(line)) {
                floats.add(Double.parseDouble(line));
            }
        });

        loadFileIfExists("strings.txt", line -> {
            if (!line.trim().isEmpty()) {
                strings.add(line);
            }
        });
    }

    private void loadFileIfExists(String filenameSuffix, java.util.function.Consumer<String> handler) {
        String path = Paths.get(outputDir, prefix + filenameSuffix).toString();
        File file = new File(path);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    handler.accept(line.trim());
                }
            } catch (IOException e) {
                System.err.println("Ошибка при чтении предыдущего файла: " + path + " — " + e.getMessage());
            }
        }
    }

    public void printShortStats() {
        System.out.println("Статистика (краткая):");
        System.out.println("Integers: " + integers.size());
        System.out.println("Floats: " + floats.size());
        System.out.println("Strings: " + strings.size());
    }

    public void printFullStats() {
        System.out.println("Статистика (полная):");

        System.out.println("Integers:");
        if (!integers.isEmpty()) {
            BigInteger sum = BigInteger.ZERO;
            BigInteger min = integers.get(0);
            BigInteger max = integers.get(0);
            for (BigInteger val : integers) {
                sum = sum.add(val);
                if (val.compareTo(min) < 0) min = val;
                if (val.compareTo(max) > 0) max = val;
            }
            System.out.println("  Count: " + integers.size());
            System.out.println("  Min: " + min);
            System.out.println("  Max: " + max);
            System.out.println("  Sum: " + sum);
            System.out.println("  Avg: " + sum.divide(BigInteger.valueOf(integers.size())));
        } else {
            System.out.println("  No data");
        }

        System.out.println("Floats:");
        if (!floats.isEmpty()) {
            double sum = 0, min = floats.get(0), max = floats.get(0);
            for (double val : floats) {
                sum += val;
                if (val < min) min = val;
                if (val > max) max = val;
            }
            System.out.println("  Count: " + floats.size());
            System.out.println("  Min: " + min);
            System.out.println("  Max: " + max);
            System.out.println("  Sum: " + sum);
            System.out.println("  Avg: " + (sum / floats.size()));
        } else {
            System.out.println("  No data");
        }

        System.out.println("Strings:");
        if (!strings.isEmpty()) {
            int minLen = strings.get(0).length();
            int maxLen = strings.get(0).length();
            for (String s : strings) {
                int len = s.length();
                if (len < minLen) minLen = len;
                if (len > maxLen) maxLen = len;
            }
            System.out.println("  Count: " + strings.size());
            System.out.println("  Shortest: " + minLen);
            System.out.println("  Longest: " + maxLen);
        } else {
            System.out.println("  No data");
        }
    }

}
