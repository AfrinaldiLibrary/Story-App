package com.example.storyapp.fragments.detail

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
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

//        postponeEnterTransition()
//        (view.parent as? ViewGroup)?.doOnPreDraw {
//            startPostponedEnterTransition()
//        }

        Log.e("transitionDetail", binding.tvName.transitionName)
        Log.e("transitionDetail", binding.tvDate.transitionName)
        Log.e("transitionDetail", binding.tvDesc.transitionName)
        Log.e("transitionDetail", binding.ivPhoto.transitionName)
        showDetails()
    }

    private fun showDetails() {
        binding.tvName.text = args.name
        binding.tvDate.text = args.date.withDateFormat()
        binding.tvDesc.text = args.desc
        Glide.with(binding.root)
            .load(args.photo)
            .centerCrop()
            .into(binding.ivPhoto)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_image)
    }

//    private fun showDetail() {
//        with(DetailFragmentArgs.fromBundle(arguments as Bundle)) {
//            val dataName = name
//            val dataDate = date
//            val dataDescription = desc
//            val dataPhoto = photo
//
//            binding.apply {
//                tvName.text = dataName
//                tvDate.text = dataDate.withDateFormat()
//                tvDesc.text = dataDescription
//                Glide.with(binding.root)
//                    .load(dataPhoto)
//                    .listener(object : RequestListener<Drawable> {
//                        override fun onLoadFailed(
//                            e: GlideException?,
//                            model: Any?,
//                            target: Target<Drawable>?,
//                            isFirstResource: Boolean
//                        ): Boolean {
//                            view?.doOnPreDraw {
//                                startPostponedEnterTransition()
//                            }
//                            return false
//                        }
//
//                        override fun onResourceReady(
//                            resource: Drawable?,
//                            model: Any?,
//                            target: Target<Drawable>?,
//                            dataSource: DataSource?,
//                            isFirstResource: Boolean
//                        ): Boolean {
//                            view?.doOnPreDraw {
//                                startPostponedEnterTransition()
//                            }
//                            return false
//                        }
//                    })
//                    .centerCrop()
//                    .into(ivPhoto)
//            }
//        }
//    }
}