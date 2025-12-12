package cr.ac.utn.conversordemonedas

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class HistoryActivity : AppCompatActivity() {

    private lateinit var listHistory: ListView
    private lateinit var txtEmptyHistory: TextView
    private lateinit var btnBackHistory: Button
    private lateinit var btnClearHistory: Button

    private lateinit var adapter: ArrayAdapter<String>
    private val historyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_activity)

        initViews()

        setupListView()

        setupListeners()

        loadHistory()
    }

    private fun initViews() {
        listHistory = findViewById(R.id.listHistory)
        txtEmptyHistory = findViewById(R.id.txtEmptyHistory)
        btnBackHistory = findViewById(R.id.btnBackHistory)
        btnClearHistory = findViewById(R.id.btnClearHistory)
    }

    private fun setupListView() {
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            historyList
        )
        listHistory.adapter = adapter
    }

    private fun setupListeners() {
        //Back button
        btnBackHistory.setOnClickListener {
            finish()
        }

        btnClearHistory.setOnClickListener {
            clearHistory()
        }
    }

    private fun loadHistory() {
        try {
            val prefs = getSharedPreferences("history", Context.MODE_PRIVATE)
            val saved = prefs.getStringSet("conversions", emptySet()) ?: emptySet()

            historyList.clear()
            historyList.addAll(saved.sortedDescending())

            updateUI()

        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Error cargando historial: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun clearHistory() {
        val prefs = getSharedPreferences("history", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()

        historyList.clear()
        adapter.notifyDataSetChanged()

        updateUI()

        Toast.makeText(
            this,
            getString(R.string.history_cleared),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun updateUI() {
        if (historyList.isEmpty()) {
            listHistory.visibility = View.GONE
            txtEmptyHistory.visibility = View.VISIBLE
        } else {
            listHistory.visibility = View.VISIBLE
            txtEmptyHistory.visibility = View.GONE
            adapter.notifyDataSetChanged()
        }
    }
}