package live.bokurano.evaluationclient.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import live.bokurano.evaluationclient.database.Evaluation
import live.bokurano.evaluationclient.database.EvaluationDatabase
import live.bokurano.evaluationclient.network.CourseContainer
import live.bokurano.evaluationclient.network.EvalApi
import live.bokurano.evaluationclient.network.LoginResponse
import live.bokurano.evaluationclient.network.asDomainModel
import timber.log.Timber

class EvaluationRepository(
    private val database: EvaluationDatabase,
    private val loginResponse: LoginResponse
) {

    val evaluations: LiveData<List<Evaluation>> =
        database.evaluationDao.findEvaluationByStudentId(loginResponse.userId)

    suspend fun refreshData() {
        withContext(Dispatchers.IO) {
            if (loginResponse.jwtToken == "undefined") {
                return@withContext
            }
            val courseResult = EvalApi.retrofitService.getUnevaluatedCoursesAsync(
                loginResponse.jwtToken,
                loginResponse.userId
            ).await()
            if (evaluations.value.isNullOrEmpty() && courseResult.result.isNotEmpty()) {
                val evalList = CourseContainer(courseResult.result).asDomainModel()
                evalList.stream().forEach {
                    it.currentStudentId = loginResponse.userId
                }
                Timber.i("will insert $evalList")
                database.evaluationDao.insertAll(evalList)
            }
        }
    }
}