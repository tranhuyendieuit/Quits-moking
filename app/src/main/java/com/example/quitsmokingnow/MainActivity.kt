package com.example.quitsmokingnow

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.quitsmokingnow.database.DetaiSmokingDatabase
import com.example.quitsmokingnow.databinding.ActivityMainBinding
import com.example.quitsmokingnow.model.DetailSmokingEntity
import com.example.quitsmokingnow.model.TimeUnit
import com.example.quitsmokingnow.viewmodel.DetailSmokingViewModel
import com.example.quitsmokingnow.viewmodel.TimeViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var detailSmokingViewModel: DetailSmokingViewModel
    private lateinit var list: List<DetailSmokingEntity>
    private lateinit var formatter: DateTimeFormatter
    private lateinit var dateStart: LocalDateTime
    private lateinit var dateNow: LocalDateTime

    private lateinit var timeViewModel: TimeViewModel
    var milis: Long = 0
    var minitues: Int = 0
    var hours: Int = 0
    var days: Int = 0
    lateinit var timeUnit: TimeUnit

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

        binding.imgSetting.setOnClickListener {
            val intent = Intent(this, SmokingSetting::class.java)
            startActivity(intent)
            finish()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView(){
        DetaiSmokingDatabase.getDatabase(this)?.mDetailDao()?.deleteAllDetail()

        timeViewModel = ViewModelProvider(this)[TimeViewModel::class.java]
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        // get list from room-database
        list = DetaiSmokingDatabase.getDatabase(this)?.mDetailDao()?.getAll()!!
        Log.d("AAA",list.toString())
        if(list.size == 0){
            val intent = Intent(this, DefaultIntroduceFragment::class.java)
           startActivity(intent)
        }else if(list.size>0) {
            Log.d("AAA", list.toString())

            startCaculatorTime() // run hour counter with every update is 30 second

            // call viewmodel and update lên ui ux
            timeViewModel.getTimeViewModel().observe(this, {
                binding.minites.text = "${it.minites}"
                binding.hours.text = "${it.hours}"
                binding.days.text = "${it.days}"
                val totalHours = it.days * 24 + it.hours
                getInfoUser(totalHours)
            })
        }
    }

    private fun startCaculatorTime() {
        Timer().schedule(object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                getTime()
            }
        }, 0, 30000)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTime() {
        val timeStart: String = list[0].time // get time start quit smoking from RoomDatabase
        val timeCurrent: String = LocalDateTime.now()
            .format(
                DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm")
            ) // get time current from system

        dateStart = LocalDateTime.parse(timeStart, formatter)
        dateNow = LocalDateTime.parse(timeCurrent, formatter)

        // caculator distance time-start to time-current with type Integer;
        milis = (java.time.Duration.between(dateStart, dateNow).toMillis())



        minitues = Integer.parseInt("${(milis / 1000 / 60) % 60}")
        hours = Integer.parseInt("${(milis / 1000 / 60 / 60) % 24}")
        days = Integer.parseInt("${milis / 1000 / 60 / 60 / 24}")

        //call ViewModel and Update ;
        timeUnit = TimeUnit(minitues, hours, days)
        timeViewModel.setTime(timeUnit)

    }

    private fun getInfoUser(total_hours: Int) {

        val ciri = (list[0].amount).toFloat()
        val price = (list[0].price).toFloat()


        val wonBack = 24 - hours
        val dayQuit = days
        val moneySaved = (price * ciri) / 24 * total_hours
        val criticizeAvoid = ciri / 24 * total_hours


        binding.dayquits.text = "${dayQuit}"
        binding.moneySave.text = "${moneySaved.toInt()}"
        binding.criAvoid.text = "${criticizeAvoid.toInt()}"
        binding.wonback.text = "${wonBack}"
    }
}