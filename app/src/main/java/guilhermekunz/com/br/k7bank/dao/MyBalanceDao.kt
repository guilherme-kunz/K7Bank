package guilhermekunz.com.br.k7bank.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import guilhermekunz.com.br.k7bank.api.response.MyBalanceResponse

@Dao
interface MyBalanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(myBalance: MyBalanceResponse)

    @Delete
    suspend fun remove(myBalance: MyBalanceResponse)

    @Update
    suspend fun update(myBalance: MyBalanceResponse)

    @Query("SELECT * FROM table_my_balance")
    fun getAll() : LiveData<List<MyBalanceResponse>>

}