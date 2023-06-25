package com.kotlinspring.repository

import com.kotlinspring.util.PostgreSQLContainerInitializer
import courseEntityList
import instructorEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream

//@TestInstance(TestInstance.Lifecycle.PER_CLASS) #test with in-memory h2
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class CourseRepositoryTest: PostgreSQLContainerInitializer() {
    @Autowired
    lateinit var courseRepository: CourseRepository
    @Autowired
    lateinit var instructorRepository: InstructorRepository

    @BeforeEach
    fun setup() {
        courseRepository.deleteAll()
        instructorRepository.deleteAll()
        val instructor = instructorEntity()
        instructorRepository.save(instructor)
        courseRepository.saveAll(courseEntityList(instructor))
    }

    @Test
    fun findByNameContaining() {
        val courses = courseRepository.findByNameContaining("SpringBoot")
        Assertions.assertEquals(2, courses.size)
    }

    @Test
    fun findCourseByName() {
        val courses = courseRepository.findCourseByName("SpringBoot")
        Assertions.assertEquals(2, courses.size)
    }

    @ParameterizedTest
    @MethodSource("courseAndSize")
    fun findCoursesByName_multipleTest(name: String, expectedSize: Int) {
        val courses = courseRepository.findCourseByName(name)
        Assertions.assertEquals(expectedSize, courses.size)
    }

    companion object {
        @JvmStatic
        fun courseAndSize(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments("SpringBoot", 2),
                Arguments.arguments("Kotlin", 1),
                Arguments.arguments("NodeJs", 1)
            )
        }
    }
}
