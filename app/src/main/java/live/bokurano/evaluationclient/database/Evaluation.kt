package live.bokurano.evaluationclient.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "evaluation")
data class Evaluation(
    @PrimaryKey(autoGenerate = true)
    val evalId: Long = 0L,
    @ColumnInfo(name = "course_id")
    var courseId: String,
    @ColumnInfo(name = "course_name")
    var courseName: String,
    @ColumnInfo(name = "course_teacher")
    var courseTeacher: String,
    @ColumnInfo(name = "course_year")
    var courseYear: Int,
    @ColumnInfo(name = "course_semester")
    var courseSemester: Int,
    @ColumnInfo(name = "current_student_id")
    var currentStudentId: String = "",
    @ColumnInfo(name = "rate")
    var rate: Int = 0,
    @ColumnInfo(name = "comment")
    var comment: String = "",
    @ColumnInfo(name = "complete")
    var complete: Boolean = false
)