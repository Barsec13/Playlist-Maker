package com.practicum.playlistmaker.db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.db.data.entity.FavoriteTrackEntity

@Dao
interface FavoriteTrackDao {
    @Insert(entity = FavoriteTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTrack(track: FavoriteTrackEntity)

    @Delete(entity = FavoriteTrackEntity::class,)
    suspend fun deleteTrackOnFavorite(track: FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_track_table")
    suspend fun getFavoriteTracks(): List<FavoriteTrackEntity>

    @Query("SELECT id FROM favorite_track_table")
    suspend fun getIdFavoriteTrack(): List<Int>

    @Query("SELECT * FROM favorite_track_table WHERE id =:id")
    suspend fun getFavoriteTrackById(id:Int):FavoriteTrackEntity
}