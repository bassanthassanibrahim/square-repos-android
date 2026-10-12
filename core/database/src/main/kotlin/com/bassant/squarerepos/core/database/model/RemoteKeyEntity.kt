package com.bassant.squarerepos.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Pagination bookkeeping for one remote list.
 *
 * @property label identifies the list (e.g. the organization name).
 * @property nextPage next page to request, or `null` when the last page was reached.
 * @property lastUpdated time of the last full refresh, used to decide if the cache is still fresh.
 */
@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val label: String,
    @ColumnInfo(name = "next_page") val nextPage: Int?,
    @ColumnInfo(name = "last_updated") val lastUpdated: Long,
)
