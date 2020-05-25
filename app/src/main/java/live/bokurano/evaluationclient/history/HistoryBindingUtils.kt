package live.bokurano.evaluationclient.history

import android.widget.TextView
import androidx.databinding.BindingAdapter
import live.bokurano.evaluationclient.R
import live.bokurano.evaluationclient.network.WebEvaluation

@BindingAdapter("courseName")
fun TextView.setCourseName(item: WebEvaluation?) {
    item?.let {
        text = item.courseName
    }
}

@BindingAdapter("courseTeacher")
fun TextView.setCourseTeacher(item: WebEvaluation?) {
    item?.let {
        text = context.resources.getString(R.string.course_teacher, item.courseTeacher)
    }
}

@BindingAdapter("courseYear")
fun TextView.setCourseYear(item: WebEvaluation?) {
    item?.let {
        text =
            context.resources.getString(R.string.course_year, item.courseYear, item.courseYear + 1)
    }
}

@BindingAdapter("courseSemester")
fun TextView.setCourseSemester(item: WebEvaluation?) {
    item?.let {
        text = context.resources.getString(R.string.course_semester, item.courseSemester)
    }
}

@BindingAdapter("courseRate")
fun TextView.setCourseRate(item: WebEvaluation?) {
    item?.let {
        text = context.resources.getString(R.string.history_rate, it.rate.toString())
    }
}

@BindingAdapter("courseComment")
fun TextView.setCourseComment(item: WebEvaluation?) {
    item?.let {
        text = context.resources.getString(R.string.history_comment, item.comment)
    }
}