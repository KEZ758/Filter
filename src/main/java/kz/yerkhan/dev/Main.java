package kz.yerkhan.dev;


import java.util.List;

public class Main {
    public static void main( String[] args ) {
        try {
            // 1. Разбор аргументов
            ArgumentParser parser = new ArgumentParser(args);

            // 2. Получение параметров
            List<String> inputFiles = parser.getInputFiles();
            String outputDir = parser.getOutputDirectory();
            String prefix = parser.getPrefix();
            boolean append = parser.isAppendMode();

            // 3. Создание процессора
            FileProcessor processor = new FileProcessor(inputFiles, outputDir, prefix, append);

            // 4. Обработка файлов
            processor.processFiles();

            // 5. Вывод статистики
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
