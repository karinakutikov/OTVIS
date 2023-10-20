package com.example.myapplication.db;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TreeDao_Impl implements TreeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Tree> __insertionAdapterOfTree;

  private final EntityDeletionOrUpdateAdapter<Tree> __deletionAdapterOfTree;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllTrees;

  public TreeDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTree = new EntityInsertionAdapter<Tree>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `tree` (`uid`,`id_num`,`latitude_num`,`Longitude_num`,`diameter_num`,`species_info`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Tree value) {
        stmt.bindLong(1, value.uid);
        if (value.idNum == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.idNum);
        }
        if (value.latitudeNum == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.latitudeNum);
        }
        if (value.longitudeNum == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.longitudeNum);
        }
        if (value.diameterNum == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.diameterNum);
        }
        if (value.speciesInfo == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.speciesInfo);
        }
      }
    };
    this.__deletionAdapterOfTree = new EntityDeletionOrUpdateAdapter<Tree>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `tree` WHERE `uid` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Tree value) {
        stmt.bindLong(1, value.uid);
      }
    };
    this.__preparedStmtOfDeleteAllTrees = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM tree";
        return _query;
      }
    };
  }

  @Override
  public void insertTree(final Tree... trees) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTree.insert(trees);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteTree(final Tree tree) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfTree.handle(tree);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllTrees() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllTrees.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAllTrees.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Tree>> getAllTrees() {
    final String _sql = "SELECT * FROM tree";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"tree"}, false, new Callable<List<Tree>>() {
      @Override
      public List<Tree> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
          final int _cursorIndexOfIdNum = CursorUtil.getColumnIndexOrThrow(_cursor, "id_num");
          final int _cursorIndexOfLatitudeNum = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude_num");
          final int _cursorIndexOfLongitudeNum = CursorUtil.getColumnIndexOrThrow(_cursor, "Longitude_num");
          final int _cursorIndexOfDiameterNum = CursorUtil.getColumnIndexOrThrow(_cursor, "diameter_num");
          final int _cursorIndexOfSpeciesInfo = CursorUtil.getColumnIndexOrThrow(_cursor, "species_info");
          final List<Tree> _result = new ArrayList<Tree>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Tree _item;
            _item = new Tree();
            _item.uid = _cursor.getInt(_cursorIndexOfUid);
            if (_cursor.isNull(_cursorIndexOfIdNum)) {
              _item.idNum = null;
            } else {
              _item.idNum = _cursor.getString(_cursorIndexOfIdNum);
            }
            if (_cursor.isNull(_cursorIndexOfLatitudeNum)) {
              _item.latitudeNum = null;
            } else {
              _item.latitudeNum = _cursor.getString(_cursorIndexOfLatitudeNum);
            }
            if (_cursor.isNull(_cursorIndexOfLongitudeNum)) {
              _item.longitudeNum = null;
            } else {
              _item.longitudeNum = _cursor.getString(_cursorIndexOfLongitudeNum);
            }
            if (_cursor.isNull(_cursorIndexOfDiameterNum)) {
              _item.diameterNum = null;
            } else {
              _item.diameterNum = _cursor.getString(_cursorIndexOfDiameterNum);
            }
            if (_cursor.isNull(_cursorIndexOfSpeciesInfo)) {
              _item.speciesInfo = null;
            } else {
              _item.speciesInfo = _cursor.getString(_cursorIndexOfSpeciesInfo);
            }
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
  public Tree find(final int id) {
    final String _sql = "SELECT * FROM tree WHERE id_num = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUid = CursorUtil.getColumnIndexOrThrow(_cursor, "uid");
      final int _cursorIndexOfIdNum = CursorUtil.getColumnIndexOrThrow(_cursor, "id_num");
      final int _cursorIndexOfLatitudeNum = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude_num");
      final int _cursorIndexOfLongitudeNum = CursorUtil.getColumnIndexOrThrow(_cursor, "Longitude_num");
      final int _cursorIndexOfDiameterNum = CursorUtil.getColumnIndexOrThrow(_cursor, "diameter_num");
      final int _cursorIndexOfSpeciesInfo = CursorUtil.getColumnIndexOrThrow(_cursor, "species_info");
      final Tree _result;
      if(_cursor.moveToFirst()) {
        _result = new Tree();
        _result.uid = _cursor.getInt(_cursorIndexOfUid);
        if (_cursor.isNull(_cursorIndexOfIdNum)) {
          _result.idNum = null;
        } else {
          _result.idNum = _cursor.getString(_cursorIndexOfIdNum);
        }
        if (_cursor.isNull(_cursorIndexOfLatitudeNum)) {
          _result.latitudeNum = null;
        } else {
          _result.latitudeNum = _cursor.getString(_cursorIndexOfLatitudeNum);
        }
        if (_cursor.isNull(_cursorIndexOfLongitudeNum)) {
          _result.longitudeNum = null;
        } else {
          _result.longitudeNum = _cursor.getString(_cursorIndexOfLongitudeNum);
        }
        if (_cursor.isNull(_cursorIndexOfDiameterNum)) {
          _result.diameterNum = null;
        } else {
          _result.diameterNum = _cursor.getString(_cursorIndexOfDiameterNum);
        }
        if (_cursor.isNull(_cursorIndexOfSpeciesInfo)) {
          _result.speciesInfo = null;
        } else {
          _result.speciesInfo = _cursor.getString(_cursorIndexOfSpeciesInfo);
        }
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
