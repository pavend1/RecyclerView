package otus.gpb.recyclerview.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Chat(

    @DrawableRes val avatar: Int,
    @StringRes val chatName: Int,
    @StringRes val messageAuthor: Int?,
    @DrawableRes val messageImage: Int?,
    @StringRes val message: Int,
    @StringRes val messageDate: Int,
    val verified: Boolean,
    val mute: Boolean,
    val deliveredStatus: Int?,
    val official: Boolean,
    val newMessagesCount: Int,
)
