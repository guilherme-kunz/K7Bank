package guilhermekunz.com.br.k7bank.api

import guilhermekunz.com.br.k7bank.api.response.MyBalanceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface BankApi {

    @GET("myBalance")
    suspend fun getMyBalance(
        @Header("token") token: String
    ): Response<MyBalanceResponse>

}