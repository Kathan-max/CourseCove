package com.course_cove.autocorrect;

import java.util.*;

public class Trie {
    private TrieNode root;
    private Map<String, String> prevWords;

    public Trie() {
        root = new TrieNode();
        prevWords = new HashMap<>();
    }

    public void insert(String word) {
        String newWord = word.toLowerCase();
        prevWords.put(newWord, word);
        TrieNode current = root;
        for (char ch : newWord.toCharArray()) {
            current.getChildren().putIfAbsent(ch, new TrieNode());
            current = current.getChildren().get(ch);
        }
        current.setEndOfWord(true);
    }

    public List<String> autocomplete(String prefix) {
        prefix = prefix.toLowerCase();
        List<String> suggestions = new ArrayList<>();
        TrieNode node = findLastNodeOfPrefix(prefix);

        if (node != null) {
            findAllWordsWithPrefix(node, prefix, suggestions);
        }
        List<String> suggest2 = new ArrayList<>();
        for (String sug : suggestions) {
            suggest2.add(this.prevWords.get(sug));
        }
        return suggest2;
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