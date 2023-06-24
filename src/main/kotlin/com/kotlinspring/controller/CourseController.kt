package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.service.CourseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/courses")
class CourseController(val courseService: CourseService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@RequestBody courseDTO: CourseDTO): CourseDTO = courseService.addCourse(courseDTO)


/*    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun retrieveAllCourses(): List<CourseDTO> = courseService.retrieveAllCourses()*/

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun retrieveAllCourses(
        @RequestParam(
            "course_name",
            required = false
        ) courseName: String?
    ): List<CourseDTO> = courseService.retrieveAllCourses(courseName)

    @PutMapping("/{course_id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateCourse(@PathVariable("course_id") courseId: Int, @RequestBody updatedCourseDTO: CourseDTO): CourseDTO {
        return courseService.updateCourse(courseId, updatedCourseDTO)
    }

    @DeleteMapping("/{course_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCourse(@PathVariable("course_id") courseId: Int) {
        courseService.deleteCourse(courseId)
    }


}