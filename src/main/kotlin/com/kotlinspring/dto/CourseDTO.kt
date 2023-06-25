package com.kotlinspring.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


data class CourseDTO(
    val id: Int?,
    @get:NotBlank(message = "courseDTO.name must not be blank")
    var name: String,
    @get:NotNull(message = "courseDTO.category must not be blank")
    val category: String
)