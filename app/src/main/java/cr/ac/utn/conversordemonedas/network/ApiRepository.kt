package cr.ac.utn.conversordemonedas.network

import retrofit2.Call

object ApiRepository {
    private val api = ApiClient.apiService

    //Conversion
    fun convertCurrency(from: String, to: String, amount: Double): Call<Map<String, Any>> {
        val body = mapOf(
            "from" to from,
            "to" to to,
            "amount" to amount
        )
        return api.convert(body)
    }

    //Get coins
    fun getCurrencies(): Call<List<Map<String, Any>>> {
        return api.getCurrencies()
    }


    //CRUD of coins
    fun createCurrency(code: String, name: String, rate: Double): Call<Map<String, Any>> {
        val body = mapOf(
            "code" to code,
            "name" to name,
            "rate" to rate
        )
        return api.createCurrency(body)
    }

    //Update currency
    fun updateCurrency(id: String, code: String, name: String, rate: Double): Call<Map<String, Any>> {
        val body = mapOf(
            "code" to code,
            "name" to name,
            "rate" to rate
        )
        return api.updateCurrency(id, body)
    }

    //Delete currency
    fun deleteCurrency(id: String): Call<Map<String, Any>> {
        return api.deleteCurrency(id)
    }
}