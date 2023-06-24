package com.kotlinspring.repository;

import courseEntityList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryTest {
    @Autowired
    lateinit var courseRepository: CourseRepository;

    @BeforeEach
    fun setup() {
        courseRepository.deleteAll();
        courseRepository.saveAll(courseEntityList())
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
