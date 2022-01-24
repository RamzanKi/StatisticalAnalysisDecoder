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
            System.out.println("""
                    Для декодирования введите ключ
                    Для использования BruteForce введите 1
                    Для использования statisticalAnalysis введите 2""");
            int decodeKey = Integer.parseInt(systemBr.readLine());
            if (key == decodeKey) {
                String decode = new Decoder().decodeText(encode, key);
                String str = decode.replaceAll("(\\.\\s)", ".\n");
                System.out.println(str);
                writer1.write(str);
            } else if (decodeKey == 1) {
                System.out.println("настало время Брутальной расшифровки...\n");
                String brute = new Decoder().etTuBrute(encode, 0);
                String str2 = brute.replaceAll("(\\.\\s)", ".\n");
                System.out.println(str2);
                System.out.println(" \nПроверка:\nВерный результат? введите да или нет:");
                String answer = systemBr.readLine();
                while (true) {
                    if (answer.equals("да")) {
                        break;
                    } else {
                        String brute2 = new Decoder().etTuBrute(encode, key);
                        System.out.println(brute2.replaceAll("(\\.\\s)", ".\n"));

                        System.out.println("\nПроверка:\nВерный результат? введите да или нет:");
                        String answer2 = systemBr.readLine();
                        if (answer2.equals("да")){
                            break;
                        }
                    }
                }
                writer1.write(str2);
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
                String statistical = new Decoder().statisticalAnalysis(encode, testLine).replaceAll("(\\.\\s)", ".\n");
                System.out.println(statistical);
                writer1.write(statistical);
            }
        }
    }
}

