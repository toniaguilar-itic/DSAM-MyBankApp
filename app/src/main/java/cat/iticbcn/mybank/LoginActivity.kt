package cat.iticbcn.mybank

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {

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

    }
    private fun initListeners() {

    }
}