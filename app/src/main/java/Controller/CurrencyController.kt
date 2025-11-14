package cr.ac.utn.conversordemonedas

data class Currency(
    var code: String,
    var name: String,
    var rate: Double
)

class CurrencyController {
    private val currencyList = mutableListOf<Currency>()

    init {
        //Datos iniciales de ejemplo
        currencyList.add(Currency("USD", "Dólar estadounidense", 1.0))
        currencyList.add(Currency("CRC", "Colón costarricense", 520.0))
        currencyList.add(Currency("EUR", "Euro", 0.92))
    }

    //CREATE
    fun addCurrency(currency: Currency): Boolean {
        if (currencyList.any { it.code.equals(currency.code, ignoreCase = true) }) return false
        currencyList.add(currency)
        return true
    }

    //READ
    fun getAllCurrencies(): List<Currency> {
        return currencyList
    }

    //SEARCH
    fun searchCurrency(code: String): Currency? {
        return currencyList.find { it.code.equals(code, ignoreCase = true) }
    }

    //UPDATE
    fun updateCurrency(code: String, name: String, rate: Double): Boolean {
        val currency = searchCurrency(code)
        return if (currency != null) {
            currency.name = name
            currency.rate = rate
            true
        } else false
    }

    //DELETE
    fun deleteCurrency(code: String): Boolean {
        return currencyList.removeIf { it.code.equals(code, ignoreCase = true) }
    }

    //DELETE ALL
    fun clearAll() {
        currencyList.clear()
    }
}