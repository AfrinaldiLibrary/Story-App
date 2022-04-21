package com.example.storyapp.fragments.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentDetailBinding
import com.example.storyapp.helper.withDateFormat


class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(DetailFragmentArgs.fromBundle(arguments as Bundle)){
            val dataName = name
            val dataDate = date
            val dataDescription = description
            val dataPhoto = photo

            binding.apply {
                tvName.text = dataName
                tvDate.text = dataDate.withDateFormat()
                tvDesc.text = dataDescription
                Glide.with(binding.root)
                    .load(dataPhoto)
                    .centerCrop()
                    .into(ivPhoto)
            }
        }
    }
}