package pt.isel.leim.my_score.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import pt.isel.leim.my_score.R


class ListOldGames : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

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
        setContentView(R.layout.activity_list_old_games)
        auth = Firebase.auth
        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance(urlDataBase);
        addOldGameToUI()

        setSupportActionBar(findViewById(R.id.main_toolbar))
        var actionBar = getSupportActionBar()
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)
    }

    fun decodeFromFirebaseBase64(image: String?): Bitmap? {
        val decodedByteArray = Base64.decode(image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
    }


    fun addOldGameToUI() {
        var ref = firebaseDatabase!!.getReference("users").child(auth.uid!!).child("games")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                //numGames = dataSnapshot.getValue<Int>()!!
                var count = 0
                for (postSnapshot in dataSnapshot.children) {
                    count += 1
                    var nodeKey:String = postSnapshot.key as String
                    var imgBase64:String = postSnapshot.child("imageEncodedG").value as String
                    var imgBitmap: Bitmap? = decodeFromFirebaseBase64(imgBase64)

                    //layout
                    val linearLayout = LinearLayout(this@ListOldGames)
                    linearLayout.id = View.generateViewId()
                    linearLayout.setGravity(Gravity.CENTER)
                    linearLayout.setBackgroundColor(Color.parseColor("#EBE6FC"))
                    val linLayoutParams = LinearLayout.LayoutParams(
                        750,
                        200
                    )
                    linLayoutParams.gravity = Gravity.CENTER
                    linearLayout.setLayoutParams(linLayoutParams)

                    // image
                    val gameImage = ImageView(this@ListOldGames)
                    gameImage.id = View.generateViewId()
                    val layoutParams = LinearLayout.LayoutParams(
                        345,
                        145
                    )
                    layoutParams.setMargins(0, 20, 20, 20)
                    //layout_constraintEnd_toEndOf
                    layoutParams.gravity = Gravity.CENTER or Gravity.RIGHT
                    //gameImage.setLayoutParams(layoutParams)
                    gameImage.setImageBitmap(imgBitmap)
                    //gameImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.grupo_foot))

                    //  TextView
                    val gameName = TextView(this@ListOldGames)
                    gameName.id = View.generateViewId()
                    gameName.typeface = ResourcesCompat.getFont(this@ListOldGames, R.font.bangers)
                    gameName.textSize = 28f
                    gameName.setTextColor(Color.parseColor("#FF000000"))
                    gameName.text = "GAME#$count"
                    gameName.setGravity(Gravity.CENTER)
                    val layoutParamsTV = LinearLayout.LayoutParams(
                        350,
                        125
                    )
                    layoutParamsTV.gravity = Gravity.CENTER
                    gameName.setLayoutParams(layoutParamsTV)
                    gameName.textAlignment = View.TEXT_ALIGNMENT_CENTER

                    //adding
                    val llMain = findViewById<LinearLayout>(R.id.mainLinear)
                    linearLayout.addView(gameImage)
                    linearLayout.addView(gameName)
                    llMain.addView(linearLayout)
                    val space = Space(this@ListOldGames)
                    space.setLayoutParams(
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            45
                        )
                    )
                    llMain.addView(space)
                    linearLayout.setOnClickListener {
                        val intent = Intent(this@ListOldGames, OldGame::class.java)
                        intent.putExtra("nodeKey", nodeKey)
                        startActivity(intent)
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Failed", "Here")
            }
        }
        ref.addValueEventListener(postListener)
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