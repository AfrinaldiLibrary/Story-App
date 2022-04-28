package com.example.storyapp.fragments.detail

import android.os.Bundle
import android.transition.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentDetailBinding
import com.example.storyapp.helper.withDateFormat

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.detail_page)

        showDetails()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = AutoTransition()
        animation.duration = 300
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    private fun showDetails() {
        binding.tvName.text = args.name
        binding.tvDate.text = args.date.withDateFormat()
        binding.tvDesc.text = args.desc
        Glide.with(binding.root)
            .load(args.photo)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_image)
            )
            .centerCrop()
            .into(binding.ivPhoto)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}