package com.vhenri.cachew.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.vhenri.cachew.R
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

    private fun initBindings() {
        binding.viewModel = mainViewModel
        binding.btnRunSim.setOnClickListener {
            binding.infoText.visibility = View.VISIBLE
            binding.infoText.text = getString(R.string.loading)
            mainViewModel.runSimulation()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initObservers() {
        mainViewModel.totalUniqueColors.observe(this, Observer {
            binding.infoText.visibility = View.GONE
            binding.uniqueColorsText.visibility = View.VISIBLE
            val text = getString(R.string.unique_colors_text)
            binding.uniqueColorsText.text = "$text $it"
        })
        mainViewModel.averageUniqueColors.observe(this, Observer {
            binding.infoText.visibility = View.GONE
            binding.averageColorsText.visibility = View.VISIBLE
            val text = getString(R.string.average_unique_colors)
            val num = "%.2f".format(it)
            binding.averageColorsText.text = "$text $num"
        })
        mainViewModel.showError.observe(this, Observer {
            if (it) {
                showErrorAlert()
            }
        })
    }

    private fun showErrorAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(getString(R.string.error_oom_title))
        dialog.setMessage(getString(R.string.error_oom_text))
        dialog.setPositiveButton(android.R.string.ok) { _, _ ->
            finish()
            startActivity(intent)
        }
        dialog.show()
    }
}