package live.bokurano.evaluationclient

import android.app.Application
import timber.log.Timber

class TimberConfiguration: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}