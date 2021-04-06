package com.in28minutes.soap.webservices.soapcoursemanagement.service;

import com.in28minutes.soap.webservices.soapcoursemanagement.bean.Course;

import java.io.Serializable;
import java.util.List;

public interface CourseDetailsService extends Serializable {

    Course findById(int id);

    List<Course> findAll();

    CourseDetailsServiceImpl.Status deleteById(int id);
}
