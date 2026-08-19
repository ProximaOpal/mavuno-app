package com.mavuno.church.data

import androidx.annotation.DrawableRes

data class Sermon(
    val id: String,
    val title: String,
    val speaker: String,
    val duration: String,
    val category: String,
    @DrawableRes val imageRes: Int,
    val scripture: String = "Matthew 5:9",
    val description: String = "In this message, Pastor James explores how living with intentional purpose transforms our daily work, relationships, and faith journey."
)

data class ChurchEvent(
    val id: String,
    val title: String,
    val meta: String,
    val eyebrow: String,
    @DrawableRes val imageRes: Int,
    val date: String,
    val location: String,
    val description: String
)

data class GivingOption(
    val id: String,
    val title: String,
    val meta: String,
    val eyebrow: String,
    val paybill: String = "508000",
    val accountNumber: String,
    val description: String
)

data class ChatMessage(
    val id: String,
    val text: String,
    val sender: MessageSender,
    val time: String
)

enum class MessageSender {
    USER,
    BOT
}

enum class ContactType {
    CALL,
    PRAYER,
    LOCATION
}

data class ContactChannel(
    val id: String,
    val title: String,
    val detail: String,
    val meta: String,
    val type: ContactType
)

data class DailyWord(
    val verse: String,
    val scripture: String,
    val reflection: String
)
