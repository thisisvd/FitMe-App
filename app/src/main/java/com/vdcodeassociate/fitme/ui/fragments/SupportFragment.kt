package com.vdcodeassociate.fitme.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hsalf.smileyrating.SmileyRating
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.databinding.FragmentSupportBinding

class SupportFragment : Fragment(R.layout.fragment_support) {

    // viewBinding
    private lateinit var binding: FragmentSupportBinding

    // getting arguments as passed
    val args: SupportFragmentArgs by navArgs()

    // private smile count
    private var smileCount = 4

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSupportBinding.bind(view)

        // getting arguments as passed
        val isHelp = args?.myArgs

        binding.apply {

            // get ready which fragment is being used
            if (isHelp != null) {
                setUpFragment(isHelp)
                binding.toolbar.text = isHelp
            }

            // Smile Listener
            smileRating.setSmileySelectedListener { type ->

                when {
                    SmileyRating.Type.GREAT == type -> smileCount = 5
                    SmileyRating.Type.GOOD == type -> smileCount = 4
                    SmileyRating.Type.OKAY == type -> smileCount = 3
                    SmileyRating.Type.BAD == type -> smileCount = 2
                    SmileyRating.Type.TERRIBLE == type -> smileCount = 1
                }

            }

            // on button clicked
            supportButton.setOnClickListener {
                onButtonPressed(isHelp!!)
            }

            // onBack pressed
            back.setOnClickListener {
                requireActivity().onBackPressed()
            }

            // edit text on watch listener
            supportEditText.apply {
                doOnTextChanged { _, _, _, _ ->
                    if (supportLayout.error != null) {
                        supportLayout.error = null
                    }
                }
            }

        }

    }

    // on button pressed
    private fun onButtonPressed(isHelp: String) {
        if (!isTextEmpty()) {
            if (isHelp == "Get Help!") {
                showDialog(
                    "Request Sent!",
                    "Your request has been successfully recorded and will be responded soon!",
                    R.drawable.help_red_icons8
                )
            } else {
                showDialog(
                    "Feedback!",
                    "Thank you for taking the time to provide your feedback, it help us to improve your experience!",
                    R.drawable.feedback_big_icons8
                )
            }
        }
    }

    // on complete dialog
    private fun showDialog(title: String, message: String, icon: Int) {
        val dialog = MaterialAlertDialogBuilder(
            requireContext(),
            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
        )
            .setTitle(title)
            .setMessage(message)
            .setIcon(icon)
            .setPositiveButton("OK") { _, _ ->
                requireActivity().onBackPressed()
            }
            .create()
        dialog.show()
    }

    private fun isTextEmpty(): Boolean {
        var result = false

        binding.apply {

            if (supportEditText.text!!.isEmpty()) {
                supportLayout.error = "Required!"
                result = true
            }

        }

        return result
    }

    private fun setUpFragment(isHelp: String) {
        binding.apply {

            if (isHelp == "Get Help!") {
                supportImage.setImageResource(R.drawable.inquiry_icons8)
                supportUText.text = "Type your query below you will be responded soon!"
                supportEditText.hint = "Type your issue here!"
                supportButton.text = "Send your request"
            } else {
                supportUTextSmall.visibility = View.VISIBLE
                smileRating.visibility = View.VISIBLE
                supportImage.setImageResource(R.drawable.feedback_ultra_icons8)
                supportUText.text = "Do you have some feedback?"
                supportUTextSmall.text = "So we did a good job?"
                supportEditText.hint = "Tell us in words..."
                supportButton.text = "Submit"
            }

        }

    }

}