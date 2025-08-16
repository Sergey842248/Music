package code.name.monkey.retromusic.fragments.base

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.base.AbsCustomAdapter
import code.name.monkey.retromusic.fragments.ReloadType
import code.name.monkey.retromusic.util.RetroUtil

abstract class AbsCustomFragment<A, LM : RecyclerView.LayoutManager> :
    AbsRecyclerViewFragment<A, LM>() where A : RecyclerView.Adapter<*>, A : AbsCustomAdapter<*, *> {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override val titleRes: Int
        get() = R.string.sort_order_custom

    override val emptyMessage: Int
        get() = R.string.nothing_to_see

    override val isShuffleVisible: Boolean
        get() = false

    open fun onDataSetChanged() {
        RetroUtil.runOnMainThread {
            (adapter as? RecyclerView.Adapter<*>)?.notifyDataSetChanged()
        }
    }

    open fun setSortOrder(sortOrder: String) {
        libraryViewModel.forceReload(ReloadType.Songs)
    }

    abstract fun loadSortOrder(): String

    abstract fun saveSortOrder(sortOrder: String)

    abstract fun loadGridSize(): Int

    abstract fun saveGridSize(gridColumns: Int)

    abstract fun loadGridSizeLand(): Int

    abstract fun saveGridSizeLand(gridColumns: Int)

    abstract fun loadLayoutRes(): Int

    abstract fun saveLayoutRes(layoutRes: Int)

    abstract fun setCustomOrder(songs: List<Long>)

    abstract fun getCustomOrder(): List<Long>

    protected val maxGridSize: Int
        get() = if (RetroUtil.isLandscape) {
            resources.getInteger(R.integer.max_columns_land)
        } else {
            resources.getInteger(R.integer.max_columns)
        }

    open fun setGridSize(gridSize: Int) {
        if (layoutManager is GridLayoutManager) {
            (layoutManager as GridLayoutManager).spanCount = gridSize
        }
        (adapter as? RecyclerView.Adapter<*>)?.notifyDataSetChanged()
    }

    open fun itemLayoutRes(): Int {
        return loadLayoutRes()
    }

    open fun getGridSize(): Int {
        return if (RetroUtil.isLandscape) {
            loadGridSizeLand()
        } else {
            loadGridSize()
        }
    }

    open fun getSortOrder(): String {
        return loadSortOrder()
    }

    open fun setAndSaveSortOrder(sortOrder: String) {
        saveSortOrder(sortOrder)
        setSortOrder(sortOrder)
    }

    open fun setAndSaveLayoutRes(layoutRes: Int) {
        saveLayoutRes(layoutRes)
        libraryViewModel.forceReload(ReloadType.Songs)
    }

    open fun setAndSaveGridSize(gridSize: Int) {
        if (RetroUtil.isLandscape) {
            saveGridSizeLand(gridSize)
        } else {
            saveGridSize(gridSize)
        }
        setGridSize(gridSize)
    }
}
