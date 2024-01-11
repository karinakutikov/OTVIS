package com.example.myapplication.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile TreeDao _treeDao;

  private volatile SurveyDao _surveyDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(5) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `tree` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `survey_id` INTEGER NOT NULL, `id_num` TEXT, `latitude_num` TEXT, `Longitude_num` TEXT, `diameter_num` TEXT, `species_info` TEXT, FOREIGN KEY(`survey_id`) REFERENCES `survey`(`sid`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE INDEX IF NOT EXISTS `index_tree_survey_id` ON `tree` (`survey_id`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `survey` (`sid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `survey_name` TEXT, `survey_date` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'd958ac74b435dc6ee975a3b02ac1e8e5')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `tree`");
        _db.execSQL("DROP TABLE IF EXISTS `survey`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      public void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        _db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsTree = new HashMap<String, TableInfo.Column>(7);
        _columnsTree.put("uid", new TableInfo.Column("uid", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTree.put("survey_id", new TableInfo.Column("survey_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTree.put("id_num", new TableInfo.Column("id_num", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTree.put("latitude_num", new TableInfo.Column("latitude_num", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTree.put("Longitude_num", new TableInfo.Column("Longitude_num", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTree.put("diameter_num", new TableInfo.Column("diameter_num", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTree.put("species_info", new TableInfo.Column("species_info", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTree = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysTree.add(new TableInfo.ForeignKey("survey", "CASCADE", "NO ACTION",Arrays.asList("survey_id"), Arrays.asList("sid")));
        final HashSet<TableInfo.Index> _indicesTree = new HashSet<TableInfo.Index>(1);
        _indicesTree.add(new TableInfo.Index("index_tree_survey_id", false, Arrays.asList("survey_id"), Arrays.asList("ASC")));
        final TableInfo _infoTree = new TableInfo("tree", _columnsTree, _foreignKeysTree, _indicesTree);
        final TableInfo _existingTree = TableInfo.read(_db, "tree");
        if (! _infoTree.equals(_existingTree)) {
          return new RoomOpenHelper.ValidationResult(false, "tree(com.example.myapplication.db.Tree).\n"
                  + " Expected:\n" + _infoTree + "\n"
                  + " Found:\n" + _existingTree);
        }
        final HashMap<String, TableInfo.Column> _columnsSurvey = new HashMap<String, TableInfo.Column>(3);
        _columnsSurvey.put("sid", new TableInfo.Column("sid", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSurvey.put("survey_name", new TableInfo.Column("survey_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSurvey.put("survey_date", new TableInfo.Column("survey_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSurvey = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSurvey = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSurvey = new TableInfo("survey", _columnsSurvey, _foreignKeysSurvey, _indicesSurvey);
        final TableInfo _existingSurvey = TableInfo.read(_db, "survey");
        if (! _infoSurvey.equals(_existingSurvey)) {
          return new RoomOpenHelper.ValidationResult(false, "survey(com.example.myapplication.db.Survey).\n"
                  + " Expected:\n" + _infoSurvey + "\n"
                  + " Found:\n" + _existingSurvey);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "d958ac74b435dc6ee975a3b02ac1e8e5", "c52e12055de65941cf651c08ab89338e");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "tree","survey");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `tree`");
      _db.execSQL("DELETE FROM `survey`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(TreeDao.class, TreeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SurveyDao.class, SurveyDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public TreeDao treeDao() {
    if (_treeDao != null) {
      return _treeDao;
    } else {
      synchronized(this) {
        if(_treeDao == null) {
          _treeDao = new TreeDao_Impl(this);
        }
        return _treeDao;
      }
    }
  }

  @Override
  public SurveyDao surveyDao() {
    if (_surveyDao != null) {
      return _surveyDao;
    } else {
      synchronized(this) {
        if(_surveyDao == null) {
          _surveyDao = new SurveyDao_Impl(this);
        }
        return _surveyDao;
      }
    }
  }
}
