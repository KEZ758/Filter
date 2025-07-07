package kz.yerkhan.dev;


import java.util.List;

public class Main {
    public static void main( String[] args ) {
        try {
            ArgumentParser parser = new ArgumentParser(args);

            List<String> inputFiles = parser.getInputFiles();
            String outputDir = parser.getOutputDirectory();
            String prefix = parser.getPrefix();
            boolean append = parser.isAppendMode();

            FileProcessor processor = new FileProcessor(inputFiles, outputDir, prefix, append);

            processor.processFiles();

            if (parser.isFullStats()) {
                processor.printFullStats();
            } else {
                processor.printShortStats();
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
