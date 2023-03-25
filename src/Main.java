import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        try {
            String result = calc(input);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("throws Exception");
            /*В ТЗ указаны строки Output с комментариями. По умолчанию комментарии не выводятся, но при необходимости
            можно закомментировать предыдущую строку и раскомментировать следующую строку,
            которая будет выводить описание выбрасываемого исключения.*/
            //System.out.println("throws Exception //т.к." + String.valueOf(e).split("java.lang.Exception:")[1]);
        }
    }

    public static String calc(String input) throws Exception {
        // Разделяем строку на операнды и оператор
        String[] tokens = input.split(" ");
        if (tokens.length == 1) {
            throw new Exception("строка не является математической операцией");
        }
        else if ((tokens.length != 3)) {
            throw new Exception("формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        }
        String operand1 = tokens[0];
        String operator = tokens[1];
        String operand2 = tokens[2];

        // Определяем, являются ли операнды римскими или арабскими числами
        boolean isRoman = isRoman(operand1) && isRoman(operand2);
        boolean isArabic = isArabic(operand1) && isArabic(operand2);
        if (!isRoman && !isArabic) {
            throw new Exception("формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        }
        if (isRoman && isArabic) {
            throw new Exception("используются одновременно разные системы счисления");
        }

        // Преобразуем операнды к одному формату
        int num1 = isRoman ? romanToArabic(operand1) : Integer.parseInt(operand1);
        int num2 = isRoman ? romanToArabic(operand2) : Integer.parseInt(operand2);

        // Выполняем операцию
        int result;
        switch (operator) {
            case "+" -> result = num1 + num2;
            case "-" -> result = num1 - num2;
            case "*" -> result = num1 * num2;
            case "/" -> {
                if (num2 == 0) {
                    throw new Exception("деление на ноль");
                }
                result = num1 / num2;
            }
            default -> throw new Exception("строка не является математической операцией");
        }

        // Проверяем, является ли результат отрицательным при работе с римскими числами
        if (isRoman && result < 1) {
            throw new Exception("в римской системе нет отрицательных чисел");
        }

        // Преобразуем результат к нужному формату и возвращаем
        return isRoman ? arabicToRoman(result) : Integer.toString(result);
    }

    // Проверяет, является ли строка римским числом
    private static boolean isRoman(String s) {
        return s.matches("[IVX]+");
    }

    // Проверяет, является ли строка арабским числом
    private static boolean isArabic(String s) {
        try {
            int num = Integer.parseInt(s);
            return num >= 1 && num <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Преобразует римское число в арабское
    private static int romanToArabic(String s) throws Exception {
        int result = 0;
        int prevValue = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            int currValue = romanDigitToArabic(s.charAt(i));
            if (currValue < prevValue) {
                result -= currValue;
            } else {
                result += currValue;
            }
            prevValue = currValue;
        }
        return result;
    }

    // Преобразует одну римскую цифру в арабскую
    private static int romanDigitToArabic(char c) throws Exception {
        return switch (c) {
            case 'I' -> 1;
            case 'V' -> 5;
            case 'X' -> 10;
            default -> throw new Exception("неверно введено римское число");
        };
    }

    // Преобразует арабское число в римское
    private static String arabicToRoman(int number) {
        String[] romanSymbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (number >= values[i]) {
                result.append(romanSymbols[i]);
                number -= values[i];
            }
        }
        return result.toString();
    }
}