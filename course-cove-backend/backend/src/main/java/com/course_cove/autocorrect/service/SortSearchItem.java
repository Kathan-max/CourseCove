package com.course_cove.autocorrect.service;

import java.util.ArrayList;
import java.util.List;
import com.course_cove.autocorrect.CourseObject;

public class SortSearchItem {
    /**
     * Sorts the list of course objects and returns the sorted list
     * 
     * @param courseObjects List of course objects to be sorted
     * @param sortField     Field to sort by (PRICE or RATING)
     * @return Sorted list of course objects
     */
    public List<CourseObject> sortSearchResults(List<CourseObject> courseObjects, String sortField) {
        // Validate input
        if (courseObjects == null || courseObjects.isEmpty()) {
            return new ArrayList<>();
        }

        // Create a copy of the list to preserve the original
        List<CourseObject> sortedObjects = new ArrayList<>(courseObjects);

        // Perform counting sort based on the specified field
        switch (sortField.trim()) {
            case "price":
                countingSortByPrice(sortedObjects);
                break;
            case "rating":
                countingSortByRating(sortedObjects);
                break;
            default:
                throw new IllegalArgumentException("Unsupported sort field: " + sortField);
        }

        return sortedObjects;
    }

    /**
     * Counting Sort for Course Object Prices (Ascending Order)
     * 
     * @param courseObjects List of course objects to be sorted
     */
    private void countingSortByPrice(List<CourseObject> courseObjects) {
        // Find the range of prices
        double minPrice = courseObjects.stream().mapToDouble(obj -> obj.price).min().orElse(0);

        // Scale prices to integers
        int[] scaledPrices = new int[courseObjects.size()];
        for (int i = 0; i < courseObjects.size(); i++) {
            scaledPrices[i] = (int) Math.round((courseObjects.get(i).price - minPrice) * 100);
        }

        // Find max scaled price
        int maxScaledPrice = 0;
        for (int price : scaledPrices) {
            maxScaledPrice = Math.max(maxScaledPrice, price);
        }

        // Create counting array
        int[] count = new int[maxScaledPrice + 1];
        for (int price : scaledPrices) {
            count[price]++;
        }

        // Modify count array to store actual position of each object
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        // Create output array
        List<CourseObject> output = new ArrayList<>(courseObjects.size());
        output.addAll(java.util.Collections.nCopies(courseObjects.size(), null));

        // Build the output array (ascending order)
        for (int i = courseObjects.size() - 1; i >= 0; i--) {
            int index = --count[scaledPrices[i]];
            output.set(index, courseObjects.get(i));
        }

        // Copy sorted array back to original list
        courseObjects.clear();
        courseObjects.addAll(output);
    }

    /**
     * Counting Sort for Course Object Ratings (Descending Order)
     * 
     * @param courseObjects List of course objects to be sorted
     */
    private void countingSortByRating(List<CourseObject> courseObjects) {
        // Scale ratings to integers (multiply by 10)
        int[] scaledRatings = new int[courseObjects.size()];
        for (int i = 0; i < courseObjects.size(); i++) {
            scaledRatings[i] = (int) Math.round(courseObjects.get(i).rating * 10);
        }

        // Find max scaled rating
        int maxScaledRating = 50; // 5.0 * 10

        // Create counting array
        int[] count = new int[maxScaledRating + 1];
        for (int rating : scaledRatings) {
            count[rating]++;
        }

        // Modify count array to store actual position of each object
        for (int i = count.length - 2; i >= 0; i--) {
            count[i] += count[i + 1];
        }

        // Create output array
        List<CourseObject> output = new ArrayList<>(courseObjects.size());
        output.addAll(java.util.Collections.nCopies(courseObjects.size(), null));

        // Build the output array (descending order)
        for (int i = 0; i < courseObjects.size(); i++) {
            int scaledRating = scaledRatings[i];
            int index = count[scaledRating] - 1;
            output.set(index, courseObjects.get(i));
            count[scaledRating]--;
        }

        // Copy sorted array back to original list
        courseObjects.clear();
        courseObjects.addAll(output);
    }
    // Note: CourseObject class is assumed to be defined in another file
    // as per the provided course object definition
}