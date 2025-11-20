package cat.iticbcn.mybank

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var btnContinuar : MaterialButton
    private lateinit var ivBack: ImageView
    private lateinit var etDocument: EditText
    private lateinit var etData: EditText

    private lateinit var rgDocument: RadioGroup
    private lateinit var tvInfoSeguretat: TextView
    private lateinit var tvInfoPrivacitat: TextView
    private lateinit var cvCalendar: CalendarView

    private lateinit var ivCalendar: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        verifyLoggedIn()
        initComponents()
        initUI()
        initListeners()
    }

    private fun verifyLoggedIn() {
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val loggedIn = sharedPref.getBoolean("LOGGED_IN", false)

        if (loggedIn) {
            goToLoginActivity()
            finish()  // Tanca aquesta activitat per evitar tornar enrere
        }
    }

    private fun initComponents() {
        ivBack = findViewById<ImageView>(R.id.ivBack)
        btnContinuar = findViewById<MaterialButton>(R.id.btnContinuar)
        etDocument = findViewById<EditText>(R.id.etDocument)
        etData = findViewById<EditText>(R.id.etDataNaixement)
        rgDocument = findViewById<RadioGroup>(R.id.rgDocument)
        tvInfoSeguretat = findViewById<TextView>(R.id.tvInfoSeguretat)
        tvInfoPrivacitat = findViewById<TextView>(R.id.tvInfoPrivacitat)
        cvCalendar = findViewById<CalendarView>(R.id.cvCalendar)
        ivCalendar = findViewById<ImageView>(R.id.ivCalendar)
    }

    private fun initUI() {
        cvCalendar.visibility=View.GONE
    }
    private fun initListeners() {
        ivBack.setOnClickListener {
            terminarAplicacio()
        }

        btnContinuar.setOnClickListener {
            if (isValidForm()) {
                // Connetar BBDD
                // Rebre imatge, psw
                val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                with (sharedPrefs.edit()) {
                    putString("USER_NAME" , "Toni Aguilar")
                    putString("USER_AVATAR","ic_untitled")
                    putBoolean("LOGGED_IN", true)
                    apply()   // o commit()
                }
                goToLoginActivity()
                finish()
            }
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

        etData.addTextChangedListener { text ->
            val dataInput = text.toString()
            if (dataInput.length == 10) {
                if (!isValidBirthDate(dataInput)) {
                    etData.error = getString(R.string.invalid_birth_date)
                } else {
                    etData.error = null
                }
            }
        }

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

            val btnTancar = info_seg.findViewById<MaterialButton>(R.id.btnTancar)
            btnTancar.setOnClickListener {
                info_seg.dismiss()
            }

        }

        tvInfoPrivacitat.setOnClickListener {
            val info_priv = Dialog(this)
            info_priv.setContentView(R.layout.dialog_politica_privacitat)
            info_priv.show()

            val btnTancar = info_priv.findViewById<MaterialButton>(R.id.btnTancar)
            btnTancar.setOnClickListener {
                info_priv.dismiss()
            }
        }

        ivCalendar.setOnClickListener {
/*
            val transition = AutoTransition()
            transition.duration = 300
            TransitionManager.beginDelayedTransition(cvCalendar, transition)

            cvCalendar.visibility = if (cvCalendar.visibility == View.VISIBLE) View.GONE else View.VISIBLE

*/

            //Segona opció: Fer-ho amb el DatePickerDialog

            val inicial = Calendar.getInstance().apply {
                set(2000, Calendar.JANUARY, 1)
            }

            val any = inicial.get(Calendar.YEAR)
            val mes = inicial.get(Calendar.MONTH)
            val dia = inicial.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this, // o requireContext() si estàs en un Fragment
                { _, anySeleccionat, mesSeleccionat, diaSeleccionat ->
                    val dataText = String.format(
                        "%02d/%02d/%d",
                        diaSeleccionat,
                        mesSeleccionat + 1,
                        anySeleccionat
                    )
                    etData.setText(dataText)
                },
                any,
                mes,
                dia
            )

            datePicker.datePicker.minDate =
                Calendar.getInstance().apply { set(1900, Calendar.JANUARY, 1) }.timeInMillis
            datePicker.datePicker.maxDate =
                System.currentTimeMillis() // opcional: no permet dates futures
            datePicker.show()


        }

        // on below line we are adding set on
        // date change listener for calendar view.
        cvCalendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // In this Listener we are getting values
            // such as year, month and day of month
            // on below line we are creating a variable
            // in which we are adding all the variables in it.
            val Date = (String.format("%02d",dayOfMonth) + "/"
                    + String.format("%02d",month) + "/" + year)

            // set this date in TextView for Display
            etData.setText(Date)

            //Close view
            view.visibility = View.GONE
        }

    }

    private fun goToLoginActivity() {
        val intent = android.content.Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun isValidForm(): Boolean {
        var isValid = true

        val documentType = when (rgDocument.checkedRadioButtonId) {
            R.id.rbDNI -> "DNI"
            R.id.rbPassport -> "Passport"
            else -> ""
        }

        val documentNumber = etDocument.text.toString()
        val birthDate = etData.text.toString()

        if (documentNumber.isEmpty()) {
            etDocument.error = getString(R.string.required_field)
            isValid = false
        } else {
            if (documentType == "DNI" && !isValidDniNie(documentNumber)) {
                etDocument.error = getString(R.string.invalid_dni_nie)
                isValid = false
            }
        }

        if (birthDate.isEmpty()) {
            etData.error = getString(R.string.required_field)
            isValid = false
        } else {
            if (!isValidBirthDate(birthDate)) {
                etData.error = getString(R.string.invalid_birth_date)
                isValid = false
            }
        }

        return isValid
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






