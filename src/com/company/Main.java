package com.company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;



public class Main {
    public static String alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ.,;:?!–-\" ";
    public static char[] cryptoChars = alphabet.toCharArray();

    public static void main(String[] args) throws IOException {
        BufferedReader systemBr = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите путь к файлу, который нужно зашифровать: ");
        String path = systemBr.readLine();
        int key;
        StringBuilder text = new StringBuilder();
        System.out.println("Введите путь в хранилище, в котором хотите создать файл: ");
        String newPath = systemBr.readLine();
        System.out.println("Введите имя файла, который хотите создать: ");
        String newFileName = systemBr.readLine();
        try (BufferedReader reader1 = Files.newBufferedReader(Path.of(path));
             BufferedWriter writer1 = Files.newBufferedWriter(Files.createFile(Path.of(newPath + newFileName)))
        ) {
            while (reader1.ready()) {
                text.append(reader1.readLine());
            }
            String s = text.toString();

            System.out.println("Введите ключ кодирования: ");
            key = Integer.parseInt(systemBr.readLine());
            String encode = new Encoder().encodeText(s, key);
            System.out.println(encode);
            writer1.write(encode);
            System.out.println("""
                    Для декодирования введите ключ
                    Для использования BruteForce введите 1
                    Для использования statisticalAnalysis введите 2""");
            int decodeKey = Integer.parseInt(systemBr.readLine());
            System.out.println("Введите путь к файлу, который хотите расшифровать: ");
            String fileToDecodeName = systemBr.readLine();
            System.out.println("Введите путь к файлу, в который хотите записать результат: ");
            String resultFile = systemBr.readLine();
            StringBuilder toDecode = new StringBuilder();
            try (BufferedReader readerToDecode = Files.newBufferedReader(Path.of(fileToDecodeName));
                 BufferedWriter writerToDecode = Files.newBufferedWriter(Files.createFile(Path.of(resultFile)))
            ) {
                while (readerToDecode.ready()) {
                    toDecode.append(readerToDecode.readLine());
                }
                String encodeFile = toDecode.toString();
                if (key == decodeKey) {
                    String decode = new Decoder().decodeText(encodeFile, key);
                    String str = decode.replaceAll("(\\.\\s)", ".\n");
                    System.out.println(str);
                    writerToDecode.write(str);
                } else if (decodeKey == 1) {
                    System.out.println("настало время Брутальной расшифровки...\n");
                    String brute = new Decoder().etTuBrute(encodeFile, 0);
                    String str2 = brute.replaceAll("(\\.\\s)", ".\n");
                    System.out.println(str2);
                    System.out.println(" \nПроверка:\nВерный результат? введите да или нет:");
                    String answer = systemBr.readLine();
                    while (true) {
                        if (answer.equals("да")) {
                            break;
                        } else {
                            str2 = new Decoder().etTuBrute(encodeFile, key);
                            System.out.println(str2.replaceAll("(\\.\\s)", ".\n"));

                            System.out.println("\nПроверка:\nВерный результат? введите да или нет:");
                            String answer2 = systemBr.readLine();
                            if (answer2.equals("да")) {
                                break;
                            }
                        }
                    }
                    writerToDecode.write(str2);
                } else if (decodeKey == 2) {
                    System.out.println("Введите путь к тестовому файлу для сбора статистики: ");
                    String pathTest = systemBr.readLine();
                    StringBuilder test = new StringBuilder();
                    try (BufferedReader reader = Files.newBufferedReader(Path.of(pathTest))) {
                        while (reader.ready()) {
                            test.append(reader.readLine());
                        }
                    }
                    String testLine = test.toString();
                    System.out.println("идёт анализ, подождите...");
                    System.out.println();
                    String statistical = new Decoder().statisticalAnalysis(encodeFile, testLine).replaceAll("(\\.\\s)", ".\n");
                    System.out.println(statistical);
                    writerToDecode.write(statistical);
                }
            }
        }
    }
}

