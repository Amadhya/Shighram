package com.example.parksmart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.example.parksmart.databinding.PersonalInfoFragmentBinding
import com.example.parksmart.ui.payment.PaymentFragmentDirections


class PersonalInfoFragment : Fragment() {

    private lateinit var viewModel: PersonalInfoViewModel

    private lateinit var sharedPreference: SharedPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("abca","in--------------")
        val binding = DataBindingUtil.inflate<PersonalInfoFragmentBinding>(inflater,
            R.layout.personal_info_fragment,container,false)

        sharedPreference = SharedPreference(requireContext())

        val token = sharedPreference.getValueString("token")

        viewModel = ViewModelProviders.of(this).get(PersonalInfoViewModel::class.java)

        if (token != null) {
            viewModel.fetchPersonalInfo(token)
        }

        binding.nameButton.setOnClickListener{ navigateToEditName() }

        binding.emailButton.setOnClickListener{ navigateToEditEmail() }

        binding.phoneButton.setOnClickListener{ navigateToEditPhone() }

        return binding.root
    }

    private fun navigateToEditName() {
        viewModel.userInfoLiveData.observe(this, Observer {
            it.doneEvent { it1 ->
                if (it1["status"] == "200"){
                    val actions = PersonalInfoFragmentDirections.actionPersonalInfoFragmentToEditNameFragment(
                        it1["firstName"].toString(), it1["lastName"].toString()
                    )

                    NavHostFragment.findNavController(this@PersonalInfoFragment).navigate(actions)
                }
            }
        })
        viewModel.errorLiveData.observe(this, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun navigateToEditPhone() {
        viewModel.userInfoLiveData.observe(this, Observer {
            it.doneEvent { it1 ->
                if (it1["status"] == "200"){
                    val actions = PersonalInfoFragmentDirections.actionPersonalInfoFragmentToEditPhoneFragment(
                        it1["phone"].toString()
                    )

                    NavHostFragment.findNavController(this@PersonalInfoFragment).navigate(actions)
                }
            }
        })
        viewModel.errorLiveData.observe(this, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun navigateToEditEmail() {
        Log.i("abca","nav in--------------")
        viewModel.userInfoLiveData.observe(this, Observer {
            it.doneEvent { it1 ->
                if (it1["status"] == "200"){
                    val actions = PersonalInfoFragmentDirections.actionPersonalInfoFragmentToEditEmailFragment(
                        it1["email"].toString()
                    )

                    NavHostFragment.findNavController(this@PersonalInfoFragment).navigate(actions)
                }
            }
        })
        viewModel.errorLiveData.observe(this, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }
}
