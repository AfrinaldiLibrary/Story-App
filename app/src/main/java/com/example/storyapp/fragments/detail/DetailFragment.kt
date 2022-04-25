package com.example.storyapp.fragments.detail

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentDetailBinding
import com.example.storyapp.fragments.TestFragment
import com.example.storyapp.helper.withDateFormat


class DetailFragment : Fragment(){
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
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.detail_page)


        ViewCompat.setTransitionName(binding.ivPhoto, "iv_photo")
        ViewCompat.setTransitionName(binding.tvDate, "tv_date")
        ViewCompat.setTransitionName(binding.tvName, "tv_name")
        ViewCompat.setTransitionName(binding.tvDesc, "tv_desc")

        showDetail()

        binding.card.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                Pair(binding.testImage, "test_image"),
                Pair(binding.testDate, "test_date"),
                Pair(binding.testName, "test_name"),
                Pair(binding.testDesc, "test_desc")
            )

            view.findNavController().navigate(R.id.action_detailFragment_to_testFragment, null, null,extras)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_image)
    }

    private fun showDetail() {
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