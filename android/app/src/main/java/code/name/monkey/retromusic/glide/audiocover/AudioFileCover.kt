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
package code.name.monkey.retromusic.glide.audiocover

/** @author Karim Abou Zeid (kabouzeid)
 */
class AudioFileCover(
    val filePath: String,
    val songTitle: String,
    val artistName: String
) {
    override fun hashCode(): Int {
        var result = filePath.hashCode()
        result = 31 * result + songTitle.hashCode()
        result = 31 * result + artistName.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AudioFileCover

        if (filePath != other.filePath) return false
        if (songTitle != other.songTitle) return false
        if (artistName != other.artistName) return false

        return true
    }
}
