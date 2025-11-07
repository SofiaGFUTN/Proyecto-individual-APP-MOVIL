package cr.ac.utn.conversordemonedas

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cr.ac.utn.conversordemonedas.ui.theme.ConversorDeMonedasTheme

class ConversionActivity : ComponentActivity() {

    private val controller = CurrencyController()
    private lateinit var adapterRecent: ArrayAdapter<String>
    private val recentConversions = mutableListOf<String>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ConversorDeMonedasTheme { } }
        setContentView(R.layout.conversion_activity)

        //Referencias del layout XML
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

        //El adaptador para conversiones recientes
        adapterRecent = ArrayAdapter(this, android.R.layout.simple_list_item_1, recentConversions)
        listRecent.adapter = adapterRecent

        //Recargar los spinners
        fun refreshSpinners() {
            val codes = controller.getAllCurrencies().map { it.code }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, codes)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerFrom.adapter = adapter
            spinnerTo.adapter = adapter
        }

        refreshSpinners()

        //AGREGAR
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

                    if (code.isEmpty() || name.isEmpty() || rate == null) {
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

        //EDITAR
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

                    if (code.isEmpty() || name.isEmpty() || rate == null) {
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

        //ELIMINAR
        iconDelete.setOnClickListener {
            val input = EditText(this)
            input.hint = getString(R.string.hint_currency_code)

            AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_delete_currency))
                .setView(input)
                .setPositiveButton(getString(R.string.btn_delete_currency)) { _, _ ->
                    val code = input.text.toString().uppercase()
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

        //BUSCAR
        iconSearch.setOnClickListener {
            val input = EditText(this)
            input.hint = getString(R.string.hint_currency_code)

            AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_search_currency))
                .setView(input)
                .setPositiveButton(getString(R.string.btn_ok)) { _, _ ->
                    val code = input.text.toString().uppercase()
                    val currency = controller.searchCurrency(code)
                    if (currency != null) {
                        Toast.makeText(
                            this,
                            "${currency.name}: ${currency.rate}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(this, getString(R.string.msg_currency_not_found), Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .show()
        }

        //CONVERTIR MONEDAS
        btnConvert.setOnClickListener {
            val amountText = etAmount.text.toString()
            if (amountText.isEmpty()) {
                Toast.makeText(this, getString(R.string.msg_invalid_input), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fromCode = spinnerFrom.selectedItem?.toString()
            val toCode = spinnerTo.selectedItem?.toString()
            if (fromCode == null || toCode == null) {
                Toast.makeText(this, "Seleccione la moneda", Toast.LENGTH_SHORT).show()
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
                recentConversions.add(0, record)
                adapterRecent.notifyDataSetChanged()
            } else {
                Toast.makeText(this, getString(R.string.msg_conversion_error), Toast.LENGTH_SHORT).show()
            }
        }

        //LIMPIAR
        btnClear.setOnClickListener {
            etAmount.text.clear()
            txtResult.text = "—"
        }

        //ACTUALIZAR
        btnUpdate.setOnClickListener {
            Toast.makeText(this, getString(R.string.msg_rate_updated), Toast.LENGTH_SHORT).show()
        }
    }
}