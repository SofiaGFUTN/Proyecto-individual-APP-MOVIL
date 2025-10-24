package Model

data class Conversion(
    val fromCurrency: Currency,
    val toCurrency: Currency,
    val amount: Double,
    val result: Double
)