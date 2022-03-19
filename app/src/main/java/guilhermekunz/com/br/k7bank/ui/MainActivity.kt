package guilhermekunz.com.br.k7bank.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import guilhermekunz.com.br.k7bank.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}