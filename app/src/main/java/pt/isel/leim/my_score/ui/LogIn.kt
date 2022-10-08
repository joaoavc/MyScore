package pt.isel.leim.my_score.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pt.isel.leim.my_score.R

class LogIn : AppCompatActivity() {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]


    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        // [END initialize_auth]

        val currentUser = auth.currentUser

        if(currentUser != null){
            enterAccount()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]

        submit();
        noAccountSignIn();
    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
        }
    }
    // [END on_start_check_user]

    private fun enterAccount(){
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "EmailPassword"
    }


    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(LogIn.TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    enterAccount()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(LogIn.TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, getString(R.string.auth_fail),
                        Toast.LENGTH_SHORT).show()
                }
            }
        // [END sign_in_with_email]
    }

    private fun submit() {
        val btn: Button = findViewById(R.id.button_login)
        btn.setOnClickListener(View.OnClickListener {
            val emailEditText = findViewById<TextView>(R.id.mail_login)
            val emailStr = emailEditText.text.toString()
            val passwordEditText = findViewById<TextView>(R.id.password_login)
            val passwordStr = passwordEditText.text.toString()
            signIn(emailStr, passwordStr)
        })
    }

    private fun noAccountSignIn(){
        val memberSignIn: TextView = findViewById(R.id.not_member_login)
        memberSignIn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        })
    }
}