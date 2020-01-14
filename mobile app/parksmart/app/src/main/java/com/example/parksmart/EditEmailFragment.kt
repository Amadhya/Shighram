package com.example.parksmart

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        Log.i("abca","in edit--------------")
        val binding = DataBindingUtil.inflate<FragmentEditEmailBinding>(inflater,
            R.layout.fragment_edit_email,container,false)

        viewModel = ViewModelProviders.of(this).get(EditViewModel::class.java)

        sharedPreference = SharedPreference(requireContext())

        binding.email.setText(arguments?.get("email").toString())

        binding.saveButton.setOnClickListener{onSaveChanges(binding.email.text.toString())}

        return binding.root
    }

    private fun onSaveChanges(email: String) {
        val data = mapOf("email" to email)
        val token = sharedPreference.getValueString("token")

        if (token != null) {
            viewModel.fetchEditDetails(data, token)

            viewModel.editLiveData.observe(this, Observer {
                Log.i("abca", "--------------")
                if (it["status"] == "200"){
                    sharedPreference.save("token", it["token"].toString())
                }
                Toast.makeText(requireContext(), it["message"], Toast.LENGTH_LONG).show()
            })
            viewModel.errorLiveData.observe(this, Observer {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            })
        }
    }

}
