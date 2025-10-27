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
package code.name.monkey.retromusic.adapter

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.base.AbsMultiSelectAdapter
import code.name.monkey.retromusic.adapter.base.MediaEntryViewHolder
import code.name.monkey.retromusic.extensions.getTintedDrawable
import code.name.monkey.retromusic.glide.RetroGlideExtension
import code.name.monkey.retromusic.glide.audiocover.AudioFileCover
import code.name.monkey.retromusic.interfaces.ICallbacks
import code.name.monkey.retromusic.util.MusicUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.MediaStoreSignature
import me.zhanghai.android.fastscroll.PopupTextProvider
import code.name.monkey.retromusic.model.Song
import java.io.File
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

class SongFileAdapter(
    override val activity: AppCompatActivity,
    private var dataSet: List<Song>,
    private val itemLayoutRes: Int,
    private val iCallbacks: ICallbacks?
) : AbsMultiSelectAdapter<SongFileAdapter.ViewHolder, Song>(
    activity, R.menu.menu_media_selection
), PopupTextProvider {

    init {
        this.setHasStableIds(true)
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataSet[position].albumName == "Folder") {
            FOLDER
        } else {
            FILE
        }
    }

    override fun getItemId(position: Int): Long {
        return dataSet[position].id
    }

    fun swapDataSet(songs: List<Song>) {
        this.dataSet = songs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, index: Int) {
        val song = dataSet[index]
        holder.itemView.isActivated = isChecked(song)
        holder.title?.text = song.title
        if (getItemViewType(index) == FOLDER) {
            holder.text?.isVisible = false
            holder.image?.setImageResource(R.drawable.ic_folder)
            holder.image?.setColorFilter(
                ATHUtil.resolveColor(
                    activity,
                    androidx.appcompat.R.attr.colorControlNormal
                ), PorterDuff.Mode.SRC_IN
            )
        } else {
            holder.text?.isVisible = true
            holder.text?.text = song.artistName
            if (holder.image != null) {
                loadFileImage(song, holder)
            }
        }
    }

    private fun loadFileImage(song: Song, holder: ViewHolder) {
        val iconColor = ATHUtil.resolveColor(activity, androidx.appcompat.R.attr.colorControlNormal)
        val error = activity.getTintedDrawable(R.drawable.ic_audio_file, iconColor)
        Glide.with(activity)
            .load(AudioFileCover(song.data, song.title, song.artistName))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(error)
            .placeholder(error)
            .transition(RetroGlideExtension.getDefaultTransition())
            .signature(MediaStoreSignature("", song.dateModified, 0))
            .into(holder.image!!)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getIdentifier(position: Int): Song {
        return dataSet[position]
    }

    override fun getName(model: Song): String {
        return model.title
    }

    override fun onMultipleItemAction(menuItem: MenuItem, selection: List<Song>) {
        if (iCallbacks == null) return
        // This is a bit of a hack, but it's the easiest way to make this work
        // without changing the ICallbacks interface.
        val files = selection.map { File(it.data) }
        iCallbacks.onMultipleItemAction(menuItem, files as ArrayList<File>)
    }

    override fun getPopupText(position: Int): String {
        return if (position >= dataSet.lastIndex) "" else getSectionName(position)
    }

    private fun getSectionName(position: Int): String {
        return MusicUtil.getSectionName(dataSet[position].title)
    }

    inner class ViewHolder(itemView: View) : MediaEntryViewHolder(itemView) {

        init {
            if (menu != null && iCallbacks != null) {
                menu?.setOnClickListener { v ->
                    val position = layoutPosition
                    if (isPositionInRange(position)) {
                        iCallbacks.onFileMenuClicked(File(dataSet[position].data), v)
                    }
                }
            }
            if (imageTextContainer != null) {
                imageTextContainer?.cardElevation = 0f
            }
        }

        override fun onClick(v: View?) {
            val position = layoutPosition
            if (isPositionInRange(position)) {
                if (isInQuickSelectMode) {
                    toggleChecked(position)
                } else {
                    iCallbacks?.onFileSelected(File(dataSet[position].data))
                }
            }
        }

        override fun onLongClick(v: View?): Boolean {
            val position = layoutPosition
            return isPositionInRange(position) && toggleChecked(position)
        }

        private fun isPositionInRange(position: Int): Boolean {
            return position >= 0 && position < dataSet.size
        }
    }

    companion object {

        private const val FILE = 0
        private const val FOLDER = 1

        fun readableFileSize(size: Long): String {
            if (size <= 0) return "$size B"
            val units = arrayOf("B", "KB", "MB", "GB", "TB")
            val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
            return DecimalFormat("#,##0.##").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
        }
    }
}
