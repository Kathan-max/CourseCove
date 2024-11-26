package com.course_cove.autocorrect;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode current = root;
        for (char ch : word.toCharArray()) {
            current.getChildren().putIfAbsent(ch, new TrieNode());
            current = current.getChildren().get(ch);
        }
        current.setEndOfWord(true);
    }

    public List<String> autocomplete(String prefix) {
        List<String> suggestions = new ArrayList<>();
        TrieNode node = findLastNodeOfPrefix(prefix);

        if (node != null) {
            findAllWordsWithPrefix(node, prefix, suggestions);
        }

        return suggestions;
    }

    private TrieNode findLastNodeOfPrefix(String prefix) {
        TrieNode current = root;
        for (char ch : prefix.toCharArray()) {
            if (!current.getChildren().containsKey(ch)) {
                return null;
            }
            current = current.getChildren().get(ch);
        }
        return current;
    }

    private void findAllWordsWithPrefix(TrieNode node, String prefix, List<String> suggestions) {
        if (node.isEndOfWord()) {
            suggestions.add(prefix);
        }

        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            findAllWordsWithPrefix(
                    entry.getValue(),
                    prefix + entry.getKey(),
                    suggestions);
        }
    }
}