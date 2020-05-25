package live.bokurano.evaluationclient.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import live.bokurano.evaluationclient.database.EvaluationDao

class DetailViewModel(
    val database: EvaluationDao,
    val evalId: Long
) : ViewModel() {

    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main
    )

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    val evaluation = database.findEvaluationById(evalId)

    var rate = Transformations.map(evaluation) {
        it.rate
    }
    val rateToEdit = ArrayList<Int>()
    val scoreList = Transformations.map(rate) {
        itemList.map { WithScore(it.id, it.title, it.desc, rate.value?.get(it.id)!!) }
    }

    private val _validateFail = MutableLiveData<Boolean>()
    val validateFail: LiveData<Boolean>
        get() = _validateFail

    private val _validateSuccess = MutableLiveData<Boolean>()
    val validateSuccess: LiveData<Boolean>
        get() = _validateSuccess

    init {

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun validateForm(comment: String) {
        if (rateToEdit.stream().filter { it == 0 }.count() > 0L || comment.length < 10) {
            _validateFail.value = true
        } else {
            _validateSuccess.value = true
            saveEvaluation(comment)
        }
    }

    fun validateComplete() {
        _validateFail.value = null
        _validateSuccess.value = null
    }

    private fun saveEvaluation(comment: String) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val evaluationToModify = evaluation.value
                evaluationToModify?.let {
                    it.comment = comment
                    it.rate = rateToEdit
                    database.update(it)
                }
            }
            _navigateBack.value = true
        }
    }

    fun doneNavigating() {
        _navigateBack.value = null
    }

    companion object {
        val itemList = listOf(
            TextItem(0, "教师表现1", "教学内容正确，适当，丰富"),
            TextItem(1, "教师表现2", "理论联系实际，科学性与实践性相结合，体现学术前沿和科学技术新成就"),
            TextItem(2, "教师表现3", "适当地运用多种教学技巧，表达清晰生动，激发和引导学生积极思考和积极学习"),
            TextItem(3, "教师表现4", "遵守教学管理规定，充分熟练地准备课程，正确地控制教室，举止得体，认真负责"),
            TextItem(4, "教学效果", "课堂气氛积极和谐，师生互动自然，有利于学生的知识理解和培养学生的问题分析解决能力"),
            TextItem(5, "持续改进", "通过观察，提问，课堂询问，作业，测验，调查等方式评价课堂教学效果，并将评估结果用于课堂教学质量的持续提高")
        )
    }
}
