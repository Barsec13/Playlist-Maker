package com.practicum.playlistmaker.db.data.converter

import android.annotation.SuppressLint
import androidx.core.net.toUri
import com.practicum.playlistmaker.db.data.entity.PlaylistEntity
import com.practicum.playlistmaker.db.data.entity.TrackInPlaylistEntity
import com.practicum.playlistmaker.newplaylist.domain.model.Playlist
import com.practicum.playlistmaker.player.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Date

class PlaylistDbConverter {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.playListName,
            playlist.playlistDescription,
            playlist.uriCover.toString(),
            playlist.tracksList,
            playlist.quantityTracks
        )
    }

    fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            playlistEntity.id,
            playlistEntity.playListName,
            playlistEntity.playlistDescription,
            playlistEntity.uriCover.toUri(),
            playlistEntity.tracksList,
            playlistEntity.quantityTracks
        )
    }

    fun map(trackInPlaylistEntity: TrackInPlaylistEntity): Track {
        return Track(
            trackInPlaylistEntity.id,
            trackInPlaylistEntity.trackName,
            trackInPlaylistEntity.artistName,
            trackInPlaylistEntity.trackTimeMillis,
            trackInPlaylistEntity.artworkUrl100,
            trackInPlaylistEntity.artworkUrl60,
            trackInPlaylistEntity.collectionName,
            trackInPlaylistEntity.releaseDate,
            trackInPlaylistEntity.primaryGenreName,
            trackInPlaylistEntity.country,
            trackInPlaylistEntity.previewUrl,
        )
    }

    fun map(track: Track): TrackInPlaylistEntity {
        return TrackInPlaylistEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.artworkUrl60,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            getCurrentDate()
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        return sdf.format(Date())
    }
}