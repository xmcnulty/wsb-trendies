package io.xavier.topwsb.presentation.stock_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.xavier.topwsb.R
import io.xavier.topwsb.R.drawable
import io.xavier.topwsb.domain.model.trending_stock.TrendingStock
import io.xavier.topwsb.presentation.common_composables.SectionTitle
import io.xavier.topwsb.presentation.stock_list.components.LastUpdateText
import io.xavier.topwsb.presentation.stock_list.components.StockListItem
import io.xavier.topwsb.presentation.theme.defaultHorizontalPadding
import kotlinx.coroutines.launch

/**
 * Screen that displays a list of the top 20 stocks mentioned on r/wallstreetbets
 * in the past 24 hours. Uses Material 3 components and specifications.
 */
@Suppress("OPT_IN_IS_NOT_ENABLED")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListScreen(
    navToStockDetail: (TrendingStock) -> Unit,
    viewModel: StockListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val context = LocalContext.current

    val stockListState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = snackbarHostState) {
        viewModel.errorEvents.collect { errorMsgResId ->

            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    context.resources.getString(errorMsgResId),
                    withDismissAction = true,
                    actionLabel = context.resources.getString(R.string.retry)
                )
            }
        }
    }

    Scaffold(
        modifier = Modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        modifier = Modifier
                            .requiredHeight(64.dp)
                            .padding(top = 4.dp, start = 4.dp),
                        painter = painterResource(id = drawable.ic_wsb_logo),
                        contentDescription = null
                    )
                },
                actions = {
                    LastUpdateText(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {
                                viewModel.refreshStocks()
                            },
                        state = state
                    )
                }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            state = stockListState,
            contentPadding = innerPadding
        ) {
            item {
                SectionTitle(
                    title = "Trending on WallstreetBets",
                    modifier = Modifier.padding(defaultHorizontalPadding)
                )
            }

            items(state.trendingStocks) { stock ->
                StockListItem(
                    trendingStock = stock,
                    onItemClick = navToStockDetail
                )
            }
        }
    }
}