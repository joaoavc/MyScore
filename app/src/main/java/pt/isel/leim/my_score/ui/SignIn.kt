package pt.isel.leim.my_score.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import org.json.JSONException
import org.json.JSONObject
import pt.isel.leim.my_score.R
import java.io.FileWriter
import java.io.PrintWriter


class SignIn : AppCompatActivity() {
    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private val urlDataBase: String =
        "https://myscore-ef1a7-default-rtdb.europe-west1.firebasedatabase.app"



    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]

        submit();
        accountLogIn();
    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            enterAccount()
        }
    }
    // [END on_start_check_user]

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                    enterAccount()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, getString(R.string.end_game),
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        // [END create_user_with_email]
    }

    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                // Email Verification sent
            }
        // [END send_email_verification]
    }

    private fun updateUI(user: FirebaseUser?) {

    }

    private fun reload() {

    }

    companion object {
        private const val TAG = "EmailPassword"
    }


    fun submit() {
        val btn: Button = findViewById(R.id.button_signin)
        btn.setOnClickListener(View.OnClickListener {
            val emailEditText = findViewById<TextView>(R.id.mail_signin)
            val emailStr = emailEditText.text.toString()
            val passwordEditText = findViewById<TextView>(R.id.password_signin)
            val passwordStr = passwordEditText.text.toString()
            createAccount(emailStr, passwordStr)
        })
    }

    private fun enterAccount(){
        val intent = Intent(this, Menu::class.java)
        //intent.putExtra("namePlanet", findViewById<TextView>(R.id.textViewp).text)
        startActivity(intent)
    }

    private fun accountLogIn(){
        val memberSignIn: TextView = findViewById(R.id.member_signin)
        memberSignIn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
        })
    }


}