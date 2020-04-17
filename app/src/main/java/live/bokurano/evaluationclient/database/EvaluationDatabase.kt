package live.bokurano.evaluationclient.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Evaluation::class], version = 1, exportSchema = false)
abstract class EvaluationDatabase : RoomDatabase() {
    abstract val evaluationDao: EvaluationDao

    companion object {
        @Volatile
        private var INSTANCE: EvaluationDatabase? = null

        fun getInstance(context: Context): EvaluationDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EvaluationDatabase::class.java,
                        "evaluation"
                    ).fallbackToDestructiveMigration().build()
                }
                return instance
            }
        }
    }
}