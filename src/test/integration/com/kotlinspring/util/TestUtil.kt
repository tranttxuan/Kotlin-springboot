import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.entity.Instructor

fun courseDTO(
    id: Int? = null,
    name: String = "Build RestFul APis using SpringBoot and Kotlin",
    category: String = "Development",
    instructorId: Int? = null,
) = CourseDTO(id, name, category, instructorId)


fun courseEntityList(instructor: Instructor? = null) = listOf(
    Course(
        null,
        "Build RestFul APIs using SpringBoot and Kotlin", "Development",
        instructor
    ),
    Course(
        null,
        "Build RestFul APIs using SpringBoot", "Development",
        instructor
    ),
    Course(
        null,
        "Build RestFul APIs using NodeJs", "Development",
        instructor
    )
)

fun instructorEntity(name: String = "XXX") = Instructor(null, name)