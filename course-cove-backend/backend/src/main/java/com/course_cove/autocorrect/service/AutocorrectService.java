package com.course_cove.autocorrect.service;

import com.course_cove.autocorrect.Trie;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class AutocorrectService {
    private Trie trie;

    public AutocorrectService() {
        trie = new Trie();
        // Predefined list of words for demonstration
        String[] words = {
                "java", "javascript", "python", "c++", "ruby",
                "react", "angular", "vue", "spring", "hibernate"
        };

        for (String word : words) {
            trie.insert(word);
        }
    }

    public List<String> getSuggestions(String query) {
        return trie.autocomplete(query.toLowerCase());
    }
}
