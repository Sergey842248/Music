package code.name.monkey.retromusic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.databinding.ItemFolderTreeBinding
import java.io.File

class TreeViewAdapter(
    private val listener: TreeViewAdapter.OnFolderClickListener
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
                listener.onFolderClick(item.file)
            }
            // Add indentation based on depth
            val paddingLeft = item.depth * 30 // 30 pixels per level of depth
            binding.root.setPadding(paddingLeft, 0, 0, 0)
        }
    }

    interface OnFolderClickListener {
        fun onFolderClick(file: File)
    }

    data class TreeItem(val file: File, val depth: Int)
}
