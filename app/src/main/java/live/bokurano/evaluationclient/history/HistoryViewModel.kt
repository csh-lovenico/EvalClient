package live.bokurano.evaluationclient.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import live.bokurano.evaluationclient.network.EvalApi
import live.bokurano.evaluationclient.network.LoginResponse
import live.bokurano.evaluationclient.network.WebEvaluation
import timber.log.Timber

class HistoryViewModel(loginResponse: LoginResponse) : ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main
    )

    private val token = loginResponse.jwtToken
    private val userId = loginResponse.userId
    private val _historyList = MutableLiveData<List<WebEvaluation>>()
    val historyList: LiveData<List<WebEvaluation>>
        get() = _historyList

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        coroutineScope.launch {
            val historyDeferred = EvalApi.retrofitService.getHistoryAsync(token, userId)
            try {
                val response = historyDeferred.await()
                _historyList.value = response.result
                Timber.i(response.toString())
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}
