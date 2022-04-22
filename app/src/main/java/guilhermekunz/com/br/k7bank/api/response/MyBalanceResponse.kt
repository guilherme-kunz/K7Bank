package guilhermekunz.com.br.k7bank.api.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MyBalanceResponse(
   @SerializedName("amount") val amount: Int
): Serializable