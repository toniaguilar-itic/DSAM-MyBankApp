package cat.iticbcn.mybank

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var btnContinuar : MaterialButton
    private lateinit var ivBack: ImageView
    private lateinit var etDocument: EditText

    private lateinit var rgDocument: RadioGroup
    private lateinit var tvInfoSeguretat: TextView


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
        // initUI()
        initListeners()
    }
    private fun initComponents() {
        ivBack = findViewById<ImageView>(R.id.ivBack)
        btnContinuar = findViewById<MaterialButton>(R.id.btnContinuar)
        etDocument = findViewById<EditText>(R.id.etDocument)
        rgDocument = findViewById<RadioGroup>(R.id.rgDocument)
        tvInfoSeguretat = findViewById<TextView>(R.id.tvInfoSeguretat)
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
                if (rgDocument.checkedRadioButtonId==R.id.rbDNI && etDocument.length() == 9) {
                    if (isValidDniNie(etDocument.text.toString())) {
                        etDocument.error = null
                    } else {
                        etDocument.error = getString(R.string.error_dni)
                    }
                }
            }
        })

        rgDocument.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId==R.id.rbDNI && etDocument.length() == 9) {
                if (isValidDniNie(etDocument.text.toString())) {
                    etDocument.error = null
                } else {
                    etDocument.error = getString(R.string.error_dni)
                }
            } else {
                etDocument.error = null
            }
        }

        tvInfoSeguretat.setOnClickListener {
            val info_seg = Dialog(this)
            info_seg.setContentView(R.layout.dialog_info_seguretat)
            info_seg.show()

        }
    }

    private fun goToLoginActivity() {
        val intent = android.content.Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    /**
     * Mostra un diàleg de confirmació per sortir de l'aplicació.
     * Si l'usuari confirma, es tanca l'activitat actual.
     *    - finish()          -> només tanca l’Activity actual
     *    - finishAffinity()  -> tanca aquesta Activity i totes les que hi ha a la pila
     *    - System.exit(0)    -> tanca l’aplicació completament. Força la finalització
     *                           del procés de l’aplicació.
     *                           No és recomanat per Android, ja que el sistema gestiona
     *                           la memòria i pot causar errors o comportaments inesperats.
     */
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

    private fun validarData() {
        TODO("Not yet implemented")
    }

    /**
     * Valida si un DNI o NIE és vàlid.
     * @param valor El DNI o NIE a validar.
     * @return true si és vàlid, false en cas contrari.
     */
    private fun isValidDniNie(valor: String): Boolean {
        val dniNie = valor.trim().uppercase()

        // Expressió regular per validar el format bàsic
        val regex = Regex("^[XYZ]?[0-9]{7,8}[A-Z]$")

        if (!regex.matches(dniNie)) return false

        // Lletra de control
        val lletres = "TRWAGMYFPDXBNJZSQVHLCKE"

        // Substituïm la lletra inicial dels NIE per la seva equivalència numèrica
        val numero = when (dniNie[0]) {
            'X' -> dniNie.replaceFirst('X', '0')
            'Y' -> dniNie.replaceFirst('Y', '1')
            'Z' -> dniNie.replaceFirst('Z', '2')
            else -> dniNie
        }

        // Separem número i lletra
        val partNumerica = numero.dropLast(1)
        val lletraEsperada = lletres[partNumerica.toInt() % 23]
        val lletraReal = dniNie.last()

        return lletraReal == lletraEsperada
    }

    /**
     * Valida si una data de naixement és vàlida.
     * La data ha d'estar en el format especificat (per defecte "dd/MM/yyyy").
     * La data ha de ser anterior o igual a la data actual i no pot ser anterior a fa 100 anys.
     * @param data La data de naixement a validar.
     * @param format El format de la data (per defecte "dd/MM/yyyy").
     * @return true si la data és vàlida, false en cas contrari.
     */
    fun isValidBirthDate(data: String, format: String = "dd/MM/yyyy"): Boolean {
        return try {
            if (data.length != 10) return false

            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.isLenient = false // important per validar la data real

            val dataParsed = sdf.parse(data) ?: return false
            val avui = Date()

            // limit serà fa 100 anys
            val limit = Calendar.getInstance().apply {
                add(Calendar.YEAR, -100)  // fa 100 anys
            }

            // Retorna true només si la data no és futura i no és anterior a fa 100 anys
            !dataParsed.after(avui) && !dataParsed.before(limit.time)
        } catch (_: Exception) {
            // Si hi ha una excepció en el parsing, la data no és vàlida
            false
        }
    }

}






