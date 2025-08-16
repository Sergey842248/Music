package code.name.monkey.retromusic.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PlaylistDao_Impl implements PlaylistDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PlaylistEntity> __insertionAdapterOfPlaylistEntity;

  private final EntityInsertionAdapter<SongEntity> __insertionAdapterOfSongEntity;

  private final EntityDeletionOrUpdateAdapter<SongEntity> __deletionAdapterOfSongEntity;

  private final EntityDeletionOrUpdateAdapter<PlaylistEntity> __deletionAdapterOfPlaylistEntity;

  private final SharedSQLiteStatement __preparedStmtOfRenamePlaylist;

  private final SharedSQLiteStatement __preparedStmtOfDeletePlaylistSongs;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSongFromPlaylist;

  public PlaylistDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlaylistEntity = new EntityInsertionAdapter<PlaylistEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `PlaylistEntity` (`playlist_id`,`playlist_name`) VALUES (nullif(?, 0),?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlaylistEntity entity) {
        statement.bindLong(1, entity.getPlayListId());
        statement.bindString(2, entity.getPlaylistName());
      }
    };
    this.__insertionAdapterOfSongEntity = new EntityInsertionAdapter<SongEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `SongEntity` (`song_key`,`playlist_creator_id`,`id`,`title`,`track_number`,`year`,`duration`,`data`,`date_modified`,`album_id`,`album_name`,`artist_id`,`artist_name`,`composer`,`album_artist`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SongEntity entity) {
        statement.bindLong(1, entity.getSongPrimaryKey());
        statement.bindLong(2, entity.getPlaylistCreatorId());
        statement.bindLong(3, entity.getId());
        statement.bindString(4, entity.getTitle());
        statement.bindLong(5, entity.getTrackNumber());
        if (entity.getYear() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getYear());
        }
        statement.bindLong(7, entity.getDuration());
        statement.bindString(8, entity.getData());
        statement.bindLong(9, entity.getDateModified());
        statement.bindLong(10, entity.getAlbumId());
        statement.bindString(11, entity.getAlbumName());
        statement.bindLong(12, entity.getArtistId());
        statement.bindString(13, entity.getArtistName());
        if (entity.getComposer() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getComposer());
        }
        if (entity.getAlbumArtist() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getAlbumArtist());
        }
      }
    };
    this.__deletionAdapterOfSongEntity = new EntityDeletionOrUpdateAdapter<SongEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `SongEntity` WHERE `song_key` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SongEntity entity) {
        statement.bindLong(1, entity.getSongPrimaryKey());
      }
    };
    this.__deletionAdapterOfPlaylistEntity = new EntityDeletionOrUpdateAdapter<PlaylistEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `PlaylistEntity` WHERE `playlist_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlaylistEntity entity) {
        statement.bindLong(1, entity.getPlayListId());
      }
    };
    this.__preparedStmtOfRenamePlaylist = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE PlaylistEntity SET playlist_name = ? WHERE playlist_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeletePlaylistSongs = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM SongEntity WHERE playlist_creator_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteSongFromPlaylist = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM SongEntity WHERE playlist_creator_id = ? AND id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object createPlaylist(final PlaylistEntity playlistEntity,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPlaylistEntity.insertAndReturnId(playlistEntity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSongsToPlaylist(final List<SongEntity> songEntities,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSongEntity.insert(songEntities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePlaylistSongs(final List<SongEntity> songs,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSongEntity.handleMultiple(songs);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePlaylist(final PlaylistEntity playlistEntity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPlaylistEntity.handle(playlistEntity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePlaylists(final List<PlaylistEntity> playlistEntities,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPlaylistEntity.handleMultiple(playlistEntities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object renamePlaylist(final long playlistId, final String name,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfRenamePlaylist.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, name);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, playlistId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfRenamePlaylist.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePlaylistSongs(final long playlistId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeletePlaylistSongs.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, playlistId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeletePlaylistSongs.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSongFromPlaylist(final long playlistId, final long songId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSongFromPlaylist.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, playlistId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, songId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteSongFromPlaylist.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public List<PlaylistEntity> playlist(final String name) {
    final String _sql = "SELECT * FROM PlaylistEntity WHERE playlist_name = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, name);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPlayListId = CursorUtil.getColumnIndexOrThrow(_cursor, "playlist_id");
      final int _cursorIndexOfPlaylistName = CursorUtil.getColumnIndexOrThrow(_cursor, "playlist_name");
      final List<PlaylistEntity> _result = new ArrayList<PlaylistEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final PlaylistEntity _item;
        final long _tmpPlayListId;
        _tmpPlayListId = _cursor.getLong(_cursorIndexOfPlayListId);
        final String _tmpPlaylistName;
        _tmpPlaylistName = _cursor.getString(_cursorIndexOfPlaylistName);
        _item = new PlaylistEntity(_tmpPlayListId,_tmpPlaylistName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Object playlists(final Continuation<? super List<PlaylistEntity>> $completion) {
    final String _sql = "SELECT * FROM PlaylistEntity";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PlaylistEntity>>() {
      @Override
      @NonNull
      public List<PlaylistEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlayListId = CursorUtil.getColumnIndexOrThrow(_cursor, "playlist_id");
          final int _cursorIndexOfPlaylistName = CursorUtil.getColumnIndexOrThrow(_cursor, "playlist_name");
          final List<PlaylistEntity> _result = new ArrayList<PlaylistEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PlaylistEntity _item;
            final long _tmpPlayListId;
            _tmpPlayListId = _cursor.getLong(_cursorIndexOfPlayListId);
            final String _tmpPlaylistName;
            _tmpPlaylistName = _cursor.getString(_cursorIndexOfPlaylistName);
            _item = new PlaylistEntity(_tmpPlayListId,_tmpPlaylistName);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object playlistsWithSongs(
      final Continuation<? super List<PlaylistWithSongs>> $completion) {
    final String _sql = "SELECT * FROM PlaylistEntity";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<List<PlaylistWithSongs>>() {
      @Override
      @NonNull
      public List<PlaylistWithSongs> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfPlayListId = CursorUtil.getColumnIndexOrThrow(_cursor, "playlist_id");
            final int _cursorIndexOfPlaylistName = CursorUtil.getColumnIndexOrThrow(_cursor, "playlist_name");
            final LongSparseArray<ArrayList<SongEntity>> _collectionSongs = new LongSparseArray<ArrayList<SongEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfPlayListId);
              if (!_collectionSongs.containsKey(_tmpKey)) {
                _collectionSongs.put(_tmpKey, new ArrayList<SongEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipSongEntityAscodeNameMonkeyRetromusicDbSongEntity(_collectionSongs);
            final List<PlaylistWithSongs> _result = new ArrayList<PlaylistWithSongs>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final PlaylistWithSongs _item;
              final PlaylistEntity _tmpPlaylistEntity;
              final long _tmpPlayListId;
              _tmpPlayListId = _cursor.getLong(_cursorIndexOfPlayListId);
              final String _tmpPlaylistName;
              _tmpPlaylistName = _cursor.getString(_cursorIndexOfPlaylistName);
              _tmpPlaylistEntity = new PlaylistEntity(_tmpPlayListId,_tmpPlaylistName);
              final ArrayList<SongEntity> _tmpSongsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfPlayListId);
              _tmpSongsCollection = _collectionSongs.get(_tmpKey_1);
              _item = new PlaylistWithSongs(_tmpPlaylistEntity,_tmpSongsCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<PlaylistWithSongs> getPlaylist(final long playlistId) {
    final String _sql = "SELECT * FROM PlaylistEntity WHERE playlist_id= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, playlistId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"SongEntity",
        "PlaylistEntity"}, true, new Callable<PlaylistWithSongs>() {
      @Override
      @Nullable
      public PlaylistWithSongs call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfPlayListId = CursorUtil.getColumnIndexOrThrow(_cursor, "playlist_id");
            final int _cursorIndexOfPlaylistName = CursorUtil.getColumnIndexOrThrow(_cursor, "playlist_name");
            final LongSparseArray<ArrayList<SongEntity>> _collectionSongs = new LongSparseArray<ArrayList<SongEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfPlayListId);
              if (!_collectionSongs.containsKey(_tmpKey)) {
                _collectionSongs.put(_tmpKey, new ArrayList<SongEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipSongEntityAscodeNameMonkeyRetromusicDbSongEntity(_collectionSongs);
            final PlaylistWithSongs _result;
            if (_cursor.moveToFirst()) {
              final PlaylistEntity _tmpPlaylistEntity;
              final long _tmpPlayListId;
              _tmpPlayListId = _cursor.getLong(_cursorIndexOfPlayListId);
              final String _tmpPlaylistName;
              _tmpPlaylistName = _cursor.getString(_cursorIndexOfPlaylistName);
              _tmpPlaylistEntity = new PlaylistEntity(_tmpPlayListId,_tmpPlaylistName);
              final ArrayList<SongEntity> _tmpSongsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfPlayListId);
              _tmpSongsCollection = _collectionSongs.get(_tmpKey_1);
              _result = new PlaylistWithSongs(_tmpPlaylistEntity,_tmpSongsCollection);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object isSongExistsInPlaylist(final long playlistId, final long songId,
      final Continuation<? super List<SongEntity>> $completion) {
    final String _sql = "SELECT * FROM SongEntity WHERE playlist_creator_id = ? AND id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, playlistId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, songId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SongEntity>>() {
      @Override
      @NonNull
      public List<SongEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSongPrimaryKey = CursorUtil.getColumnIndexOrThrow(_cursor, "song_key");
          final int _cursorIndexOfPlaylistCreatorId = CursorUtil.getColumnIndexOrThrow(_cursor, "playlist_creator_id");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfTrackNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "track_number");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfData = CursorUtil.getColumnIndexOrThrow(_cursor, "data");
          final int _cursorIndexOfDateModified = CursorUtil.getColumnIndexOrThrow(_cursor, "date_modified");
          final int _cursorIndexOfAlbumId = CursorUtil.getColumnIndexOrThrow(_cursor, "album_id");
          final int _cursorIndexOfAlbumName = CursorUtil.getColumnIndexOrThrow(_cursor, "album_name");
          final int _cursorIndexOfArtistId = CursorUtil.getColumnIndexOrThrow(_cursor, "artist_id");
          final int _cursorIndexOfArtistName = CursorUtil.getColumnIndexOrThrow(_cursor, "artist_name");
          final int _cursorIndexOfComposer = CursorUtil.getColumnIndexOrThrow(_cursor, "composer");
          final int _cursorIndexOfAlbumArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "album_artist");
          final List<SongEntity> _result = new ArrayList<SongEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SongEntity _item;
            final long _tmpSongPrimaryKey;
            _tmpSongPrimaryKey = _cursor.getLong(_cursorIndexOfSongPrimaryKey);
            final long _tmpPlaylistCreatorId;
            _tmpPlaylistCreatorId = _cursor.getLong(_cursorIndexOfPlaylistCreatorId);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final int _tmpTrackNumber;
            _tmpTrackNumber = _cursor.getInt(_cursorIndexOfTrackNumber);
            final String _tmpYear;
            if (_cursor.isNull(_cursorIndexOfYear)) {
              _tmpYear = null;
            } else {
              _tmpYear = _cursor.getString(_cursorIndexOfYear);
            }
            final long _tmpDuration;
            _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            final String _tmpData;
            _tmpData = _cursor.getString(_cursorIndexOfData);
            final long _tmpDateModified;
            _tmpDateModified = _cursor.getLong(_cursorIndexOfDateModified);
            final long _tmpAlbumId;
            _tmpAlbumId = _cursor.getLong(_cursorIndexOfAlbumId);
            final String _tmpAlbumName;
            _tmpAlbumName = _cursor.getString(_cursorIndexOfAlbumName);
            final long _tmpArtistId;
            _tmpArtistId = _cursor.getLong(_cursorIndexOfArtistId);
            final String _tmpArtistName;
            _tmpArtistName = _cursor.getString(_cursorIndexOfArtistName);
            final String _tmpComposer;
            if (_cursor.isNull(_cursorIndexOfComposer)) {
              _tmpComposer = null;
            } else {
              _tmpComposer = _cursor.getString(_cursorIndexOfComposer);
            }
            final String _tmpAlbumArtist;
            if (_cursor.isNull(_cursorIndexOfAlbumArtist)) {
              _tmpAlbumArtist = null;
            } else {
              _tmpAlbumArtist = _cursor.getString(_cursorIndexOfAlbumArtist);
            }
            _item = new SongEntity(_tmpSongPrimaryKey,_tmpPlaylistCreatorId,_tmpId,_tmpTitle,_tmpTrackNumber,_tmpYear,_tmpDuration,_tmpData,_tmpDateModified,_tmpAlbumId,_tmpAlbumName,_tmpArtistId,_tmpArtistName,_tmpComposer,_tmpAlbumArtist);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<SongEntity>> songsFromPlaylist(final long playlistId) {
    final String _sql = "SELECT * FROM SongEntity WHERE playlist_creator_id = ? ORDER BY song_key asc";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, playlistId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"SongEntity"}, false, new Callable<List<SongEntity>>() {
      @Override
      @Nullable
      public List<SongEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSongPrimaryKey = CursorUtil.getColumnIndexOrThrow(_cursor, "song_key");
          final int _cursorIndexOfPlaylistCreatorId = CursorUtil.getColumnIndexOrThrow(_cursor, "playlist_creator_id");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfTrackNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "track_number");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfData = CursorUtil.getColumnIndexOrThrow(_cursor, "data");
          final int _cursorIndexOfDateModified = CursorUtil.getColumnIndexOrThrow(_cursor, "date_modified");
          final int _cursorIndexOfAlbumId = CursorUtil.getColumnIndexOrThrow(_cursor, "album_id");
          final int _cursorIndexOfAlbumName = CursorUtil.getColumnIndexOrThrow(_cursor, "album_name");
          final int _cursorIndexOfArtistId = CursorUtil.getColumnIndexOrThrow(_cursor, "artist_id");
          final int _cursorIndexOfArtistName = CursorUtil.getColumnIndexOrThrow(_cursor, "artist_name");
          final int _cursorIndexOfComposer = CursorUtil.getColumnIndexOrThrow(_cursor, "composer");
          final int _cursorIndexOfAlbumArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "album_artist");
          final List<SongEntity> _result = new ArrayList<SongEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SongEntity _item;
            final long _tmpSongPrimaryKey;
            _tmpSongPrimaryKey = _cursor.getLong(_cursorIndexOfSongPrimaryKey);
            final long _tmpPlaylistCreatorId;
            _tmpPlaylistCreatorId = _cursor.getLong(_cursorIndexOfPlaylistCreatorId);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final int _tmpTrackNumber;
            _tmpTrackNumber = _cursor.getInt(_cursorIndexOfTrackNumber);
            final String _tmpYear;
            if (_cursor.isNull(_cursorIndexOfYear)) {
              _tmpYear = null;
            } else {
              _tmpYear = _cursor.getString(_cursorIndexOfYear);
            }
            final long _tmpDuration;
            _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            final String _tmpData;
            _tmpData = _cursor.getString(_cursorIndexOfData);
            final long _tmpDateModified;
            _tmpDateModified = _cursor.getLong(_cursorIndexOfDateModified);
            final long _tmpAlbumId;
            _tmpAlbumId = _cursor.getLong(_cursorIndexOfAlbumId);
            final String _tmpAlbumName;
            _tmpAlbumName = _cursor.getString(_cursorIndexOfAlbumName);
            final long _tmpArtistId;
            _tmpArtistId = _cursor.getLong(_cursorIndexOfArtistId);
            final String _tmpArtistName;
            _tmpArtistName = _cursor.getString(_cursorIndexOfArtistName);
            final String _tmpComposer;
            if (_cursor.isNull(_cursorIndexOfComposer)) {
              _tmpComposer = null;
            } else {
              _tmpComposer = _cursor.getString(_cursorIndexOfComposer);
            }
            final String _tmpAlbumArtist;
            if (_cursor.isNull(_cursorIndexOfAlbumArtist)) {
              _tmpAlbumArtist = null;
            } else {
              _tmpAlbumArtist = _cursor.getString(_cursorIndexOfAlbumArtist);
            }
            _item = new SongEntity(_tmpSongPrimaryKey,_tmpPlaylistCreatorId,_tmpId,_tmpTitle,_tmpTrackNumber,_tmpYear,_tmpDuration,_tmpData,_tmpDateModified,_tmpAlbumId,_tmpAlbumName,_tmpArtistId,_tmpArtistName,_tmpComposer,_tmpAlbumArtist);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<SongEntity>> favoritesSongsLiveData(final String playlistName) {
    final String _sql = "SELECT `song_key`, `playlist_creator_id`, `id`, `title`, `track_number`, `year`, `duration`, `data`, `date_modified`, `album_id`, `album_name`, `artist_id`, `artist_name`, `composer`, `album_artist` FROM (SELECT * FROM SongEntity ,(SELECT playlist_id FROM PlaylistEntity WHERE playlist_name= ? LIMIT 1) AS playlist WHERE playlist_creator_id= playlist.playlist_id)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, playlistName);
    return __db.getInvalidationTracker().createLiveData(new String[] {"SongEntity",
        "PlaylistEntity"}, false, new Callable<List<SongEntity>>() {
      @Override
      @Nullable
      public List<SongEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSongPrimaryKey = 0;
          final int _cursorIndexOfPlaylistCreatorId = 1;
          final int _cursorIndexOfId = 2;
          final int _cursorIndexOfTitle = 3;
          final int _cursorIndexOfTrackNumber = 4;
          final int _cursorIndexOfYear = 5;
          final int _cursorIndexOfDuration = 6;
          final int _cursorIndexOfData = 7;
          final int _cursorIndexOfDateModified = 8;
          final int _cursorIndexOfAlbumId = 9;
          final int _cursorIndexOfAlbumName = 10;
          final int _cursorIndexOfArtistId = 11;
          final int _cursorIndexOfArtistName = 12;
          final int _cursorIndexOfComposer = 13;
          final int _cursorIndexOfAlbumArtist = 14;
          final List<SongEntity> _result = new ArrayList<SongEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SongEntity _item;
            final long _tmpSongPrimaryKey;
            _tmpSongPrimaryKey = _cursor.getLong(_cursorIndexOfSongPrimaryKey);
            final long _tmpPlaylistCreatorId;
            _tmpPlaylistCreatorId = _cursor.getLong(_cursorIndexOfPlaylistCreatorId);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final int _tmpTrackNumber;
            _tmpTrackNumber = _cursor.getInt(_cursorIndexOfTrackNumber);
            final String _tmpYear;
            if (_cursor.isNull(_cursorIndexOfYear)) {
              _tmpYear = null;
            } else {
              _tmpYear = _cursor.getString(_cursorIndexOfYear);
            }
            final long _tmpDuration;
            _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
            final String _tmpData;
            _tmpData = _cursor.getString(_cursorIndexOfData);
            final long _tmpDateModified;
            _tmpDateModified = _cursor.getLong(_cursorIndexOfDateModified);
            final long _tmpAlbumId;
            _tmpAlbumId = _cursor.getLong(_cursorIndexOfAlbumId);
            final String _tmpAlbumName;
            _tmpAlbumName = _cursor.getString(_cursorIndexOfAlbumName);
            final long _tmpArtistId;
            _tmpArtistId = _cursor.getLong(_cursorIndexOfArtistId);
            final String _tmpArtistName;
            _tmpArtistName = _cursor.getString(_cursorIndexOfArtistName);
            final String _tmpComposer;
            if (_cursor.isNull(_cursorIndexOfComposer)) {
              _tmpComposer = null;
            } else {
              _tmpComposer = _cursor.getString(_cursorIndexOfComposer);
            }
            final String _tmpAlbumArtist;
            if (_cursor.isNull(_cursorIndexOfAlbumArtist)) {
              _tmpAlbumArtist = null;
            } else {
              _tmpAlbumArtist = _cursor.getString(_cursorIndexOfAlbumArtist);
            }
            _item = new SongEntity(_tmpSongPrimaryKey,_tmpPlaylistCreatorId,_tmpId,_tmpTitle,_tmpTrackNumber,_tmpYear,_tmpDuration,_tmpData,_tmpDateModified,_tmpAlbumId,_tmpAlbumName,_tmpArtistId,_tmpArtistName,_tmpComposer,_tmpAlbumArtist);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<SongEntity> favoritesSongs(final long playlistId) {
    final String _sql = "SELECT * FROM SongEntity WHERE playlist_creator_id= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, playlistId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfSongPrimaryKey = CursorUtil.getColumnIndexOrThrow(_cursor, "song_key");
      final int _cursorIndexOfPlaylistCreatorId = CursorUtil.getColumnIndexOrThrow(_cursor, "playlist_creator_id");
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfTrackNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "track_number");
      final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final int _cursorIndexOfData = CursorUtil.getColumnIndexOrThrow(_cursor, "data");
      final int _cursorIndexOfDateModified = CursorUtil.getColumnIndexOrThrow(_cursor, "date_modified");
      final int _cursorIndexOfAlbumId = CursorUtil.getColumnIndexOrThrow(_cursor, "album_id");
      final int _cursorIndexOfAlbumName = CursorUtil.getColumnIndexOrThrow(_cursor, "album_name");
      final int _cursorIndexOfArtistId = CursorUtil.getColumnIndexOrThrow(_cursor, "artist_id");
      final int _cursorIndexOfArtistName = CursorUtil.getColumnIndexOrThrow(_cursor, "artist_name");
      final int _cursorIndexOfComposer = CursorUtil.getColumnIndexOrThrow(_cursor, "composer");
      final int _cursorIndexOfAlbumArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "album_artist");
      final List<SongEntity> _result = new ArrayList<SongEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SongEntity _item;
        final long _tmpSongPrimaryKey;
        _tmpSongPrimaryKey = _cursor.getLong(_cursorIndexOfSongPrimaryKey);
        final long _tmpPlaylistCreatorId;
        _tmpPlaylistCreatorId = _cursor.getLong(_cursorIndexOfPlaylistCreatorId);
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        final int _tmpTrackNumber;
        _tmpTrackNumber = _cursor.getInt(_cursorIndexOfTrackNumber);
        final String _tmpYear;
        if (_cursor.isNull(_cursorIndexOfYear)) {
          _tmpYear = null;
        } else {
          _tmpYear = _cursor.getString(_cursorIndexOfYear);
        }
        final long _tmpDuration;
        _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
        final String _tmpData;
        _tmpData = _cursor.getString(_cursorIndexOfData);
        final long _tmpDateModified;
        _tmpDateModified = _cursor.getLong(_cursorIndexOfDateModified);
        final long _tmpAlbumId;
        _tmpAlbumId = _cursor.getLong(_cursorIndexOfAlbumId);
        final String _tmpAlbumName;
        _tmpAlbumName = _cursor.getString(_cursorIndexOfAlbumName);
        final long _tmpArtistId;
        _tmpArtistId = _cursor.getLong(_cursorIndexOfArtistId);
        final String _tmpArtistName;
        _tmpArtistName = _cursor.getString(_cursorIndexOfArtistName);
        final String _tmpComposer;
        if (_cursor.isNull(_cursorIndexOfComposer)) {
          _tmpComposer = null;
        } else {
          _tmpComposer = _cursor.getString(_cursorIndexOfComposer);
        }
        final String _tmpAlbumArtist;
        if (_cursor.isNull(_cursorIndexOfAlbumArtist)) {
          _tmpAlbumArtist = null;
        } else {
          _tmpAlbumArtist = _cursor.getString(_cursorIndexOfAlbumArtist);
        }
        _item = new SongEntity(_tmpSongPrimaryKey,_tmpPlaylistCreatorId,_tmpId,_tmpTitle,_tmpTrackNumber,_tmpYear,_tmpDuration,_tmpData,_tmpDateModified,_tmpAlbumId,_tmpAlbumName,_tmpArtistId,_tmpArtistName,_tmpComposer,_tmpAlbumArtist);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<Boolean> checkPlaylistExists(final long playlistId) {
    final String _sql = "SELECT EXISTS(SELECT * FROM PlaylistEntity WHERE playlist_id = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, playlistId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"PlaylistEntity"}, false, new Callable<Boolean>() {
      @Override
      @Nullable
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp == null ? null : _tmp != 0;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipSongEntityAscodeNameMonkeyRetromusicDbSongEntity(
      @NonNull final LongSparseArray<ArrayList<SongEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipSongEntityAscodeNameMonkeyRetromusicDbSongEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `song_key`,`playlist_creator_id`,`id`,`title`,`track_number`,`year`,`duration`,`data`,`date_modified`,`album_id`,`album_name`,`artist_id`,`artist_name`,`composer`,`album_artist` FROM `SongEntity` WHERE `playlist_creator_id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "playlist_creator_id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfSongPrimaryKey = 0;
      final int _cursorIndexOfPlaylistCreatorId = 1;
      final int _cursorIndexOfId = 2;
      final int _cursorIndexOfTitle = 3;
      final int _cursorIndexOfTrackNumber = 4;
      final int _cursorIndexOfYear = 5;
      final int _cursorIndexOfDuration = 6;
      final int _cursorIndexOfData = 7;
      final int _cursorIndexOfDateModified = 8;
      final int _cursorIndexOfAlbumId = 9;
      final int _cursorIndexOfAlbumName = 10;
      final int _cursorIndexOfArtistId = 11;
      final int _cursorIndexOfArtistName = 12;
      final int _cursorIndexOfComposer = 13;
      final int _cursorIndexOfAlbumArtist = 14;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<SongEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final SongEntity _item_1;
          final long _tmpSongPrimaryKey;
          _tmpSongPrimaryKey = _cursor.getLong(_cursorIndexOfSongPrimaryKey);
          final long _tmpPlaylistCreatorId;
          _tmpPlaylistCreatorId = _cursor.getLong(_cursorIndexOfPlaylistCreatorId);
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final String _tmpTitle;
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
          final int _tmpTrackNumber;
          _tmpTrackNumber = _cursor.getInt(_cursorIndexOfTrackNumber);
          final String _tmpYear;
          if (_cursor.isNull(_cursorIndexOfYear)) {
            _tmpYear = null;
          } else {
            _tmpYear = _cursor.getString(_cursorIndexOfYear);
          }
          final long _tmpDuration;
          _tmpDuration = _cursor.getLong(_cursorIndexOfDuration);
          final String _tmpData;
          _tmpData = _cursor.getString(_cursorIndexOfData);
          final long _tmpDateModified;
          _tmpDateModified = _cursor.getLong(_cursorIndexOfDateModified);
          final long _tmpAlbumId;
          _tmpAlbumId = _cursor.getLong(_cursorIndexOfAlbumId);
          final String _tmpAlbumName;
          _tmpAlbumName = _cursor.getString(_cursorIndexOfAlbumName);
          final long _tmpArtistId;
          _tmpArtistId = _cursor.getLong(_cursorIndexOfArtistId);
          final String _tmpArtistName;
          _tmpArtistName = _cursor.getString(_cursorIndexOfArtistName);
          final String _tmpComposer;
          if (_cursor.isNull(_cursorIndexOfComposer)) {
            _tmpComposer = null;
          } else {
            _tmpComposer = _cursor.getString(_cursorIndexOfComposer);
          }
          final String _tmpAlbumArtist;
          if (_cursor.isNull(_cursorIndexOfAlbumArtist)) {
            _tmpAlbumArtist = null;
          } else {
            _tmpAlbumArtist = _cursor.getString(_cursorIndexOfAlbumArtist);
          }
          _item_1 = new SongEntity(_tmpSongPrimaryKey,_tmpPlaylistCreatorId,_tmpId,_tmpTitle,_tmpTrackNumber,_tmpYear,_tmpDuration,_tmpData,_tmpDateModified,_tmpAlbumId,_tmpAlbumName,_tmpArtistId,_tmpArtistName,_tmpComposer,_tmpAlbumArtist);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
