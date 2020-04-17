package live.bokurano.evaluationclient.overview

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import live.bokurano.evaluationclient.database.EvaluationDao
import live.bokurano.evaluationclient.network.LoginResponse

class OverviewViewModelFactory(
    private val dataSource: EvaluationDao,
    private val application: Application,
    private val loginResponse: LoginResponse
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        @Suppress("unchecked_cast")
        if (modelClass.isAssignableFrom(OverviewViewModel::class.java)) {
            return OverviewViewModel(dataSource, application, loginResponse) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}