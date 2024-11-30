package com.course_cove.autocorrect.service;

import org.springframework.stereotype.Service;
import org.springframework.web.cors.CorsUtils;

import java.util.List;
import java.util.PriorityQueue;
import java.lang.Character.Subset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.text.StyledEditorKit;

import java.util.Map;
import com.course_cove.autocorrect.AVLTree;
import com.course_cove.autocorrect.BoyerMooreFilter;
import com.course_cove.autocorrect.CourseObject;
import com.course_cove.autocorrect.CourseRankerHeap;
import com.course_cove.autocorrect.EditDistance;
import com.course_cove.autocorrect.LongestCommonSubsequence;
import com.course_cove.autocorrect.CourseRankerHeap.CourseRankingData;
import com.course_cove.autocorrect.dto.SearchFilterDTO;
import com.course_cove.autocorrect.service.DataReader;
import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;

import ch.qos.logback.core.net.SyslogOutputStream;

@Service
public class SearchService {

    // Edit Distance Filtering
    public List<CourseObject> filterByII(String useQuery, DataReader dataReader,
            Integer p) {
        EditDistance ed = new EditDistance();
        List<CourseObject> L1 = new ArrayList<>();
        for (String courseTitle : dataReader.getCourseNames()) {
            int res = ed.calculateEditDistance(useQuery, courseTitle);
            if (res < dataReader.getEdThreshold()) {
                L1.add(dataReader.getCourseMap().get(courseTitle));
            }
        }
        return L1;
    }

    public static Map<String, Integer> filterMap(Map<String, Integer> inputMap) {
        // Create a new HashMap to store the filtered results
        Map<String, Integer> filteredMap = new HashMap<>();

        // Iterate through the map and add only entries with non-zero values to the new
        // map
        for (Map.Entry<String, Integer> entry : inputMap.entrySet()) {
            if (entry.getValue() != 0) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }

        return filteredMap;
    }

    public List<CourseObject> filterByII(String userQuery, DataReader dataReader) {
        List<String> query_words = dataReader.remove_stop_words(userQuery);
        Map<String, Map<String, Integer>> DF_doc = dataReader.getDf();
        Map<String, Map<String, Integer>> TF_freq = new HashMap<>();
        int N = 0; // total Courses
        Set<String> total_courses = new HashSet<>();
        for (String query_w : query_words) {
            Map<String, Integer> hh = DF_doc.getOrDefault(query_w.toLowerCase(), null);
            if (hh == null) {
                continue;
            } else {
                // N += hh.size();
                hh = filterMap(hh);
                total_courses.addAll(hh.keySet());
                TF_freq.put(query_w, hh);
            }
        }
        List<String> total_courses2 = new ArrayList<>(total_courses);
        N = total_courses2.size();
        Double[][] TF_IDF = new Double[N][query_words.size()];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < query_words.size(); j++) {
                Map<String, Integer> hh2 = DF_doc.getOrDefault(query_words.get(j).toLowerCase(), null);
                hh2 = filterMap(hh2);
                if (hh2 == null) {
                    TF_IDF[i][j] = 0.0;
                } else {
                    TF_IDF[i][j] = TF_freq.get(query_words.get(j)).getOrDefault(total_courses2.get(i), 1)
                            * Math.abs(Math.log(N / (1 + hh2.size())));
                }
            }
        }
        List<CourseObject> first_course_list = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            int count = 0;
            for (int j = 0; j < query_words.size(); j++) {
                count += TF_IDF[i][j];
            }
            if (count >= dataReader.gettdidfThreshold()) {
                first_course_list.add(dataReader.getCourseMap().get(total_courses2.get(i)));
            }
        }
        return first_course_list;
    }

    // AVL pricing tree Filtering
    public List<CourseObject> filterByMaxPrice(List<CourseObject> L, Double maxPrice) {
        List<CourseObject> L2 = new ArrayList<>();
        if (maxPrice != 0.0) {
            AVLTree courseTree = new AVLTree();
            for (CourseObject cObj : L) {
                courseTree.insert(cObj);
            }
            L2 = courseTree.filterCoursesByMaxPrice(maxPrice);
        }
        return L2;
    }

    // Tags based filtering
    public Set<CourseObject> filterByTags(String useQuery, List<String> tags, DataReader dataReader) {
        // List<CourseObject> L3 = new ArrayList<>();
        Set<CourseObject> L3 = new LinkedHashSet<>();
        LongestCommonSubsequence lcs = new LongestCommonSubsequence();
        for (String tag : tags) {
            List<String> course_tag = dataReader.getTagsHashMap().getOrDefault(tag, null);
            if (course_tag != null) {
                for (String courseTitle : course_tag) {
                    int res = lcs.calculateLCS(courseTitle, useQuery);
                    if (res >= dataReader.getLCSThreshold()) {
                        L3.add(dataReader.getCourseMap().get(courseTitle));
                    }
                }
            }
        }
        return L3;
    }

    public List<CourseObject> reorder_courses(String userQuery, List<String> userTags,
            List<CourseObject> unordered_Courses) {
        CourseRankerHeap rankingHeap = new CourseRankerHeap();
        PriorityQueue<CourseRankingData> rankedCoursesHeap = rankingHeap.rankCourses(unordered_Courses, userQuery,
                userTags);
        List<CourseObject> orderedCourse = rankingHeap.getRankedCourses(rankedCoursesHeap);

        return orderedCourse;
    }

    public Set<CourseObject> filterByCourseLvl(String userQuery, String courseLvl, List<CourseObject> L4,
            Map<String, List<String>> courseLvlHashMap, Map<String, CourseObject> courseMap) {
        BoyerMooreFilter byfilter = new BoyerMooreFilter();
        // String useQuery, String courseLvl,
        // Map<String, List<String>> courseLvlHashMap,
        // Map<String, CourseObject> courseMap
        Set<CourseObject> filteredCourse = byfilter.filterByCourseLvl(userQuery, courseLvl, courseLvlHashMap,
                courseMap, L4);
        // List<CourseObject> L5 = new ArrayList<>(filteredCourse);
        return filteredCourse;
    }

    public String checkSpell(SearchFilterDTO filterDTO, DataReader dataReader) {
        EditDistance eDistance = new EditDistance();
        Set<String> courseNames = new HashSet<>(dataReader.getTagsHashMap().keySet());
        List<String> courseNames2 = dataReader.getCourseNames();
        courseNames.addAll(courseNames2);
        String mostClose = "";
        int min_val = Integer.MAX_VALUE;
        for (String courseName : courseNames) {
            int res = eDistance.calculateEditDistance(courseName, filterDTO.getQuery());
            if (min_val >= res) {
                min_val = res;
                mostClose = courseName;
            }
        }
        if (min_val == 0) {
            mostClose = "";
        }
        System.out.println("mostClose: " + mostClose);
        return mostClose;
    }

    public Set<CourseObject> simpleFilter(Set<CourseObject> L5, Double maxPrice) {
        Set<CourseObject> L__ = new HashSet<>();
        if (maxPrice != null) {
            for (CourseObject c_obj : L5) {
                if (c_obj.price <= maxPrice) {
                    L__.add(c_obj);
                }
            }
            return L__;
        } else {
            return L5;
        }
    }

    public List<CourseObject> getItems(SearchFilterDTO filterDTO, DataReader dataReader) {
        System.out.println("Received Search Request: ");
        System.out.println(filterDTO.getAllparas());
        String useQuery = filterDTO.getQuery();
        // List<CourseObject> L1 = filterByEd(useQuery, dataReader);
        List<CourseObject> L1 = filterByII(useQuery, dataReader, 0);
        System.out.println("Initially Fetched Course List length (used Inverted Indexing): " + L1.size());

        // AVL pricing tree Filtering
        Double maxPrice = filterDTO.getMaxPrice();
        List<CourseObject> L2 = new ArrayList<>();
        if (maxPrice != null) {
            L2 = filterByMaxPrice(L1, maxPrice);
        } else {
            L2 = L1;
        }
        System.out.println("Data length after Price Filtering: " + L2.size());

        // Tags based filtering
        Set<CourseObject> L3 = new HashSet<>();
        if (filterDTO.getTags().size() > 0) {
            L3 = filterByTags(useQuery, filterDTO.getTags(), dataReader);
        }
        System.out.println("Data length after Tag Filtering: " + L3.size());
        // Taking the Union of the L3 and L2
        L3.addAll(L2);
        System.out.println("Final Data length (L2 + L3) after Filtering: " + L3.size());

        // Course Level based filtering.
        List<CourseObject> L4 = new ArrayList<>(L3);
        String courseLvl = filterDTO.getCourseLevel();
        Set<CourseObject> L5_courseLvl = filterByCourseLvl(useQuery, courseLvl, L4, dataReader.getCourseLvlMap(),
                dataReader.getCourseMap());
        List<CourseObject> unordered_Courses = new ArrayList<>(simpleFilter(L5_courseLvl, maxPrice));
        System.out.println("Data length (L4) after Course Lvl filtering: " + unordered_Courses.size());

        // Reordering based on the Word Frequency -> Page ranking

        List<String> userTags = filterDTO.getTags();
        List<CourseObject> ordered_courses = reorder_courses(useQuery, userTags, unordered_Courses);
        System.out.println("Final Data length: " + ordered_courses.size());
        return ordered_courses;

    }
}
