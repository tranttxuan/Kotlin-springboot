package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.repository.CourseRepository
import courseEntityList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerIntegrationTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun settup(){
        courseRepository.deleteAll()
        val courses = courseEntityList()
        courseRepository.saveAll(courses)

    }

    @Test
    fun addCourse() {
        val courseDTO = CourseDTO(null, "Build Restful APIs using SpringBoot and Kotlin", "Development")
        val saveResult = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(courseDTO::class.java)
            .returnResult()
            .responseBody
        Assertions.assertTrue {
            saveResult!!.id != null
        }
    }

    @Test
    fun retrieveAllCourses() {
        val courseDTO = CourseDTO(null, "Build Restful APIs using SpringBoot and Kotlin", "Development")
        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(courseDTO::class.java)
            .returnResult()
            .responseBody
       Assertions.assertEquals(3, courseDTOs!!.size)
    }


}