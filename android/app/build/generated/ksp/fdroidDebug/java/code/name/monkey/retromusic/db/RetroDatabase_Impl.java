package code.name.monkey.retromusic.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RetroDatabase_Impl extends RetroDatabase {
  private volatile PlaylistDao _playlistDao;

  private volatile PlayCountDao _playCountDao;

  private volatile HistoryDao _historyDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(25) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `PlaylistEntity` (`playlist_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `playlist_name` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `SongEntity` (`song_key` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `playlist_creator_id` INTEGER NOT NULL, `id` INTEGER NOT NULL, `title` TEXT NOT NULL, `track_number` INTEGER NOT NULL, `year` TEXT, `duration` INTEGER NOT NULL, `data` TEXT NOT NULL, `date_modified` INTEGER NOT NULL, `album_id` INTEGER NOT NULL, `album_name` TEXT NOT NULL, `artist_id` INTEGER NOT NULL, `artist_name` TEXT NOT NULL, `composer` TEXT, `album_artist` TEXT)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_SongEntity_playlist_creator_id_id` ON `SongEntity` (`playlist_creator_id`, `id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `HistoryEntity` (`id` INTEGER NOT NULL, `title` TEXT NOT NULL, `track_number` INTEGER NOT NULL, `year` TEXT, `duration` INTEGER NOT NULL, `data` TEXT NOT NULL, `date_modified` INTEGER NOT NULL, `album_id` INTEGER NOT NULL, `album_name` TEXT NOT NULL, `artist_id` INTEGER NOT NULL, `artist_name` TEXT NOT NULL, `composer` TEXT, `album_artist` TEXT, `time_played` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `PlayCountEntity` (`id` INTEGER NOT NULL, `title` TEXT NOT NULL, `track_number` INTEGER NOT NULL, `year` TEXT, `duration` INTEGER NOT NULL, `data` TEXT NOT NULL, `date_modified` INTEGER NOT NULL, `album_id` INTEGER NOT NULL, `album_name` TEXT NOT NULL, `artist_id` INTEGER NOT NULL, `artist_name` TEXT NOT NULL, `composer` TEXT, `album_artist` TEXT, `time_played` INTEGER NOT NULL, `play_count` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'd79cd029d87214eccf85cc89d1ad8876')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `PlaylistEntity`");
        db.execSQL("DROP TABLE IF EXISTS `SongEntity`");
        db.execSQL("DROP TABLE IF EXISTS `HistoryEntity`");
        db.execSQL("DROP TABLE IF EXISTS `PlayCountEntity`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPlaylistEntity = new HashMap<String, TableInfo.Column>(2);
        _columnsPlaylistEntity.put("playlist_id", new TableInfo.Column("playlist_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylistEntity.put("playlist_name", new TableInfo.Column("playlist_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlaylistEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlaylistEntity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlaylistEntity = new TableInfo("PlaylistEntity", _columnsPlaylistEntity, _foreignKeysPlaylistEntity, _indicesPlaylistEntity);
        final TableInfo _existingPlaylistEntity = TableInfo.read(db, "PlaylistEntity");
        if (!_infoPlaylistEntity.equals(_existingPlaylistEntity)) {
          return new RoomOpenHelper.ValidationResult(false, "PlaylistEntity(code.name.monkey.retromusic.db.PlaylistEntity).\n"
                  + " Expected:\n" + _infoPlaylistEntity + "\n"
                  + " Found:\n" + _existingPlaylistEntity);
        }
        final HashMap<String, TableInfo.Column> _columnsSongEntity = new HashMap<String, TableInfo.Column>(15);
        _columnsSongEntity.put("song_key", new TableInfo.Column("song_key", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSongEntity.put("playlist_creator_id", new TableInfo.Column("playlist_creator_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSongEntity.put("id", new TableInfo.Column("id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSongEntity.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSongEntity.put("track_number", new TableInfo.Column("track_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSongEntity.put("year", new TableInfo.Column("year", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSongEntity.put("duration", new TableInfo.Column("duration", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSongEntity.put("data", new TableInfo.Column("data", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSongEntity.put("date_modified", new TableInfo.Column("date_modified", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSongEntity.put("album_id", new TableInfo.Column("album_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSongEntity.put("album_name", new TableInfo.Column("album_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSongEntity.put("artist_id", new TableInfo.Column("artist_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSongEntity.put("artist_name", new TableInfo.Column("artist_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSongEntity.put("composer", new TableInfo.Column("composer", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSongEntity.put("album_artist", new TableInfo.Column("album_artist", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSongEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSongEntity = new HashSet<TableInfo.Index>(1);
        _indicesSongEntity.add(new TableInfo.Index("index_SongEntity_playlist_creator_id_id", true, Arrays.asList("playlist_creator_id", "id"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoSongEntity = new TableInfo("SongEntity", _columnsSongEntity, _foreignKeysSongEntity, _indicesSongEntity);
        final TableInfo _existingSongEntity = TableInfo.read(db, "SongEntity");
        if (!_infoSongEntity.equals(_existingSongEntity)) {
          return new RoomOpenHelper.ValidationResult(false, "SongEntity(code.name.monkey.retromusic.db.SongEntity).\n"
                  + " Expected:\n" + _infoSongEntity + "\n"
                  + " Found:\n" + _existingSongEntity);
        }
        final HashMap<String, TableInfo.Column> _columnsHistoryEntity = new HashMap<String, TableInfo.Column>(14);
        _columnsHistoryEntity.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoryEntity.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoryEntity.put("track_number", new TableInfo.Column("track_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoryEntity.put("year", new TableInfo.Column("year", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoryEntity.put("duration", new TableInfo.Column("duration", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoryEntity.put("data", new TableInfo.Column("data", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoryEntity.put("date_modified", new TableInfo.Column("date_modified", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoryEntity.put("album_id", new TableInfo.Column("album_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoryEntity.put("album_name", new TableInfo.Column("album_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoryEntity.put("artist_id", new TableInfo.Column("artist_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoryEntity.put("artist_name", new TableInfo.Column("artist_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoryEntity.put("composer", new TableInfo.Column("composer", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoryEntity.put("album_artist", new TableInfo.Column("album_artist", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHistoryEntity.put("time_played", new TableInfo.Column("time_played", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHistoryEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHistoryEntity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHistoryEntity = new TableInfo("HistoryEntity", _columnsHistoryEntity, _foreignKeysHistoryEntity, _indicesHistoryEntity);
        final TableInfo _existingHistoryEntity = TableInfo.read(db, "HistoryEntity");
        if (!_infoHistoryEntity.equals(_existingHistoryEntity)) {
          return new RoomOpenHelper.ValidationResult(false, "HistoryEntity(code.name.monkey.retromusic.db.HistoryEntity).\n"
                  + " Expected:\n" + _infoHistoryEntity + "\n"
                  + " Found:\n" + _existingHistoryEntity);
        }
        final HashMap<String, TableInfo.Column> _columnsPlayCountEntity = new HashMap<String, TableInfo.Column>(15);
        _columnsPlayCountEntity.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayCountEntity.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayCountEntity.put("track_number", new TableInfo.Column("track_number", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayCountEntity.put("year", new TableInfo.Column("year", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayCountEntity.put("duration", new TableInfo.Column("duration", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayCountEntity.put("data", new TableInfo.Column("data", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayCountEntity.put("date_modified", new TableInfo.Column("date_modified", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayCountEntity.put("album_id", new TableInfo.Column("album_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayCountEntity.put("album_name", new TableInfo.Column("album_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayCountEntity.put("artist_id", new TableInfo.Column("artist_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayCountEntity.put("artist_name", new TableInfo.Column("artist_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayCountEntity.put("composer", new TableInfo.Column("composer", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayCountEntity.put("album_artist", new TableInfo.Column("album_artist", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayCountEntity.put("time_played", new TableInfo.Column("time_played", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayCountEntity.put("play_count", new TableInfo.Column("play_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlayCountEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlayCountEntity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlayCountEntity = new TableInfo("PlayCountEntity", _columnsPlayCountEntity, _foreignKeysPlayCountEntity, _indicesPlayCountEntity);
        final TableInfo _existingPlayCountEntity = TableInfo.read(db, "PlayCountEntity");
        if (!_infoPlayCountEntity.equals(_existingPlayCountEntity)) {
          return new RoomOpenHelper.ValidationResult(false, "PlayCountEntity(code.name.monkey.retromusic.db.PlayCountEntity).\n"
                  + " Expected:\n" + _infoPlayCountEntity + "\n"
                  + " Found:\n" + _existingPlayCountEntity);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "d79cd029d87214eccf85cc89d1ad8876", "6750de71bf6400f6892a4835f9197a47");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "PlaylistEntity","SongEntity","HistoryEntity","PlayCountEntity");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `PlaylistEntity`");
      _db.execSQL("DELETE FROM `SongEntity`");
      _db.execSQL("DELETE FROM `HistoryEntity`");
      _db.execSQL("DELETE FROM `PlayCountEntity`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(PlaylistDao.class, PlaylistDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PlayCountDao.class, PlayCountDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(HistoryDao.class, HistoryDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public PlaylistDao playlistDao() {
    if (_playlistDao != null) {
      return _playlistDao;
    } else {
      synchronized(this) {
        if(_playlistDao == null) {
          _playlistDao = new PlaylistDao_Impl(this);
        }
        return _playlistDao;
      }
    }
  }

  @Override
  public PlayCountDao playCountDao() {
    if (_playCountDao != null) {
      return _playCountDao;
    } else {
      synchronized(this) {
        if(_playCountDao == null) {
          _playCountDao = new PlayCountDao_Impl(this);
        }
        return _playCountDao;
      }
    }
  }

  @Override
  public HistoryDao historyDao() {
    if (_historyDao != null) {
      return _historyDao;
    } else {
      synchronized(this) {
        if(_historyDao == null) {
          _historyDao = new HistoryDao_Impl(this);
        }
        return _historyDao;
      }
    }
  }
}
