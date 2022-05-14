package com.example.storyapp.fragments.map

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.storyapp.R
import com.example.storyapp.data.PrefManager
import com.example.storyapp.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment() {
    private lateinit var mMap: GoogleMap
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val mapViewModel: MapViewModel by viewModels()
    private lateinit var prefManager: PrefManager
    private var locationMap: ArrayList<LatLng>? = null
    private var userMap: ArrayList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private val callback = OnMapReadyCallback { googleMap ->
        locationMap = ArrayList()
        userMap = ArrayList()
        mMap = googleMap
        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }
        
        init()
        cekLocation()
        setMapStyle()
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
            if (!success) {
                Log.e("MapFragment", "Gagal")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e("MapFragment", "Error: ", exception)
        }
    }

    private fun cekLocation() {
        val token = prefManager.getToken().toString()
        mapViewModel.loadStoryLocationData(token)

        mapViewModel.getLocation().observe(viewLifecycleOwner){
            if (it != null){
                for (i in it.indices) {
                    locationMap!!.add(LatLng(it[i].lat, it[i].lon))
                    userMap!!.add(it[i].name)

                    mMap.addMarker(
                        MarkerOptions()
                            .position(locationMap!![i])
                            .title(userMap!![i])
                    )
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationMap!![i], 15f))
                }
            }
        }
    }

    private fun init() {
        prefManager = PrefManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}