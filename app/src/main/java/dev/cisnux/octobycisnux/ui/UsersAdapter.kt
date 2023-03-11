package dev.cisnux.octobycisnux.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import dev.cisnux.octobycisnux.databinding.ListTileItemBinding
import dev.cisnux.octobycisnux.domain.User
import dev.cisnux.octobycisnux.utils.OnItemClickListener

class UsersAdapter(inline val onItemClickListener: OnItemClickListener) :
    ListAdapter<User, UsersAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: ListTileItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User): Unit = with(binding) {
            username.text = user.username
            userPict.load(user.profilePict) {
                crossfade(true)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ListTileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        holder.itemView.setOnClickListener {
             onItemClickListener(user.username)
        }
    }
}
