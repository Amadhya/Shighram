package com.example.parksmart

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.parksmart.databinding.FragmentEditNameBinding


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

        binding.firstName.setText(arguments?.get("firstName").toString())
        binding.lastName.setText(arguments?.get("lastName").toString())

        binding.saveButton.setOnClickListener{onSaveChanges(binding.firstName.text.toString(), binding.lastName.text.toString())}

        return binding.root
    }

    private fun onSaveChanges(firstName: String, lastName: String) {
        val data = mapOf("firstName" to firstName, "lastName" to lastName)
        val token = sharedPreference.getValueString("token")
        if (token != null) {
            viewModel.fetchEditDetails(data, token)

            viewModel.editLiveData.observe(this, Observer {
                Toast.makeText(requireContext(), it["message"], Toast.LENGTH_LONG).show()
            })
            viewModel.errorLiveData.observe(this, Observer {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            })
        }
    }

}
