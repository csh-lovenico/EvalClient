package live.bokurano.evaluationclient.teacher

import android.widget.TextView
import androidx.databinding.BindingAdapter
import live.bokurano.evaluationclient.R
import live.bokurano.evaluationclient.network.WebStat

@BindingAdapter("courseName")
fun TextView.setCourseName(item: WebStat?) {
    item?.let {
        text = item.courseName
    }
}

@BindingAdapter("courseYear")
fun TextView.setCourseYear(item: WebStat?) {
    item?.let {
        text =
            context.resources.getString(R.string.course_year, item.courseYear, item.courseYear + 1)
    }
}

@BindingAdapter("courseSemester")
fun TextView.setCourseSemester(item: WebStat?) {
    item?.let {
        text = context.resources.getString(R.string.course_semester, item.courseSemester)
    }
}

@BindingAdapter("averageRating")
fun TextView.setAverageRate(item: WebStat?) {
    item?.let {
        text = context.resources.getString(R.string.stat_average, item.average.toString())
    }
}

@BindingAdapter("studentNum")
fun TextView.setStudentNum(item: WebStat?) {
    item?.let {
        text = context.resources.getString(R.string.stat_student_num, item.studentNum)
    }
}

