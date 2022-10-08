package pt.isel.leim.my_score.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import pt.isel.leim.my_score.R

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        goAbout()
        goSettings()
        goHelp()
        goNewGame()
        goHistoric()
    }


    private fun goAbout(){
        val about: TextView = findViewById(R.id.about_menu)
        about.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, About::class.java)
            startActivity(intent)
        })
    }

    private fun goSettings(){
        val settings: TextView = findViewById(R.id.settings_menu)
        settings.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        })
    }

    private fun goHelp(){
        val help: TextView = findViewById(R.id.help_menu)
        help.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, Help::class.java)
            startActivity(intent)
        })
    }

    private fun goNewGame(){
        val newGame: TextView = findViewById(R.id.new_game_menu)
        newGame.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, NewGame::class.java)
            startActivity(intent)
        })
    }

    private fun goHistoric(){
        val historic: TextView = findViewById(R.id.historic_menu)
        historic.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ListOldGames::class.java)
            startActivity(intent)
        })
    }


}