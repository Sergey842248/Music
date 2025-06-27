package code.name.monkey.retromusic.adapter.base

import androidx.recyclerview.widget.RecyclerView

interface AbsCustomAdapter<E, VH : RecyclerView.ViewHolder> {
    abstract var dataSet: List<E>

    abstract fun swapDataSet(dataSet: List<E>)
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    fun notifyItemMoved(fromPosition: Int, toPosition: Int)
    fun notifyDataSetChanged()
}
