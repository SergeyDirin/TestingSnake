package com.sdirin.games.testingsnake.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.sdirin.games.testingsnake.R
import com.sdirin.games.testingsnake.utils.TopScores
import kotlinx.android.synthetic.main.game_over.*


class GameOverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_over)

        btn_new_game.setOnClickListener {
            finish()
        }

        btn_clear.setOnClickListener{
            TopScores(this).clearTop()
            showTop()
        }

        showTop()
    }

    fun showTop(){
        val topScores = TopScores(this)
        val top = topScores.getTop()
        for(id in (0..4)){
            if (top.size > id){
                val dateId = resources.getIdentifier("tv_top${id + 1}_date", "id", this.packageName)
                findViewById<TextView>(dateId).text = top[id].date
                val scoreId = resources.getIdentifier("tv_top${id + 1}_score", "id", this.packageName)
                findViewById<TextView>(scoreId).text = top[id].score.toString()
            } else {
                val dateId = resources.getIdentifier("tv_top${id + 1}_date", "id", this.packageName)
                findViewById<TextView>(dateId).text = "Unknown"
                val scoreId = resources.getIdentifier("tv_top${id + 1}_score", "id", this.packageName)
                findViewById<TextView>(scoreId).text = "-"

                main_layout.recomputeViewAttributes(findViewById<TextView>(dateId))
                main_layout.recomputeViewAttributes(findViewById<TextView>(scoreId))
            }
        }
    }
}
