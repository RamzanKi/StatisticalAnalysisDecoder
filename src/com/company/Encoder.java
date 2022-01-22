package com.company;

import static com.company.Main.cryptoChars;

public class Encoder {
    public String encodeText(String text, int key) {
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
        return sb.toString();
    }
}
