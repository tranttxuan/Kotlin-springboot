package com.kotlinspring.entity

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "Courses")
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,
    @get:NotBlank(message = "courseDTO.name must not be blank")
    var name: String,
    @get:NotBlank(message = "courseDTO.category must not be blank")
    var category: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="INSTRUCTOR_ID", nullable = false)
    val instructor: Instructor? = null
)
