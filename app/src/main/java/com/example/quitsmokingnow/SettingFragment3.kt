package com.example.quitsmokingnow

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.quitsmokingnow.database.DetaiSmokingDatabase
import com.example.quitsmokingnow.databinding.FragmentSetting3Binding
import com.example.quitsmokingnow.model.DetailSmokingEntity


class SettingFragment3 : Fragment() {
    private lateinit var binding: FragmentSetting3Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetting3Binding.inflate(layoutInflater)
        binding.next.setOnClickListener {
            updatePrice()
        }
        return binding.root
    }

    private fun updatePrice() {
        val listInfo: List<DetailSmokingEntity> =
            DetaiSmokingDatabase.getDatabase(requireContext())?.mDetailDao()
                ?.getAll()!!
        val detail = listInfo[0]
        if (TextUtils.isEmpty(binding.edtPrice.toString().trim())) {
            return
        }
        val price: String = binding.edtPrice.text.toString().trim()
        detail.price = "${price.toDouble() / binding.chooseCriInpack.value}"
        DetaiSmokingDatabase.getDatabase(requireContext())?.mDetailDao()
            ?.update(detail)!!
        startActivity(Intent(requireActivity(),MainActivity::class.java))
    }
}