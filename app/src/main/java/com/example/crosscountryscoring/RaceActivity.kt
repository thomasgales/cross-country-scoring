package com.example.crosscountryscoring

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.crosscountryscoring.databinding.ActivityRaceBinding

class RaceActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
