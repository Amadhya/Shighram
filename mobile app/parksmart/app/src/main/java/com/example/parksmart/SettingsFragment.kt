package com.example.parksmart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.example.parksmart.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var sharedPreference: SharedPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentSettingsBinding>(inflater,
            R.layout.fragment_settings,container,false)

        sharedPreference = SharedPreference(requireContext())

        binding.personalInfoButton.setOnClickListener{navigateToPersonalInfo()}

        binding.paymentHistoryButton.setOnClickListener{navigateToPaymentHistory()}

        binding.logoutButton.setOnClickListener{onLogOut()}

        return binding.root
    }

    private fun navigateToPersonalInfo() {
        val actions = SettingsFragmentDirections.actionSettingsFragmentToPersonalInfoFragment()

        NavHostFragment.findNavController(this).navigate(actions)
    }

    private fun navigateToPaymentHistory() {
        val actions = SettingsFragmentDirections.actionSettingsFragmentToPaymentHistoryFragment()

        NavHostFragment.findNavController(this).navigate(actions)
    }

    private fun onLogOut() {
        sharedPreference.clearSharedPreference()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }
}
