package com.example.bankcards.util;

import java.util.concurrent.ThreadLocalRandom;

public class Util {

    public static String getMaskerCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return cardNumber;
        }
        String lastFour = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + lastFour;

    }

    public static String generateCardNumber() {
        return String.format("%04d %04d %04d %04d",
                ThreadLocalRandom.current().nextInt(10000),
                ThreadLocalRandom.current().nextInt(10000),
                ThreadLocalRandom.current().nextInt(10000),
                ThreadLocalRandom.current().nextInt(10000));
    }
}
