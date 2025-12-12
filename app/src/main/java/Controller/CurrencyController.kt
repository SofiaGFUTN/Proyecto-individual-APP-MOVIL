package cr.ac.utn.conversordemonedas.controller

import cr.ac.utn.conversordemonedas.model.Currency
import cr.ac.utn.conversordemonedas.network.RemoteCurrencyDataManager

object CurrencyController {

    fun getAllCurrencies(onSuccess: (List<Currency>) -> Unit, onError: (String) -> Unit) {
        RemoteCurrencyDataManager.getAllCurrencies(
            { list -> onSuccess(list) },
            { t -> onError(t.message ?: "Unknown") }
        )
    }

    fun addCurrency(currency: Currency, onSuccess: () -> Unit, onError: (String) -> Unit) {
        RemoteCurrencyDataManager.addCurrency(
            currency.code,
            currency.name,
            currency.rate,
            { onSuccess() },
            { t -> onError(t.message ?: "Unknown") }
        )
    }

    fun updateCurrency(id: String, currency: Currency, onSuccess: () -> Unit, onError: (String) -> Unit) {
        RemoteCurrencyDataManager.updateCurrency(
            id,
            currency.name,
            currency.rate,
            { onSuccess() },
            { t -> onError(t.message ?: "Unknown") }
        )
    }

    fun deleteCurrency(id: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        RemoteCurrencyDataManager.deleteCurrency(
            id,
            { onSuccess() },
            { t -> onError(t.message ?: "Unknown") }
        )
    }
}