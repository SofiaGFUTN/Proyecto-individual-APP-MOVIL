package cr.ac.utn.conversordemonedas.data

import cr.ac.utn.conversordemonedas.model.Conversion

class MemoryDataManager : IDataManager {

    private val conversionList = mutableListOf<Conversion>()
    private var idCounter = 1

    override fun addConversion(conversion: Conversion) {
        conversion.id = idCounter++
        conversionList.add(conversion)
    }

    override fun getAllConversions(): MutableList<Conversion> = conversionList

    override fun updateConversion(conversion: Conversion) {
        val index = conversionList.indexOfFirst { it.id == conversion.id }
        if (index != -1) {
            conversionList[index] = conversion
        }
    }

    override fun deleteConversion(id: Int) {
        conversionList.removeIf { it.id == id }
    }

    override fun findConversionByCurrency(currency: String): MutableList<Conversion> {
        return conversionList.filter {
            it.fromCurrency.contains(currency, true) || it.toCurrency.contains(currency, true)
        }.toMutableList()
    }
}