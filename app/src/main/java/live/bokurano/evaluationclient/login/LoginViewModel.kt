package live.bokurano.evaluationclient.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import live.bokurano.evaluationclient.network.EvalApi
import live.bokurano.evaluationclient.network.LoginUser
import retrofit2.HttpException
import timber.log.Timber

class LoginViewModel : ViewModel() {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main
    )

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun login(loginUser: LoginUser) {
        coroutineScope.launch {
            val loginDeferred = EvalApi.retrofitService.userLoginAsync(loginUser)
            Timber.i(loginUser.toString())
            try {
                val response = loginDeferred.await()
                Timber.i(response.toString())
            } catch (e: HttpException) {
                Timber.e("${e.code()} ${e.message()}")
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}
