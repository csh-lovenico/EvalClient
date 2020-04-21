package live.bokurano.evaluationclient.detail

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import live.bokurano.evaluationclient.database.EvaluationDao

class DetailViewModel(
    val database: EvaluationDao,
    val evalId: Long
) : ViewModel() {

    val rate: ObservableField<Int> = ObservableField()

    val comment: ObservableField<String> = ObservableField()

    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main
    )

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    val evaluation = database.findEvaluationById(evalId)

    private val _validateFail = MutableLiveData<Boolean>()
    val validateFail: LiveData<Boolean>
        get() = _validateFail

    private val _validateSuccess = MutableLiveData<Boolean>()
    val validateSuccess: LiveData<Boolean>
        get() = _validateSuccess

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    fun validateForm(rate: Int, comment: String) {
        if (rate == 0 || comment.length < 10) {
            _validateFail.value = true
        } else {
            _validateSuccess.value = true
            saveEvaluation(rate, comment)
        }
    }

    fun validateComplete() {
        _validateFail.value = null
        _validateSuccess.value = null
    }

    private fun saveEvaluation(rate: Int, comment: String) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                val evaluationToModify = evaluation.value
                evaluationToModify?.let {
                    it.comment = comment
                    it.rate = rate
                    database.update(it)
                }
            }
            _navigateBack.value = true
        }
    }

    fun doneNavigating() {
        _navigateBack.value = null
    }
}
