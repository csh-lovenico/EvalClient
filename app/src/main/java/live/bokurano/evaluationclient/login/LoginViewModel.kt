package live.bokurano.evaluationclient.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import live.bokurano.evaluationclient.network.EvalApi
import live.bokurano.evaluationclient.network.LoginResponse
import live.bokurano.evaluationclient.network.LoginUser
import retrofit2.HttpException

class LoginViewModel : ViewModel() {
    private val _showIncorrectToast = MutableLiveData<Boolean>()
    val showIncorrectToast: LiveData<Boolean>
        get() = _showIncorrectToast

    private val _showErrorToast = MutableLiveData<Boolean>()
    val showErrorToast: LiveData<Boolean>
        get() = _showErrorToast

    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean>
        get() = _navigateToMain

    private val _response = MutableLiveData<LoginResponse>()
    val response: LiveData<LoginResponse>
        get() = _response

    private val _isLoggingIn = MutableLiveData<Boolean>()
    val isLoggingIn: LiveData<Boolean>
        get() = _isLoggingIn

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main
    )

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun login(loginUser: LoginUser) {
        _isLoggingIn.value = true
        coroutineScope.launch {
            val loginDeferred = EvalApi.retrofitService.userLoginAsync(loginUser)
            try {
                val response = loginDeferred.await()
                _response.value = response
                _navigateToMain.value = true
            } catch (e: HttpException) {
                _response.value = null
                if (e.code() == 401) {
                    _showIncorrectToast.value = true
                } else {
                    _showErrorToast.value = true
                }
            } catch (e: Exception) {
                _response.value = null
                _showErrorToast.value = true
            }
        }
    }

    fun showIncorrectToastComplete() {
        _showIncorrectToast.value = false
    }

    fun showErrorToastComplete() {
        _showErrorToast.value = false
    }

    fun loginComplete() {
        _isLoggingIn.value = false
    }

    fun navigateComplete() {
        _navigateToMain.value = false
    }

    fun clearCache() {
        _response.value = null
    }

}
