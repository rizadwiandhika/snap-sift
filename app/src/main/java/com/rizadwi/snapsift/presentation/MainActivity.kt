package com.rizadwi.snapsift.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

        replaceFragment(SourceFragment(::replaceFragment))
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(R.id.flFrame, fragment)
            .addToBackStack(fragment::class.simpleName).commit()
    }
}