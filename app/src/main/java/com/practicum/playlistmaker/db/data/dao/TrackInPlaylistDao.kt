package com.practicum.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.db.data.entity.TrackInPlaylistEntity

@Dao
interface TrackInPlaylistDao {
    @Insert(entity = TrackInPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackInPlaylist(track: TrackInPlaylistEntity)

    @Query("SELECT * FROM track_in_playlist_table")
    suspend fun getAllTrackInPlaylists(): List<TrackInPlaylistEntity>

    @Query("SELECT * FROM track_in_playlist_table WHERE id =:trackId")
    suspend fun getPlaylistById(trackId: Int): TrackInPlaylistEntity

    @Delete(entity = TrackInPlaylistEntity::class)
    suspend fun deleteTrack(trackEntity: TrackInPlaylistEntity)
}