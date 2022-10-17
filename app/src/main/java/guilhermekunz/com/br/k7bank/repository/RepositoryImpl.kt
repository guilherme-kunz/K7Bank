package guilhermekunz.com.br.k7bank.repository

import guilhermekunz.com.br.k7bank.api.BankApi
import guilhermekunz.com.br.k7bank.api.model.ApiError
import guilhermekunz.com.br.k7bank.api.response.DetailStatementResponse
import guilhermekunz.com.br.k7bank.api.response.MyBalanceResponse
import guilhermekunz.com.br.k7bank.api.response.MyStatementResponse
import guilhermekunz.com.br.k7bank.api.response.NetworkResponse
import guilhermekunz.com.br.k7bank.dao.MyBalanceDao

class RepositoryImpl(val api: BankApi) : Repository {

    private val TOKEN =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"

    override suspend fun getMyBalance(): NetworkResponse<MyBalanceResponse?> {
        return try {
            val response = api.getMyBalance(TOKEN)
            if (response.isSuccessful) {
                NetworkResponse.Success(response.body()!!)
            } else {
                NetworkResponse.Failed(Exception())
            }
        } catch (e: Exception) {
            NetworkResponse.Failed(ApiError.GenericException())
        }
    }

    override suspend fun getMyStatement(
        limit: String,
        offset: String
    ): NetworkResponse<MyStatementResponse?> {
        return try {
            val response = api.getMyStatement(TOKEN, limit, offset)
            if (response.isSuccessful) {
                NetworkResponse.Success(response.body()!!)
            } else {
                NetworkResponse.Failed(Exception())
            }
        } catch (e: Exception) {
            NetworkResponse.Failed(ApiError.GenericException())
        }
    }

    override suspend fun getMyStatementDetail(id: String): NetworkResponse<DetailStatementResponse?> {
        return try {
            val response = api.getMyStatementDetail(TOKEN, id)
            if (response.isSuccessful) {
                NetworkResponse.Success(response.body()!!)
            } else {
                NetworkResponse.Failed(Exception())
            }
        } catch (e: Exception) {
            NetworkResponse.Failed(ApiError.GenericException())
        }
    }

}

interface Repository {
    suspend fun getMyBalance(): NetworkResponse<MyBalanceResponse?>
    suspend fun getMyStatement(limit: String, offset: String): NetworkResponse<MyStatementResponse?>
    suspend fun getMyStatementDetail(id: String): NetworkResponse<DetailStatementResponse?>
}