package com.kotlinspring.controller

import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.service.InstructorService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest

import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [InstructorController::class])
@AutoConfigureWebTestClient
class InstructorControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient
    @MockkBean
    lateinit var instructorServiceMock: InstructorService

    @Test
    fun addInstructor(){
        val instructorDTO = InstructorDTO(null, "XXX")
        every { instructorServiceMock.createInstructor( any()) } returns InstructorDTO(1, "XXX")

        val savedInstructorDTO = webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(instructorDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(InstructorDTO::class.java)
            .returnResult()
            .responseBody
        Assertions.assertTrue(savedInstructorDTO!!.id != null)
    }
    @Test
    fun addInstructor_validation() {
        val instructorDTO = InstructorDTO(null, "")
        val messageError = "instructorDTO.name must not be blank"
        every { instructorServiceMock.createInstructor( any()) } throws Exception(messageError)
        val response = webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(instructorDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody
        Assertions.assertEquals(messageError, response)
    }
}