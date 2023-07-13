package com.example.gencybersocialmediaapi.Model;

public class encrypt {
    public static char encrypt_character(char character, int shift) {
        int ascii_value = (int) character;
        int new_ascii_value = ascii_value + shift;
        char new_character = (char) new_ascii_value;
        return new_character;
    }

    public static String encrypt(String string, int shift) {
        String newString = "";
        for (int i = 0; i < string.length(); i++) {
            char newChar = encrypt_character(string.charAt(i), shift);
            newString += newChar;
        }
        return newString;
    }

    public static void main(String[] args) {
        System.out.println(encrypt("Testing my encryption method.", 5));
    }
}
