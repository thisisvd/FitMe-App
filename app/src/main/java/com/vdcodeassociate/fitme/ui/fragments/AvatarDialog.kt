package com.vdcodeassociate.fitme.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.vdcodeassociate.fitme.R
import com.vdcodeassociate.fitme.constants.Constants.AVATAR_ID
import com.vdcodeassociate.fitme.databinding.ChooseAvatarDialogBinding

class AvatarDialog: DialogFragment() {

    // viewBinding
    private lateinit var binding: ChooseAvatarDialogBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ChooseAvatarDialogBinding.bind(view)

        binding.apply {

            back.setOnClickListener {
                dialog!!.dismiss()
            }

            avatarBoy1.setOnClickListener {
                AVATAR_ID = R.drawable.profile_boy1_icons8
                dialog!!.dismiss()
            }

            avatarBoy2.setOnClickListener {
                AVATAR_ID = R.drawable.profile_boy2_icons8
                dialog!!.dismiss()
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.choose_avatar_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog!!.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

}