package live.bokurano.evaluationclient.database

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "evaluation")
@TypeConverters(ListConverter::class)
data class Evaluation(
    @PrimaryKey(autoGenerate = true)
    val evalId: Long = 0L,
    @ColumnInfo(name = "course_id")
    var courseId: String,
    @ColumnInfo(name = "course_name")
    var courseName: String,
    @ColumnInfo(name = "course_teacher")
    var courseTeacher: String,
    @ColumnInfo(name = "course_teacher_id")
    var courseTeacherId: String,
    @ColumnInfo(name = "course_year")
    var courseYear: Int,
    @ColumnInfo(name = "course_semester")
    var courseSemester: Int,
    @ColumnInfo(name = "current_student_id")
    var currentStudentId: String = "",
    @ColumnInfo(name = "rate")
    var rate: List<Int>,
    @ColumnInfo(name = "comment")
    var comment: String = "",
    @ColumnInfo(name = "complete")
    var complete: Boolean = false
)

class ListConverter {
    val gson = Gson()

    @TypeConverter
    fun listToString(list: List<Int>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToList(source: String): List<Int> {
        return gson.fromJson(source, object : TypeToken<List<Int>>() {}.type)
    }
}