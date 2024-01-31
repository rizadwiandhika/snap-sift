package com.rizadwi.snapsift.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rizadwi.snapsift.R
import com.rizadwi.snapsift.databinding.ActivityMainBinding
import com.rizadwi.snapsift.presentation.fragment.SourceFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.flFrame, SourceFragment()).commit()
    }
}