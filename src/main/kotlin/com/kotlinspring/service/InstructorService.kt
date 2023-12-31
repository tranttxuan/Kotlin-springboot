package com.kotlinspring.service

import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.entity.Instructor
import com.kotlinspring.repository.InstructorRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class InstructorService(val instructorRepository: InstructorRepository) {

    fun createInstructor(instructorDTO: InstructorDTO): InstructorDTO {
        val instructor = instructorDTO.let { Instructor(it.id, it.name) }
        val createdInstructor = instructorRepository.save(instructor)
        return createdInstructor.let { InstructorDTO(it.id, it.name) }
    }

    fun findByInstructorId(instructorId: Int): Optional<Instructor> {
        return instructorRepository.findById(instructorId)
    }

    fun retrieveAllInstructors(): List<InstructorDTO> {
        return instructorRepository.findAll().map { InstructorDTO(it.id, it.name) }
    }
}
