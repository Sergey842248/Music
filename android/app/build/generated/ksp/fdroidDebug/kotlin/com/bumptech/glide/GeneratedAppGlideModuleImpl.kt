package com.bumptech.glide

import android.content.Context
import code.name.monkey.retromusic.glide.RetroMusicGlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpLibraryGlideModule
import kotlin.Boolean
import kotlin.Suppress

internal class GeneratedAppGlideModuleImpl(
  @Suppress("UNUSED_PARAMETER")
  context: Context,
) : GeneratedAppGlideModule() {
  private val appGlideModule: RetroMusicGlideModule
  init {
    appGlideModule = RetroMusicGlideModule()
  }

  public override fun registerComponents(
    context: Context,
    glide: Glide,
    registry: Registry,
  ) {
    OkHttpLibraryGlideModule().registerComponents(context, glide, registry)
    appGlideModule.registerComponents(context, glide, registry)
  }

  public override fun applyOptions(context: Context, builder: GlideBuilder) {
    appGlideModule.applyOptions(context, builder)
  }

  public override fun isManifestParsingEnabled(): Boolean = false
}
