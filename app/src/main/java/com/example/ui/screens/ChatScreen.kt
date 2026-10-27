package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatMessage
import com.example.ui.components.StatusPill
import com.example.ui.theme.*
import com.example.viewmodel.IllyViewModel

@Composable
fun ChatScreen(
    viewModel: IllyViewModel,
    modifier: Modifier = Modifier
) {
    val channels by viewModel.repository.channels.collectAsState()
    val messages by viewModel.repository.messages.collectAsState()
    val activeEmployee by viewModel.repository.activeEmployee.collectAsState()

    var selectedChannelId by remember { mutableStateOf("general") }
    var inputMessage by remember { mutableStateOf("") }

    val activeChannel = channels.find { it.id == selectedChannelId } ?: channels.first()
    val channelMessages = messages.filter { it.channelId == selectedChannelId }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    ) {
        // Top Channels Bar
        Surface(
            color = IllySurfaceDark,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "ILLY Team Channels & DMs",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(channels) { ch ->
                        val isSelected = ch.id == selectedChannelId
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) IllyBlue.copy(alpha = 0.25f) else IllyCardGlass,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) IllyBlue else IllyBorder
                            ),
                            modifier = Modifier
                                .testTag("channel_${ch.id}")
                                .clickable { selectedChannelId = ch.id }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = if (ch.isDirectMessage) Icons.Default.Person else Icons.Default.Tag,
                                    contentDescription = null,
                                    tint = if (isSelected) IllyBlue else IllyTextSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = ch.name,
                                    color = if (isSelected) Color.White else IllyTextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Channel Header Description
        Surface(
            color = IllyBgDark,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = activeChannel.description,
                    color = IllyTextMuted,
                    fontSize = 11.sp,
                    maxLines = 1
                )
                StatusPill(
                    text = "Real-Time Sync",
                    textColor = IllySuccess,
                    backgroundColor = IllySuccess.copy(alpha = 0.15f),
                    icon = Icons.Default.Sync
                )
            }
        }

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            if (channelMessages.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Default.ChatBubbleOutline,
                                contentDescription = null,
                                tint = IllyTextMuted,
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = "Start of ${activeChannel.name}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Send a message to collaborate with the ILLY team.",
                                color = IllyTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            items(channelMessages, key = { it.id }) { msg ->
                val isMe = msg.senderId == activeEmployee.id || msg.isMe
                MessageBubble(msg = msg, isMe = isMe)
            }
        }

        // Message Input Field
        Surface(
            color = IllySurfaceDark,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, IllyBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { /* attachments */ }) {
                    Icon(
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = "Attach file",
                        tint = IllyTextSecondary
                    )
                }

                OutlinedTextField(
                    value = inputMessage,
                    onValueChange = { inputMessage = it },
                    placeholder = {
                        Text(
                            "Message ${activeChannel.name}...",
                            color = IllyTextMuted,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field"),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IllyBlue,
                        unfocusedBorderColor = IllyBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                IconButton(
                    onClick = {
                        if (inputMessage.isNotBlank()) {
                            viewModel.repository.sendMessage(selectedChannelId, inputMessage)
                            inputMessage = ""
                        }
                    },
                    modifier = Modifier
                        .testTag("chat_send_button")
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(IllyRed)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(msg: ChatMessage, isMe: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isMe) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(msg.senderColorHex)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = msg.senderName.take(1),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            if (!isMe) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = msg.senderName,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = msg.senderRole,
                        color = Color(msg.senderColorHex),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = msg.timestamp,
                        color = IllyTextMuted,
                        fontSize = 9.sp
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
            }

            Surface(
                shape = RoundedCornerShape(
                    topStart = 14.dp,
                    topEnd = 14.dp,
                    bottomStart = if (isMe) 14.dp else 2.dp,
                    bottomEnd = if (isMe) 2.dp else 14.dp
                ),
                color = if (isMe) IllyRed.copy(alpha = 0.85f) else IllyCardGlass,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isMe) IllyRed else IllyBorder
                )
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                    Text(
                        text = msg.text,
                        color = Color.White,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    if (isMe) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = msg.timestamp,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 8.sp,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }

            // Reactions badges
            if (msg.reactions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    msg.reactions.forEach { (emoji, count) ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = IllySurfaceDark,
                            border = androidx.compose.foundation.BorderStroke(1.dp, IllyBorder)
                        ) {
                            Text(
                                text = "$emoji $count",
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
