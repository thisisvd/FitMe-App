package com.vdcodeassociate.fitme.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.databinding.FragmentSupportBinding
import com.vdcodeassociate.fitme.utils.Resource
import com.vdcodeassociate.fitme.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SupportFragment : Fragment(R.layout.fragment_support) {

    private val TAG = "SupportFragment"

    // viewBinding
    private lateinit var binding: FragmentSupportBinding

    // getting arguments as passed
    private val args: SupportFragmentArgs by navArgs()

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSupportBinding.bind(view)

        // getting arguments as passed
        val isHelp = args.myArgs

        binding.apply {

            // get ready which fragment is being used
            setUpFragment(isHelp)
            binding.toolbar.text = isHelp

            // on button clicked
            supportButton.setOnClickListener {
                onButtonPressed(isHelp)
            }

            // onBack pressed
            back.setOnClickListener {
                findNavController().popBackStack()
            }

            // edit text on watch listener
            supportEditText.apply {
                doOnTextChanged { _, _, _, _ ->
                    if (supportLayout.error != null) {
                        supportLayout.error = null
                    }
                }
            }

            // view model observers
            viewModelObservers()
        }
    }

    // observers
    private fun viewModelObservers() {
        binding.apply {
            viewModel.supportObserver.observe(viewLifecycleOwner) { value ->
                when (value) {
                    is Resource.Success -> {
                        value.data?.let {
                            progressCircular.visibility = View.GONE
                            if (it == "Feedback") {
                                showDialog(
                                    "Feedback!",
                                    "Thank you for taking the time to provide your feedback, it help us to improve your experience!",
                                    R.drawable.feedback_big_icons8
                                )
                            } else if (it == "Help") {
                                showDialog(
                                    "Request Sent!",
                                    "Your request has been successfully recorded and will be responded soon!",
                                    R.drawable.help_red_icons8
                                )
                            }
                        }
                    }

                    is Resource.Error -> {
                        value.message?.let { message ->
                            Log.d(TAG, "An error occurred : $message")
                            Snackbar.make(requireView(), "Error occurred!", Snackbar.LENGTH_SHORT)
                                .show()
                        }
                        progressCircular.visibility = View.GONE
                    }

                    is Resource.Loading -> {
                        Log.d(TAG, "Loading...")
                        progressCircular.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    // on button pressed
    private fun onButtonPressed(isHelp: String) {
        binding.apply {
            if (!isTextEmpty(isHelp)) {
                if (isHelp == "Get Help!") {
                    viewModel.addHelp(
                        supportEditTextHelpEmail.text.toString(), supportEditText.text.toString()
                    )
                } else {
                    viewModel.addFeedback(supportEditText.text.toString())
                }
            }
        }
    }

    // on complete dialog
    private fun showDialog(title: String, message: String, icon: Int) {
        val dialog = MaterialAlertDialogBuilder(
            requireContext(), R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
        ).setTitle(title).setMessage(message).setIcon(icon).setPositiveButton("OK") { _, _ ->
            findNavController().popBackStack()
        }.setCancelable(false).create()
        dialog.show()
    }

    private fun isTextEmpty(isHelp: String): Boolean {
        binding.apply {
            var result = false

            if (isHelp == "Get Help!") {
                if (supportEditTextHelpEmail.text!!.isEmpty()) {
                    supportLayoutHelpEmail.error = "Required!"
                    result = true
                }
            }

            if (supportEditText.text!!.isEmpty()) {
                supportLayout.error = "Required!"
                result = true
            }

            return result
        }
    }

    private fun setUpFragment(isHelp: String) {
        binding.apply {
            if (isHelp == "Get Help!") {
                supportImage.setImageResource(R.drawable.inquiry_icons8)
                supportUText.text = "Type your query below you will be responded soon!"
                supportEditText.hint = "Type your issue here!"
                supportButton.text = "Send your request"
                supportEditTextHelpEmail.hint = "Type your email here!"
                supportLayoutHelpEmail.visibility = View.VISIBLE
            } else {
                supportUTextSmall.visibility = View.VISIBLE
                supportImage.setImageResource(R.drawable.feedback_ultra_icons8)
                supportUText.text = "Do you have some feedback?"
                supportUTextSmall.text = "So we did a good job?"
                supportEditText.hint = "Tell us in words..."
                supportButton.text = "Submit"
                supportLayoutHelpEmail.visibility = View.GONE
            }
        }
    }
}