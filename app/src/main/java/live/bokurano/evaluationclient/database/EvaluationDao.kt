package live.bokurano.evaluationclient.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EvaluationDao {
    @Insert
    fun insertAll(evaluations: List<Evaluation>)

    @Update
    fun update(evaluation: Evaluation)

    @Query("SELECT * FROM evaluation WHERE current_student_id = :studentId AND complete = 0")
    fun findEvaluationByStudentId(studentId: String): LiveData<List<Evaluation>>

    @Query("SELECT * FROM evaluation WHERE evalId = :id")
    fun findEvaluationById(id: Long): Evaluation?

    @Query("DELETE FROM evaluation")
    fun clearAll()
}