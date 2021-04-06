package com.in28minutes.soap.webservices.soapcoursemanagement.service;

import com.in28minutes.soap.webservices.soapcoursemanagement.bean.Course;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CourseDetailsServiceImpl implements CourseDetailsService {

    public enum Status {
        SUCCESS, FAILURE;
    }

    private static List<Course> courses = new ArrayList<>();

    @PostConstruct
    private void init() {
        courses.add(new Course(1, "Spring", "10 Steps"));
        courses.add(new Course(2, "Spring MVC", "10 Examples"));
        courses.add(new Course(3, "Spring Boot", "6k Students"));
        courses.add(new Course(4, "Spring Maven", "Most popular Maven course."));
    }

    @Override
    public Course findById(int id) {
        for (Course course : courses) {
            if (course.getId() == id) {
                return course;
            }
        }

        return null;
    }

    @Override
    public List<Course> findAll() {
        return courses;
    }

    @Override
    public Status deleteById(int id) {
        Iterator<Course> iter = courses.iterator();

        while (iter.hasNext()) {
            Course course = iter.next();

            if (course.getId() == id) {
                iter.remove();
                return Status.SUCCESS;
            }
        }

        return Status.FAILURE;
    }
}
