package live.bokurano.evaluationclient.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import live.bokurano.evaluationclient.database.EvaluationDao

class DetailViewModelFactory(
    private val dataSource: EvaluationDao,
    private val evalId: Long
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(dataSource, evalId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}