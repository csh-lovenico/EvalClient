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
    val courseTeacherId: String,
    val courseYear: Int,
    val courseSemester: Int
)

data class WebEvaluation(
    val id: String,
    val currentStudentId: String,
    val courseId: String,
    val courseName: String,
    val courseTeacher: String,
    val courseTeacherId: String,
    val courseSemester: Int,
    val courseYear: Int,
    val rate: List<Int>,
    val comment: String
)

data class WebStat(
    val id: String,
    val courseId: String,
    val courseName: String,
    val courseTeacher: String,
    val courseTeacherId: String,
    val courseSemester: Int,
    val courseYear: Int,
    val average: List<Double>,
    val studentNum: Int,
    val comments: List<String>
)

@JsonClass(generateAdapter = true)
data class TransferResult<T>(
    val status: Int,
    val result: T?,
    val message: String?
)


fun CourseContainer.asDomainModel(): List<Evaluation> {
    return courses.map {
        Evaluation(
            courseId = it.courseId,
            courseName = it.courseName,
            courseYear = it.courseYear,
            courseSemester = it.courseSemester,
            courseTeacher = it.courseTeacher,
            courseTeacherId = it.courseTeacherId,
            rate = arrayListOf(0, 0, 0, 0, 0, 0)
        )
    }
}