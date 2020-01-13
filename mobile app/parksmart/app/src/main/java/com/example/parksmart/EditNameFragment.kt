package com.example.parksmart

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.parksmart.databinding.FragmentEditNameBinding
import com.example.parksmart.databinding.FragmentPaymentDetailsBinding


class EditNameFragment : Fragment() {

    private lateinit var viewModel: EditViewModel

    private lateinit var sharedPreference: SharedPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentEditNameBinding>(inflater,
            R.layout.fragment_edit_name,container,false)

        viewModel = ViewModelProviders.of(this).get(EditViewModel::class.java)

        sharedPreference = SharedPreference(requireContext())

        binding.saveButton.setOnClickListener{onSaveChanges(binding.firstName.text.toString(), binding.lastName.text.toString())}

        return binding.root
    }

    private fun onSaveChanges(firstName: String, lastName: String) {
        val data = mapOf("firstName" to firstName, "lastName" to lastName)
        val user_id = sharedPreference.getValueString("user_id")

        if (user_id != null) {
            viewModel.fetchEditDetails(data, user_id)

            viewModel.editLiveData.observe(this, Observer {
                if (it["status"] == "200"){

                }
            })
        }
    }

}
