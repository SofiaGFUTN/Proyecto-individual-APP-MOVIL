package cr.ac.utn.conversordemonedas.model

data class Conversion(
    var id: Int = 0,
    var fromCurrency: String,
    var toCurrency: String,
    var amount: Double,
    var result: Double
)