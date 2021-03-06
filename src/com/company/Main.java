package com.company;

import java.io.*;



public class Main {
    public static String alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя.,!?- ";
    public static char[] cryptoChars = alphabet.toCharArray();

    public static void main(String[] args) throws IOException {
        BufferedReader systemBr = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("""
                    Зашифровать файл 1
                    Расшифровать файл 2
                    Выход 3""");
            String verdict = systemBr.readLine();
            if (verdict.equals("1")) {
                new Encoder().encodeText();
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
                    new Decoder().statisticalAnalysis();

                }
            }
            if (verdict.equals("3")){
                System.exit(0);
            }
        }
    }
}



