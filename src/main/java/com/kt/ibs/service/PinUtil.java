package com.kt.ibs.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public final class PinUtil {

    private static final Character[] ALLOWED_PIN_CHARACTERS = new Character[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final int PIN_SIZE = 5;

    public static String generatePin() {
        StringBuilder sb = new StringBuilder();
        List<Character> asList = Arrays.asList(ALLOWED_PIN_CHARACTERS);
        Collections.shuffle(asList);

        for (int i = 0; i < PIN_SIZE; i++) {
            sb.append(asList.get(i));
        }
        return sb.toString();
    }
}
