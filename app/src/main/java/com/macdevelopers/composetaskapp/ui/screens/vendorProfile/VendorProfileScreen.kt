package com.macdevelopers.composetaskapp.ui.screens.vendorProfile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.macdevelopers.shared.data.remote.dto.VendorDto
// ...existing imports...
import com.macdevelopers.composetaskapp.ui.components.AppButton
import com.macdevelopers.composetaskapp.ui.components.AppCard
import com.macdevelopers.composetaskapp.ui.components.AppText
import com.macdevelopers.composetaskapp.R
import com.macdevelopers.composetaskapp.ui.theme.ComposeTaskAppTheme
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
// removed KeyboardOptions/KeyboardType/ImeAction imports to keep UI-only form simple

@Composable
fun VendorProfileScreen(
    viewModel: VendorProfileViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onCreateProfileClick: () -> Unit,
    onEditProfileClick: () -> Unit
) {
    val state = viewModel.state.value

    VendorProfileScreenContent(
        state = state,
        onBackClick = onBackClick,
        onCreateProfileClick = onCreateProfileClick,
        onEditProfileClick = onEditProfileClick,
        onCreateVendor = { businessName: String, description: String?, category: String?, location: String?, gst: String? ->
            viewModel.createVendor(businessName, description, category, location, gst)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorProfileScreenContent(
    state: VendorProfileUiState,
    onBackClick: () -> Unit,
    onCreateProfileClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onCreateVendor: (businessName: String, description: String?, category: String?, location: String?, gst: String?) -> Unit
) {
    var showCreateForm by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { AppText(text = stringResource(R.string.menu_profile)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.error != null) {
                AppText(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    if (showCreateForm) {
                        CreateVendorForm(
                            onCancel = { showCreateForm = false },
                            onCreate = { businessName, description, category, location, gst ->
                                // Hide the form and call ViewModel create
                                showCreateForm = false
                                onCreateVendor(businessName, description, category, location, gst)
                                // Also call external handler if the caller wants to navigate
                                onCreateProfileClick()
                            }
                        )
                    } else {
                        if (state.vendor == null) {
                            // Empty State - No Profile
                            NoProfileState(
                                onCreateProfileClick = { showCreateForm = true },
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            // Profile Exists - Display Profile
                            ProfileExistsState(
                                vendor = state.vendor,
                                onEditProfileClick = onEditProfileClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreateVendorForm(
    onCancel: () -> Unit,
    onCreate: (businessName: String, description: String?, category: String?, location: String?, gst: String?) -> Unit
) {
    var businessName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var gst by remember { mutableStateOf("") }

    var businessNameError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val strCreateVendorTitle = stringResource(R.string.label_create_vendor_profile)
        val strBusinessNameHint = stringResource(R.string.label_business_name_hint)
        val strCategoryHint = stringResource(R.string.label_category_hint)
        val strDescriptionHint = stringResource(R.string.label_description_hint)
        val strLocationHint = stringResource(R.string.label_location_hint)
        val strGstHint = stringResource(R.string.label_gst_hint)
        val strCancel = stringResource(R.string.label_cancel)
        val strErrorNameRequired = stringResource(R.string.error_name_required)
        val strErrorInvalidEmail = stringResource(R.string.error_invalid_email)

        AppText(
            text = strCreateVendorTitle,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = businessName,
            onValueChange = {
                businessName = it
                if (it.isNotBlank()) businessNameError = null
            },
            label = { Text(text = strBusinessNameHint) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (businessNameError != null) {
            AppText(text = businessNameError!!, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text(text = strCategoryHint) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(text = strDescriptionHint) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text(text = strLocationHint) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = gst,
            onValueChange = { gst = it },
            label = { Text(text = strGstHint) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                Text(text = strCancel)
            }
            AppButton(
                text = stringResource(R.string.label_create_vendor_profile),
                onClick = {
                    // Basic validation
                    var valid = true
                    if (businessName.isBlank()) {
                        businessNameError = strErrorNameRequired
                        valid = false
                    }

                    if (valid) {
                        // Call create handler with raw inputs; ViewModel will build the DTO
                        onCreate(
                            businessName,
                            if (description.isNullOrBlank()) null else description,
                            if (category.isNullOrBlank()) null else category,
                            if (location.isNullOrBlank()) null else location,
                            if (gst.isNullOrBlank()) null else gst
                        )
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun NoProfileState(
    onCreateProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Empty State Icon
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(60.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Title
        AppText(
            text = stringResource(R.string.label_no_vendor_profile),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Subtitle
        AppText(
            text = stringResource(R.string.label_no_vendor_profile_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Create Profile Button
        AppButton(
            text = stringResource(R.string.label_create_vendor_profile),
            onClick = onCreateProfileClick,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProfileExistsState(
    vendor: VendorDto,
    onEditProfileClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        // Profile Header Card
        AppCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Business Name and Edit Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AppText(
                        text = vendor.businessName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onEditProfileClick,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Verification Badge and Category
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (vendor.verified) {
                        VerificationBadge()
                    }
                    vendor.category?.let {
                        CategoryChip(category = it)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Description
                vendor.description?.let {
                    AppText(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Rating and Reviews Section
        AppCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                AppText(
                    text = stringResource(R.string.label_performance),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Rating Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        AppText(
                            text = stringResource(R.string.label_average_rating),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFFFFC107)
                            )
                            AppText(
                                text = "%.1f".format(vendor.averageRating ?: 0.0),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        AppText(
                            text = stringResource(R.string.label_total_reviews),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AppText(
                            text = "${vendor.totalReviews ?: 0}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Business Information Section
        AppCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                AppText(
                    text = stringResource(R.string.label_business_information),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Location
                vendor.location?.let {
                    InfoRow(
                        icon = Icons.Default.LocationOn,
                        label = stringResource(R.string.label_location),
                        value = it,
                        isHighlight = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // GST Number
                vendor.gstNumber?.let {
                    InfoRow(
                        icon = Icons.Default.Info,
                        label = stringResource(R.string.label_gst_number),
                        value = it
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Email
                vendor.ownerEmail?.let {
                    InfoRow(
                        icon = Icons.Default.Email,
                        label = stringResource(R.string.label_contact_email),
                        value = it
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Created Date
                vendor.createdAt?.let {
                    InfoRow(
                        icon = Icons.Default.Info,
                        label = stringResource(R.string.label_member_since),
                        value = it.take(10) // Show only date part
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Edit Profile Button
        AppButton(
            text = stringResource(R.string.label_edit_profile),
            onClick = onEditProfileClick,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    isHighlight: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = if (isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.width(12.dp))
            AppText(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        AppText(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isHighlight) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.padding(start = 32.dp)
        )
    }
}

@Composable
fun VerificationBadge() {
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFF4CAF50).copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = stringResource(R.string.cd_verified),
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(16.dp)
            )
            AppText(
                text = stringResource(R.string.label_verified),
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun CategoryChip(category: String) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        AppText(
            text = category,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VendorProfileScreenPreviewWithProfile() {
    ComposeTaskAppTheme {
        VendorProfileScreenContent(
            state = VendorProfileUiState(
                vendor = VendorDto(
                    id = "ca7eca56-18ee-4101-8138-90616359a061",
                    businessName = "Wel Corp",
                    description = "A small scale software development company based in Mumbai specializing in Mobile apps and web solutions.",
                    category = "IT Services",
                    location = "Mumbai, Maharashtra, India",
                    gstNumber = "ABCD001246784",
                    verified = true,
                    ownerEmail = "john@example.com",
                    createdAt = "2026-04-02T18:23:28.381056",
                    averageRating = 4.5,
                    totalReviews = 12
                )
            ),
                    onBackClick = {},
                    onCreateProfileClick = {},
                    onEditProfileClick = {},
                    onCreateVendor = { _, _, _, _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VendorProfileScreenPreviewNoProfile() {
    ComposeTaskAppTheme {
        VendorProfileScreenContent(
            state = VendorProfileUiState(vendor = null),
            onBackClick = {},
            onCreateProfileClick = {},
            onEditProfileClick = {},
            onCreateVendor = { _, _, _, _, _ -> }
        )
    }
}
