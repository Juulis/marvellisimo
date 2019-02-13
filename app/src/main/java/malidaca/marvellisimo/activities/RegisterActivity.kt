package malidaca.marvellisimo.activities

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import malidaca.marvellisimo.R
import malidaca.marvellisimo.services.FireBaseService
import malidaca.marvellisimo.utilities.SnackbarManager
import java.util.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var view: View
    private lateinit var snackbarManager: SnackbarManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        register_button.setOnClickListener(this)

        view = findViewById(android.R.id.content)
        snackbarManager = SnackbarManager()
        Picasso.get().load(R.drawable.background).placeholder(R.drawable.background).fit().into(register_background)
    }

    private fun createAccount(email: String, password: String, firstName: String, lastName: String) {
        if (email.isNotBlank() && password.isNotBlank() && firstName.isNotBlank() && lastName.isNotBlank()) {
            FireBaseService.createUser(email, password, firstName, lastName, this, view)
            Timer().schedule(
                    object : java.util.TimerTask() {
                        override fun run() {
                            finish()
                        }
                    },
                    3000
            )
        } else {
            snackbarManager.createSnackbar(view, getString(R.string.registration_failed_fields_missing), Color.RED)
        }
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.register_button -> createAccount(register_email.text.toString(), register_password.text.toString(), register_firstName.text.toString(), register_lastName.text.toString())
        }
    }

}
