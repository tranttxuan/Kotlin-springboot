package com.kotlinspring.entity

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "Instructors")
data class Instructor(
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    val id: Int?,
    @get:NotBlank(message = "InstructorDTO.name must not be blank")
    val name: String,
    @OneToMany(mappedBy = "instructor", cascade = [CascadeType.ALL], orphanRemoval = true)
    var courses: List<Course> = mutableListOf()
)
