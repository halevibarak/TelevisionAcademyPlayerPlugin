package com.applicaster.plugin.televisionacademyplayer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.applicaster.plugin_manager.playersmanager.Playable
import com.bitmovin.player.BitmovinPlayer
import com.bitmovin.player.config.media.SourceConfiguration
import com.bitmovin.player.config.media.SourceItem
import com.bitmovin.player.config.vr.VRContentType
import kotlinx.android.synthetic.main.activity_player.*

class TAPlayerActivity : AppCompatActivity() {

    private val KEY_CURRENT_PROGRESS = "KEY_CURRENT_PROGRESS"

    private var bitmovinPlayer: BitmovinPlayer? = null

    private var contentVideoURL: String? = null
    private var currentProgress: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        contentVideoURL = with(intent) { extras?.getString(PlayerContract.KEY_PLAYABLE) }
        if (savedInstanceState != null) {
            currentProgress = savedInstanceState.getDouble(KEY_CURRENT_PROGRESS, 0.0)
        }
        setContentView(R.layout.activity_player)
        bitmovinPlayer = bitmovinPlayerView.player
        initializePlayer()

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            this.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    override fun onStart() {
        super.onStart()
        bitmovinPlayerView.onStart()
    }

    override fun onResume() {
        super.onResume()
        bitmovinPlayerView.onResume()
    }

    override fun onPause() {
        bitmovinPlayerView.onPause()
        super.onPause()
    }

    override fun onStop() {
        bitmovinPlayerView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        bitmovinPlayerView.onDestroy()
        super.onDestroy()
    }

    private fun initializePlayer() {
        getContentVideoUrl()?.apply {
            // TODO:: uncomment it and use vrSourceItem as a source to turn on VR
//            val vrSourceItem = SourceItem(this)
//            // Get the current VRConfiguration of the SourceItem
//            val vrConfiguration = vrSourceItem.vrConfiguration
//            // Set the VrContentType on the VRConfiguration
//            vrConfiguration.vrContentType = VRContentType.SINGLE
//            // Set the start position to 180 degrees
//            vrConfiguration.startPosition = 180.0

            val sourceConfiguration = SourceConfiguration()
            sourceConfiguration.addSourceItem(this)
//            sourceConfiguration.addSourceItem(vrSourceItem)
            sourceConfiguration.startOffset = currentProgress
            bitmovinPlayer?.config?.playbackConfiguration?.isAutoplayEnabled = true
            bitmovinPlayer?.load(sourceConfiguration)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble(KEY_CURRENT_PROGRESS, bitmovinPlayer?.currentTime ?: 0.0)
    }

    private fun getContentVideoUrl() =
        if (ConfigurationRepository.testVideoUrl.isEmpty()) contentVideoURL else ConfigurationRepository.testVideoUrl
}