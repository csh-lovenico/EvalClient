package live.bokurano.evaluationclient.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import live.bokurano.evaluationclient.network.LoginResponse

@Suppress("UNCHECKED_CAST")
class HistoryViewModelFactory(
    private val loginResponse: LoginResponse
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(loginResponse) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}