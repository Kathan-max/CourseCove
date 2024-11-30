package com.course_cove.autocorrect;

public class EditDistance {
    /**
     * Computes the Levenshtein (Edit) Distance between two strings.
     * 
     * The Edit Distance is the minimum number of single-character edits
     * (insertions, deletions, or substitutions) required to change one string into
     * another.
     * 
     * @param str1 The first input string
     * @param str2 The second input string
     * @return The edit distance between the two strings
     */
    public int calculateEditDistance(String str1, String str2) {
        str1 = str1.toLowerCase();
        str2 = str2.toLowerCase();
        // Handle null or empty string cases
        if (str1 == null)
            str1 = "";
        if (str2 == null)
            str2 = "";

        // Create a matrix to store edit distances
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];

        // Initialize first row and column
        for (int i = 0; i <= str1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= str2.length(); j++) {
            dp[0][j] = j;
        }

        // Fill in the rest of the matrix
        for (int i = 1; i <= str1.length(); i++) {
            for (int j = 1; j <= str2.length(); j++) {
                // If characters are the same, no operation needed
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Find the minimum of three operations:
                    // 1. Insert
                    // 2. Delete
                    // 3. Substitute
                    dp[i][j] = 1 + Math.min(
                            dp[i - 1][j], // Deletion
                            Math.min(
                                    dp[i][j - 1], // Insertion
                                    dp[i - 1][j - 1] // Substitution
                            ));
                }
            }
        }

        // Return the bottom-right cell which contains the edit distance
        return dp[str1.length()][str2.length()];
    }
}