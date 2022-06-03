package guilhermekunz.com.br.k7bank.api.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MyStatementResponse(
    @SerializedName("items") val myStatementItems: List<MyStatementItem>
): Serializable

data class MyStatementItem(
    @SerializedName("amount") val amount: Int,
    @SerializedName("bankName") val bankName: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("description") val description: String,
    @SerializedName("from") val from: String,
    @SerializedName("id") val id: String,
    @SerializedName("tType") val tType: String,
    @SerializedName("to") val to: String
):Serializable