package cr.ac.utn.conversordemonedas.network

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    //Create currency
    @POST("currencies")
    fun createCurrency(@Body body: Map<String, Any>): Call<Map<String, Any>>

    //Read all currencies
    @GET("currencies")
    fun getCurrencies(): Call<List<Map<String, Any>>>

    //Update by id
    @PUT("currencies/{id}")
    fun updateCurrency(@Path("id") id: String, @Body body: Map<String, Any>): Call<Map<String, Any>>

    //Delete by id
    @DELETE("currencies/{id}")
    fun deleteCurrency(@Path("id") id: String): Call<Map<String, Any>>

    //Convert currency
    @POST("convert")
    fun convert(@Body body: Map<String, Any>): Call<Map<String, Any>>
}