package com.in28minutes.soap.webservices.soapcoursemanagement.soap;

import com.example.myschema.*;
import com.in28minutes.soap.webservices.soapcoursemanagement.bean.Course;
import com.in28minutes.soap.webservices.soapcoursemanagement.exception.CourseNotFoundException;
import com.in28minutes.soap.webservices.soapcoursemanagement.service.CourseDetailsService;
import com.in28minutes.soap.webservices.soapcoursemanagement.service.CourseDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.List;

@Endpoint
public class CourseDetailsEndpoint {

    @Autowired
    private CourseDetailsService courseDetailsService;

    @PayloadRoot(namespace = "http://in28minutes/courses", localPart = "GetCourseDetailsRequest")
    @ResponsePayload
    public JAXBElement<GetCourseDetailsResponseType> processCourseDetailsRequest(@RequestPayload JAXBElement<GetCourseDetailsRequestType> request) {

        Course course = courseDetailsService.findById(request.getValue().getId());

        if (course == null) {
            throw new CourseNotFoundException("Invalid course id: " + request.getValue().getId());
        }

        return createJaxbElement(mapCourseDetails(courseDetailsService.findById(request.getValue().getId())), GetCourseDetailsResponseType.class);
    }

    @PayloadRoot(namespace = "http://in28minutes/courses", localPart = "GetAllCourseDetailsRequest")
    @ResponsePayload
    public JAXBElement<GetAllCourseDetailsResponseType> processAllCourseDetailsRequest(@RequestPayload JAXBElement<GetAllCourseDetailsRequestType> request) {
        return createJaxbElement(mapAllCourseDetails(courseDetailsService.findAll()), GetAllCourseDetailsResponseType.class);
    }

    @PayloadRoot(namespace = "http://in28minutes/courses", localPart = "DeleteCourseDetailsRequest")
    @ResponsePayload
    public JAXBElement<DeleteCourseDetailsResponseType> deleteCourseDetailsRequest(@RequestPayload JAXBElement<DeleteCourseDetailsRequestType> request) {
        DeleteCourseDetailsResponseType responseType = new DeleteCourseDetailsResponseType();
        responseType.setStatus(mapStatus(courseDetailsService.deleteById(request.getValue().getId())));
        return createJaxbElement(responseType, DeleteCourseDetailsResponseType.class);
    }

    private GetCourseDetailsResponseType mapCourseDetails(Course course) {
        GetCourseDetailsResponseType response = new GetCourseDetailsResponseType();
        response.setCourseDetails(mapCourse(course));
        return response;
    }

    private StatusType mapStatus(CourseDetailsServiceImpl.Status status) {
        return StatusType.valueOf(status.toString());
    }

    private GetAllCourseDetailsResponseType mapAllCourseDetails(List<Course> courses) {
        GetAllCourseDetailsResponseType response = new GetAllCourseDetailsResponseType();

        for (Course course :courses) {
            CourseDetailsType mapCourse = mapCourse(course);
            response.getAllCourseDetails().add(mapCourse);
        }

        return response;
    }

    private CourseDetailsType mapCourse(Course course) {
        CourseDetailsType courseDetails = new CourseDetailsType();
        courseDetails.setId(course.getId());
        courseDetails.setName(course.getName());
        courseDetails.setDescription(course.getDescription());

        return courseDetails;
    }

    private <T> JAXBElement<T> createJaxbElement(T object, Class<T> clazz) {
        return new JAXBElement<>(new QName(clazz.getSimpleName()), clazz, object);
    }
}
