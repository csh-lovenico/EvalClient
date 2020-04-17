package live.bokurano.evaluationclient.network

import com.squareup.moshi.JsonClass
import live.bokurano.evaluationclient.database.Evaluation


@JsonClass(generateAdapter = true)
data class CourseContainer(val courses: List<Course>)

@JsonClass(generateAdapter = true)
data class Course(
    val courseId: String,
    val courseName: String,
    val courseTeacher: String,
    val courseYear: Int,
    val courseSemester: Int
)

@JsonClass(generateAdapter = true)
data class TransferResult<T>(
    val status: Int,
    val result: T,
    val message: String
)


fun CourseContainer.asDomainModel(): List<Evaluation> {
    return courses.map {
        Evaluation(
            courseId = it.courseId,
            courseName = it.courseName,
            courseYear = it.courseYear,
            courseSemester = it.courseSemester,
            courseTeacher = it.courseTeacher
        )
    }
}