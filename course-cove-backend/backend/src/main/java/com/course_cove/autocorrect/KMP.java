package com.course_cove.autocorrect;

public class KMP {

    public String stopWords = "a an the and or but for to of on in with at by from as is was are were be been being have has had do does did will shall can could would should may might must this that these those it its he she they we you I me my mine your yours his her hers their theirs our ours who whom which what when where why how";

    // Preprocess the pattern to create the longest prefix suffix (LPS) array
    public int[] computeLPSArray(String pattern) {
        int[] lps = new int[pattern.length()];
        int len = 0;
        int i = 1;

        while (i < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    // KMP search algorithm
    public boolean search(String pattern) {
        int[] lps = computeLPSArray(pattern);
        int i = 0; // Index for text
        int j = 0; // Index for pattern
        String text = stopWords;
        while (i < text.length()) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }
            if (j == pattern.length()) {
                return true; // Pattern found
            } else if (i < text.length() && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return false; // Pattern not found
    }
}
