package com.vdcodeassociate.fitme.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.vdcodeassociate.fitme.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {

    // TAG
    private val TAG = "VideoFragment"

    // view binding
    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    // nav args
    private val args: VideoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            // back
            back.setOnClickListener {
                findNavController().popBackStack()
            }

            // set up UI
            setUpUI()

            // load video
            loadVideo()

        }
    }

    // load video
    private fun loadVideo() {
        binding.apply {

            if (args.videoItem.id != null) {

                lifecycle.addObserver(youtubePlayerView)

                youtubePlayerView.addYouTubePlayerListener(object :
                    AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(args.videoItem.id.videoId, 0f)
                    }
                })
            }
        }
    }

    // set up UI
    private fun setUpUI() {
        binding.apply {
            args.apply {
                if (videoItem.id != null) {

                    // channel name
                    channelName.text = videoItem.snippet.channelTitle

                    // title
                    videoTitle.text = videoItem.snippet.title

                    // description
                    videoDescription.text = videoItem.snippet.description

                    // share video
                    shareVideo.setOnClickListener {
                        val videoLink = "https://www.youtube.com/watch?v=${videoItem.id.videoId}"
                        val sharingIntent = Intent(Intent.ACTION_SEND)
                        sharingIntent.type = "text/plain"
                        val shareBody = "Hi!, I founded an useful fitness video.\n\n" +
                                "See the video below.\n\nVideo link : $videoLink" +
                                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
                        startActivity(sharingIntent)
                    }

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}