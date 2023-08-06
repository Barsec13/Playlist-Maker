package com.practicum.playlistmaker.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val playListName: String,
    val playlistDescription: String,
    val uriCover: String,
    val tracksList: String? = null,
    val quantityTracks: Int = 0,
)