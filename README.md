# 🔍 Online Education Platform Analysis System

## Overview

This project presents a robust search and recommendation system for online education platforms (like Coursera, edX, Udemy). The system implements advanced algorithms for **semantic course discovery**, **user query analysis**, **autocomplete**, **spell correction**, and **ranking/filtering**, all built using **Java** and custom data structures.

Developed as part of the COMP 8547 – Advanced Computing Concepts course, the project scrapes and processes educational content, enabling intelligent, scalable search for courses with enhanced user experience.

---

## 🔑 Key Features

### 🔎 1. Inverted Indexing with TF-IDF (By Kathan Bhavsar)
- Enables contextual course retrieval.
- Calculates term relevance using `TF-IDF`.
- Delivers high-precision search results.

### 📈 2. Query Frequency Tracking (By Kathan Bhavsar)
- Uses `KMP algorithm` and `HashMap` to track frequently searched terms.
- Filters stop words and logs query metrics.

### 🔤 3. Autocomplete Suggestions (By Hetanshu Patel)
- Built using a `Trie` data structure.
- Supports case-insensitive prefix search.
- Preserves original casing using an auxiliary HashMap.

### ✅ 4. Spell Correction (By Hetanshu Patel)
- Implements `Levenshtein Distance` algorithm.
- Suggests nearest course/tag when query has typos.

### 📊 5. Course Reranking (By Dev Patel)
- Uses a `Priority Queue` to rank courses by match scores.
- Custom scoring on course description and tag overlap.

### 🕸️ 6. Web Crawling + HTML Parsing (By Kush Patel)
- Scrapes course data from Coursera, edX, TeamTreehouse, Udemy, and Codecademy.
- Parses HTML using `JSoup` and extracts structured metadata.

### 🧹 7. Regex Data Validation (By Kalp Kalani)
- Cleans and validates extracted data using regex patterns.
- Standardizes course content across platforms.

### 💸 8. Max Price Filtering (By Kalp Kalani)
- Implements an `AVL Tree` for efficient budget-based filtering.
- Allows users to set a maximum price threshold.

### 🔖 9. Tag-Based Filtering (By Kathan Bhavsar)
- Uses a `HashMap` + `LCS algorithm` for semantic tag matching.
- Returns only relevant courses based on tags and similarity score.

### 🎓 10. Course Level Filtering (By Kathan Bhavsar)
- Filters courses by difficulty (Beginner, Intermediate, Advanced).
- Uses `Boyer-Moore` string matching for title alignment.

### ⭐ 11. Sorting by Price & Rating (By Dev Patel)
- Implements optimized `Counting Sort`.
- Scales float values to integers for accurate and fast sorting.

---

## ⚙️ Technologies Used

- **Language:** Java
- **Libraries:** JSoup (for web scraping), Java I/O, Regex
- **Algorithms & Data Structures:**
  - Trie, AVL Tree, PriorityQueue, HashMap
  - TF-IDF, KMP, Edit Distance, LCS, Boyer-Moore, Counting Sort

---

## 📂 Project Architecture

+-- src/
| +-- crawler/
| | +-- UWCrawler.java
| | +-- HTMLtoTextConverter.java
| | +-- AdditionalInfoExtractor.java
| +-- search/
| | +-- SearchService.java
| | +-- InvertedIndexing.java
| | +-- SearchSource.java
| | +-- CourseRankerHeap.java
| | +-- SortSearchItem.java
| +-- autocomplete/
| | +-- Trie.java
| | +-- TrieNode.java
| | +-- AutocorrectService.java
| | +-- AutocorrectController.java
| | +-- EditDistance.java
| +-- filters/
| | +-- BoyerMooreFilter.java
| | +-- LongestCommonSubsequence.java
| | +-- AVLTree.java
| +-- utils/
| | +-- DataReader.java
| | +-- Extracting_info.java


---

## 📈 Performance Highlights

| Feature                    | Algorithm Used           | Time Complexity           |
|---------------------------|--------------------------|---------------------------|
| Inverted Indexing         | TF-IDF                   | O(n * m)                  |
| Query Frequency           | KMP + HashMap            | O(k + m * w)              |
| Autocomplete              | Trie                     | O(k + w * d)              |
| Spell Correction          | Levenshtein Distance     | O(k * m * n)              |
| Course Ranking            | Priority Queue           | O(m * (k * l + p * q))    |
| Max Price Filtering       | AVL Tree                 | O(log n)                  |
| Sorting                   | Counting Sort            | O(n + k)                  |

---

## 👥 Team Members

**Team TryCatch - Group 7 (Section 3)**  
- Kathan Piyushkumar Bhavsar  
- Kalp Jigneshbhai Kalani  
- Dev Pinakinbhai Patel  
- Kush Janak Patel  
- Hetanshu Hemant Patel  

---

## 📚 References

- [Coursera](https://www.coursera.org/)  
- [edX](https://www.edx.org/)  
- [Udemy](https://www.udemy.com/)  
- [Codecademy](https://www.codecademy.com/)  
- [TeamTreehouse](https://teamtreehouse.com/)  

---

## 📄 License

This project was developed for academic purposes as part of the University of Windsor's COMP 8547 course. Please contact the authors for any external or commercial use.

