package com.course_cove.autocorrect;

public class LongestCommonSubsequence {
    public int calculateLCS(String str1, String str2) {
        // Handle null or empty string cases
        if (str1 == null)
            str1 = "";
        if (str2 == null)
            str2 = "";

        // Create a matrix to store LCS lengths
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];

        // Build the dp table
        for (int i = 1; i <= str1.length(); i++) {
            for (int j = 1; j <= str2.length(); j++) {
                // If characters match, add 1 to the LCS of previous characters
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                }
                // If characters don't match, take the maximum of
                // LCS without including current character from either string
                else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        // Return the bottom-right cell which contains the LCS length
        return dp[str1.length()][str2.length()];
    }
}
