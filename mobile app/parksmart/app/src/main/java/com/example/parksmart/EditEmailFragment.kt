package com.example.parksmart

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.parksmart.databinding.FragmentEditEmailBinding


class EditEmailFragment : Fragment() {

    private lateinit var viewModel: EditViewModel

    private lateinit var sharedPreference: SharedPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentEditEmailBinding>(inflater,
            R.layout.fragment_edit_email,container,false)

        viewModel = ViewModelProviders.of(this).get(EditViewModel::class.java)

        sharedPreference = SharedPreference(requireContext())

        binding.saveButton.setOnClickListener{onSaveChanges(binding.email.text.toString())}

        return binding.root
    }

    private fun onSaveChanges(email: String) {
        val data = mapOf("email" to email)
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
