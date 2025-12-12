package cr.ac.utn.conversordemonedas.network

import cr.ac.utn.conversordemonedas.model.Currency
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RemoteCurrencyDataManager {
    private val api: ApiService = ApiClient.apiService

    private fun mapToCurrency(m: Map<String, Any>, id: String?): Currency {
        //Search for code, symbol, or generate from name
        val code = m["code"]?.toString()
            ?: m["symbol"]?.toString()
            ?: m["name"]?.toString()?.take(3)?.uppercase()
            ?: "???"
        //Converts Firebase data
        val name = m["name"]?.toString() ?: ""
        val rate = when (val r = m["rate"]) {
            is Number -> r.toDouble()
            is String -> r.toDoubleOrNull() ?: 0.0
            else -> 0.0
        }
        return Currency(code = code, name = name, rate = rate, id = id)
    }

    //Gets all Firebase coins
    fun getAllCurrencies(onResult: (List<Currency>) -> Unit, onError: ((Throwable) -> Unit)? = null) {
        api.getCurrencies().enqueue(object : Callback<List<Map<String, Any>>> {
            override fun onResponse(call: Call<List<Map<String, Any>>>, response: Response<List<Map<String, Any>>>) {
                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()!!.mapIndexed { index, item ->
                        val id = item["id"]?.toString()
                        mapToCurrency(item, id)
                    }
                    onResult(list)
                } else {
                    onError?.invoke(Throwable("Invalid response: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                onError?.invoke(t)
            }
        })
    }

    //Add all Firebase coins
    fun addCurrency(code: String, name: String, rate: Double, onDone: (() -> Unit)? = null, onError: ((Throwable) -> Unit)? = null) {
        val body = mapOf("code" to code, "name" to name, "rate" to rate)
        api.createCurrency(body).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                if (response.isSuccessful) onDone?.invoke()
                else onError?.invoke(Throwable("Create failed: ${response.code()}"))
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) { onError?.invoke(t) }
        })
    }

    //Update Firebase coins
    fun updateCurrency(id: String, name: String, rate: Double, onDone: (() -> Unit)? = null, onError: ((Throwable) -> Unit)? = null) {
        val body = mapOf("name" to name, "rate" to rate)
        api.updateCurrency(id, body).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                if (response.isSuccessful) onDone?.invoke()
                else onError?.invoke(Throwable("Update failed: ${response.code()}"))
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) { onError?.invoke(t) }
        })
    }

    //Delete Firebase coins
    fun deleteCurrency(id: String, onDone: (() -> Unit)? = null, onError: ((Throwable) -> Unit)? = null) {
        api.deleteCurrency(id).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                if (response.isSuccessful) onDone?.invoke()
                else onError?.invoke(Throwable("Delete failed: ${response.code()}"))
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) { onError?.invoke(t) }
        })
    }
}