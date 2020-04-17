package live.bokurano.evaluationclient.overview

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import live.bokurano.evaluationclient.R
import live.bokurano.evaluationclient.database.Evaluation

@BindingAdapter("courseName")
fun TextView.setCourseName(item: Evaluation?) {
    item?.let {
        text = item.courseName
    }
}

@BindingAdapter("courseTeacher")
fun TextView.setCourseTeacher(item: Evaluation?) {
    item?.let {
        text = context.resources.getString(R.string.course_teacher, item.courseTeacher)
    }
}

@BindingAdapter("courseYear")
fun TextView.setCourseYear(item: Evaluation?) {
    item?.let {
        text =
            context.resources.getString(R.string.course_year, item.courseYear, item.courseYear + 1)
    }
}

@BindingAdapter("courseSemester")
fun TextView.setCourseSemester(item: Evaluation?) {
    item?.let {
        text = context.resources.getString(R.string.course_semester, item.courseSemester)
    }
}

@BindingAdapter("starStatus")
fun ImageView.setStarStatus(item: Evaluation?) {
    item?.let {
        when (item.rate) {
            0 -> {
                setImageResource(R.drawable.ic_star_black_24dp)
            }
            5 -> {
                setImageResource(R.drawable.ic_star_yellow_24dp)
            }
            else -> {
                setImageResource(R.drawable.ic_star_half_yellow_24dp)
            }
        }
    }
}