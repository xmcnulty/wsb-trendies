package io.xavier.topwsb.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.xavier.topwsb.data.local.entities.TrendingStockEntity
import io.xavier.topwsb.domain.model.wsb_comment.WsbComment

/**
 * Database used to cache trending stock information.
 */
@Database(
    entities = [TrendingStockEntity::class, WsbComment::class],
    version = 2,
    exportSchema = false
)
abstract class TrendiesDatabase: RoomDatabase() {
    abstract val trendingStockDao: TrendingStockDao
    abstract val commentsDao: WsbCommentsDao
}