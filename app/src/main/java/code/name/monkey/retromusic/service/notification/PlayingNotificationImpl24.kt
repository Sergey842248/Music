/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package code.name.monkey.retromusic.service.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import code.name.monkey.appthemehelper.util.VersionUtils
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.MainActivity
import code.name.monkey.retromusic.glide.RetroGlideExtension
import code.name.monkey.retromusic.glide.RetroGlideExtension.songCoverOptions
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.service.MusicService.Companion.ACTION_QUIT
import code.name.monkey.retromusic.service.MusicService.Companion.ACTION_REWIND
import code.name.monkey.retromusic.service.MusicService.Companion.ACTION_SKIP
import code.name.monkey.retromusic.service.MusicService.Companion.ACTION_TOGGLE_PAUSE
import code.name.monkey.retromusic.service.MusicService.Companion.TOGGLE_FAVORITE
import code.name.monkey.retromusic.util.PreferenceUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

@SuppressLint("RestrictedApi")
class PlayingNotificationImpl24(
    val context: MusicService,
    mediaSessionToken: MediaSessionCompat.Token,
) : PlayingNotification(context) {

    private var currentTarget: CustomTarget<Bitmap>? = null

    init {
        val action = Intent(context, MainActivity::class.java)
        action.putExtra(MainActivity.EXPAND_PANEL, PreferenceUtil.isExpandPanel)
        action.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val clickIntent =
            PendingIntent.getActivity(
                context,
                0,
                action,
                PendingIntent.FLAG_UPDATE_CURRENT or if (VersionUtils.hasMarshmallow())
                    PendingIntent.FLAG_IMMUTABLE
                else 0
            )

        val serviceName = ComponentName(context, MusicService::class.java)
        val intent = Intent(ACTION_QUIT)
        intent.component = serviceName
        val deleteIntent = PendingIntent.getService(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or (if (VersionUtils.hasMarshmallow())
                PendingIntent.FLAG_IMMUTABLE
            else 0)
        )
        val toggleFavorite = buildFavoriteAction(false)
        val playPauseAction = buildPlayAction(true)
        val previousAction = NotificationCompat.Action(
            R.drawable.ic_skip_previous,
            context.getString(R.string.action_previous),
            retrievePlaybackAction(ACTION_REWIND)
        )
        val nextAction = NotificationCompat.Action(
            R.drawable.ic_skip_next,
            context.getString(R.string.action_next),
            retrievePlaybackAction(ACTION_SKIP)
        )
        val dismissAction = NotificationCompat.Action(
            R.drawable.ic_close,
            context.getString(R.string.action_cancel),
            retrievePlaybackAction(ACTION_QUIT)
        )
        setSmallIcon(R.drawable.ic_notification)
        setContentIntent(clickIntent)
        setDeleteIntent(deleteIntent)
        setShowWhen(false)
        addAction(previousAction)
        addAction(playPauseAction)
        addAction(nextAction)
        addAction(dismissAction)

        setStyle(
            MediaStyle()
                .setMediaSession(mediaSessionToken)
                .setShowActionsInCompactView(1, 2, 3)
        )
        setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    }

    override fun updateMetadata(song: Song, onUpdate: () -> Unit) {
        if (song == Song.emptySong) return
        setContentTitle(song.title)
        setContentText(song.artistName)
        setSubText(song.albumName)

        // Displays a temporary image while Glide loads the actual album image.
        // This allows us to update the notification immediately
        // without waiting for Glide.
        setAlbumArtImage(null)
        onUpdate()

        val bigNotificationImageSize = context.resources
            .getDimensionPixelSize(R.dimen.notification_big_image_size)
        currentTarget?.let { Glide.with(context).clear(it) }
        currentTarget = Glide.with(context)
            .asBitmap()
            .songCoverOptions(song)
            .load(RetroGlideExtension.getSongModel(song))
            //.checkIgnoreMediaStore()
            .centerCrop()
            .into(object : CustomTarget<Bitmap>(
                bigNotificationImageSize,
                bigNotificationImageSize
            ) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    setLargeIcon(resource)
                    onUpdate()
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    setAlbumArtImage(null)
                    onUpdate()
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    setAlbumArtImage(null)
                    onUpdate()
                }
            })
    }

    private fun setAlbumArtImage(image: Bitmap?) {
        if (image == null) {
            setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.default_audio_art))
        } else {
            setLargeIcon(image)
        }
    }

    private fun buildPlayAction(isPlaying: Boolean): NotificationCompat.Action {
        val playButtonResId =
            if (isPlaying) R.drawable.ic_pause_white_48dp else R.drawable.ic_play_arrow_white_48dp
        return NotificationCompat.Action.Builder(
            playButtonResId,
            context.getString(R.string.action_play_pause),
            retrievePlaybackAction(ACTION_TOGGLE_PAUSE)
        ).build()
    }

    private fun buildFavoriteAction(isFavorite: Boolean): NotificationCompat.Action {
        val favoriteResId =
            if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        return NotificationCompat.Action.Builder(
            favoriteResId,
            context.getString(R.string.action_toggle_favorite),
            retrievePlaybackAction(TOGGLE_FAVORITE)
        ).build()
    }

    override fun setPlaying(isPlaying: Boolean) {
        mActions[2] = buildPlayAction(isPlaying)
    }

    override fun updateFavorite(isFavorite: Boolean) {
        mActions[0] = buildFavoriteAction(isFavorite)
    }

    override fun clear(context: Context) {
        Glide.with(context.applicationContext).clear(currentTarget)
    }

    private fun retrievePlaybackAction(action: String): PendingIntent {
        val serviceName = ComponentName(context, MusicService::class.java)
        val intent = Intent(action)
        intent.component = serviceName
        return PendingIntent.getService(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or
                    if (VersionUtils.hasMarshmallow()) PendingIntent.FLAG_IMMUTABLE
                    else 0
        )
    }

    companion object {

        fun from(
            context: MusicService,
            notificationManager: NotificationManager,
            mediaSession: MediaSessionCompat,
        ): PlayingNotification {
            if (VersionUtils.hasOreo()) {
                createNotificationChannel(context, notificationManager)
            }
            return PlayingNotificationImpl24(context, mediaSession.sessionToken)
        }
    }
}
