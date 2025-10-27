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
            binding.root.setOnClickListener {
                listener.onItemClick(item.file)
            }
            binding.expandCollapseIcon.setOnClickListener {
                if (item.isDirectory) {
                    listener.onExpansionToggle(item.file)
                }
            }
            binding.expandCollapseIcon.isVisible = item.hasChildren
            binding.expandCollapseIcon.rotation = if (item.isExpanded) 90f else 0f
            // Add indentation based on depth, but since arrow is at start, indent from after arrow
            val paddingLeft = (item.depth * 24) + 32 // Roughly for arrow
            binding.root.setPadding(paddingLeft, 0, 0, 0)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(file: File)
        fun onExpansionToggle(file: File)
    }

    data class TreeItem(val file: File, val depth: Int, val hasChildren: Boolean = false, val isExpanded: Boolean = false, val isDirectory: Boolean = true)
}
