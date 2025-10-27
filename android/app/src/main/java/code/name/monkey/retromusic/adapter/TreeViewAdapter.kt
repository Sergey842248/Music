package code.name.monkey.retromusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.databinding.ItemFolderTreeBinding
import java.io.File

class TreeViewAdapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<TreeViewAdapter.ViewHolder>() {

    private val items = mutableListOf<TreeItem>()

    fun submitList(newItems: List<TreeItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFolderTreeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemFolderTreeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TreeItem) {
            binding.folderName.text = item.file.name
            if (item.isDirectory) {
                binding.root.setOnClickListener {
                    listener.onExpansionToggle(item.file)
                }
            } else {
                binding.root.setOnClickListener {
                    listener.onItemClick(item.file)
                }
            }
            binding.expandCollapseIcon.setOnClickListener {
                if (item.isDirectory) {
                    listener.onExpansionToggle(item.file)
                }
            }
            binding.expandCollapseIcon.isVisible = item.hasChildren
            binding.expandCollapseIcon.rotation = if (item.isExpanded) 90f else 0f

            val density = binding.root.context.resources.displayMetrics.density
            val indentation = (item.depth * 24 * density).toInt() // 24dp per depth level

            val expandCollapseIconParams = binding.expandCollapseIcon.layoutParams as ViewGroup.MarginLayoutParams
            val folderIconParams = binding.folderIcon.layoutParams as ViewGroup.MarginLayoutParams

            val chevronWidth = (24 * density).toInt() // Width of the chevron icon in pixels
            val gapBetweenChevronAndFolder = (8 * density).toInt() // Gap between chevron and folder icon in pixels

            if (item.hasChildren) {
                binding.expandCollapseIcon.isVisible = true
                expandCollapseIconParams.marginStart = indentation
                folderIconParams.marginStart = gapBetweenChevronAndFolder
            } else {
                binding.expandCollapseIcon.isVisible = false
                // If no children, the folder icon needs to take the place of the chevron + its gap
                expandCollapseIconParams.marginStart = 0 // Not visible, so margin doesn't matter for it
                folderIconParams.marginStart = indentation + chevronWidth + gapBetweenChevronAndFolder
            }

            binding.expandCollapseIcon.layoutParams = expandCollapseIconParams
            binding.folderIcon.layoutParams = folderIconParams
        }
    }

    interface OnItemClickListener {
        fun onItemClick(file: File)
        fun onExpansionToggle(file: File)
    }

    data class TreeItem(val file: File, val depth: Int, val hasChildren: Boolean = false, val isExpanded: Boolean = false, val isDirectory: Boolean = true)
}
