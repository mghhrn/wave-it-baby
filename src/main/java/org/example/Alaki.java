package org.example;

public class Alaki {

    public static void main(String[] args) {
        int a = Integer.MIN_VALUE;
        System.out.println(a);

        short s = 0;
        System.out.println(s);

        a = 32768;
        s = (short) a;
        System.out.println(s);
    }
}
