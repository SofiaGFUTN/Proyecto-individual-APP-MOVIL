package cr.ac.utn.conversordemonedas

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import cr.ac.utn.conversordemonedas.model.Currency
import cr.ac.utn.conversordemonedas.network.RemoteCurrencyDataManager
import cr.ac.utn.conversordemonedas.controller.CurrencyController

class ConversionActivity : ComponentActivity() {

    private lateinit var adapterRecent: ArrayAdapter<String>
    private val recentConversions = mutableListOf<String>()

    private var currencyList: List<Currency> = emptyList()

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.conversion_activity)

        //Variables
        val spinnerFrom = findViewById<Spinner>(R.id.typeMoney1_conversion)
        val spinnerTo = findViewById<Spinner>(R.id.typeMoney2_conversion)
        val etAmount = findViewById<EditText>(R.id.editTextNumberDecimal)
        val txtResult = findViewById<TextView>(R.id.txtResult)
        val txtRate = findViewById<TextView>(R.id.currentRate_conversion)
        val listRecent = findViewById<ListView>(R.id.listRecentConversions)

        val btnConvert = findViewById<Button>(R.id.btnConvert)
        val btnClear = findViewById<Button>(R.id.btnClear)
        val btnUpdate = findViewById<Button>(R.id.btnUpdateRate)

        val iconAdd = findViewById<ImageButton>(R.id.iconAdd)
        val iconEdit = findViewById<ImageButton>(R.id.iconEdit)
        val iconDelete = findViewById<ImageButton>(R.id.iconDelete)
        val iconSearch = findViewById<ImageButton>(R.id.iconSearch)

        adapterRecent = ArrayAdapter(this, android.R.layout.simple_list_item_1, recentConversions)
        listRecent.adapter = adapterRecent

        loadHistory()

        fun refreshSpinners() {
            val codes = currencyList.map { it.code }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, codes)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerFrom.adapter = adapter
            spinnerTo.adapter = adapter
        }

        loadCurrencies { refreshSpinners() }

        fun highlight(view: EditText, error: Boolean) {
            view.setBackgroundColor(if (error) Color.parseColor("#FFCDD2") else Color.TRANSPARENT)
        }

        //Add currency
        iconAdd.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.dialog_currency, null)
            val etCode = view.findViewById<EditText>(R.id.etCode)
            val etName = view.findViewById<EditText>(R.id.etName)
            val etRate = view.findViewById<EditText>(R.id.etRate)

            AlertDialog.Builder(this)
                .setTitle("Agregar moneda")
                .setView(view)
                .setPositiveButton("Guardar") { _, _ ->
                    val code = etCode.text.toString().uppercase()
                    val name = etName.text.toString()
                    val rate = etRate.text.toString().toDoubleOrNull()

                    val invalid = code.isEmpty() || name.isEmpty() || rate == null
                    highlight(etCode, code.isEmpty())
                    highlight(etName, name.isEmpty())
                    highlight(etRate, rate == null)

                    if (invalid) {
                        Toast.makeText(this, "Datos inválidos", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    RemoteCurrencyDataManager.addCurrency(code, name, rate!!,
                        onDone = {
                            runOnUiThread {
                                Toast.makeText(this, "Moneda agregada", Toast.LENGTH_SHORT).show()
                                loadCurrencies { refreshSpinners() }
                            }
                        },
                        onError = {
                            runOnUiThread {
                                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        //EDIT CURRENCY
        iconEdit.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.dialog_currency, null)
            val etCode = view.findViewById<EditText>(R.id.etCode)
            val etName = view.findViewById<EditText>(R.id.etName)
            val etRate = view.findViewById<EditText>(R.id.etRate)

            AlertDialog.Builder(this)
                .setTitle("Editar moneda")
                .setView(view)
                .setPositiveButton("Guardar") { _, _ ->
                    val code = etCode.text.toString().uppercase()
                    val name = etName.text.toString()
                    val rate = etRate.text.toString().toDoubleOrNull()

                    val invalid = code.isEmpty() || name.isEmpty() || rate == null
                    highlight(etCode, code.isEmpty())
                    highlight(etName, name.isEmpty())
                    highlight(etRate, rate == null)

                    if (invalid) {
                        Toast.makeText(this, "Datos inválidos", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    val currency = currencyList.find { it.code == code }
                    if (currency == null) {
                        Toast.makeText(this, "Moneda no encontrada", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    RemoteCurrencyDataManager.updateCurrency(currency.id!!, name, rate!!,
                        onDone = {
                            runOnUiThread {
                                Toast.makeText(this, "Moneda actualizada", Toast.LENGTH_SHORT).show()
                                loadCurrencies { refreshSpinners() }
                            }
                        },
                        onError = {
                            runOnUiThread {
                                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        //REMOVE MONEY
        iconDelete.setOnClickListener {
            val input = EditText(this)
            input.hint = "Código de moneda"

            AlertDialog.Builder(this)
                .setTitle("Eliminar moneda")
                .setView(input)
                .setPositiveButton("Eliminar") { _, _ ->
                    val code = input.text.toString().uppercase()
                    highlight(input, code.isEmpty())
                    if (code.isEmpty()) return@setPositiveButton

                    val currency = currencyList.find { it.code == code }
                    if (currency == null) {
                        Toast.makeText(this, "No existe", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    RemoteCurrencyDataManager.deleteCurrency(currency.id!!,
                        onDone = {
                            runOnUiThread {
                                Toast.makeText(this, "Eliminada", Toast.LENGTH_SHORT).show()
                                loadCurrencies { refreshSpinners() }
                            }
                        },
                        onError = {
                            runOnUiThread {
                                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        //SEARCH FOR MONEY
        iconSearch.setOnClickListener {
            val input = EditText(this)
            input.hint = "Código de moneda"

            AlertDialog.Builder(this)
                .setTitle("Buscar moneda")
                .setView(input)
                .setPositiveButton("OK") { _, _ ->
                    val code = input.text.toString().uppercase()
                    if (code.isEmpty()) return@setPositiveButton

                    val currency = currencyList.find { it.code == code }
                    if (currency != null)
                        Toast.makeText(this, "${currency.name}: ${currency.rate}", Toast.LENGTH_LONG).show()
                    else
                        Toast.makeText(this, "No encontrada", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        //CONVERT CURRENCY
        btnConvert.setOnClickListener {
            try {
                val amountText = etAmount.text.toString()
                val from = spinnerFrom.selectedItem?.toString()
                val to = spinnerTo.selectedItem?.toString()

                if (amountText.isEmpty()) {
                    Toast.makeText(this, "Ingrese una cantidad", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val amount = amountText.toDoubleOrNull()
                if (amount == null || amount <= 0) {
                    Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (from == null || to == null) {
                    Toast.makeText(this, "Seleccione las monedas", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val fromCurrency = currencyList.find { it.code == from }
                val toCurrency = currencyList.find { it.code == to }

                if (fromCurrency == null || toCurrency == null) {
                    Toast.makeText(this, "Error: Monedas no encontradas", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                //LOCAL CONVERSION
                // Fórmula: amount * (toRate / fromRate)
                val result = amount * (toCurrency.rate / fromCurrency.rate)

                txtResult.text = String.format("%.2f", result)
                txtRate.text = "1 $from = ${String.format("%.4f", toCurrency.rate / fromCurrency.rate)} $to"

                //Save to recent history
                val record = "$amount $from → ${String.format("%.2f", result)} $to"
                if (!recentConversions.contains(record)) {
                    recentConversions.add(0, record)
                    if (recentConversions.size > 10) {
                        recentConversions.removeLast()
                    }
                }

                adapterRecent.notifyDataSetChanged()
                saveHistory()

                Toast.makeText(this, "Conversión exitosa", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }

        btnClear.setOnClickListener {
            etAmount.text.clear()
            txtResult.text = "—"
            txtRate.text = ""
        }

        btnUpdate.setOnClickListener {
            loadCurrencies { refreshSpinners() }
            Toast.makeText(this, "Tarifas actualizadas", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadCurrencies(onFinish: () -> Unit) {
        RemoteCurrencyDataManager.getAllCurrencies(
            onResult = { list ->
                currencyList = list
                runOnUiThread { onFinish() }
            },
            onError = { error ->
                runOnUiThread {
                    Toast.makeText(this, "Error loading currencies: ${error.message}", Toast.LENGTH_SHORT).show()
                    onFinish()
                }
            }
        )
    }

    private fun saveHistory() {
        val prefs = getSharedPreferences("history", Context.MODE_PRIVATE)
        prefs.edit().putStringSet("conversions", recentConversions.toSet()).apply()
    }

    private fun loadHistory() {
        val prefs = getSharedPreferences("history", Context.MODE_PRIVATE)
        val saved = prefs.getStringSet("conversions", emptySet()) ?: emptySet()
        recentConversions.clear()
        recentConversions.addAll(saved.sortedDescending())
    }
}