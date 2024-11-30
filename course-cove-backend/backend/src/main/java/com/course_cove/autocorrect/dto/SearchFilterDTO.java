package com.course_cove.autocorrect.dto;

import java.util.List;
import java.util.Map;

public class SearchFilterDTO {
    private String query;
    private Double maxPrice;
    private String courseLevel;
    private List<String> tags;

    public SearchFilterDTO() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getAllparas() {
        return "SearchFilterDTO{" +
                "query='" + query + '\'' +
                ", maxPrice=" + maxPrice +
                ", courseLevel='" + courseLevel + '\'' +
                ", tags=" + tags +
                '}';
    }
}
