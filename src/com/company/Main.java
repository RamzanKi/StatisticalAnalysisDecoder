package com.company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Stream;

public class Main {
    public static String alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    public static String symbols = ".,;:?!–\" ";
    public static String cryptoSymbols = alphabet + symbols;
    public static char[] cryptoChars = cryptoSymbols.toCharArray();

    public static void main(String[] args) throws IOException {
//        C:\Users\Ramzan\Desktop\br.txt
//        C:\Users\Ramzan\Desktop\text.txt
//        C:\Users\Ramzan\Desktop\test.txt
//        System.out.println((char)50);

        BufferedReader systemBr = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите путь к файлу: ");
        String path = systemBr.readLine();
        int key;
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader1 = Files.newBufferedReader(Path.of(path));
//             BufferedWriter writer1 = Files.newBufferedWriter(Files.createFile(Path.of("C:\\Users\\Ramzan\\IdeaProjects\\Encoder\\src\\com\\company\\newDecodeFile")))
        ) {
            while (reader1.ready()) {
                text.append(reader1.readLine());
            }

            String s = text.toString();

            System.out.println("Введите ключ кодирования: ");
            key = Integer.parseInt(systemBr.readLine());
            String encode = encodeText(s, key);
            System.out.println(encode);
            System.out.println("""
                    Для декодирования введите ключ
                    Для использования BruteForce введите 1
                    Для использования statisticalAnalysis введите 2""");
            int decodeKey = Integer.parseInt(systemBr.readLine());
            if (key == decodeKey) {
                String decode = decodeText(encode, key);
                String str = decode.replaceAll("\\.\s", ".\n");
                System.out.println(str);
//                writer1.write(str);
            } else if (decodeKey == 1) {
                System.out.println("настало время Брутальной расшифровки...\n");
                String brute = etTuBrute(encode);
                String str2 = brute.replaceAll("\\.\s", ".\n");
                System.out.println(str2);
//                writer1.write(str2);
            } else if (decodeKey == 2) {
                System.out.println("Введите путь к тестовому файлу: ");
                String pathTest = systemBr.readLine();
                StringBuilder test = new StringBuilder();
                try (BufferedReader reader = Files.newBufferedReader(Path.of(pathTest))) {
                    while (reader.ready()) {
                        test.append(reader.readLine());
                    }
                }
                String testLine = test.toString();

                System.out.println("анализ...");
                System.out.println();
                String statistical = statisticalAnalysis(encode, testLine);
                System.out.println(statistical);
//                writer1.write(statistical);
            }
        }
    }

    public static String encodeText(String text, int key) {
        char[] chars = text.toCharArray();
        StringBuilder sb = new StringBuilder();
        if (key < 0) {
            key = -key;
        }
            for (int i = 0; i < chars.length; i++) {
                for (int j = 0; j < cryptoChars.length; j++) {
                    if (chars[i] == cryptoChars[j]) {
                        chars[i] = cryptoChars[(j + key) % cryptoChars.length];
                        sb.append(chars[i]);
                        break;
                    }
                }
            }

//        if (key < 0) {
//            StringBuilder rev = new StringBuilder(cryptoSymbols);
//            char[] cryptoRev = rev.reverse().toString().toCharArray();
//            for (int i = 0; i < chars.length; i++) {
//                for (int j = 0; j < cryptoRev.length; j++) {
//                    if (chars[i] == cryptoRev[j]) {
//                        chars[i] = cryptoRev[(j - key) % cryptoRev.length];
//                        sb.append(chars[i]);
//                        break;
//                    }
//                }
//            }
//        }
//            for (char c : chars) {
//                sb.append((char) (c + key));
//            }
        return sb.toString();
    }


    public static String decodeText(String encode, int key) {

        StringBuilder sb = new StringBuilder();
        char[] chars = encode.toCharArray();
        StringBuilder rev = new StringBuilder(cryptoSymbols);
        if (key < 0) {
            key = -key;
        }
        char[] cryptoRev = rev.reverse().toString().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            for (int j = 0; j < cryptoRev.length; j++) {
                if (chars[i] == cryptoRev[j]) {
                    chars[i] = cryptoRev[(j + key) % cryptoRev.length];
                    sb.append(chars[i]);
                    break;
                }
            }
        }

//        while (true) {
//            decode = encodeText(encode, -key);
//            if (decode.matches("[а-яА-Я](?s).*\\b,\s\\b.*")) {
//                break;
//            }
//        }
        return sb.toString();
    }

    //BruteForce. путем перебора, подобрать ключ и расшифровать текст.
    public static String etTuBrute(String encode) { // медленно работает с большим количеством текста (но хотя бы работает...)
        int key = 0;
        String decode = "";
        while (true){
            decode = decodeText(encode, key);
            if (decode.matches("[а-яА-Я](?s).*\\b,\s\\b.*")){
                break;
            } else {
                decode = "";
                key++;
            }
        }
//        while (true){
//            decode = encodeText(encode, key);
//            if (decode.matches("[а-яА-Я](?s).*\\b,\s\\b.*")){
//                break;
//            } else {
//                decode = "";
//                decode = encodeText(encode, -key);
//                if (decode.matches("[а-яА-Я](?s).*\\b,\s\\b.*")){
//                    break;
//                }
//                decode = "";
//                key++;
//            }
//        }
        return decode;
    }

    public static String statisticalAnalysis(String encode, String test) {
        char[] chars = encode.toCharArray();
        StringBuilder sb = new StringBuilder();
        HashMap<Character, Integer> mapTest = new HashMap<>();
        HashMap<Character, Integer> mapEncode = new HashMap<>();
        ArrayList<Character> cryptoCharList = new ArrayList<>();


        ArrayList<Integer> testCountList = new ArrayList<>();
        ArrayList<Character> testCharList = new ArrayList<>();
        int count1 = 0;
        for (Character aChar : cryptoCharList) {
            count1 = 0;
            for (int i = 0; i < test.length(); i++) {
                char c = test.charAt(i);
                Character aChar1 = aChar;
                if (test.charAt(i) == aChar) {
                    count1++;
                }
            }
            if (count1 > 0) {
                testCountList.add(count1);
            }
            for (int i = 0; i < test.length(); i++) {
                if (test.charAt(i) == aChar) {
                    testCharList.add(test.charAt(i));
                    break;
                }
            }
        }
        Integer[] testCountArr = testCountList.toArray(new Integer[0]);
        Character[] testCharArray = testCharList.toArray(new Character[0]);
        for (int i = 0; i < testCountArr.length; i++) {
            Integer integer = testCountArr[i];
            Character character = testCharArray[i];
            mapTest.put(character, integer);
        }


        ArrayList<Integer> encodeCountList = new ArrayList<>();
        ArrayList<Character> encodeCharList = new ArrayList<>();
        int count2 = 0;
        for (Character aChar : cryptoCharList) {
            count2 = 0;
            for (int i = 0; i < encode.length(); i++) {
                if (test.charAt(i) == aChar) {
                    count2++;
                }
            }
            if (count2 > 0) {
                encodeCountList.add(count2);
            }
            for (int i = 0; i < encode.length(); i++) {
                if (encode.charAt(i) == aChar) {
                    encodeCharList.add(encode.charAt(i));
                    break;
                }
            }
        }
        Integer[] encCountArr = encodeCountList.toArray(new Integer[0]);
        Character[] encCharArray = encodeCharList.toArray(new Character[0]);
        for (int i = 0; i < encCountArr.length; i++) {
            Integer integer1 = encCountArr[i];
            Character character1 = encCharArray[i];
            mapEncode.put(character1, integer1);
        }


        for (var encodePair : mapEncode.entrySet()) {
            for (var testPair : mapTest.entrySet()) {
                int encodeKey = encodePair.getValue();
                char encodeChar = encodePair.getKey();
                int testKey = testPair.getValue();
                char testChar = testPair.getKey();
                if (encodeKey == testKey) {
                    encodeChar = testChar;

                    sb.append(encodeChar);
                }
            }
        }
        return sb.toString();
    }

    public static boolean approximatelyEqual(int desiredValue, int actualValue, int tolerancePercentage) {
        float diff = Math.abs(desiredValue - actualValue);         //  1000 - 950  = 50
        float tolerance = tolerancePercentage / 100 * desiredValue;  //  20/100*1000 = 200
        return diff < tolerance;                                   //  50<200      = true
    }
}