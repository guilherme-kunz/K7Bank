package guilhermekunz.com.br.k7bank.api.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DetailStatementResponse(
    @SerializedName("amount") val amount: Int,
    @SerializedName("authentication") val authentication: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: String,
    @SerializedName("tType") val tType: String,
    @SerializedName("to") val to: String,
    @SerializedName("from") val from: String,
    @SerializedName("bankName") val bankName: String
): Serializable