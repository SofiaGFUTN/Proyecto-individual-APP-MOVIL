package Controller

import Model.Currency
import Model.Conversion
import Data.MemoryDataManager

class CurrencyController(private val dataManager: MemoryDataManager) {

    fun convert(fromCode: String, toCode: String, amount: Double): Conversion? {
        val from = dataManager.getCurrency(fromCode)
        val to = dataManager.getCurrency(toCode)

        if (from != null && to != null) {
            val result = amount / from.rate * to.rate
            return Conversion(from, to, amount, result)
        }
        return null
    }

    fun getAvailableCurrencies(): List<Currency> {
        return dataManager.getCurrencies()
    }
}