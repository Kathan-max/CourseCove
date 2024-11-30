package com.course_cove.autocorrect;

import com.course_cove.autocorrect.CourseObject;

import java.sql.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BoyerMooreFilter {
    public Set<CourseObject> filterByCourseLvl(String useQuery, String courseLvl,
            Map<String, List<String>> courseLvlHashMap,
            Map<String, CourseObject> courseMap, List<CourseObject> L4) {

        String[] queryWords = useQuery.toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\s+]", "")
                .split("\\s+");
        Set<CourseObject> filteredCourses = new LinkedHashSet<>();
        // System.out.println(queryWords[0]);
        // System.out.println(courseLvl);
        List<String> courseLvl_list = null;
        if (courseLvl == "") {
            courseLvl = "Advanced";
            List<String> courseLvlHashList = courseLvlHashMap.getOrDefault(courseLvl, null);
            // System.out.println(courseLvlHashList);
            for (String courseTitle : courseLvlHashList) {
                if (matchAnyWord(courseTitle.toLowerCase(), queryWords)) {
                    filteredCourses.add(courseMap.get(courseTitle));
                }
            }
            for (CourseObject c_obj : L4) {
                if (courseLvlHashList.contains(c_obj.title)) {
                    filteredCourses.add(c_obj);
                }
            }
            courseLvl = "Beginner";
            courseLvlHashList = courseLvlHashMap.getOrDefault(courseLvl, null);
            // System.out.println(courseLvlHashList);
            for (String courseTitle : courseLvlHashList) {
                if (matchAnyWord(courseTitle.toLowerCase(), queryWords)) {
                    filteredCourses.add(courseMap.get(courseTitle));
                }
            }
            for (CourseObject c_obj : L4) {
                if (courseLvlHashList.contains(c_obj.title)) {
                    filteredCourses.add(c_obj);
                }
            }
            courseLvl = "Intermediate";
            courseLvlHashList = courseLvlHashMap.getOrDefault(courseLvl, null);
            // System.out.println(courseLvlHashList);
            for (String courseTitle : courseLvlHashList) {
                if (matchAnyWord(courseTitle.toLowerCase(), queryWords)) {
                    filteredCourses.add(courseMap.get(courseTitle));
                }
            }
            for (CourseObject c_obj : L4) {
                if (courseLvlHashList.contains(c_obj.title)) {
                    filteredCourses.add(c_obj);
                }
            }
        } else {
            List<String> courseLvlHashList = courseLvlHashMap.getOrDefault(courseLvl, null);
            // System.out.println(courseLvlHashList);
            for (String courseTitle : courseLvlHashList) {
                if (matchAnyWord(courseTitle.toLowerCase(), queryWords)) {
                    filteredCourses.add(courseMap.get(courseTitle));
                }
            }
            for (CourseObject c_obj : L4) {
                if (courseLvlHashList.contains(c_obj.title)) {
                    filteredCourses.add(c_obj);
                }
            }
        }
        // System.out.println(filteredCourses);
        return filteredCourses;
    }

    private boolean matchAnyWord(String courseTitle, String[] queryWords) {
        Map<String, int[]> wordBadCharHeuristics = new HashMap<>();
        for (String word : queryWords) {
            // System.out.println("word: " + word);
            if (word.length() < 2)
                continue;

            int[] badCharHeuristic = buildBadCharHeuristic(word);
            wordBadCharHeuristics.put(word, badCharHeuristic);
            if (boyerMoorePatternMatch(courseTitle, word, badCharHeuristic)) {
                return true;
            }
        }
        return false;
    }

    private int[] buildBadCharHeuristic(String pattern) {
        int[] badChar = new int[256];
        Arrays.fill(badChar, -1);
        for (int i = 0; i < pattern.length(); i++) {
            badChar[(int) pattern.charAt(i)] = i;
        }
        return badChar;
    }

    private boolean boyerMoorePatternMatch(String text, String pattern, int[] badChar) {
        int n = text.length();
        int m = pattern.length();

        if (m == 0)
            return true;

        int s = 0;
        while (s <= (n - m)) {
            int j = m - 1;
            while (j >= 0 && pattern.charAt(j) == text.charAt(s + j)) {
                j--;
            }
            if (j < 0) {
                return true;
            } else {
                // Add bounds checking for text.charAt(s + j)
                int charIndex = text.charAt(s + j);
                int badCharShift = j - (charIndex < 256 ? badChar[charIndex] : -1);
                s += Math.max(1, badCharShift);
            }
        }
        return false;
    }

}
