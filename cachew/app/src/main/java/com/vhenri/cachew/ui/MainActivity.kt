package com.vhenri.cachew.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.vhenri.cachew.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initBindings()
        initObservers()
    }

    private fun initBindings(){
        binding.viewModel = mainViewModel
    }

    private fun initObservers(){
        mainViewModel.totalAvailableBulbs.observe(this, Observer {
            Log.d("###", "$it")
        })
    }
}