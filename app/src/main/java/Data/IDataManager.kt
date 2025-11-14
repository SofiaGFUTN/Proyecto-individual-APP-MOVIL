package cr.ac.utn.conversordemonedas.data

import cr.ac.utn.conversordemonedas.model.Conversion

interface IDataManager {
    fun addConversion(conversion: Conversion)
    fun getAllConversions(): MutableList<Conversion>
    fun updateConversion(conversion: Conversion)
    fun deleteConversion(id: Int)
    fun findConversionByCurrency(currency: String): MutableList<Conversion>
}