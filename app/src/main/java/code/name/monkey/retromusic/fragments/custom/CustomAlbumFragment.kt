package code.name.monkey.retromusic.fragments.custom

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.album.AlbumAdapter
import code.name.monkey.retromusic.fragments.GridStyle
import code.name.monkey.retromusic.fragments.ReloadType
import code.name.monkey.retromusic.fragments.base.AbsCustomFragment
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import code.name.monkey.retromusic.adapter.base.AbsCustomAdapter

class CustomAlbumFragment : AbsCustomFragment<AlbumAdapter, LinearLayoutManager>() {

    override val isShuffleVisible: Boolean
        get() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        libraryViewModel.getAlbums().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter?.swapDataSet(it)
            } else {
                adapter?.swapDataSet(listOf())
            }
        }
    }

    override fun createAdapter(): AlbumAdapter {
        val dataSet = if (adapter == null) ArrayList() else adapter!!.dataSet
        return AlbumAdapter(
            requireActivity(),
            dataSet,
            R.layout.item_list,
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
            val albums = adapter?.dataSet?.map { it.id }
            PreferenceUtil.albumCustomOrder = albums ?: emptyList()
        }
    }

    override fun setSortOrder(sortOrder: String) {
        libraryViewModel.forceReload(ReloadType.Albums)
    }

    override fun loadSortOrder(): String {
        return PreferenceUtil.albumSortOrder
    }

    override fun saveSortOrder(sortOrder: String) {
        PreferenceUtil.albumSortOrder = sortOrder
    }

    override fun loadGridSize(): Int {
        return PreferenceUtil.albumGridSize
    }

    override fun saveGridSize(gridColumns: Int) {
        PreferenceUtil.albumGridSize = gridColumns
    }

    override fun loadGridSizeLand(): Int {
        return PreferenceUtil.albumGridSizeLand
    }

    override fun saveGridSizeLand(gridColumns: Int) {
        PreferenceUtil.albumGridSizeLand = gridColumns
    }

    override fun loadLayoutRes(): Int {
        return PreferenceUtil.albumGridStyle.layoutResId
    }

    override fun saveLayoutRes(layoutRes: Int) {
        PreferenceUtil.albumGridStyle = GridStyle.values().first { gridStyle ->
            gridStyle.layoutResId == layoutRes
        }
    }

    override fun setCustomOrder(songs: List<Long>) {
        PreferenceUtil.albumCustomOrder = songs
    }

    override fun getCustomOrder(): List<Long> {
        return PreferenceUtil.albumCustomOrder
    }

    override fun onResume() {
        super.onResume()
        libraryViewModel.forceReload(ReloadType.Albums)
    }
}
