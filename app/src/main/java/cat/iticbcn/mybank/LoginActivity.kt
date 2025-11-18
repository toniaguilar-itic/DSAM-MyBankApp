package cat.iticbcn.mybank

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class LoginActivity : AppCompatActivity() {

    private lateinit var llBack : LinearLayout
    private lateinit var tvNoSoc : TextView
    private lateinit var llNoRecordo: LinearLayout
    private lateinit var llMesInfo: LinearLayout

    private lateinit var ivAvatar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponents()
        initListeners()
        accioPerEsborrar()
    }

    private fun accioPerEsborrar() {
        ivAvatar = findViewById(R.id.ivAvatar)
        ivAvatar.setOnClickListener { v ->
            val intent = android.content.Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initComponents() {
        llBack = findViewById<LinearLayout>(R.id.llBack)
        llNoRecordo = findViewById<LinearLayout>(R.id.llNoRecordo)
        llMesInfo = findViewById<LinearLayout>(R.id.llMesInfo)

    }
    private fun initListeners() {
        llBack.setOnClickListener{
            //val intent = android.content.Intent(this, MainActivity::class.java)
            //startActivity(intent)
            finish()
        }

        llNoRecordo.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Si no recordes la contrasenya, no ports entrar")
                .setMessage("Contactarem amb tu telefònicament per ajudar-te a recuperar l'accés al teu compte.")
                .setPositiveButton("D'acord") { dialog, which ->
                    dialog.dismiss()
                }
                .setCancelable(true) // permet tocar fora per cancel·lar
                .show()
        }

        llMesInfo.setOnClickListener {
            val elements = listOf(
                Pair ("Política de Privadesa per a clients i potencials clients d\'ING","Us expliquem d\'una manera senzilla i transparent el tractament que fem de les vostres dades personals."),
                Pair( "Quines dades personals tractem?","Només les dades necessàries per complir amb les finalitats previstes."),
                Pair("Amb qui i per què compartim les teves dades?","Compartim dades amb altres entitats del Grup ING i amb tercers. T\'expliquem per què."),
                Pair("Quins són els teus drets i com exercir-los?","Coneix els teus drets per tenir un control sobre les dades."),
                Pair("Quant de temps conservem les teves dades?","Només el temps necessari per complir amb la finalitat per a què els recollim o per complir amb la llei."),
                Pair("Com protegim les teves dades personals?","Apliquem les mesures necessàries per garantir que les teves dades estiguin segures")
            )

            val dialeg = Dialog(this)
            dialeg.setContentView(R.layout.dialog_security_info_extra)

            val container = dialeg.findViewById<LinearLayout>(R.id.llBody)
            val inflater = LayoutInflater.from(this)

            for ((titol,descripcio) in elements) {
                val item = inflater.inflate(R.layout.item_securiry_info,container,false)

                item.findViewById<TextView>(R.id.tvTitol).text = titol
                item.findViewById<TextView>(R.id.tvDescripcio).text = descripcio


                // (Opcional) Afegeix un listener o canvia el color
                item.setOnClickListener {
                    Toast.makeText(this, "Has clicat $titol", Toast.LENGTH_SHORT).show()
                }

                // Finalment, afegeix la targeta al layout
                container.addView(item)
            }

            dialeg.show()

            val btnTancar = dialeg.findViewById<MaterialButton>(R.id.btnTancar)
            btnTancar.setOnClickListener {
                dialeg.dismiss()
            }
        }


    }
}