package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.service.CourseService
import com.ninjasquad.springmockk.MockkBean
import courseDTO
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient
import java.lang.RuntimeException

@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMock: CourseService

    @Test
    fun addCourse() {
        val courseDTO = CourseDTO(null, "Build Restful APIs using SpringBoot and Kotlin", "Development")

        every { courseServiceMock.addCourse(any()) } returns courseDTO(
            1,
            "Build Restful APIs using SpringBoot and Kotlin",
            "Development"
        )

        val savedCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody
        Assertions.assertTrue(savedCourseDTO!!.id != null)
    }

    @Test
    fun addCourse_validation() {
        val courseDTO = CourseDTO(null, "", "Development")

        val messageError = "courseDTO.name must not be blank"

        every { courseServiceMock.addCourse(any()) } throws Exception(messageError)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody
        Assertions.assertEquals(messageError, response)
    }

    @Test
    fun addCourse_runTimeException() {
        val courseDTO = CourseDTO(null, "Build Restful APIs using SpringBoot and Kotlin", "Development")

        val errorMessage = "Unexpected error occured"
        every { courseServiceMock.addCourse(any()) } throws RuntimeException(errorMessage)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody
        Assertions.assertEquals( errorMessage, response)
    }

    @Test
    fun retrieveAllCourses() {

        every { courseServiceMock.retrieveAllCourses(null) }.returnsMany(
            listOf(
                courseDTO(
                    1,
                    "Build Restful APIs using SpringBoot and Kotlin",
                    "Development"
                ),
                courseDTO(
                    2,
                    "Build Restful APIs using Kotlin",
                    "Development"
                )
            )
        )

        val savedCourseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody
        Assertions.assertTrue(savedCourseDTOs!!.size == 2)
    }

    @Test
    fun updateCourse() {
        every { courseServiceMock.updateCourse(any(), any()) } returns courseDTO(
            100,
            "Build Restful APIs using SpringBoot",
            "Development"
        )

        val updatedCourseDTO = CourseDTO(null, "Build Restful APIs using SpringBoot", "Development")

        val savedCourseDTO = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", 100)
            .bodyValue(updatedCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody
        Assertions.assertEquals("Build Restful APIs using SpringBoot", savedCourseDTO!!.name)
    }

    @Test
    fun deleteCourses() {
        every { courseServiceMock.deleteCourse(any()) } just runs

        webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", 100)
            .exchange()
            .expectStatus().isNoContent
    }

}