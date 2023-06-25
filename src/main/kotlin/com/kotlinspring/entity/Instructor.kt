package com.kotlinspring.entity

import javax.persistence.*

@Entity
@Table(name = "INSTRUCTORS")
data class Instructor(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?,
    val name: String,
    @OneToMany(
        mappedBy = "instructor",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var courses: List<Course> = mutableListOf()
)
