package com.practicum.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.db.data.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class,)
    suspend fun deleteTrackOnFavorite(track: TrackEntity)

    @Query("SELECT * FROM favorite_track_table")
    suspend fun getFavoriteTracks(): List<TrackEntity>

    @Query("SELECT id FROM favorite_track_table")
    suspend fun getIdFavoriteTrack(): List<Int>

    @Query("SELECT * FROM favorite_track_table WHERE id =:id")
    suspend fun getFavoriteTrackById(id:Int):TrackEntity
}