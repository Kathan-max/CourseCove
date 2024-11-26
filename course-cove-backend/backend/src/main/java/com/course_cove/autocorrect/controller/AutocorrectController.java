package com.course_cove.autocorrect.controller;

import com.course_cove.autocorrect.service.AutocorrectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class AutocorrectController {
    @Autowired
    private AutocorrectService autocorrectService;

    @GetMapping("/autocorrect")
    public List<String> getAutocorrectSuggestions(@RequestParam String query) {
        return autocorrectService.getSuggestions(query);
    }
}