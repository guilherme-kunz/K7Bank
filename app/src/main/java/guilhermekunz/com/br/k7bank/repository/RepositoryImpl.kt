package guilhermekunz.com.br.k7bank.repository

import guilhermekunz.com.br.k7bank.api.BankApi
import guilhermekunz.com.br.k7bank.api.response.DetailStatementResponse
import guilhermekunz.com.br.k7bank.api.response.MyBalanceResponse
import guilhermekunz.com.br.k7bank.api.response.MyStatementResponse

class RepositoryImpl(val api: BankApi) : Repository {

    private val TOKEN =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5csdfsdf"

    override suspend fun getMyBalance(): MyBalanceResponse? {
        val response = api.getMyBalance(TOKEN)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    override suspend fun getMyStatement(limit: String, offset: String): MyStatementResponse? {
        val response = api.getMyStatement(TOKEN, limit, offset)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    override suspend fun getMyStatementDetail(id: String): DetailStatementResponse? {
        val response = api.getMyStatementDetail(TOKEN, id)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

}

interface Repository {
    suspend fun getMyBalance(): MyBalanceResponse?
    suspend fun getMyStatement(limit: String, offset: String): MyStatementResponse?
    suspend fun getMyStatementDetail(id: String): DetailStatementResponse?
}