package guilhermekunz.com.br.k7bank.api.response

import com.google.gson.annotations.SerializedName

data class MyBalanceResponse(
   @SerializedName("amount") val amount: Int
)