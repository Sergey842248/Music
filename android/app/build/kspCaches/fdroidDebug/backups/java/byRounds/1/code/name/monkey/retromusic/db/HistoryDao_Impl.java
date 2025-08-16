package code.name.monkey.retromusic.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
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
public final class HistoryDao_Impl implements HistoryDao {
  private final RoomDatabase __db;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSongInHistory;

  private final SharedSQLiteStatement __preparedStmtOfClearHistory;

  private final EntityUpsertionAdapter<HistoryEntity> __upsertionAdapterOfHistoryEntity;

  public HistoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__preparedStmtOfDeleteSongInHistory = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM HistoryEntity WHERE id= ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearHistory = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM HistoryEntity";
        return _query;
      }
    };
    this.__upsertionAdapterOfHistoryEntity = new EntityUpsertionAdapter<HistoryEntity>(new EntityInsertionAdapter<HistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `HistoryEntity` (`id`,`title`,`track_number`,`year`,`duration`,`data`,`date_modified`,`album_id`,`album_name`,`artist_id`,`artist_name`,`composer`,`album_artist`,`time_played`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HistoryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindLong(3, entity.getTrackNumber());
        if (entity.getYear() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getYear());
        }
        statement.bindLong(5, entity.getDuration());
        statement.bindString(6, entity.getData());
        statement.bindLong(7, entity.getDateModified());
        statement.bindLong(8, entity.getAlbumId());
        statement.bindString(9, entity.getAlbumName());
        statement.bindLong(10, entity.getArtistId());
        statement.bindString(11, entity.getArtistName());
        if (entity.getComposer() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getComposer());
        }
        if (entity.getAlbumArtist() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getAlbumArtist());
        }
        statement.bindLong(14, entity.getTimePlayed());
      }
    }, new EntityDeletionOrUpdateAdapter<HistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `HistoryEntity` SET `id` = ?,`title` = ?,`track_number` = ?,`year` = ?,`duration` = ?,`data` = ?,`date_modified` = ?,`album_id` = ?,`album_name` = ?,`artist_id` = ?,`artist_name` = ?,`composer` = ?,`album_artist` = ?,`time_played` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HistoryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindLong(3, entity.getTrackNumber());
        if (entity.getYear() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getYear());
        }
        statement.bindLong(5, entity.getDuration());
        statement.bindString(6, entity.getData());
        statement.bindLong(7, entity.getDateModified());
        statement.bindLong(8, entity.getAlbumId());
        statement.bindString(9, entity.getAlbumName());
        statement.bindLong(10, entity.getArtistId());
        statement.bindString(11, entity.getArtistName());
        if (entity.getComposer() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getComposer());
        }
        if (entity.getAlbumArtist() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getAlbumArtist());
        }
        statement.bindLong(14, entity.getTimePlayed());
        statement.bindLong(15, entity.getId());
      }
    });
  }

  @Override
  public void deleteSongInHistory(final long songId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSongInHistory.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, songId);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteSongInHistory.release(_stmt);
    }
  }

  @Override
  public Object clearHistory(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearHistory.acquire();
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
          __preparedStmtOfClearHistory.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertSongInHistory(final HistoryEntity historyEntity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfHistoryEntity.upsert(historyEntity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public List<HistoryEntity> historySongs() {
    final String _sql = "SELECT * FROM HistoryEntity ORDER BY time_played DESC LIMIT 100";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
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
      final int _cursorIndexOfTimePlayed = CursorUtil.getColumnIndexOrThrow(_cursor, "time_played");
      final List<HistoryEntity> _result = new ArrayList<HistoryEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final HistoryEntity _item;
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
        final long _tmpTimePlayed;
        _tmpTimePlayed = _cursor.getLong(_cursorIndexOfTimePlayed);
        _item = new HistoryEntity(_tmpId,_tmpTitle,_tmpTrackNumber,_tmpYear,_tmpDuration,_tmpData,_tmpDateModified,_tmpAlbumId,_tmpAlbumName,_tmpArtistId,_tmpArtistName,_tmpComposer,_tmpAlbumArtist,_tmpTimePlayed);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<HistoryEntity>> observableHistorySongs() {
    final String _sql = "SELECT * FROM HistoryEntity ORDER BY time_played DESC LIMIT 100";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"HistoryEntity"}, false, new Callable<List<HistoryEntity>>() {
      @Override
      @Nullable
      public List<HistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
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
          final int _cursorIndexOfTimePlayed = CursorUtil.getColumnIndexOrThrow(_cursor, "time_played");
          final List<HistoryEntity> _result = new ArrayList<HistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HistoryEntity _item;
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
            final long _tmpTimePlayed;
            _tmpTimePlayed = _cursor.getLong(_cursorIndexOfTimePlayed);
            _item = new HistoryEntity(_tmpId,_tmpTitle,_tmpTrackNumber,_tmpYear,_tmpDuration,_tmpData,_tmpDateModified,_tmpAlbumId,_tmpAlbumName,_tmpArtistId,_tmpArtistName,_tmpComposer,_tmpAlbumArtist,_tmpTimePlayed);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
