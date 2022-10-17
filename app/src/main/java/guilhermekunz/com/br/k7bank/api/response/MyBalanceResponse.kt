package guilhermekunz.com.br.k7bank.api.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "table_my_balance")
data class MyBalanceResponse(
   @PrimaryKey(autoGenerate = true)
   @SerializedName("amount") val amount: Int
): Serializable