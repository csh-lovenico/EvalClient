package live.bokurano.evaluationclient.overview

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.*
import live.bokurano.evaluationclient.database.EvaluationDao
import live.bokurano.evaluationclient.database.EvaluationDatabase
import live.bokurano.evaluationclient.network.EvalApi
import live.bokurano.evaluationclient.network.LoginResponse
import live.bokurano.evaluationclient.repository.EvaluationRepository
import retrofit2.HttpException
import timber.log.Timber

class OverviewViewModel(
    val database: EvaluationDao,
    application: Application,
    loginResponse: LoginResponse
) : ViewModel() {

    private var viewModelJob = SupervisorJob()

    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main
    )

    private val _uploadSuccess = MutableLiveData<Boolean>()
    val uploadSuccess: LiveData<Boolean>
        get() = _uploadSuccess

    private val _navigateToDetail = MutableLiveData<Long>()
    val navigateToDetail: LiveData<Long>
        get() = _navigateToDetail

    private val _tooManyFullStar = MutableLiveData<Boolean>()
    val tooManyFullStar: LiveData<Boolean>
        get() = _tooManyFullStar

    private val _uploadError = MutableLiveData<Boolean>()
    val uploadError: LiveData<Boolean>
        get() = _uploadError

    private val savedResponse = loginResponse

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean>
        get() = _loginSuccess

    private val _credentialExpired = MutableLiveData<Boolean>()
    val credentialExpired: LiveData<Boolean>
        get() = _credentialExpired

    private val _notLoggedIn = MutableLiveData<Boolean>()
    val notLoggedIn: LiveData<Boolean>
        get() = _notLoggedIn

    private val _networkError = MutableLiveData<Boolean>()
    val networkError: LiveData<Boolean>
        get() = _networkError

    private val evaluationRepository =
        EvaluationRepository(EvaluationDatabase.getInstance(application), loginResponse)

    val evaluationList = evaluationRepository.evaluations

    val unfinished = Transformations.map(evaluationList) {
        evaluationList.value?.stream()?.filter { it.rate == 0 && !it.complete }?.count().toString()
    }

    val finished = Transformations.map(evaluationList) {
        evaluationList.value?.stream()?.filter { it.rate != 0 && !it.complete }?.count().toString()
    }

    val halfStar = Transformations.map(evaluationList) {
        evaluationList.value?.stream()?.filter { it.rate in 1..4 && !it.complete }?.count()
            .toString()
    }

    val fullStar = Transformations.map(evaluationList) {
        evaluationList.value?.stream()?.filter { it.rate == 5 && !it.complete }?.count().toString()
    }

    init {
        refreshDataFromRepository()
    }

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                evaluationRepository.refreshData()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun onEvaluationClicked(evalId: Long) {
        Timber.i(evalId.toString())
        _navigateToDetail.value = evalId
    }

    fun onNavigateComplete() {
        _navigateToDetail.value = null
    }

    fun checkLoginState() {
        Timber.i(savedResponse.toString())
        if (savedResponse.jwtToken == "undefined") {
            notLoggedIn()
        } else {
            coroutineScope.launch {
                val testDeferred =
                    EvalApi.retrofitService.connectionTestAsync(savedResponse.jwtToken)
                try {
                    val response = testDeferred.await()
                    if (response["status"] == "ok") {
                        _loginSuccess.value = true
                        _notLoggedIn.value = false
                    }
                } catch (e: HttpException) {
                    Timber.e(e)
                    notLoggedIn()
                    if (e.code() == 401) {
                        _credentialExpired.value = true
                    } else {
                        _networkError.value = true
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                    notLoggedIn()
                    _networkError.value = true
                }
            }
        }
    }

    fun checkState() {
        if (fullStar.value!!.toInt() > finished.value!!.toInt() * 0.2) {
            _tooManyFullStar.value = true
        } else {
            postEvaluations()
        }
    }


    private fun postEvaluations() {
        coroutineScope.launch {
            val postDeferred = EvalApi.retrofitService.postEvaluationAsync(
                savedResponse.jwtToken,
                evaluationList.value!!
            )
            try {
                val response = postDeferred.await()
                Timber.i(response.status.toString())
                setEvaluationCompleted()
                _uploadSuccess.value = true

            } catch (e: Exception) {
                Timber.e(e)
                _uploadError.value = true
            }
        }
    }

    private suspend fun setEvaluationCompleted() {
        withContext(Dispatchers.IO) {
            evaluationList.value!!.stream().forEach {
                it.complete = true
            }
            database.updateAll(evaluationList.value!!)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun notLoggedIn() {
        _notLoggedIn.value = true
        _loginSuccess.value = false
    }

    fun setStateComplete() {
        _credentialExpired.value = false
        _networkError.value = false
    }

    fun setUploadStateComplete() {
        _tooManyFullStar.value = null
        _uploadSuccess.value = null
        _uploadError.value = null
    }
}
