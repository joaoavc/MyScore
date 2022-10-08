package pt.isel.leim.my_score.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import android.widget.Chronometer.OnChronometerTickListener
import androidx.appcompat.app.AppCompatActivity
import pt.isel.leim.my_score.R


class Game : AppCompatActivity() {

    private lateinit var homeTeamName : String
    private lateinit var awayTeamName : String
    private lateinit var locationName : String
    private lateinit var time : String
    private var gameEndend:Boolean = false
    private var awayGoal  = 0
    private var homeGoal  = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_game)

        setActivity()

        // calling the action bar
        setSupportActionBar(findViewById(R.id.main_toolbar))
        var actionBar = getSupportActionBar()
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)

        timer()
        homeGoal()
        awayGoal()
        startAnimation()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.game_menu, menu)

        if (!gameEndend) {
            menu.getItem(0).setVisible(false)
            menu.getItem(1).setVisible(true)
        }
        else {
            menu.getItem(0).setVisible(true)
            menu.getItem(1).setVisible(false)
        }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_stop_game -> {
                quitGame()
                return true
            }
            R.id.menu_save_game -> {
                saveGame()
                return true
            }
            android.R.id.home -> {
                if(!gameEndend)
                    Toast.makeText(this, getString(R.string.end_game), Toast.LENGTH_SHORT).show();
                else{
                    quitGame()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(!gameEndend)
            Toast.makeText(this, getString(R.string.end_game), Toast.LENGTH_SHORT).show();
        else{
            quitGame()
        }
    }

    private fun setActivity(){
        val extras = intent.extras
        if (extras != null) {
            homeTeamName = extras.getString("homeTeamName").toString()
            awayTeamName = extras.getString("awayTeamName").toString()
            locationName = extras.getString("location").toString()
            time = extras.getString("time").toString()

            val homeTeam = findViewById<TextView>(R.id.game_home_team)
            homeTeam.text = homeTeamName

            val awayTeam = findViewById<TextView>(R.id.game_away_team)
            awayTeam.text = awayTeamName

            val local = findViewById<TextView>(R.id.game_half)
            local.text = locationName
        }
    }


    private fun saveGame(){
        val intent = Intent(this, SaveGame::class.java)
        intent.putExtra("homeTeamName", homeTeamName)
        intent.putExtra("awayTeamName", awayTeamName)
        intent.putExtra("location", locationName)
        intent.putExtra("homeGoal", homeGoal)
        intent.putExtra("awayGoal", awayGoal)
        startActivity(intent)
    }

    private fun gameStopped(){
        val intent = Intent(this, pt.isel.leim.my_score.ui.Menu::class.java)
        gameEndend = true
        startActivity(intent)
    }

    private fun quitGame(){
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(getString(R.string.close_game))
            .setMessage(getString(R.string.ask_save))
            .setPositiveButton(getString(R.string.yes),
                DialogInterface.OnClickListener { dialog, which -> gameStopped() })
            .setNegativeButton(getString(R.string.no), null)
            .show()

    }

    private fun timer(){
        val simpleChronometer = findViewById<View>(R.id.simpleChronometer) as Chronometer // initiate a chronometer
        var timeEnd = ""
        if(time.length == 1)
            timeEnd = "0$time:00"
        else
            "$time:00"
        if(simpleChronometer.text.toString() != timeEnd) {
            simpleChronometer.start() // start a chronometer
            simpleChronometer.onChronometerTickListener = OnChronometerTickListener { chronometer ->
                if (chronometer.text.toString().equals(timeEnd, ignoreCase = true)) {
                    simpleChronometer.stop()
                    gameEndend = true
                    stopAnimatio()
                    invalidateOptionsMenu()
                }
            }
        }
    }

    private fun homeGoal(){
        val btn: Button = findViewById(R.id.home_add_goal)
        btn.setOnClickListener(View.OnClickListener {
            homeGoal += 1
            val homeScore = findViewById<TextView>(R.id.home_score)
            homeScore.text = ""+homeGoal
        })
        val btn2: Button = findViewById(R.id.home_sub_goal)
        btn2.setOnClickListener(View.OnClickListener {
            if(homeGoal>0) {
                homeGoal -= 1
                val homeScore = findViewById<TextView>(R.id.home_score)
                homeScore.text = "" + homeGoal
            }
        })
    }

    private fun awayGoal(){
        val btn: Button = findViewById(R.id.away_add_goal)
        btn.setOnClickListener(View.OnClickListener {
            awayGoal += 1
            val awayScore = findViewById<TextView>(R.id.away_score)
            awayScore.text = ""+awayGoal
        })
        val btn2: Button = findViewById(R.id.away_sub_goal)
        btn2.setOnClickListener(View.OnClickListener {
            if(awayGoal > 0) {
                awayGoal -= 1
                val awayScore = findViewById<TextView>(R.id.away_score)
                awayScore.text = "" + awayGoal
            }
        })
    }

    private fun startAnimation(){
        val ballImage = findViewById(R.id.ball_animation) as ImageView
        val rotateBomb: Animation = AnimationUtils.loadAnimation(this, R.anim.animation)
        ballImage.startAnimation(rotateBomb)
    }

    private fun stopAnimatio(){
        val ballImage = findViewById(R.id.ball_animation) as ImageView
        ballImage.getAnimation().cancel();
        ballImage.clearAnimation();
    }


}