package kz.yerkhan.dev;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ArgumentParser {
    private String outputDirectory = ".";
    private String prefix = "";
    private boolean appendMode = false;
    private boolean shortStats = false;
    private boolean fullStats = false;
    private final List<String> inputFiles = new ArrayList<>();

    public ArgumentParser(String[] args) {
        parseArgs(args);
    }

    private void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o":
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException("После -o нужно указать путь");
                    }
                    outputDirectory = args[++i];
                    break;

                case "-p":
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException("После -p нужно указать префикс");
                    }
                    prefix = args[++i];
                    if (prefix.matches(".*\\.(txt|csv|log|json|xml)$")) {
                        throw new IllegalArgumentException("Вы указали имя файла вместо префикса: " + prefix +
                                ". Пример правильного использования: -p result_");
                    }
                    break;

                case "-a":
                    appendMode = true;
                    break;

                case "-s":
                    shortStats = true;
                    break;

                case "-f":
                    fullStats = true;
                    break;

                default:
                    if (args[i].startsWith("-")) {
                        throw new IllegalArgumentException("Неизвестная опция: " + args[i]);
                    }
                    inputFiles.add(args[i]);
                    break;
            }
        }

        if (!shortStats && !fullStats) {
            shortStats = true;
        }

        if (inputFiles.isEmpty()) {
            throw new IllegalArgumentException("Не указаны входные файлы");
        }

        File outDir = new File(outputDirectory);
        if (!outDir.exists() || !outDir.isDirectory()) {
            throw new IllegalArgumentException("Некорректный путь к папке: " + outputDirectory);
        }
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isAppendMode() {
        return appendMode;
    }

    public boolean isShortStats() {
        return shortStats;
    }

    public boolean isFullStats() {
        return fullStats;
    }

    public List<String> getInputFiles() {
        return inputFiles;
    }
}
