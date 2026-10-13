package com.bassant.squarerepos.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Cached GitHub repository.
 *
 * @property position Index of the repository in GitHub's response order. The list is sorted by it,
 * so pages always appear in the same order the API returned them.
 */
@Entity(
    tableName = "repos",
    indices = [Index("position")],
)
data class RepoEntity(
    @PrimaryKey val id: Long,
    val name: String,
    @ColumnInfo(name = "full_name") val fullName: String,
    val description: String?,
    val stars: Int,
    val forks: Int,
    val language: String?,
    @ColumnInfo(name = "html_url") val htmlUrl: String,
    val position: Int,
)
