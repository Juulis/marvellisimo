package malidaca.marvellisimo.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import malidaca.marvellisimo.R
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import android.graphics.Color
import malidaca.marvellisimo.services.FireBaseService
import malidaca.marvellisimo.utilities.SnackbarManager


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var view: View
    private lateinit var snackbarManager: SnackbarManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_login_button.setOnClickListener(this)
        login_register_button.setOnClickListener(this)

        view = findViewById(android.R.id.content)
        snackbarManager = SnackbarManager()

    }

    private fun signIn(email: String, password: String) {
        if (email.isNotBlank() && password.isNotBlank()) {
            FireBaseService.signIn(email, password, this, view)
            finish()
        } else {
            snackbarManager.createSnackbar(view, resources.getString(R.string.signin_failed_missing_fields), Color.RED)

        }
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.login_register_button -> {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
            R.id.login_login_button -> signIn(login_email.text.toString(), login_password.text.toString())
        }
    }
}
