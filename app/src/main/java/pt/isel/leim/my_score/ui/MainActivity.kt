package pt.isel.leim.my_score.ui


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pt.isel.leim.my_score.R


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    public override fun onStart() {
        super.onStart()
        auth = Firebase.auth

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            enterAccount()
        }
        else{
            goLogIn()
        }
    }


    private fun enterAccount(){
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
    }

    private fun goLogIn(){
        val intent = Intent(this, LogIn::class.java)
        startActivity(intent)
    }


}

