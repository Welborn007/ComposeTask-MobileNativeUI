package com.macdevelopers.composetaskapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.macdevelopers.composetaskapp.R
import com.macdevelopers.shared.data.remote.dto.VendorDto
import com.macdevelopers.composetaskapp.ui.components.AppCard
import com.macdevelopers.composetaskapp.ui.components.AppText
import com.macdevelopers.composetaskapp.ui.components.NetworkErrorBanner
import com.macdevelopers.composetaskapp.ui.theme.ComposeTaskAppTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()

    HomeScreenContent(
        state = viewModel.state.value,
        isConnected = isConnected,
        onLogout = {
            viewModel.logout()
            onLogout()
        },
        onRefresh = {
            viewModel.getVendors()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    state: HomeUiState,
    isConnected: Boolean,
    onLogout: () -> Unit,
    onRefresh: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    AppText(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))
                NavigationDrawerItem(
                    label = { AppText(text = stringResource(id = R.string.menu_home)) },
                    selected = true,
                    onClick = {
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { AppText(text = stringResource(id = R.string.menu_logout)) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            onLogout()
                        }
                    },
                    icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { AppText(text = stringResource(id = R.string.app_name)) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = stringResource(id = R.string.cd_menu))
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                NetworkErrorBanner(
                    message = stringResource(R.string.error_no_internet),
                    isVisible = !isConnected,
                    modifier = Modifier.fillMaxWidth()
                )

                PullToRefreshBox(
                    isRefreshing = state.isLoading,
                    onRefresh = onRefresh,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (state.error != null && state.vendors.isEmpty()) {
                        AppText(
                            text = state.error,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    } else {
                        VendorList(vendors = state.vendors)
                    }
                }
            }
        }
    }
}

@Composable
fun VendorList(vendors: List<VendorDto>) {
     if (vendors.isEmpty()) {
         Box(
             modifier = Modifier.fillMaxSize(),
             contentAlignment = Alignment.Center
         ) {
             AppText(
                 text = "No vendors available",
                 style = MaterialTheme.typography.bodyLarge,
                 color = MaterialTheme.colorScheme.onSurfaceVariant
             )
         }
     } else {
         LazyColumn(
             modifier = Modifier.fillMaxSize(),
             contentPadding = PaddingValues(16.dp),
             verticalArrangement = Arrangement.spacedBy(12.dp)
         ) {
             item {
                 Column {
                     AppText(
                         text = stringResource(id = R.string.label_available_vendors),
                         style = MaterialTheme.typography.headlineSmall,
                         fontWeight = FontWeight.Bold
                     )
                     AppText(
                         text = "${vendors.size} vendor${if (vendors.size != 1) "s" else ""} available",
                         style = MaterialTheme.typography.bodySmall,
                         color = MaterialTheme.colorScheme.onSurfaceVariant,
                         modifier = Modifier.padding(top = 4.dp)
                     )
                 }
             }
             items(vendors) { vendor ->
                 VendorItem(vendor = vendor)
             }
         }
     }
 }

@Composable
fun VendorItem(vendor: VendorDto) {
     AppCard(
         modifier = Modifier
             .fillMaxWidth()
             .clickable { /* Handle vendor click */ }
     ) {
         Column(
             modifier = Modifier.padding(16.dp)
         ) {
             // Header Row: Business Name and Verification Badge
             Row(
                 verticalAlignment = Alignment.CenterVertically,
                 modifier = Modifier.fillMaxWidth()
             ) {
                 AppText(
                     text = vendor.businessName,
                     style = MaterialTheme.typography.titleLarge,
                     fontWeight = FontWeight.Bold,
                     modifier = Modifier.weight(1f)
                 )
                 if (vendor.verified) {
                     VerificationBadge()
                 }
             }

             Spacer(modifier = Modifier.height(8.dp))

             // Category and Rating Row
             Row(
                 modifier = Modifier.fillMaxWidth(),
                 horizontalArrangement = Arrangement.SpaceBetween,
                 verticalAlignment = Alignment.CenterVertically
             ) {
                 vendor.category?.let {
                     CategoryChip(category = it)
                 }
                 RatingBadge(
                     rating = vendor.averageRating ?: 0.0,
                     reviewCount = vendor.totalReviews ?: 0
                 )
             }

             Spacer(modifier = Modifier.height(12.dp))

             // Description
             vendor.description?.let {
                 AppText(
                     text = it,
                     style = MaterialTheme.typography.bodyMedium,
                     color = MaterialTheme.colorScheme.onSurfaceVariant,
                     maxLines = 2,
                     modifier = Modifier.fillMaxWidth()
                 )
             }

             Spacer(modifier = Modifier.height(12.dp))

             // Info Section
             Column(
                 modifier = Modifier.fillMaxWidth(),
                 verticalArrangement = Arrangement.spacedBy(8.dp)
             ) {
                 InfoRow(
                     icon = Icons.Default.LocationOn,
                     text = vendor.location ?: stringResource(id = R.string.label_not_available),
                     isHighlight = true
                 )
                 InfoRow(
                     icon = Icons.Default.Info,
                     text = stringResource(id = R.string.label_gst, vendor.gstNumber ?: stringResource(id = R.string.label_not_available))
                 )
             }
         }
     }
 }

@Composable
fun InfoRow(icon: ImageVector, text: String, isHighlight: Boolean = false) {
     Row(
         verticalAlignment = Alignment.CenterVertically,
         modifier = Modifier
             .fillMaxWidth()
             .padding(vertical = 2.dp)
     ) {
         Icon(
             imageVector = icon,
             contentDescription = null,
             modifier = Modifier.size(16.dp),
             tint = if (isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
         )
         Spacer(modifier = Modifier.width(8.dp))
         AppText(
             text = text,
             style = MaterialTheme.typography.bodySmall,
             color = if (isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
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
                 contentDescription = stringResource(id = R.string.cd_verified),
                 tint = Color(0xFF4CAF50),
                 modifier = Modifier.size(16.dp)
             )
             AppText(
                 text = "Verified",
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

@Composable
fun RatingBadge(rating: Double, reviewCount: Int) {
     Box(
         modifier = Modifier
             .background(
                 color = MaterialTheme.colorScheme.secondaryContainer,
                 shape = RoundedCornerShape(12.dp)
             )
             .padding(horizontal = 10.dp, vertical = 6.dp)
     ) {
         Row(
             verticalAlignment = Alignment.CenterVertically,
             horizontalArrangement = Arrangement.spacedBy(4.dp)
         ) {
             Icon(
                 imageVector = Icons.Default.Star,
                 contentDescription = null,
                 modifier = Modifier.size(14.dp),
                 tint = Color(0xFFFFC107)
             )
             AppText(
                 text = "%.1f".format(rating),
                 style = MaterialTheme.typography.labelSmall,
                 fontWeight = FontWeight.SemiBold,
                 color = MaterialTheme.colorScheme.onSecondaryContainer
             )
             AppText(
                 text = "($reviewCount)",
                 style = MaterialTheme.typography.labelSmall,
                 color = MaterialTheme.colorScheme.onSecondaryContainer
             )
         }
     }
 }

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ComposeTaskAppTheme {
        HomeScreenContent(
            state = HomeUiState(
                vendors = listOf(
                    VendorDto(
                        id = "ca7eca56-18ee-4101-8138-90616359a061",
                        businessName = "Wel corp",
                        description = "A small scale software development company based in Mumbai specializing in Mobile apps",
                        category = "IT Services",
                        location = "Mumbai, Maharashtra, India",
                        gstNumber = "ABCD001246784",
                        verified = true,
                        ownerEmail = "john@example.com",
                        createdAt = "2026-04-02T18:23:28.381056",
                        averageRating = 0.0,
                        totalReviews = 0
                    ),
                    VendorDto(
                        id = "sw7eca56-23ee-4101-8138-90616359a061",
                        businessName = "Tech Solutions",
                        description = "Providing modern web solutions for enterprise clients.",
                        category = "Software",
                        location = "Bangalore, India",
                        gstNumber = "GSTR123456789",
                        verified = false,
                        ownerEmail = "contact@techsol.com",
                        createdAt = "2026-04-02T18:23:28.381056",
                        averageRating = 10.0,
                        totalReviews = 1
                    )
                )
            ),
            onLogout = {},
            onRefresh = {},
            isConnected = true
        )
    }
}
