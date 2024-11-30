package com.course_cove.autocorrect.service;

import com.course_cove.autocorrect.Trie;
import java.util.List;

import javax.xml.crypto.Data;

import org.springframework.stereotype.Service;
import com.course_cove.autocorrect.service.DataReader;

@Service
public class AutocorrectService {
    private Trie trie;
    private final DataReader dataReader;

    public AutocorrectService() {
        this.dataReader = new DataReader();
        this.dataReader.loadJsonFiles();
        trie = new Trie();
        // System.out.println(dataReader.getCourseNames());
        for (String word : dataReader.getCourseNames()) {
            trie.insert(word);
        }
    }

    public List<String> getSuggestions(String query) {
        return trie.autocomplete(query);
    }

    public DataReader getDataReader() {
        return this.dataReader;
    }
}
