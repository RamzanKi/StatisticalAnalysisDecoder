package com.company;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static com.company.Main.*;

public class Decoder {

    public void decodeText(int key) throws IOException {
        String encodeFile = getFileContent();
        StringBuilder sb = new StringBuilder();
        char[] chars = encodeFile.toCharArray();
        StringBuilder rev = new StringBuilder(alphabet);
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
        System.out.println(sb.toString().replaceAll("(\\.\\s)", ".\n"));
        writeTextInFile(sb.toString().replaceAll("(\\.\\s)", ".\n"));
    }


    //BruteForce. путем перебора, подобрать ключ и расшифровать текст.
    public void etTuBrute() throws IOException {
        String decode = "";

        String encodeFile = getFileContent();

        StringBuilder sb = new StringBuilder();
        char[] chars = encodeFile.toCharArray();
        StringBuilder rev = new StringBuilder(alphabet);

        char[] cryptoRev = rev.reverse().toString().toCharArray();
        int key = 0;
        while (true) {
            for (int i = 0; i < chars.length; i++) {
                for (int j = 0; j < cryptoRev.length; j++) {
                    if (chars[i] == cryptoRev[j]) {
                        chars[i] = cryptoRev[(j + key) % cryptoRev.length];
                        sb.append(chars[i]);
                        break;
                    }
                }
            }
            decode = sb.toString();
            String[] split = decode.split("\\s");
            for (int i = 0; i < split.length; i++) {
                if (split[i].length() > 40) {
                    chars = encodeFile.toCharArray();
                    sb.setLength(0);
                    decode = "";
                    key++;
                    break;
                }
            }
            if (decode.length() > 0) {
                break;
            }
        }
        System.out.println(decode.replaceAll("(\\.\\s)", ".\n"));
        writeTextInFile(decode.replaceAll("(\\.\\s)", ".\n"));

    }

    public void statisticalAnalysis() throws IOException {
        BufferedReader systemBr = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите путь к тестовому файлу для сбора статистики: ");
        String pathTest = systemBr.readLine();
        StringBuilder testLine = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(Path.of(pathTest))) {
            while (reader.ready()) {
                testLine.append(reader.readLine());
            }
        }
        String test = testLine.toString().toLowerCase();

        String encodeFile = getFileContent();

        char[] encodeChars = encodeFile.toCharArray();
        StringBuilder sb = new StringBuilder();

        LinkedHashMap<Character, Integer> mapTest = createStatisticMap(test);
        LinkedHashMap<Character, Integer> mapEncode = createStatisticMap(encodeFile);

        LinkedHashMap<Character, Integer> testLinkedHashMap = sortHashMapByValues(mapTest);
        LinkedHashMap<Character, Integer> encodeLinkedHashMap = sortHashMapByValues(mapEncode);

        List<Character> testKeys = new ArrayList<>(testLinkedHashMap.keySet());
        List<Character> encodeKeys = new ArrayList<>(encodeLinkedHashMap.keySet());

        Collections.reverse(testKeys);
        Collections.reverse(encodeKeys);


        LinkedHashMap<Character, Character> finMap = new LinkedHashMap<>();

        for (int i = 0; i < encodeKeys.size(); i++) {
            finMap.put(encodeKeys.get(i), testKeys.get(i));
        }

        for (int i = 0; i < encodeChars.length; i++) {
            for (var pair : finMap.entrySet()) {
                if (encodeChars[i] == pair.getKey()) {
                    encodeChars[i] = pair.getValue();
                    sb.append(encodeChars[i]);
                    break;
                }
            }
        }
        System.out.println(sb.toString().replaceAll("(\\.\\s)", ".\n"));
        writeTextInFile(sb.toString().replaceAll("(\\.\\s)", ".\n"));
    }


    public LinkedHashMap<Character, Integer> createStatisticMap(String text) {
        LinkedHashMap<Character, Integer> map = new LinkedHashMap<>();

        ArrayList<Integer> testCountList = new ArrayList<>();
        ArrayList<Character> testCharList = new ArrayList<>();
        int count = 0;
        for (Character aChar : cryptoChars) {
            count = 0;
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == aChar) {
                    count++;
                }
            }
            if (count > 0) {
                testCountList.add(count);
            }
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == aChar) {
                    testCharList.add(text.charAt(i));
                    break;
                }
            }
        }
        for (int i = 0; i < testCountList.size(); i++) {
            map.put(testCharList.get(i), testCountList.get(i));
        }
        return map;
    }


    public LinkedHashMap<Character, Integer> sortHashMapByValues(HashMap<Character, Integer> passedMap) {
        LinkedHashMap<Character, Integer> sort = passedMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return sort;
    }

    public String getFileContent() throws IOException {
        BufferedReader systemBr = new BufferedReader(new InputStreamReader(System.in));
        String encodeFile;
        System.out.println("Введите путь к файлу, который хотите расшифровать: ");
        String fileToDecodeName = systemBr.readLine();

        StringBuilder toDecode = new StringBuilder();
        try (BufferedReader readerToDecode = Files.newBufferedReader(Path.of(fileToDecodeName))) {
            while (readerToDecode.ready()) {
                toDecode.append(readerToDecode.readLine());
            }
            encodeFile = toDecode.toString();
        }
        return encodeFile;
    }

    public void writeTextInFile(String decodeText) throws IOException {
        BufferedReader systemBr = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nВведите путь к файлу, в который хотите записать результат: ");
        String resultFile = systemBr.readLine();
        try (FileWriter writerToDecode = new FileWriter(resultFile, false)) {
            writerToDecode.write(decodeText);
        }
    }
}


