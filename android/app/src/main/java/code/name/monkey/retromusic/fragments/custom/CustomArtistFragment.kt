package code.name.monkey.retromusic.fragments.custom

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.artist.ArtistAdapter
import code.name.monkey.retromusic.fragments.ReloadType
import code.name.monkey.retromusic.fragments.base.AbsCustomFragment
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import code.name.monkey.retromusic.interfaces.IArtistClickListener
import code.name.monkey.retromusic.interfaces.IAlbumArtistClickListener
import code.name.monkey.retromusic.fragments.GridStyle

class CustomArtistFragment : AbsCustomFragment<ArtistAdapter, LinearLayoutManager>(), IArtistClickListener, IAlbumArtistClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        libraryViewModel.getArtists().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter?.swapDataSet(it)
            } else {
                adapter?.swapDataSet(listOf())
            }
        }
    }

    override fun createAdapter(): ArtistAdapter {
        val dataSet = if (adapter == null) ArrayList() else adapter!!.dataSet
        return ArtistAdapter(
            requireActivity(),
            dataSet,
            R.layout.item_list,
            this as IArtistClickListener, // Explicitly cast to IArtistClickListener
            null,
            false
        )
    }

    override fun createLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(requireActivity())
    }

    override fun onDataSetChanged() {
        super.onDataSetChanged()
        RetroUtil.runOnMainThread {
            val artists = adapter?.dataSet?.map { it.id }
            PreferenceUtil.artistCustomOrder = artists ?: emptyList()
        }
    }

    override fun setSortOrder(sortOrder: String) {
        libraryViewModel.forceReload(ReloadType.Artists)
    }

    override fun loadSortOrder(): String {
        return PreferenceUtil.artistSortOrder
    }

    override fun saveSortOrder(sortOrder: String) {
        PreferenceUtil.artistSortOrder = sortOrder
    }

    override fun loadGridSize(): Int {
        return PreferenceUtil.artistGridSize
    }

    override fun saveGridSize(gridColumns: Int) {
        PreferenceUtil.artistGridSize = gridColumns
    }

    override fun loadGridSizeLand(): Int {
        return PreferenceUtil.artistGridSizeLand
    }

    override fun saveGridSizeLand(gridColumns: Int) {
        PreferenceUtil.artistGridSizeLand = gridColumns
    }

    override fun loadLayoutRes(): Int {
        return PreferenceUtil.artistGridStyle.layoutResId
    }

    override fun saveLayoutRes(layoutRes: Int) {
        PreferenceUtil.artistGridStyle = GridStyle.values().first { gridStyle ->
            gridStyle.layoutResId == layoutRes
        }
    }

    override fun setCustomOrder(songs: List<Long>) {
        PreferenceUtil.artistCustomOrder = songs
    }

    override fun getCustomOrder(): List<Long> {
        return PreferenceUtil.artistCustomOrder
    }

    override fun onResume() {
        super.onResume()
        libraryViewModel.forceReload(ReloadType.Artists)
    }

    override fun onArtist(artistId: Long, view: View) {
        // Not implemented for CustomArtistFragment, as it's for reordering
    }

    override fun onAlbumArtist(artistName: String, view: View) {
        // Not implemented for CustomArtistFragment, as it's for reordering
    }
}
