package hu.ait.minimine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import hu.ait.minimine.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var flagModeOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resetButton.setOnClickListener {
            binding.mineFieldView.resetGame()
        }

        binding.flagModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            flagModeOn = isChecked
        }


    }

    fun isFlagModeOn(): Boolean {
        return flagModeOn
    }

    fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}