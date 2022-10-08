package pt.isel.leim.my_score.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import pt.isel.leim.my_score.R

class OldGame : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private val urlDataBase: String =
        "https://myscore-ef1a7-default-rtdb.europe-west1.firebasedatabase.app"
    // creating a variable for our
    // Firebase Database.
    var firebaseDatabase: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance(urlDataBase);
        setContentView(R.layout.activity_old_game)
        setActivity()

        setSupportActionBar(findViewById(R.id.main_toolbar))
        var actionBar = getSupportActionBar()
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)
    }

    private fun setActivity() {
        val extras = intent.extras
        if (extras != null) {
            var gameKey: String = extras.getString("nodeKey") as String
            var ref = firebaseDatabase!!.getReference("users").child(auth.uid!!).child("games").child(gameKey)
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    var imgBase64:String = dataSnapshot.child("imageEncodedG").value as String
                    var imgBitmap: Bitmap? = decodeFromFirebaseBase64(imgBase64)
                    var homeTeamName:String = dataSnapshot.child("homeTeamG").value as String
                    var homeTeamScore:String = dataSnapshot.child("homeTeamScoreG").value.toString()
                    var awayTeamName:String = dataSnapshot.child("awayTeamG").value as String
                    var awayTeamScore:String = dataSnapshot.child("awayTeamScoreG").value.toString()
                    var bestMoment:String = dataSnapshot.child("bestMomentG").value as String
                    var date:String = dataSnapshot.child("dateG").value as String
                    var mvp:String = dataSnapshot.child("mvpG").value as String
                    var location:String = dataSnapshot.child("locationG").value as String

                    val homeTeam = findViewById<TextView>(R.id.old_game_home_team)
                    homeTeam.text = homeTeamName
                    val homeScore = findViewById<TextView>(R.id.old_game_score_home)
                    homeScore.text = ""+homeTeamScore
                    val awayTeam = findViewById<TextView>(R.id.old_game_away_team)
                    awayTeam.text = awayTeamName
                    val awayScore = findViewById<TextView>(R.id.old_game_score_away)
                    awayScore.text = ""+awayTeamScore
                    val bestGameMoment = findViewById<TextView>(R.id.old_game_moment)
                    bestGameMoment.text = bestMoment
                    val gameDate = findViewById<TextView>(R.id.old_game_date)
                    gameDate.text = date
                    val mvpDate = findViewById<TextView>(R.id.old_game_mvp)
                    mvpDate.text = mvp
                    val gameLocation = findViewById<TextView>(R.id.old_game_location)
                    gameLocation.text = location
                    val image = findViewById<ImageView>(R.id.old_game_image)
                    image.setImageBitmap(imgBitmap)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("test", "nada")

                }
            }
            ref.addValueEventListener(postListener)
        }
    }

    fun decodeFromFirebaseBase64(image: String?): Bitmap? {
        val decodedByteArray = Base64.decode(image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}