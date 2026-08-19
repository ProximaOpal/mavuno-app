package com.mavuno.church.data

import com.mavuno.church.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object MavunoRepository {

    val sermons = listOf(
        Sermon(
            id = "1",
            title = "Rooted: Living from Purpose",
            speaker = "Pastor James",
            duration = "34 min",
            category = "Latest Series",
            imageRes = R.drawable.sermon_hero_3d,
            scripture = "Matthew 5:9",
            description = "“Blessed are the peacemakers, for they will be called children of God.”\n\nIn this message, Pastor James explores how living with intentional purpose transforms our daily work, relationships, and faith journey."
        ),
        Sermon(
            id = "2",
            title = "The Harvest Principle",
            speaker = "Pastor James",
            duration = "41 min",
            category = "Harvest Series",
            imageRes = R.drawable.church_community_3d,
            scripture = "Galatians 6:9",
            description = "“Let us not become weary in doing good, for at the proper time we will reap a harvest if we do not give up.”\n\nUnderstanding the timeless laws of sowing, cultivating patience, and trusting God for your breakthrough season."
        ),
        Sermon(
            id = "3",
            title = "Faith That Moves Mountains",
            speaker = "Pastor Grace",
            duration = "29 min",
            category = "Faith Series",
            imageRes = R.drawable.sermon_hero_3d,
            scripture = "Mark 11:23",
            description = "“Truly I tell you, if anyone says to this mountain, 'Go, throw yourself into the sea,' and does not doubt in their heart... it will be done for them.”\n\nPractical steps to build resilient faith in uncertain times."
        ),
        Sermon(
            id = "4",
            title = "Building on the Rock",
            speaker = "Pastor James",
            duration = "37 min",
            category = "Foundations",
            imageRes = R.drawable.church_community_3d,
            scripture = "Matthew 7:24",
            description = "“Everyone then who hears these words of mine and does them will be like a wise man who built his house on the rock.”\n\nHow to construct a lasting foundation for your family, career, and spiritual walk."
        )
    )

    val events = listOf(
        ChurchEvent(
            id = "1",
            title = "Sunday Service — Second Service",
            meta = "Sun · 9:00 AM & 11:30 AM · Main Auditorium",
            eyebrow = "This Week",
            imageRes = R.drawable.church_community_3d,
            date = "Sunday",
            location = "Hill City Campus, Main Auditorium",
            description = "Join us for an electrifying worship experience, powerful message, and community connection. Doors open at 8:30 AM."
        ),
        ChurchEvent(
            id = "2",
            title = "Youth & Young Adults Night",
            meta = "Fri · 6:30 PM · Youth Hall",
            eyebrow = "This Week",
            imageRes = R.drawable.sermon_hero_3d,
            date = "Friday 6:30 PM",
            location = "Youth Hall & Outdoor Terrace",
            description = "An evening of praise, deep discussions, games, and genuine fellowship for high schoolers, college students, and young professionals."
        ),
        ChurchEvent(
            id = "3",
            title = "Mid-Week Prayer & Fasting",
            meta = "Wed · 5:30 AM · Online & Prayer Room",
            eyebrow = "Ongoing",
            imageRes = R.drawable.church_community_3d,
            date = "Wednesday 5:30 AM",
            location = "Online & Main Campus Prayer Room",
            description = "Start your Wednesday aligned with God's will. Join our pastoral team in intercession for families, church, and nation."
        ),
        ChurchEvent(
            id = "4",
            title = "Marriage Encounter Retreat",
            meta = "Sep 12–13 · Naivasha Resort",
            eyebrow = "Upcoming",
            imageRes = R.drawable.sermon_hero_3d,
            date = "September 12–13",
            location = "Naivasha Resort & Conference Centre",
            description = "A weekend getaway for married couples focused on rekindling intimacy, communication, and spiritual alignment."
        )
    )

    val givingOptions = listOf(
        GivingOption(
            id = "tithe",
            title = "Tithe",
            meta = "Give your tithe via M-Pesa Paybill 508000",
            eyebrow = "Regular Tithe",
            paybill = "508000",
            accountNumber = "TITHE",
            description = "Honour God with the first fruits of your increase. Bring the full tithe into the storehouse."
        ),
        GivingOption(
            id = "offering",
            title = "Sunday Offering",
            meta = "A thanksgiving offering for the week",
            eyebrow = "One-time Gift",
            paybill = "508000",
            accountNumber = "OFFERING",
            description = "Give cheerfully as a response to God's continued goodness and daily protection."
        ),
        GivingOption(
            id = "building",
            title = "Building & Campus Fund",
            meta = "Support the new Hill City campus project",
            eyebrow = "Development",
            paybill = "508000",
            accountNumber = "BUILDING",
            description = "Investing in infrastructure, modern auditoriums, and youth development centres."
        ),
        GivingOption(
            id = "missions",
            title = "Missions & Outreach",
            meta = "Fuel community outreach in Nairobi",
            eyebrow = "Community",
            paybill = "508000",
            accountNumber = "MISSIONS",
            description = "Feeding programmes, school supplies, medical camps, and prison ministry in Kenya."
        )
    )

    val contacts = listOf(
        ContactChannel(
            id = "call",
            title = "Campus Main Desk",
            detail = "+254 700 000 000",
            meta = "Mon–Fri · 8:00 AM – 5:00 PM",
            type = ContactType.CALL
        ),
        ContactChannel(
            id = "prayer",
            title = "24/7 Prayer Line",
            detail = "prayer@mavunochurch.org",
            meta = "Confidential Pastoral Care",
            type = ContactType.PRAYER
        ),
        ContactChannel(
            id = "location",
            title = "Nairobi Campus Location",
            detail = "Hill City Campus, Bellevue, South C",
            meta = "Sunday Services: 9:00 AM & 11:30 AM",
            type = ContactType.LOCATION
        )
    )

    val dailyWord = DailyWord(
        verse = "“Blessed are the peacemakers.”",
        scripture = "Matthew 5:9 — reflect on this before Sunday service.",
        reflection = "God calls us to be agents of reconciliation in our workplaces, homes, and society."
    )

    private val _messages = MutableStateFlow(
        listOf(
            ChatMessage(
                id = "1",
                text = "Welcome to Mavuno Church! How can we pray for or support you today?",
                sender = MessageSender.BOT,
                time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    fun sendMessage(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return

        val timeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val userMsg = ChatMessage(
            id = System.currentTimeMillis().toString(),
            text = trimmed,
            sender = MessageSender.USER,
            time = timeStr
        )
        val currentList = _messages.value.toMutableList()
        currentList.add(userMsg)
        _messages.value = currentList

        // Response logic
        val lower = trimmed.lowercase()
        val replyText = when {
            lower.contains("prayer") || lower.contains("pray") ->
                "Thank you for sharing your prayer request. Our pastoral prayer team is lifting this up before God right now. 'The prayer of a righteous person is powerful and effective' (James 5:16)."
            lower.contains("service") || lower.contains("time") || lower.contains("sunday") ->
                "Our Sunday services at the Nairobi Hill City campus are at 9:00 AM and 11:30 AM! You are warmly welcome to join us in person or online."
            lower.contains("give") || lower.contains("tithe") || lower.contains("paybill") ->
                "You can give securely via M-Pesa Paybill 508000. Use Account: TITHE, OFFERING, or MISSIONS. Thank you for your generosity!"
            lower.contains("location") || lower.contains("where") || lower.contains("address") ->
                "Mavuno Church Nairobi is located at Hill City Campus, Bellevue, off Mombasa Road, South C. We'd love to see you!"
            else ->
                "Thank you for reaching out! A Mavuno leader or pastor will follow up shortly. You are always welcome in our community."
        }

        val botMsg = ChatMessage(
            id = (System.currentTimeMillis() + 1).toString(),
            text = replyText,
            sender = MessageSender.BOT,
            time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        )
        val updatedList = _messages.value.toMutableList()
        updatedList.add(botMsg)
        _messages.value = updatedList
    }
}
