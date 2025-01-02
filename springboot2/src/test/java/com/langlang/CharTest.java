package com.langlang;

public class CharTest {

    public static void main(String[] args) {

        String first = "-+0123456789";
        first = "null";
        char[] chrs = first.toCharArray();
        for (int i = 0, j = chrs.length; i < j; i++) {
            Character theC = Character.valueOf(chrs[i]);
            if (theC == 0) {
                // special case: for null
                theC = null;
                System.out.println(true);
            } else {
                System.out.println(false);
            }

        }


    }


}
