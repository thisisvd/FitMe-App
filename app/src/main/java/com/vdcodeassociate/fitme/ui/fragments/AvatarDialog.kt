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

            // on back pressed handle
            back.setOnClickListener {
                dialog!!.dismiss()
            }

            avatarBoy1.setOnClickListener {
                changeAvatarID(R.drawable.profile_boy1_icons8)
            }

            avatarBoy2.setOnClickListener {
                changeAvatarID(R.drawable.profile_boy2_icons8)
            }

            avatarBoy3.setOnClickListener {
                changeAvatarID(R.drawable.profile_boy3_icons8)
            }

            avatarBoy4.setOnClickListener {
                changeAvatarID(R.drawable.profile_boy4_icons8)
            }

            avatarGirl1.setOnClickListener {
                changeAvatarID(R.drawable.profile_girl1_icons8)
            }

            avatarGirl2.setOnClickListener {
                changeAvatarID(R.drawable.profile_girl2_icons8)
            }

            avatarGirl3.setOnClickListener {
                changeAvatarID(R.drawable.profile_girl3_icons8)
            }

            avatarGirl4.setOnClickListener {
                changeAvatarID(R.drawable.profile_girl4_icons8)
            }

            avatarOthers.setOnClickListener {
                changeAvatarID(R.drawable.profile_other_image)
            }

        }

    }

    // change const avatar id
    private fun changeAvatarID(id: Int){
        AVATAR_ID = id
        dialog!!.dismiss()
        onItemClickListener?.let {
            it(id)
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

    // On click listener
    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

}