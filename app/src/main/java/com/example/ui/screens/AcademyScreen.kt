package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.StatusPill
import com.example.ui.theme.*
import com.example.viewmodel.IllyViewModel

@Composable
fun AcademyScreen(
    viewModel: IllyViewModel,
    modifier: Modifier = Modifier
) {
    val courses by viewModel.repository.academyCourses.collectAsState()
    val documents by viewModel.repository.documents.collectAsState()

    var activeSubtab by remember { mutableStateOf(0) }
    var documentQuery by remember { mutableStateOf("") }
    var downloadedDocId by remember { mutableStateOf<String?>(null) }

    val filteredDocs = documents.filter {
        documentQuery.isBlank() || it.title.contains(documentQuery, ignoreCase = true) || it.category.contains(documentQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
    ) {
        // Academy Header
        item {
            Column {
                Text(
                    text = "ILLY Academy & Knowledge Hub",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Engineering certifications, design standards, and document vault",
                    color = IllyTextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Subtabs (Courses vs Document Vault)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (activeSubtab == 0) IllyRed else IllySurfaceDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (activeSubtab == 0) IllyRed else IllyBorder),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { activeSubtab = 0 }
                ) {
                    Text(
                        text = "Academy Courses (${courses.size})",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (activeSubtab == 1) IllyBlue else IllySurfaceDark,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (activeSubtab == 1) IllyBlue else IllyBorder),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { activeSubtab = 1 }
                ) {
                    Text(
                        text = "Document Vault (${documents.size})",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }
        }

        // TAB 0: ACADEMY COURSES
        if (activeSubtab == 0) {
            items(courses, key = { it.id }) { course ->
                GlassCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusPill(
                            text = course.category,
                            textColor = IllyCyan,
                            backgroundColor = IllyCyan.copy(alpha = 0.15f)
                        )
                        if (course.isCertified) {
                            StatusPill(
                                text = "Certified",
                                textColor = IllySuccess,
                                backgroundColor = IllySuccess.copy(alpha = 0.15f),
                                icon = Icons.Default.Verified
                            )
                        } else {
                            Text(
                                text = "${course.progressPercent}% Completed",
                                color = IllyBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = course.title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = course.description,
                        color = IllyTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { course.progressPercent / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(CircleShape),
                        color = if (course.isCertified) IllySuccess else IllyBlue,
                        trackColor = IllyBorder
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Modules: ${course.completedModules}/${course.modulesCount} • ${course.duration}",
                            color = IllyTextMuted,
                            fontSize = 11.sp
                        )

                        if (!course.isCertified) {
                            Button(
                                onClick = { viewModel.repository.completeModule(course.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = IllyBlue),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Complete Next Module", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Surface(
                                color = IllySuccess.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Badge Unlocked 🎓",
                                    color = IllySuccess,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // TAB 1: DOCUMENT VAULT
        if (activeSubtab == 1) {
            item {
                OutlinedTextField(
                    value = documentQuery,
                    onValueChange = { documentQuery = it },
                    placeholder = { Text("Search ILLY policies, standards & guidelines...", color = IllyTextMuted, fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = IllyTextSecondary) },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IllyBlue,
                        unfocusedBorderColor = IllyBorder
                    )
                )
            }

            items(filteredDocs, key = { it.id }) { doc ->
                val isDownloaded = downloadedDocId == doc.id
                GlassCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(IllyRed.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Description, contentDescription = null, tint = IllyRed, modifier = Modifier.size(20.dp))
                            }
                            Column {
                                Text(
                                    text = doc.title,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "${doc.category} • ${doc.size} • Updated ${doc.lastUpdated}",
                                    color = IllyTextSecondary,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        IconButton(
                            onClick = { downloadedDocId = doc.id }
                        ) {
                            Icon(
                                imageVector = if (isDownloaded) Icons.Default.CheckCircle else Icons.Default.FileDownload,
                                contentDescription = "Download",
                                tint = if (isDownloaded) IllySuccess else IllyBlue
                            )
                        }
                    }
                }
            }
        }
    }
}
