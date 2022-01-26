package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


public class Main {
    public static String alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ.,;:?!–-\" ";
    public static char[] cryptoChars = alphabet.toCharArray();

    public static void main(String[] args) throws IOException {
        BufferedReader systemBr = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("""
                Зашифровать файл 1.
                Расшифровать файл 2.""");
        String verdict = systemBr.readLine();
        int key;
        if (verdict.equals("1")) {
            System.out.println("Введите путь к файлу, который нужно зашифровать: ");
            String path = systemBr.readLine();

            StringBuilder text = new StringBuilder();
            System.out.println("Введите путь в хранилище, в котором хотите создать файл: ");
            String newPath = systemBr.readLine();
            System.out.println("Введите имя файла, который хотите создать: ");
            String newFileName = systemBr.readLine();
            try (BufferedReader reader1 = Files.newBufferedReader(Path.of(path));
                 FileWriter writer1 = new FileWriter(newPath + newFileName, false)
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
            }
        }
        if (verdict.equals("2")) {
            System.out.println("""
                    Для декодирования при помощи ключа введите 1
                    Для использования BruteForce введите 2
                    Для использования statisticalAnalysis введите 3""");
            int method = Integer.parseInt(systemBr.readLine());

            if (method == 1) {
                System.out.println("Введите ключ кодирования: ");
                int decodeKey = Integer.parseInt(systemBr.readLine());
                new Decoder().decodeText(decodeKey);

            } else if (method == 2) {
                new Decoder().etTuBrute();

            } else if (method == 3) {
                System.out.println("идёт анализ, подождите...");
                System.out.println();
                new Decoder().statisticalAnalysis();

            }
        }
    }
}



