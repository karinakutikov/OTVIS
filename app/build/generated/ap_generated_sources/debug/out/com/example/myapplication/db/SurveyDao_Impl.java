package com.example.myapplication.db;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SurveyDao_Impl implements SurveyDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Survey> __insertionAdapterOfSurvey;

  private final EntityDeletionOrUpdateAdapter<Survey> __deletionAdapterOfSurvey;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllSurveys;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldSurveys;

  public SurveyDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSurvey = new EntityInsertionAdapter<Survey>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `survey` (`sid`,`survey_name`,`survey_date`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Survey value) {
        stmt.bindLong(1, value.sid);
        if (value.surveyID == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.surveyID);
        }
        if (value.surveyDate == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.surveyDate);
        }
      }
    };
    this.__deletionAdapterOfSurvey = new EntityDeletionOrUpdateAdapter<Survey>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `survey` WHERE `sid` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Survey value) {
        stmt.bindLong(1, value.sid);
      }
    };
    this.__preparedStmtOfDeleteAllSurveys = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM survey";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOldSurveys = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM survey WHERE survey_date < ?";
        return _query;
      }
    };
  }

  @Override
  public void insertSurvey(final Survey... survey) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSurvey.insert(survey);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteSurvey(final Survey survey) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfSurvey.handle(survey);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllSurveys() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllSurveys.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAllSurveys.release(_stmt);
    }
  }

  @Override
  public void deleteOldSurveys(final String tenDaysAgo) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldSurveys.acquire();
    int _argIndex = 1;
    if (tenDaysAgo == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, tenDaysAgo);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteOldSurveys.release(_stmt);
    }
  }

  @Override
  public List<Survey> getAllSurveys() {
    final String _sql = "SELECT * FROM survey";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfSid = CursorUtil.getColumnIndexOrThrow(_cursor, "sid");
      final int _cursorIndexOfSurveyID = CursorUtil.getColumnIndexOrThrow(_cursor, "survey_name");
      final int _cursorIndexOfSurveyDate = CursorUtil.getColumnIndexOrThrow(_cursor, "survey_date");
      final List<Survey> _result = new ArrayList<Survey>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Survey _item;
        _item = new Survey();
        _item.sid = _cursor.getInt(_cursorIndexOfSid);
        if (_cursor.isNull(_cursorIndexOfSurveyID)) {
          _item.surveyID = null;
        } else {
          _item.surveyID = _cursor.getString(_cursorIndexOfSurveyID);
        }
        if (_cursor.isNull(_cursorIndexOfSurveyDate)) {
          _item.surveyDate = null;
        } else {
          _item.surveyDate = _cursor.getString(_cursorIndexOfSurveyDate);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<String> getAllSurveyNames() {
    final String _sql = "SELECT survey_name FROM survey";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final String _item;
        if (_cursor.isNull(0)) {
          _item = null;
        } else {
          _item = _cursor.getString(0);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Survey find(final int sid) {
    final String _sql = "SELECT * FROM survey WHERE sid = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sid);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfSid = CursorUtil.getColumnIndexOrThrow(_cursor, "sid");
      final int _cursorIndexOfSurveyID = CursorUtil.getColumnIndexOrThrow(_cursor, "survey_name");
      final int _cursorIndexOfSurveyDate = CursorUtil.getColumnIndexOrThrow(_cursor, "survey_date");
      final Survey _result;
      if(_cursor.moveToFirst()) {
        _result = new Survey();
        _result.sid = _cursor.getInt(_cursorIndexOfSid);
        if (_cursor.isNull(_cursorIndexOfSurveyID)) {
          _result.surveyID = null;
        } else {
          _result.surveyID = _cursor.getString(_cursorIndexOfSurveyID);
        }
        if (_cursor.isNull(_cursorIndexOfSurveyDate)) {
          _result.surveyDate = null;
        } else {
          _result.surveyDate = _cursor.getString(_cursorIndexOfSurveyDate);
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

  @Override
  public Survey find(final String surveyName) {
    final String _sql = "SELECT * FROM survey WHERE survey_name = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (surveyName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, surveyName);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfSid = CursorUtil.getColumnIndexOrThrow(_cursor, "sid");
      final int _cursorIndexOfSurveyID = CursorUtil.getColumnIndexOrThrow(_cursor, "survey_name");
      final int _cursorIndexOfSurveyDate = CursorUtil.getColumnIndexOrThrow(_cursor, "survey_date");
      final Survey _result;
      if(_cursor.moveToFirst()) {
        _result = new Survey();
        _result.sid = _cursor.getInt(_cursorIndexOfSid);
        if (_cursor.isNull(_cursorIndexOfSurveyID)) {
          _result.surveyID = null;
        } else {
          _result.surveyID = _cursor.getString(_cursorIndexOfSurveyID);
        }
        if (_cursor.isNull(_cursorIndexOfSurveyDate)) {
          _result.surveyDate = null;
        } else {
          _result.surveyDate = _cursor.getString(_cursorIndexOfSurveyDate);
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
