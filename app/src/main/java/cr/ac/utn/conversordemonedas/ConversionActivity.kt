package cr.ac.utn.conversordemonedas

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import cr.ac.utn.conversordemonedas.ui.theme.ConversorDeMonedasTheme

class ConversionActivity : ComponentActivity() {

    private val controller = CurrencyController()
    private lateinit var adapterRecent: ArrayAdapter<String>
    private val recentConversions = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ConversorDeMonedasTheme { } }
        setContentView(R.layout.conversion_activity)

        // --- Referencias del layout ---
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

        //Configura lista de conversiones recientes
        adapterRecent = ArrayAdapter(this, android.R.layout.simple_list_item_1, recentConversions)
        listRecent.adapter = adapterRecent

        //Cargar historial previo guardado en SharedPreferences
        loadHistory()

        //Refrescar los spinners
        fun refreshSpinners() {
            val codes = controller.getAllCurrencies().map { it.code }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, codes)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerFrom.adapter = adapter
            spinnerTo.adapter = adapter
        }
        refreshSpinners()

        //Función auxiliar para marcar errores visualmente
        fun highlightField(view: EditText, isError: Boolean) {
            if (isError) view.setBackgroundColor(Color.parseColor("#FFCDD2"))
            else view.setBackgroundColor(Color.TRANSPARENT)
        }

        //AGREGAR moneda
        iconAdd.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_currency, null)
            val etCode = dialogView.findViewById<EditText>(R.id.etCode)
            val etName = dialogView.findViewById<EditText>(R.id.etName)
            val etRate = dialogView.findViewById<EditText>(R.id.etRate)

            AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_add_currency))
                .setView(dialogView)
                .setPositiveButton(getString(R.string.btn_save)) { _, _ ->
                    val code = etCode.text.toString().uppercase()
                    val name = etName.text.toString()
                    val rate = etRate.text.toString().toDoubleOrNull()

                    val invalid = code.isEmpty() || name.isEmpty() || rate == null
                    highlightField(etCode, code.isEmpty())
                    highlightField(etName, name.isEmpty())
                    highlightField(etRate, rate == null)

                    if (invalid) {
                        Toast.makeText(this, getString(R.string.msg_invalid_input), Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    val added = controller.addCurrency(Currency(code, name, rate))
                    if (added) {
                        Toast.makeText(this, getString(R.string.msg_currency_added), Toast.LENGTH_SHORT).show()
                        refreshSpinners()
                    } else {
                        Toast.makeText(this, getString(R.string.msg_currency_exists), Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .show()
        }

        //EDITAR moneda
        iconEdit.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_currency, null)
            val etCode = dialogView.findViewById<EditText>(R.id.etCode)
            val etName = dialogView.findViewById<EditText>(R.id.etName)
            val etRate = dialogView.findViewById<EditText>(R.id.etRate)

            AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_edit_currency))
                .setView(dialogView)
                .setPositiveButton(getString(R.string.btn_save)) { _, _ ->
                    val code = etCode.text.toString().uppercase()
                    val name = etName.text.toString()
                    val rate = etRate.text.toString().toDoubleOrNull()

                    val invalid = code.isEmpty() || name.isEmpty() || rate == null
                    highlightField(etCode, code.isEmpty())
                    highlightField(etName, name.isEmpty())
                    highlightField(etRate, rate == null)

                    if (invalid) {
                        Toast.makeText(this, getString(R.string.msg_invalid_input), Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    val updated = controller.updateCurrency(code, name, rate)
                    if (updated) {
                        Toast.makeText(this, getString(R.string.msg_currency_updated), Toast.LENGTH_SHORT).show()
                        refreshSpinners()
                    } else {
                        Toast.makeText(this, getString(R.string.msg_currency_not_found), Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .show()
        }

        //ELIMINAR moneda
        iconDelete.setOnClickListener {
            val input = EditText(this)
            input.hint = getString(R.string.hint_currency_code)

            AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_delete_currency))
                .setView(input)
                .setPositiveButton(getString(R.string.btn_delete_currency)) { _, _ ->
                    val code = input.text.toString().uppercase()
                    highlightField(input, code.isEmpty())

                    if (code.isEmpty()) {
                        Toast.makeText(this, getString(R.string.msg_invalid_input), Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    if (controller.deleteCurrency(code)) {
                        Toast.makeText(this, getString(R.string.msg_currency_deleted), Toast.LENGTH_SHORT).show()
                        refreshSpinners()
                    } else {
                        Toast.makeText(this, getString(R.string.msg_currency_not_found), Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .show()
        }

        //BUSCAR moneda
        iconSearch.setOnClickListener {
            val input = EditText(this)
            input.hint = getString(R.string.hint_currency_code)

            AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_search_currency))
                .setView(input)
                .setPositiveButton(getString(R.string.btn_ok)) { _, _ ->
                    val code = input.text.toString().uppercase()
                    highlightField(input, code.isEmpty())

                    if (code.isEmpty()) {
                        Toast.makeText(this, getString(R.string.msg_invalid_input), Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    val currency = controller.searchCurrency(code)
                    if (currency != null) {
                        Toast.makeText(this, "${currency.name}: ${currency.rate}", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, getString(R.string.msg_currency_not_found), Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .show()
        }

        //CONVERTIR moneda
        btnConvert.setOnClickListener {
            val amountText = etAmount.text.toString()
            val fromCode = spinnerFrom.selectedItem?.toString()
            val toCode = spinnerTo.selectedItem?.toString()

            val amountValid = amountText.isNotEmpty()
            highlightField(etAmount, !amountValid)

            if (!amountValid) {
                Toast.makeText(this, getString(R.string.msg_invalid_input), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (fromCode == null || toCode == null) {
                Toast.makeText(this, "Seleccione las monedas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val from = controller.searchCurrency(fromCode)
            val to = controller.searchCurrency(toCode)
            val amount = amountText.toDoubleOrNull() ?: 0.0

            if (from != null && to != null) {
                val result = (amount / from.rate) * to.rate
                txtResult.text = "%.2f".format(result)
                txtRate.text = "Tarifa actual: 1 ${from.code} = ${"%.2f".format(to.rate / from.rate)} ${to.code}"

                val record = "$amount ${from.code} → ${"%.2f".format(result)} ${to.code}"

                //Evitar duplicados y limitar historial
                if (!recentConversions.contains(record)) {
                    recentConversions.add(0, record)
                    if (recentConversions.size > 10) recentConversions.removeLast()
                }

                adapterRecent.notifyDataSetChanged()
                saveHistory()
            } else {
                Toast.makeText(this, getString(R.string.msg_conversion_error), Toast.LENGTH_SHORT).show()
            }
        }

        //LIMPIAR
        btnClear.setOnClickListener {
            etAmount.text.clear()
            txtResult.text = "—"
            txtRate.text = ""
            highlightField(etAmount, false)
        }

        //ACTUALIZAR tarifa
        btnUpdate.setOnClickListener {
            Toast.makeText(this, getString(R.string.msg_rate_updated), Toast.LENGTH_SHORT).show()
        }
    }

    //Guardar en historial
    private fun saveHistory() {
        val prefs = getSharedPreferences("history", Context.MODE_PRIVATE)
        prefs.edit().putStringSet("conversions", recentConversions.toSet()).apply()
    }

    //Cargar historial guardado
    private fun loadHistory() {
        val prefs = getSharedPreferences("history", Context.MODE_PRIVATE)
        val saved = prefs.getStringSet("conversions", emptySet()) ?: emptySet()
        recentConversions.clear()
        recentConversions.addAll(saved.sortedDescending()) // muestra las más recientes arriba
    }
}