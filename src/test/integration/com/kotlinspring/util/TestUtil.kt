import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course

fun courseEntityList() = listOf(
    Course(
        null,
        "Build RestFul APIs using SpringBoot and Kotlin", "Development"
    ),
    Course(
        null,
        "Build RestFul APIs using SpringBoot", "Development",
    ),
    Course(
        null,
        "Build RestFul APIs using NodeJs", "Development",
    )
)

fun courseDTO(
    id: Int? = null,
    name: String = "Build RestFul APis using SpringBoot and Kotlin",
    category: String= "Development")= CourseDTO(id, name, category)