package com.example.quitsmokingnow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quitsmokingnow.databinding.FragmentDefaultIntroduceBinding

class DefaultIntroduceFragment : AppCompatActivity() {
    private lateinit var binding: FragmentDefaultIntroduceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDefaultIntroduceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //  Navigation.findNavController(binding.root).navigate(R.id.action_defaultIntroduceFragment_to_settingFragment1)
    }
}