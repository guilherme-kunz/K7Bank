package guilhermekunz.com.br.k7bank.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import guilhermekunz.com.br.k7bank.api.response.MyBalanceResponse
import guilhermekunz.com.br.k7bank.dao.MyBalanceDao

@Database(
    entities = [MyBalanceResponse::class],
    version = 1,
    exportSchema = false
)
abstract class MyBalanceDatabase : RoomDatabase() {

    abstract val myBalanceDao: MyBalanceDao

    companion object {

        @Volatile
        private var INSTANCE: MyBalanceDatabase? = null

        fun getInstance(context: Context): MyBalanceDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyBalanceDatabase::class.java,
                        "mybalancehash_db"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

}