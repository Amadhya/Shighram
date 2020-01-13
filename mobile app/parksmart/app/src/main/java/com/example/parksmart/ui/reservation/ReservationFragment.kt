package com.example.parksmart.ui.reservation

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.parksmart.R

class ReservationFragment : Fragment() {

    companion object {
        fun newInstance() = ReservationFragment()
    }

    private lateinit var viewModel: ReservationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reservation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ReservationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
