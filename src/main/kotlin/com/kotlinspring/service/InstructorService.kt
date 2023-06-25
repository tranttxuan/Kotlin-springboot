package com.kotlinspring.service

import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.entity.Instructor
import com.kotlinspring.repository.InstructorRepository
import org.springframework.stereotype.Service

@Service
class InstructorService(val instructorRepository: InstructorRepository) {

    fun createInstructor(instructorDTO: InstructorDTO): InstructorDTO {
        val instructor = instructorDTO.let { Instructor(it.id, it.name) }
        val createdInstructor = instructorRepository.save(instructor)
        return createdInstructor.let { InstructorDTO(it.id, it.name) }
    }
}
