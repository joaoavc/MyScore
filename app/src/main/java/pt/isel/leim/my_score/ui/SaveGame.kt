package pt.isel.leim.my_score.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pt.isel.leim.my_score.R
import pt.isel.leim.my_score.model.Game
import java.io.ByteArrayOutputStream
import java.nio.file.Files.size
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class SaveGame : AppCompatActivity() {

    private lateinit var img:Bitmap
    private lateinit var auth: FirebaseAuth
    private var numGames:Int = 0
    private val REQUEST_IMAGE_CAPTURE = 1
    private val urlDataBase: String =
        "https://myscore-ef1a7-default-rtdb.europe-west1.firebasedatabase.app"
    // creating a variable for our
    // Firebase Database.
    var firebaseDatabase: FirebaseDatabase? = null
    // creating a variable for our Database
    // Reference for Firebase.
    var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance(urlDataBase);
        setContentView(R.layout.activity_save_game)
        lunchCamera()
        save()
        setSupportActionBar(findViewById(R.id.main_toolbar))
        var actionBar = getSupportActionBar()
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)


    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun lunchCamera() {
        val btn: ImageButton = findViewById(R.id.imageButton)
        btn.setOnClickListener(View.OnClickListener {
            dispatchTakePictureIntent()
        })
    }

    private fun saveGame() {

        var mvp: EditText = findViewById(R.id.save_game_mvp);
        val mvpStr = mvp.text.toString()
        var moment: EditText = findViewById(R.id.save_game_moment);
        val momentStr = moment.text.toString()
        if (TextUtils.isEmpty(mvpStr) || TextUtils.isEmpty(momentStr) || img == null){
            Toast.makeText(this, getString(R.string.incorret_data), Toast.LENGTH_SHORT).show();
        }
        else{
            val simpleDateFormat = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDateAndTime: String = simpleDateFormat.format(Date())
            val dateKey = currentDateAndTime.replace(" ","")
            databaseReference = firebaseDatabase!!.getReference("users").child(auth.uid!!).child("games")
            val extras = intent.extras
            if (extras != null) {
                val homeTeamName = extras.getString("homeTeamName").toString()
                val awayTeamName = extras.getString("awayTeamName").toString()
                val homeScore = extras.getInt("homeGoal")
                val awayScore = extras.getInt("awayGoal")
                val locationName = extras.getString("location").toString()
                val imgBase64 = encodeBitmap(img)
                val game = Game(homeTeamName, awayTeamName, homeScore, awayScore,
                    currentDateAndTime, locationName, mvpStr, momentStr, imgBase64)
                databaseReference!!.addListenerForSingleValueEvent (object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        // inside the method of on Data change we are setting
                        // our object class to our database reference.
                        // data base reference will sends data to firebase.
                        databaseReference!!.push().setValue(game)
                        // after adding this data we are showing toast message.
                    }
                    override fun onCancelled(error: DatabaseError) {
                        // if the data is not added or it is cancelled then
                        // we are displaying a failure toast message.
                    }
                })
            }
        }
    }


    private fun save() {
        val btn: Button = findViewById(R.id.save_game_btn)
        btn.setOnClickListener(View.OnClickListener {
            saveGame()
            enterAccount()
        })
    }

    private fun enterAccount(){
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val extras = data?.extras
            img = (extras!!["data"] as Bitmap?)!!
            var imgGame: ImageView = findViewById(R.id.gameImage);
            imgGame.setImageBitmap(img)
        }
    }

    fun encodeBitmap(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val imageEncoded: String = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
        return imageEncoded
    }

    fun decodeFromFirebaseBase64(image: String?): Bitmap? {
        val decodedByteArray = Base64.decode(image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
    }

    private fun goMenu(){
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(getString(R.string.close_game))
            .setMessage(getString(R.string.ask_save))
            .setPositiveButton(getString(R.string.yes),
                DialogInterface.OnClickListener { dialog, which -> goBackMenu() })
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun goBackMenu(){
        val intent = Intent(this, Menu::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        goMenu()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                goMenu()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}