package com.macdevelopers.composetaskapp.ui.screens.home

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.macdevelopers.composetaskapp.R
import com.macdevelopers.composetaskapp.data.remote.dto.VendorDto
import com.macdevelopers.composetaskapp.ui.components.AppCard
import com.macdevelopers.composetaskapp.ui.components.AppText
import com.macdevelopers.composetaskapp.ui.theme.ComposeTaskAppTheme
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreenContent(
        state = viewModel.state.value,
        onLogout = {
            viewModel.logout()
            onLogout()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    state: HomeUiState,
    onLogout: () -> Unit
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
                    VendorList(vendors = state.vendors)
                }
            }
        }
    }
}

@Composable
fun VendorList(vendors: List<VendorDto>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            AppText(
                text = stringResource(id = R.string.label_available_vendors),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        items(vendors) { vendor ->
            VendorItem(vendor = vendor)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun VendorItem(vendor: VendorDto) {
    AppCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
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
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = stringResource(id = R.string.cd_verified),
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            vendor.category?.let {
                AppText(
                    text = it,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            vendor.description?.let {
                AppText(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            InfoRow(icon = Icons.Default.LocationOn, text = vendor.location ?: stringResource(id = R.string.label_not_available))
            InfoRow(icon = Icons.Default.Info, text = stringResource(id = R.string.label_gst, vendor.gstNumber ?: stringResource(id = R.string.label_not_available)))
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.width(8.dp))
        AppText(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
                        id = 1,
                        businessName = "Wel corp",
                        description = "A small scale software development company based in Mumbai specializing in Mobile apps",
                        category = "IT Services",
                        location = "Mumbai, Maharashtra, India",
                        gstNumber = "ABCD001246784",
                        verified = true,
                        ownerEmail = "john@example.com",
                        createdAt = "2026-04-02T18:23:28.381056"
                    ),
                    VendorDto(
                        id = 2,
                        businessName = "Tech Solutions",
                        description = "Providing modern web solutions for enterprise clients.",
                        category = "Software",
                        location = "Bangalore, India",
                        gstNumber = "GSTR123456789",
                        verified = false,
                        ownerEmail = "contact@techsol.com",
                        createdAt = "2026-04-02T18:23:28.381056"
                    )
                )
            ),
            onLogout = {}
        )
    }
}
