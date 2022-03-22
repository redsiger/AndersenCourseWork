package com.example.androidschool.andersencoursework.ui.map

import android.content.Context
import android.icu.text.Transliterator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidschool.andersencoursework.BuildConfig
import com.example.androidschool.andersencoursework.R
import com.example.androidschool.andersencoursework.databinding.FragmentMapBinding
import com.example.androidschool.andersencoursework.di.appComponent
import com.example.androidschool.andersencoursework.ui.core.BaseFragment
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MapFragment: BaseFragment<FragmentMapBinding>(R.layout.fragment_map) {

    @Inject lateinit var appContext: Context

    override fun onAttach(context: Context) {
        requireActivity().appComponent.inject(this)
        super.onAttach(context)
    }

    override fun initBinding(view: View): FragmentMapBinding = FragmentMapBinding.bind(view)

    override fun initFragment() {
//        MapKitFactory.setApiKey(BuildConfig.YANDEX_MAP_API_KEY)
        MapKitFactory.initialize(requireContext())

        moveMap(Point(55.751574, 37.573856))
    }

    private fun moveMap(point: Point) {
        viewBinding.mapView.map.move(
            CameraPosition(
                point,
                11.0f, 0.0f, 0.0f
            ),
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )
    }

    override fun onStart() {
        super.onStart()
        viewBinding.mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        viewBinding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }
}