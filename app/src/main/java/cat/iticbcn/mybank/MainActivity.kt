package cat.iticbcn.mybank

import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private lateinit var btnContinuar : MaterialButton
    private lateinit var ivBack: ImageView
    private lateinit var etDocument: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponents()
        initListeners()
    }
    private fun initComponents() {
        ivBack = findViewById<ImageView>(R.id.ivBack)
        btnContinuar = findViewById<MaterialButton>(R.id.btnContinuar)
        etDocument = findViewById<EditText>(R.id.etDocument)
    }

    private fun initListeners() {
        ivBack.setOnClickListener {
            terminarAplicacio()

        }

        btnContinuar.setOnClickListener {
            goToLoginActivity()
        }

        etDocument.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (etDocument.length() == 9) {
                    validarDNI()}
            }
        })
    }

    private fun goToLoginActivity() {
        val intent = android.content.Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    private fun terminarAplicacio() {
        AlertDialog.Builder(this)
            .setTitle("Sortir de l'aplicació")
            .setMessage("Segur que vols sortir de l'aplicació?")
            .setPositiveButton("Sí") { _, _ ->
                finish()
            }
            .setNegativeButton("No", null)
            .setCancelable(true)
            .show()
    }
    private fun validarDNI() {
        TODO("Not yet implemented")
    }

    private fun isValidDNI(dni: String): Boolean {
        var resultat = false
        if (dni.length != 9) {
            resultat = false
        } else {

        }


        return resultat
    }

}






