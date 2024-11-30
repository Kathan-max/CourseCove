package com.course_cove.autocorrect.service;

import org.springframework.stereotype.Service;
import java.util.List;
import com.course_cove.autocorrect.CourseObject;
import com.course_cove.autocorrect.service.SortSearchItem;

@Service
public class SortSearchObjects {
    public List<CourseObject> sortObjects(List<CourseObject> finalFetchedCourses, String sortField) {
        SortSearchItem sortSearchItems = new SortSearchItem();
        return sortSearchItems.sortSearchResults(finalFetchedCourses, sortField);
    }
}
