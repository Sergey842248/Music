package com.future.music.tile

import android.content.Context
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ColorBuilders.argb
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.Box
import androidx.wear.protolayout.LayoutElementBuilders.Column
import androidx.wear.protolayout.LayoutElementBuilders.Row
import androidx.wear.protolayout.Modifiers
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.material.Button
import androidx.wear.protolayout.material.ButtonDefaults
import androidx.wear.protolayout.material.Colors
import androidx.wear.protolayout.material.Text
import androidx.wear.protolayout.material.Typography
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.tooling.preview.Preview
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tooling.preview.devices.WearDevices
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.SuspendingTileService
import com.future.music.R
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable

private const val RESOURCES_VERSION = "0"
private const val PLAY_PAUSE_ACTION = "play_pause_action"
private const val NEXT_ACTION = "next_action"
private const val PREVIOUS_ACTION = "previous_action"

/**
 * Skeleton for a tile with no images.
 */
@OptIn(ExperimentalHorologistApi::class)
class MainTileService : SuspendingTileService(), MessageClient.OnMessageReceivedListener {

    private var isPlaying = false
    private var songTitle = "No song playing"

    override fun onCreate() {
        super.onCreate()
        Wearable.getMessageClient(this).addListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Wearable.getMessageClient(this).removeListener(this)
    }

    override suspend fun resourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ) = resources(requestParams)

    override suspend fun tileRequest(
        requestParams: RequestBuilders.TileRequest
    ) = tile(requestParams, this, isPlaying, songTitle)

    override fun onMessageReceived(messageEvent: MessageEvent) {
        when (messageEvent.path) {
            "/is_playing" -> {
                isPlaying = messageEvent.data.toString(Charsets.UTF_8).toBoolean()
                updateTile()
            }
            "/song_title" -> {
                songTitle = messageEvent.data.toString(Charsets.UTF_8)
                updateTile()
            }
        }
    }

    private fun updateTile() {
        getUpdater(this).requestUpdate(MainTileService::class.java)
    }
}

private fun resources(
    requestParams: RequestBuilders.ResourcesRequest
): ResourceBuilders.Resources {
    return ResourceBuilders.Resources.Builder()
        .setVersion(RESOURCES_VERSION)
        .addIdToImageMapping("play",
            ResourceBuilders.ImageResource.Builder()
                .setAndroidResourceByResId(
                    ResourceBuilders.AndroidImageResourceByResId.Builder()
                        .setResourceId(R.drawable.ic_play)
                        .build()
                ).build()
        )
        .addIdToImageMapping("pause",
            ResourceBuilders.ImageResource.Builder()
                .setAndroidResourceByResId(
                    ResourceBuilders.AndroidImageResourceByResId.Builder()
                        .setResourceId(R.drawable.ic_pause)
                        .build()
                ).build()
        )
        .addIdToImageMapping("next",
            ResourceBuilders.ImageResource.Builder()
                .setAndroidResourceByResId(
                    ResourceBuilders.AndroidImageResourceByResId.Builder()
                        .setResourceId(R.drawable.ic_next)
                        .build()
                ).build()
        )
        .addIdToImageMapping("previous",
            ResourceBuilders.ImageResource.Builder()
                .setAndroidResourceByResId(
                    ResourceBuilders.AndroidImageResourceByResId.Builder()
                        .setResourceId(R.drawable.ic_previous)
                        .build()
                ).build()
        )
        .build()
}

private fun tile(
    requestParams: RequestBuilders.TileRequest,
    context: Context,
    isPlaying: Boolean,
    songTitle: String
): TileBuilders.Tile {
    val singleTileTimeline = TimelineBuilders.Timeline.Builder()
        .addTimelineEntry(
            TimelineBuilders.TimelineEntry.Builder()
                .setLayout(
                    LayoutElementBuilders.Layout.Builder()
                        .setRoot(tileLayout(requestParams, context, isPlaying, songTitle))
                        .build()
                )
                .build()
        )
        .build()

    return TileBuilders.Tile.Builder()
        .setResourcesVersion(RESOURCES_VERSION)
        .setTileTimeline(singleTileTimeline)
        .build()
}

private fun tileLayout(
    requestParams: RequestBuilders.TileRequest,
    context: Context,
    isPlaying: Boolean,
    songTitle: String
): LayoutElementBuilders.LayoutElement {
    return PrimaryLayout.Builder(requestParams.deviceConfiguration)
        .setResponsiveContentInsetEnabled(true)
        .setContent(
            Column.Builder()
                .addContent(
                    Text.Builder(context, songTitle)
                        .setColor(argb(Colors.DEFAULT.onSurface))
                        .setTypography(Typography.TYPOGRAPHY_CAPTION1)
                        .setModifiers(
                            Modifiers.Builder()
                                .setMarquee(
                                    LayoutElementBuilders.Marquee.Builder()
                                        .setIterationCount(0)
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .addContent(
                    Row.Builder()
                        .addContent(
                            Button.Builder(context, "previous")
                                .setIcon("previous")
                                .setButtonColors(ButtonDefaults.buttonColors(Colors.DEFAULT))
                                .setClickable(
                                    ActionBuilders.Clickable.Builder()
                                        .setId(PREVIOUS_ACTION)
                                        .build()
                                )
                                .build()
                        )
                        .addContent(
                            Button.Builder(context, if (isPlaying) "pause" else "play")
                                .setIcon(if (isPlaying) "pause" else "play")
                                .setButtonColors(ButtonDefaults.buttonColors(Colors.DEFAULT))
                                .setClickable(
                                    ActionBuilders.Clickable.Builder()
                                        .setId(PLAY_PAUSE_ACTION)
                                        .build()
                                )
                                .build()
                        )
                        .addContent(
                            Button.Builder(context, "next")
                                .setIcon("next")
                                .setButtonColors(ButtonDefaults.buttonColors(Colors.DEFAULT))
                                .setClickable(
                                    ActionBuilders.Clickable.Builder()
                                        .setId(NEXT_ACTION)
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .build()
        ).build()
}

@Preview(device = WearDevices.SMALL_ROUND)
@Preview(device = WearDevices.LARGE_ROUND)
fun tilePreview(context: Context) = TilePreviewData(::resources) {
    tile(it, context, false, "Song Title")
}
