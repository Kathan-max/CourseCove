package com.course_cove.autocorrect.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.core.joran.sanity.Pair;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.DoubleToIntFunction;

import com.course_cove.autocorrect.CourseObject;

@Service
public class DataReader {

    private List<CourseObject> courseList = new ArrayList<>();
    private Map<String, List<String>> tags_hashmap = new HashMap<>();
    private Map<String, List<String>> course_lvl_hashmap = new HashMap<>();
    private List<String> courseNames = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, CourseObject> course_hashmap = new HashMap<>();
    private final Integer ed_threshold = 15;
    private final Integer lcs_threshold = 10;
    private Map<String, Map<String, Integer>> IDF = new HashMap<>();
    private final Double tdidf_threshold = 2.5;

    public List<CourseObject> getCourseList() {
        return courseList;
    }

    public List<String> getCourseNames() {
        return this.courseNames;
    }

    public Map<String, List<String>> getTagsHashMap() {
        return this.tags_hashmap;
    }

    public Integer getEdThreshold() {
        return this.ed_threshold;
    }

    public Map<String, CourseObject> getCourseMap() {
        return this.course_hashmap;
    }

    public Integer getLCSThreshold() {
        return this.lcs_threshold;
    }

    public Map<String, List<String>> getCourseLvlMap() {
        return this.course_lvl_hashmap;
    }

    public boolean findval(List<String> prev, String val) {
        for (String va : prev) {
            if (va.equals(val)) {
                return true;
            }
        }
        return false;
    }

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "aren't",
            "aren", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but",
            "by", "can't", "cannot", "could", "couldn't", "couldn't", "couldnt", "did", "didn't", "does", "doesn't",
            "don't", "doing", "don't", "during", "each", "few", "for", "from", "further", "had", "hadn't", "has",
            "hasn't", "have", "haven't", "having", "he", "he's", "he'll", "he'd", "he'll", "her", "here", "here's",
            "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "how's", "how", "how's", "i",
            "i'm", "i'll", "i'm", "i'm", "is", "isn't", "it", "it'll", "it'll", "it's", "it's", "its", "itself",
            "let", "me", "me's", "my", "myself", "might", "my", "not", "no", "nor", "no's", "not", "off", "on", "nor"));

    public List<String> remove_stop_words(String c_desc) {
        // Convert the description into lowercase and split it into words
        String[] words = c_desc.toLowerCase().split("\\s+");

        List<String> filteredWords = new ArrayList<>();
        for (String word : words) {
            // If the word is not a stop word, add it to the list
            if (!STOP_WORDS.contains(word)) {
                filteredWords.add(word);
            }
        }

        return filteredWords;
    }

    public Map<String, Map<String, Integer>> getDf() {
        return this.IDF;
    }

    public Double gettdidfThreshold() {
        return tdidf_threshold;
    }

    public void calculateDf() {
        // Get all the keys (tags) from the tags_hashmap
        List<String> tags = new ArrayList<>(this.tags_hashmap.keySet());

        for (String tag : tags) {
            // Remove stop words from the tag
            List<String> tagWords = this.remove_stop_words(tag.trim());
            // System.out.println("Tag: " + tagWords);

            // Get the corresponding CourseObject using the tag
            List<String> c_objs_titles = this.tags_hashmap.get(tag);
            // CourseObject c_obj = this.course_hashmap.get(this.tags_hashmap.get(tag));
            for (String course_title : c_objs_titles) {
                CourseObject c_obj = this.course_hashmap.getOrDefault(course_title, null);
                if (c_obj == null) {
                    continue; // Skip if CourseObject is null
                } else {
                    // System.out.println("c_title: " + c_obj.title);
                }

                // Get the description of the course and remove stop words
                String c_desc = c_obj.description;
                List<String> c_desc_list = this.remove_stop_words(c_desc);
                // System.out.println("Desc0: " + c_desc_list.get(0));

                for (String tagW : tagWords) {
                    int count = 0;

                    // Count occurrences of the tag word in the course description
                    for (String c_desc_word : c_desc_list) {
                        if (c_desc_word.equalsIgnoreCase(tagW)) {
                            count++;
                        }
                    }
                    // System.out.println("Count: " + count);
                    // Retrieve or initialize the map for this tag word
                    Map<String, Integer> c_gg = this.IDF.getOrDefault(tagW.toLowerCase(), new HashMap<>());

                    // Update the count for the current course title
                    count += c_gg.getOrDefault(c_obj.title, 0);
                    c_gg.put(c_obj.title, count);
                    // System.out.println("Final Count: " + count);
                    // Update the IDF map with the modified values
                    this.IDF.put(tagW.toLowerCase(), c_gg);
                }
            }
        }
    }

    public void removeDups() {
        List<String> newOne = new ArrayList<>();
        List<CourseObject> newOne2 = new ArrayList<>();
        for (CourseObject data : courseList) {
            if (findval(newOne, data.title)) {
                // System.out.println(data.title);
                // d += 1;
                continue;
            } else {
                newOne.add(data.title);
                newOne2.add(data);
            }
            // h += 1;
        }
        this.courseList = newOne2;
    }

    private void makeTagsHashmap() {
        for (CourseObject obj : this.courseList) {
            for (String tag : obj.tags) {
                List<String> course_tag = this.tags_hashmap.getOrDefault(tag, null);
                if (course_tag == null) {
                    course_tag = new ArrayList<>();
                }
                course_tag.add(obj.title);
                this.tags_hashmap.put(tag, course_tag);
            }
        }
    }

    private void makeCourseLvlHashmap() {
        // int k = 0;
        for (CourseObject obj : this.courseList) {
            String obj_lvl = obj.level;
            if (obj_lvl.equals("Mixed")) {
                obj_lvl = "Intermediate";
            } else if (obj_lvl.equals("Introductory")) {
                obj_lvl = "Beginner";
            } else if (obj_lvl.equals("Expert")) {
                obj_lvl = "Advanced";
            }
            List<String> course_ = this.course_lvl_hashmap.getOrDefault(obj_lvl, null);
            if (course_ == null) {
                course_ = new ArrayList<>();
            }
            course_.add(obj.title);
            this.course_lvl_hashmap.put(obj_lvl, course_);
            // k += 1;
        }
        // System.out.println("K: " + k);
        // System.out.println("K: " + this.course_lvl_hashmap.size());
    }

    private void makeCourseNameList() {
        for (CourseObject obj : this.courseList) {
            this.courseNames.add(obj.title);
        }
    }

    private void makeCourseMap() {
        for (CourseObject obj : this.courseList) {
            this.course_hashmap.put(obj.title, obj);
        }
    }

    private void readJson(File file, String platform_tag) {
        try {
            // Read the entire JSON file into a List of CourseObjects
            List<CourseObject> courses = objectMapper.readValue(file, new TypeReference<List<CourseObject>>() {
            });

            int i = 0;
            while (i < courses.size()) {
                courses.get(i).platform = platform_tag;
                i += 1;
            }
            // Add all parsed courses to the courseList
            courseList.addAll(courses);

            // System.out.println("Successfully processed file: " + file.getName());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load JSON file: " + file.getName());
        }
    }

    @PostConstruct
    public void loadJsonFiles() {
        String folderName = "E:\\Windsor\\Sem-1\\Computing Concepts\\Project\\course-cove-backend\\backend\\src\\main\\java\\com\\course_cove\\autocorrect\\data";
        File folder = new File(folderName);

        File[] jsonFiles = folder.listFiles((dir, name) -> name.endsWith(".json"));
        System.out.println("Detected total " + jsonFiles.length + " files");

        if (jsonFiles != null) {
            for (File file : jsonFiles) {
                // System.out.println("Processing file: " + file.getName());
                String platform_tag = file.getName().replace("course_data_", "").replace(".json", "");
                System.out.println(platform_tag);
                readJson(file, platform_tag);
                System.out.println("Read the data of: " + platform_tag + " platform");
            }
        }
        removeDups();
        makeTagsHashmap();
        System.out.println("Created Tags Map");
        makeCourseNameList();
        System.out.println("Created CourseName List");
        makeCourseMap();
        System.out.println("Created CourseName Map");
        makeCourseLvlHashmap();
        System.out.println("Created CourseLvl Map");
        calculateDf();
        // System.out.println(this.getDf().keySet());
        System.out.println("Created Map for II");
    }
}
