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
        evaluationList.value?.stream()?.filter { it.rate == 0 }?.count().toString()
    }

    val finished = Transformations.map(evaluationList) {
        evaluationList.value?.stream()?.filter { it.rate != 0 }?.count().toString()
    }

    val halfStar = Transformations.map(evaluationList) {
        evaluationList.value?.stream()?.filter { it.rate in 1..4 }?.count().toString()
    }

    val fullStar = Transformations.map(evaluationList) {
        evaluationList.value?.stream()?.filter { it.rate == 5 }?.count().toString()
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
}
