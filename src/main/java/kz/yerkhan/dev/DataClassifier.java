package kz.yerkhan.dev;

import java.math.BigInteger;

public class DataClassifier {

    public static boolean isInteger(String input) {
        try {
            new BigInteger(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isFloat(String input) {
        try {
            if (input.contains("f") || input.contains("F")) return false;
            Double.parseDouble(input);
            return input.contains(".") || input.toLowerCase().contains("e");
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
