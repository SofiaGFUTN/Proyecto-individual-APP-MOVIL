package Data

import Model.Currency

interface IDataManager {
    fun getCurrencies(): List<Currency>
    fun getCurrency(code: String): Currency?
    fun addCurrency(currency: Currency)
}