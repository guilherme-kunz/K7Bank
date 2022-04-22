package guilhermekunz.com.br.k7bank.api

import guilhermekunz.com.br.k7bank.api.response.DetailStatementResponse
import guilhermekunz.com.br.k7bank.api.response.MyBalanceResponse
import guilhermekunz.com.br.k7bank.api.response.MyStatementResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface BankApi {

    @GET("myBalance")
    suspend fun getMyBalance(
        @Header("token") token: String
    ): Response<MyBalanceResponse>

    @GET("myStatement/{limit}/{offset}")
    suspend fun getMyStatement(
        @Header("token") token: String,
        @Path("limit") limit: String,
        @Path("offset") offset: String
    ): Response<MyStatementResponse>

    @GET("myStatement/detail/{id}")
    suspend fun getMyStatementDetail(
        @Header("token") token: String,
        @Path("id") id: String
    ): Response<DetailStatementResponse>

}