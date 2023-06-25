package com.kotlinspring.service

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.exception.CourseNotFoundException
import com.kotlinspring.exception.InstructorNotValidException
import com.kotlinspring.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(val courseRepository: CourseRepository, val instructorService: InstructorService) {

    companion object : KLogging()

    fun addCourse(courseDTO: CourseDTO): CourseDTO {
        val instructorOption = instructorService.findByInstructorId(courseDTO.instructorId!!)
        if (!instructorOption.isPresent) {
            throw InstructorNotValidException("Instructor Not Valid for the Id: ${courseDTO.instructorId}")
        }
        val courseEntity = courseDTO.let {
            Course(null, it.name, it.category, instructorOption.get())
        }
        courseRepository.save(courseEntity)
        logger.info { "Saved course is: $courseEntity" }
        return courseEntity.let {
            CourseDTO(it.id, it.name, it.category, it.instructor!!.id)
        }
    }

    fun retrieveAllCourses(courseName: String?): List<CourseDTO> {
        val courses =
            courseName?.let { courseRepository.findCourseByName(courseName) } ?: courseRepository.findAll()

        return courses.map {
            CourseDTO(it.id, it.name, it.category)
        }

    }

    fun updateCourse(courseId: Int, updatedCourseDTO: CourseDTO): CourseDTO {
        val existingCourse = courseRepository.findById(courseId)

        return if (existingCourse.isPresent) {
            existingCourse.get()
                .let {
                    it.name = updatedCourseDTO.name
                    it.category = updatedCourseDTO.category
                    courseRepository.save(it)
                    CourseDTO(it.id, it.name, it.category)
                }
        } else {
            throw CourseNotFoundException("Not found course with id: $courseId")
        }
    }

    fun deleteCourse(courseId: Int) {
        val existingCourse = courseRepository.findById(courseId)

        if (existingCourse.isPresent) {
            existingCourse.get()
                .let {
                    courseRepository.deleteById(courseId)
                }
        } else {
            throw CourseNotFoundException("Not found course with id: $courseId")
        }
    }
}

