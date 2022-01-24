package com.company;

import java.util.*;
import java.util.stream.Collectors;

import static com.company.Main.*;

public class Decoder {
    public String decodeText(String encode, int key) {

        StringBuilder sb = new StringBuilder();
        char[] chars = encode.toCharArray();
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
        return sb.toString();
    }
    //BruteForce. путем перебора, подобрать ключ и расшифровать текст.
    public String etTuBrute(String encode, int key) {
        String decode = "";
        while (true) {
            decode = decodeText(encode, key);
            if (decode.matches("[а-яА-Я](?s).*\\b,\s\\b.*")) {
                break;
            } else {
                decode = "";
                key++;
            }
        }
        System.out.println("ключ: " + key);
        return decode;
    }


    public String statisticalAnalysis(String encode, String test) {
        char[] encodeChars = encode.toCharArray();
        StringBuilder sb = new StringBuilder();

        HashMap<Character, Integer> mapTest = createStatisticMap(test);
        HashMap<Character, Integer> mapEncode = createStatisticMap(encode);

        LinkedHashMap<Character, Integer> testLinkedHashMap = sortHashMapByValues(mapTest);
        LinkedHashMap<Character, Integer> encodeLinkedHashMap = sortHashMapByValues(mapEncode);

        List<Character> testKeys = new ArrayList<Character>(testLinkedHashMap.keySet());
        List<Character> encodeKeys = new ArrayList<Character>(encodeLinkedHashMap.keySet());

             for (int i = 0; i < encodeChars.length; i++) {
                for (int j = 0; j < encodeKeys.size(); j++) {
                    if (encodeChars[i] == encodeKeys.get(j)) {
                        encodeChars[i] = testKeys.get(j);
                        sb.append(encodeChars[i]);
                        break;
                    }
                }
            }
        return sb.toString();
    }

    public HashMap<Character, Integer> createStatisticMap (String text) {
        HashMap<Character, Integer> map = new HashMap<>();

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
}

