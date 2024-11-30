package com.course_cove.autocorrect.controller;

import com.course_cove.autocorrect.CourseObject;
import com.course_cove.autocorrect.KMP;
import com.course_cove.autocorrect.dto.SearchFilterDTO;
import com.course_cove.autocorrect.service.AutocorrectService;
import com.course_cove.autocorrect.service.DataReader;
import com.course_cove.autocorrect.service.SearchService;
import com.course_cove.autocorrect.service.SortSearchObjects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class AutocorrectController {

    @Autowired
    private DataReader dataReader;

    @Autowired
    private AutocorrectService autocorrectService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private SortSearchObjects sortSearchObjs;

    private List<CourseObject> finalFetchedCourses;
    private Map<String, Integer> searchWordCounts = new HashMap<>();
    private String correctSpell = null;

    @GetMapping("/autocorrect")
    public List<String> getAutocorrectSuggestions(@RequestParam String query) {
        return autocorrectService.getSuggestions(query);
    }

    @PostMapping("/search")
    public List<CourseObject> performSearch(@RequestBody SearchFilterDTO filterDTO) {
        if (filterDTO.getQuery().equals("")) {
            return new ArrayList<>();
        }
        String regex = "\\s*[.,!?;:]+\\s*|\\s+";
        KMP kmp = new KMP();
        String[] queryWords = Arrays.stream(filterDTO.getQuery().split(regex))
                .filter(part -> !part.isEmpty()) // Remove empty strings
                .toArray(String[]::new);
        for (String queryWord : queryWords) {
            if (kmp.search(queryWord.toLowerCase()) == false) {
                searchWordCounts.put(queryWord, searchWordCounts.getOrDefault(queryWord, 0) + 1);
            }
        }
        for (String tag : filterDTO.getTags()) {
            searchWordCounts.put(tag, searchWordCounts.getOrDefault(tag, 0) + 1);
        }
        correctSpell = searchService.checkSpell(filterDTO, dataReader);
        if (correctSpell == "") {
            correctSpell = null;
        } else {
            filterDTO.setQuery(correctSpell);
        }
        finalFetchedCourses = searchService.getItems(filterDTO, dataReader);
        return finalFetchedCourses;
    }

    @GetMapping("/sort")
    public List<CourseObject> performSorting(@RequestParam String type) {
        return sortSearchObjs.sortObjects(finalFetchedCourses, type);
    }

    @GetMapping("/search-word-counts")
    public Map<String, Integer> getSearchWordCounts() {
        return searchWordCounts;
    }

    @GetMapping("/spell-check")
    public String getSpellCorrection() {
        return correctSpell;
    }
}