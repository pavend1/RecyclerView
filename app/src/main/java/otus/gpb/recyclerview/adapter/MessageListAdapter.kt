package otus.gpb.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import otus.gpb.recyclerview.R
import otus.gpb.recyclerview.model.Chat
import java.util.Objects

class MessageListAdapter(
    private val listItems: MutableList<Chat>,
) : RecyclerView.Adapter<MessageListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.avatarImageView)
        val chatName: TextView = itemView.findViewById(R.id.chatNameTextView)
        val verified: ImageView = itemView.findViewById(R.id.verified)
        val mute: ImageView = itemView.findViewById(R.id.mute)
        val messageAuthor: TextView = itemView.findViewById(R.id.messageAuthor)
        val messageImage: ImageView = itemView.findViewById(R.id.messageImage)
        val message: TextView = itemView.findViewById(R.id.message)
        val messageDate: TextView = itemView.findViewById(R.id.message_date)
        val delivered: ImageView = itemView.findViewById(R.id.delivered)
        val newMessagesCount: TextView = itemView.findViewById(R.id.newMessagesCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_item, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]

        holder.apply {
            avatar.setImageResource(item.avatar)
            chatName.setText(item.chatName)
            message.setText(item.message)
            messageDate.setText(item.messageDate)

            if (Objects.isNull(item.messageImage)) {
                messageImage.visibility = View.GONE
            } else {
                messageImage.setImageResource(item.messageImage!!)
            }

            if (!item.verified) {
                verified.visibility = View.GONE
            }

            if (!item.mute) {
                mute.visibility = View.GONE
            }

            if (Objects.isNull(item.messageAuthor)) {
                messageAuthor.visibility = View.GONE
            } else {
                messageAuthor.setText(item.messageAuthor!!)
            }

            if (Objects.isNull(item.deliveredStatus)) {
                delivered.visibility = View.GONE
            } else {
                delivered.setImageResource(item.deliveredStatus!!)
            }

            if (item.newMessagesCount > 0) {
                newMessagesCount.text = item.newMessagesCount.toString()
            } else {
                newMessagesCount.visibility = View.GONE
            }
        }
    }

    override fun getItemCount() = listItems.size

    fun removeItem(position: Int) {
        listItems.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addMessages(newMessages: Collection<Chat>) {
        listItems.addAll(newMessages)
        notifyItemRangeInserted(listItems.size, newMessages.size)
    }
}
