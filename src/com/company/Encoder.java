package com.company;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.company.Main.cryptoChars;

public class Encoder {
    public void encodeText() throws IOException {
        BufferedReader systemBr = new BufferedReader(new InputStreamReader(System.in));
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
            int key = Integer.parseInt(systemBr.readLine());
            char[] chars = s.toCharArray();
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
            writer1.write(sb.toString());
        }
    }
}
