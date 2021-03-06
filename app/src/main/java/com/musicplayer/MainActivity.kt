package com.musicplayer

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mp:MediaPlayer
    private var totalTime:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mp = MediaPlayer.create(this, R.raw.music1)
        mp.isLooping = true
        mp.setVolume(0.5f, 0.5f)
        totalTime = mp.duration

        //volume Bar
        volumeBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekbar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        var volumeNum = progress / 100.0f
                        mp.setVolume(volumeNum, volumeNum)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }
            }
        )

        //position Bar
        positionBar.max = totalTime
        positionBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        mp.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            }
        )

        //thread
    Thread(Runnable {
        while (mp != null){
            try{
                var msg=Message()
                msg.what = mp.currentPosition
                handler.sendMessage(msg)
            }
            catch (e: InterruptedException)
            {
            }
        }
    }).start()
    }

    public override fun onBackPressed() {
        var dialog=DialogFragment();
        dialog.show(supportFragmentManager,"customDialog");
    }

    @SuppressLint("HandlerLeak")
    var handler=object:Handler(){
        override fun handleMessage(msg: Message) {
            var currentPosition=msg.what
            //update positionBar
            positionBar.progress = currentPosition

            //update labels
            var elapsedTime=createTimeLabel(currentPosition)
            elapsedTimeLabel.text=elapsedTime

            var remainingTime = createTimeLabel(totalTime - currentPosition)
            remainingTimeLabel.text="-$remainingTime"
        }
    }

    fun createTimeLabel(time: Int):String{
        var timeLabel = ""
        var min=time/1000/60
        var sec=time/1000%60
        timeLabel = "$min:"
        if(sec<10) timeLabel += "0"
        timeLabel+=sec

        return timeLabel
    }

    fun playBtnClick(v: View)
    {

        if(mp.isPlaying)
        {
            //stop
            mp.pause()
            playBtn.setBackgroundResource(R.drawable.play)
            Toast.makeText(this@MainActivity, "Song Started", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //start
            mp.start()
            playBtn.setBackgroundResource(R.drawable.stop)
            Toast.makeText(this@MainActivity, "Song Paused", Toast.LENGTH_SHORT).show();
        }
    }
}
