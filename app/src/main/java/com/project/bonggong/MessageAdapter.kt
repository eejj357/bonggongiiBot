package com.project.bonggong

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.bonggong.model.Message

class MessageAdapter(private val messages: List<Message>) :  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // ViewHolder for chatbot messages
    class ChatbotMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
    }

    // ViewHolder for user messages
    class UserMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) {
            VIEW_TYPE_USER
        } else {
            VIEW_TYPE_CHATBOT
        }
    }

    // 새로운 아이템이 화면에 나타날 때 호출
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user_message, parent, false)
            UserMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chatbot_message, parent, false)
            ChatbotMessageViewHolder(view)
        }
    }

    // 데이터 바인딩
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]

        if (holder is ChatbotMessageViewHolder) {
            holder.messageTextView.text = message.text
            // Glide로 프로필 이미지 로드
            Glide.with(holder.itemView.context)
                .load(message.profileImageRes)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.profileImageView)
        } else if (holder is UserMessageViewHolder) {
            holder.messageTextView.text = message.text
        }
    }

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_CHATBOT = 2
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}