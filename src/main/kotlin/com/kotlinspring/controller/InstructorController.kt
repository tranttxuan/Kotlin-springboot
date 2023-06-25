package com.kotlinspring.controller

import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.service.InstructorService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/v1/instructors")
@Validated
class InstructorController(val instructorService: InstructorService) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun retrieveAllInstructors(): List<InstructorDTO>{
        return instructorService.retrieveAllInstructors()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createInstructor(@RequestBody  @Valid instructorDTO: InstructorDTO): InstructorDTO {
        return instructorService.createInstructor(instructorDTO)
    }
}