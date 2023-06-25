package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.repository.CourseRepository
import com.kotlinspring.repository.InstructorRepository
import com.kotlinspring.util.PostgreSQLContainerInitializer
import courseEntityList
import instructorEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Description
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class CourseControllerIntegrationTest : PostgreSQLContainerInitializer(){

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    @BeforeEach
    fun setup() {
        courseRepository.deleteAll()
        instructorRepository.deleteAll()

        val instructor = instructorEntity()
        instructorRepository.save(instructor)

        val courses = courseEntityList(instructor)
        courseRepository.saveAll(courses)
    }

    @Test
    fun addCourse() {
        val instructor = instructorRepository.findAll().first()
        val courseDTO = CourseDTO(null, "Build Restful APIs using SpringBoot and Kotlin", "Development",instructor.id)
        val saveResult = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody
        assertTrue {
            saveResult!!.id != null
        }
    }

    @Test
    fun addCourse_validation() {
        val courseDTO = CourseDTO(null, "", "Development")
        val saveResult = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody
        assertEquals("courseDTO.instructorId must not be null, courseDTO.name must not be blank", saveResult)
    }


    @Test
    fun retrieveAllCourses() {
        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody
        assertEquals(3, courseDTOs!!.size)
    }

    @Test
    fun retrieveAllCourses_ByName() {
        val uri = UriComponentsBuilder
            .fromUriString("/v1/courses")
            .queryParam("course_name", "SpringBoot")
            .toUriString()
        val courseDTOs = webTestClient
            .get()
            .uri(uri)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody
        assertEquals(2, courseDTOs!!.size)
    }

    @Test
    @Description("Update a course successfully")
    fun updateCourses_success() {
        val instructor = instructorRepository.findAll().first()
        val course = Course(null, "Build Restful APIs using SpringBoot2 and Kotlin2", "Development", instructor)
        courseRepository.save(course)

        val updatedCourse = CourseDTO(null, "Build Restful APIs using SpringBoot", "Development", instructor.id)
        val updatedCourseDTO = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", course.id)
            .bodyValue(updatedCourse)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody
        assertEquals("Build Restful APIs using SpringBoot", updatedCourseDTO!!.name)
    }

    @Test
    fun deleteCourses() {
        val instructor = instructorRepository.findAll().first()
        val course = Course(null, "Build Restful APIs using SpringBoot2 and Kotlin2", "Development", instructor)
        courseRepository.save(course)

        webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", course.id)
            .exchange()
            .expectStatus().isNoContent
    }
}