package com.course_cove.autocorrect;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CourseRankerHeap {
    public static class CourseRankingData implements Comparable<CourseRankingData> {
        CourseObject course;
        int totalMatchScore;
        Map<String, Integer> wordCounts;
        Map<String, Integer> tagCounts;

        public CourseRankingData(CourseObject course) {
            this.course = course;
            this.totalMatchScore = 0;
            this.wordCounts = new HashMap<>();
            this.tagCounts = new HashMap<>();
        }

        @Override
        public int compareTo(CourseRankingData other) {
            return Integer.compare(this.totalMatchScore, other.totalMatchScore) * -1;
        }
    }

    private static List<String> normalizeText(String text) {
        if (text == null)
            return new ArrayList<>();
        return Arrays.stream(text.toLowerCase()
                .replaceAll("[^a-zA-Z\\s]", "") // Remove punctuation
                .split("\\s+"))
                .filter(word -> !word.isEmpty())
                .collect(Collectors.toList());
    }

    public PriorityQueue<CourseRankingData> rankCourses(
            List<CourseObject> courses,
            String userQuery,
            List<String> userTags) {
        PriorityQueue<CourseRankingData> courseRankings = new PriorityQueue<>();
        List<String> queryWords = normalizeText(userQuery);
        List<String> normalizedUserTags = userTags.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        for (CourseObject course : courses) {
            CourseRankingData rankingData = new CourseRankingData(course);
            List<String> descriptionWords = normalizeText(course.description);
            for (String queryWord : queryWords) {
                long wordCount = descriptionWords.stream()
                        .filter(word -> word.equals(queryWord))
                        .count();
                if (wordCount > 0) {
                    rankingData.wordCounts.put(queryWord, (int) wordCount);
                    rankingData.totalMatchScore += wordCount;
                }
            }
            for (String userTag : normalizedUserTags) {
                if (course.tags != null) {
                    long tagCount = course.tags.stream()
                            .filter(tag -> tag.toLowerCase().equals(userTag))
                            .count();
                    if (tagCount > 0) {
                        rankingData.tagCounts.put(userTag, (int) tagCount);
                        rankingData.totalMatchScore += tagCount;
                    }
                }
            }

            if (rankingData.totalMatchScore >= 0) {
                courseRankings.offer(rankingData);
            }
        }
        return courseRankings;
    }

    public List<CourseObject> getRankedCourses(PriorityQueue<CourseRankingData> courseHeap) {
        List<CourseObject> rankedCourses = new ArrayList<>();
        while (!courseHeap.isEmpty()) {
            rankedCourses.add(courseHeap.poll().course);
        }
        return rankedCourses;
    }

}
