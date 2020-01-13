package com.example.parksmart

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.example.parksmart.databinding.FragmentSettingsBinding
import com.example.parksmart.databinding.PersonalInfoFragmentBinding


class PersonalInfoFragment : Fragment() {

    private lateinit var viewModel: PersonalInfoViewModel

    private lateinit var sharedPreference: SharedPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<PersonalInfoFragmentBinding>(inflater,
            R.layout.personal_info_fragment,container,false)

        viewModel = ViewModelProviders.of(this).get(PersonalInfoViewModel::class.java)

        sharedPreference = SharedPreference(requireContext())

        val token = sharedPreference.getValueString("token")

        if (token != null) {
            viewModel.fetchPersonalInfo(token)
        }

        binding.nameButton.setOnClickListener{ navigateToEditName() }

        binding.emailButton.setOnClickListener{ navigateToEditEmail() }

        binding.phoneButton.setOnClickListener{ navigateToEditPhone() }

        return binding.root
    }

    fun navigateToEditName() {
        val actions = PersonalInfoFragmentDirections.actionPersonalInfoFragmentToEditNameFragment()

        NavHostFragment.findNavController(this).navigate(actions)
    }

    fun navigateToEditPhone() {
        val actions = PersonalInfoFragmentDirections.actionPersonalInfoFragmentToEditPhoneFragment()

        NavHostFragment.findNavController(this).navigate(actions)
    }

    fun navigateToEditEmail() {
        val actions = PersonalInfoFragmentDirections.actionPersonalInfoFragmentToEditEmailFragment()

        NavHostFragment.findNavController(this).navigate(actions)
    }

}
