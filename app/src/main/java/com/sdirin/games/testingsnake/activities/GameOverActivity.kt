package com.sdirin.games.testingsnake.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.sdirin.games.testingsnake.R
import com.sdirin.games.testingsnake.utils.TopScores
import kotlinx.android.synthetic.main.game_over.*


class GameOverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_over)

        tv_current_date.visibility = View.GONE
        tv_current_score.visibility = View.GONE

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
        val lastScore = topScores.getLastScore()
        var needLastScore = true
        for(id in (0..4)){
            if (top.size > id){
                val dateId = resources.getIdentifier("tv_top${id + 1}_date", "id", this.packageName)
                findViewById<TextView>(dateId).text = top[id].date
                val scoreId = resources.getIdentifier("tv_top${id + 1}_score", "id", this.packageName)
                findViewById<TextView>(scoreId).text = top[id].score.toString()
                if (top[id].date == lastScore.date && top[id].score == lastScore.score){
                    needLastScore = false
                    findViewById<TextView>(dateId).setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                    findViewById<TextView>(scoreId).setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                } else {
                    findViewById<TextView>(dateId).setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                    findViewById<TextView>(scoreId).setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                }
            } else {
                val dateId = resources.getIdentifier("tv_top${id + 1}_date", "id", this.packageName)
                findViewById<TextView>(dateId).text = "Unknown"
                val scoreId = resources.getIdentifier("tv_top${id + 1}_score", "id", this.packageName)
                findViewById<TextView>(scoreId).text = "-"

                main_layout.recomputeViewAttributes(findViewById<TextView>(dateId))
                main_layout.recomputeViewAttributes(findViewById<TextView>(scoreId))
            }
        }
        if (needLastScore && lastScore.date != "none") {
            tv_current_date.visibility = View.VISIBLE
            tv_current_date.text = lastScore.date
            tv_current_score.visibility = View.VISIBLE
            tv_current_score.text = lastScore.score.toString()
        }
    }
}
