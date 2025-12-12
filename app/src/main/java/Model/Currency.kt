package cr.ac.utn.conversordemonedas.model

data class Currency(
    val code: String,
    val name: String,
    val rate: Double,
    val id: String? = null
)