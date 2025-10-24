package Data
import Model.Currency

class MemoryDataManager : IDataManager {

    private val currencyList = mutableListOf(
        Currency("USD", "Dólar estadounidense", 1.0),
        Currency("CRC", "Colón costarricense", 520.0),
        Currency("EUR", "Euro", 0.93),
        Currency("MXN", "Peso mexicano", 18.0)
    )

    override fun getCurrencies(): List<Currency> = currencyList

    override fun getCurrency(code: String): Currency? {
        return currencyList.find { it.code == code }
    }

    override fun addCurrency(currency: Currency) {
        currencyList.add(currency)
    }
}