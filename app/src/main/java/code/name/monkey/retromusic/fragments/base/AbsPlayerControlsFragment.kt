/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.fragments.base

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.whichFragment
import code.name.monkey.retromusic.fragments.MusicSeekSkipTouchListener
import code.name.monkey.retromusic.fragments.other.VolumeFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.PreferenceUtil.TIME_DISPLAY_MODE_REMAINING
import code.name.monkey.retromusic.util.PreferenceUtil.TIME_DISPLAY_MODE_TOGGLE
import code.name.monkey.retromusic.util.PreferenceUtil.TIME_DISPLAY_MODE_TOTAL
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.android.material.slider.Slider
import code.name.monkey.retromusic.SWAP_SHUFFLE_REPEAT_BUTTONS


/**
 * Created by hemanths on 24/09/17.
 */

abstract class AbsPlayerControlsFragment(@LayoutRes layout: Int) : AbsMusicServiceFragment(layout),
    MusicProgressViewUpdateHelper.Callback, SharedPreferences.OnSharedPreferenceChangeListener {

    protected abstract fun show()

    protected abstract fun hide()

    abstract fun setColor(color: MediaNotificationProcessor)

    var lastPlaybackControlsColor: Int = 0

    var lastDisabledPlaybackControlsColor: Int = 0

    private var isSeeking = false
    private var isShowingRemainingTime = false

    open val progressSlider: Slider? = null

    open val seekBar: SeekBar? = null

    abstract val shuffleButton: ImageButton

    abstract val repeatButton: ImageButton

    open val nextButton: ImageButton? = null

    open val previousButton: ImageButton? = null

    open val songTotalTime: TextView? = null

    open val songCurrentProgress: TextView? = null

    private var progressAnimator: ObjectAnimator? = null

    override fun onUpdateProgressViews(progress: Int, total: Int) {
        if (seekBar == null) {
            progressSlider?.valueTo = total.toFloat()

            progressSlider?.value =
                progress.toFloat().coerceIn(progressSlider?.valueFrom, progressSlider?.valueTo)
        } else {
            seekBar?.max = total

            if (isSeeking) {
                seekBar?.progress = progress
            } else {
                progressAnimator =
                    ObjectAnimator.ofInt(seekBar, "progress", progress).apply {
                        duration = SLIDER_ANIMATION_TIME
                        interpolator = LinearInterpolator()
                        start()
                    }

            }
        }

        val timeDisplayMode = PreferenceUtil.timeDisplayMode
        when (timeDisplayMode) {
            TIME_DISPLAY_MODE_TOTAL -> {
                songTotalTime?.text = MusicUtil.getReadableDurationString(total.toLong())
                songTotalTime?.setOnClickListener(null) // Remove any previous click listener
            }
            TIME_DISPLAY_MODE_REMAINING -> {
                songTotalTime?.text = MusicUtil.getReadableDurationString((total - progress).toLong())
                songTotalTime?.setOnClickListener(null) // Remove any previous click listener
            }
            TIME_DISPLAY_MODE_TOGGLE -> {
                if (isShowingRemainingTime) {
                    songTotalTime?.text = MusicUtil.getReadableDurationString((total - progress).toLong())
                } else {
                    songTotalTime?.text = MusicUtil.getReadableDurationString(total.toLong())
                }
                songTotalTime?.setOnClickListener {
                    isShowingRemainingTime = !isShowingRemainingTime
                    onUpdateProgressViews(progress, total) // Update immediately
                }
            }
        }
        songCurrentProgress?.text = MusicUtil.getReadableDurationString(progress.toLong())
    }

    private fun setUpProgressSlider() {
        progressSlider?.addOnChangeListener(Slider.OnChangeListener { _, value, fromUser ->
            onProgressChange(value.toInt(), fromUser)
        })
        progressSlider?.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                onStartTrackingTouch()
            }

            override fun onStopTrackingTouch(slider: Slider) {
                onStopTrackingTouch(slider.value.toInt())
            }
        })

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                onProgressChange(progress, fromUser)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                onStartTrackingTouch()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                onStopTrackingTouch(seekBar?.progress ?: 0)
            }
        })
    }

    private fun onProgressChange(value: Int, fromUser: Boolean) {
        if (fromUser) {
            onUpdateProgressViews(value, MusicPlayerRemote.songDurationMillis)
        }
    }

    private fun onStartTrackingTouch() {
        isSeeking = true
        progressViewUpdateHelper.stop()
        progressAnimator?.cancel()
    }

    private fun onStopTrackingTouch(value: Int) {
        isSeeking = false
        MusicPlayerRemote.seekTo(value)
        progressViewUpdateHelper.start()
    }

    private lateinit var progressViewUpdateHelper: MusicProgressViewUpdateHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this)
        if (PreferenceUtil.circlePlayButton) {
            requireContext().theme.applyStyle(R.style.CircleFABOverlay, true)
        } else {
            requireContext().theme.applyStyle(R.style.RoundedFABOverlay, true)
        }
    }

    fun View.showBounceAnimation() {
        clearAnimation()
        scaleX = 0.9f
        scaleY = 0.9f
        isVisible = true
        pivotX = (width / 2).toFloat()
        pivotY = (height / 2).toFloat()

        animate().setDuration(200)
            .setInterpolator(DecelerateInterpolator())
            .scaleX(1.1f)
            .scaleY(1.1f)
            .withEndAction {
                animate().setDuration(200)
                    .setInterpolator(AccelerateInterpolator())
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .start()
            }
            .start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideVolumeIfAvailable()
    }

    override fun onStart() {
        super.onStart()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .registerOnSharedPreferenceChangeListener(this)
        setUpProgressSlider()
        setUpPrevNext()
        setUpShuffleButton()
        setUpRepeatButton()
        applyButtonSwapLogic()
    }
    
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == SWAP_SHUFFLE_REPEAT_BUTTONS) {
            applyButtonSwapLogic()
        }
    }

    private fun applyButtonSwapLogic() {
        if (PreferenceUtil.swapShuffleRepeatButtons) {
            val parent = shuffleButton.parent as? ConstraintLayout
            parent?.let {
                val constraintSet = ConstraintSet()
                constraintSet.clone(it)

                // Clear existing constraints for shuffle and repeat buttons
                constraintSet.clear(shuffleButton.id, ConstraintSet.START)
                constraintSet.clear(shuffleButton.id, ConstraintSet.END)
                constraintSet.clear(repeatButton.id, ConstraintSet.START)
                constraintSet.clear(repeatButton.id, ConstraintSet.END)

                // Swap positions: shuffleButton takes repeatButton's original position
                // and repeatButton takes shuffleButton's original position.
                // Original: repeatButton -- previousButton -- playPauseButton -- nextButton -- shuffleButton
                // New:      shuffleButton -- previousButton -- playPauseButton -- nextButton -- repeatButton

                // Shuffle button takes repeatButton's original position (leftmost)
                constraintSet.connect(shuffleButton.id, ConstraintSet.END, R.id.previousButton, ConstraintSet.START)
                constraintSet.connect(shuffleButton.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                constraintSet.setHorizontalBias(shuffleButton.id, 0.5f) // Maintain horizontal bias

                // Repeat button takes shuffleButton's original position (rightmost)
                constraintSet.connect(repeatButton.id, ConstraintSet.START, R.id.nextButton, ConstraintSet.END)
                constraintSet.connect(repeatButton.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                constraintSet.setHorizontalBias(repeatButton.id, 0.5f) // Maintain horizontal bias

                constraintSet.applyTo(parent)
                // manual app restart
            }
        }
        else {
            val parent = shuffleButton.parent as? ConstraintLayout
            parent?.let {
                val constraintSet = ConstraintSet()
                constraintSet.clone(it)

                // Clear existing constraints for shuffle and repeat buttons
                constraintSet.clear(shuffleButton.id, ConstraintSet.START)
                constraintSet.clear(shuffleButton.id, ConstraintSet.END)
                constraintSet.clear(repeatButton.id, ConstraintSet.START)
                constraintSet.clear(repeatButton.id, ConstraintSet.END)

                // Swap positions: shuffleButton takes repeatButton's original position
                // and repeatButton takes shuffleButton's original position.
                // Original: repeatButton -- previousButton -- playPauseButton -- nextButton -- shuffleButton
                // New:      shuffleButton -- previousButton -- playPauseButton -- nextButton -- repeatButton

                // Shuffle button takes repeatButton's original position (leftmost)
                constraintSet.connect(repeatButton.id, ConstraintSet.END, R.id.previousButton, ConstraintSet.START)
                constraintSet.connect(repeatButton.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                constraintSet.setHorizontalBias(repeatButton.id, 0.5f) // Maintain horizontal bias

                // Repeat button takes shuffleButton's original position (rightmost)
                constraintSet.connect(shuffleButton.id, ConstraintSet.START, R.id.nextButton, ConstraintSet.END)
                constraintSet.connect(shuffleButton.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                constraintSet.setHorizontalBias(shuffleButton.id, 0.5f) // Maintain horizontal bias

                constraintSet.applyTo(parent)
            }
        }
            
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpPrevNext() {
        nextButton?.setOnTouchListener(MusicSeekSkipTouchListener(requireActivity(), true))
        previousButton?.setOnTouchListener(MusicSeekSkipTouchListener(requireActivity(), false))
    }

    private fun setUpShuffleButton() {
        shuffleButton.setOnClickListener { MusicPlayerRemote.toggleShuffleMode() }
    }

    private fun setUpRepeatButton() {
        repeatButton.setOnClickListener { MusicPlayerRemote.cycleRepeatMode() }
    }

    fun updatePrevNextColor() {
        nextButton?.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
        previousButton?.setColorFilter(lastPlaybackControlsColor, PorterDuff.Mode.SRC_IN)
    }

    fun updateShuffleState() {
        shuffleButton.setColorFilter(
            when (MusicPlayerRemote.shuffleMode) {
                MusicService.SHUFFLE_MODE_SHUFFLE -> lastPlaybackControlsColor
                else -> lastDisabledPlaybackControlsColor
            }, PorterDuff.Mode.SRC_IN
        )
    }

    fun updateRepeatState() {
        when (MusicPlayerRemote.repeatMode) {
            MusicService.REPEAT_MODE_NONE -> {
                repeatButton.setImageResource(R.drawable.ic_repeat)
                repeatButton.setColorFilter(
                    lastDisabledPlaybackControlsColor,
                    PorterDuff.Mode.SRC_IN
                )
            }
            MusicService.REPEAT_MODE_ALL -> {
                repeatButton.setImageResource(R.drawable.ic_repeat)
                repeatButton.setColorFilter(
                    lastPlaybackControlsColor,
                    PorterDuff.Mode.SRC_IN
                )
            }
            MusicService.REPEAT_MODE_THIS -> {
                repeatButton.setImageResource(R.drawable.ic_repeat_one)
                repeatButton.setColorFilter(
                    lastPlaybackControlsColor,
                    PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    protected var volumeFragment: VolumeFragment? = null

    private fun hideVolumeIfAvailable() {
        if (PreferenceUtil.isVolumeVisibilityMode) {
            childFragmentManager.commit {
                replace<VolumeFragment>(R.id.volumeFragmentContainer)
            }
            childFragmentManager.executePendingTransactions()
        }
        volumeFragment = whichFragment(R.id.volumeFragmentContainer)
    }

    override fun onResume() {
        super.onResume()
        progressViewUpdateHelper.start()
    }

    override fun onPause() {
        super.onPause()
        progressViewUpdateHelper.stop()
    }

    override fun onStop() {
        super.onStop()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    companion object {
        const val SLIDER_ANIMATION_TIME: Long = 400
    }
}
