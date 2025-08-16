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
package code.name.monkey.retromusic.fragments

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import code.name.monkey.retromusic.R

enum class AlbumCoverStyle(
    @StringRes val titleRes: Int,
    @DrawableRes val drawableResId: Int,
    val id: Int
) {
    Card(R.string.card, R.drawable.ic_album, 3),
    Circle(R.string.circular, R.drawable.ic_album, 2),
    Flat(R.string.flat, R.drawable.ic_album, 1),
    FullCard(R.string.full_card, R.drawable.ic_album, 5),
    Full(R.string.full, R.drawable.ic_album, 4),
    Normal(R.string.normal, R.drawable.ic_album, 0),
}
