package pt.isel.leim.my_score.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.*
import pt.isel.leim.my_score.R

class NewGame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game)
        pressStartGame()

        setSupportActionBar(findViewById(R.id.main_toolbar))
        var actionBar = getSupportActionBar()
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)
    }


    private fun setNewGame (){
        // initializing our edittext and button
        var homeTeamName:EditText = findViewById(R.id.home_team_et)
        val homeTeamNameStr = homeTeamName.text.toString()

        var awayTeamName:EditText = findViewById(R.id.away_team_et);
        val awayTeamNameStr = awayTeamName.text.toString()

        var time:EditText = findViewById(R.id.time_et);
        val timeStr = time.text.toString()

        var locationName:EditText = findViewById(R.id.location_et);
        val locationNameStr = locationName.text.toString()

        if (TextUtils.isEmpty(homeTeamNameStr) || TextUtils.isEmpty(awayTeamNameStr)
            || TextUtils.isEmpty(locationNameStr)){
            Toast.makeText(this, getString(R.string.incorret_data), Toast.LENGTH_SHORT).show();
        }
        else{
            goToGame(homeTeamNameStr, awayTeamNameStr, timeStr, locationNameStr)
        }

    }

    private fun pressStartGame(){
        val btn: Button = findViewById(R.id.ball_button)
        btn.setOnClickListener(View.OnClickListener {
            setNewGame()
        })
    }

    private fun goToGame(homeTeam:String, awayTeam:String, time:String, location:String){
        val intent = Intent(this, Game::class.java)
        intent.putExtra("homeTeamName", homeTeam)
        intent.putExtra("awayTeamName", awayTeam)
        intent.putExtra("time", time)
        intent.putExtra("location", location)
        startActivity(intent)
    }

    private fun goMenu(){
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