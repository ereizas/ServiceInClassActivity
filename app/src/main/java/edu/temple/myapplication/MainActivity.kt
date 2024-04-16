package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var timerBinder : TimerService.TimerBinder
    var isConnected = false
    val serviceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            timerBinder = service as TimerService.TimerBinder
            isConnected=true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isConnected=false
        }

    }
    private lateinit var timerHandler : Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )
        timerHandler = Handler(Looper.getMainLooper()){
            true
        }
        findViewById<Button>(R.id.startButton).setOnClickListener {
            if(isConnected) timerBinder.start(100,timerHandler)
        }

        findViewById<Button>(R.id.pauseButton).setOnClickListener {
            if(isConnected) timerBinder.pause()
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if(isConnected) timerBinder.stop()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.timer_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.startOption->timerBinder.start(100,timerHandler)
            R.id.pauseOption->timerBinder.pause()
            R.id.stopOption->timerBinder.stop()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }
}