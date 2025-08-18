package com.chilluminati.rackedup.data.database.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.chilluminati.rackedup.data.database.converter.Converters;
import com.chilluminati.rackedup.data.database.entity.BodyMeasurement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BodyMeasurementDao_Impl implements BodyMeasurementDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BodyMeasurement> __insertionAdapterOfBodyMeasurement;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<BodyMeasurement> __deletionAdapterOfBodyMeasurement;

  private final EntityDeletionOrUpdateAdapter<BodyMeasurement> __updateAdapterOfBodyMeasurement;

  public BodyMeasurementDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBodyMeasurement = new EntityInsertionAdapter<BodyMeasurement>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `body_measurements` (`id`,`measurement_type`,`value`,`unit`,`body_part`,`measurement_method`,`notes`,`photo_url`,`measured_at`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BodyMeasurement entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getMeasurementType() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getMeasurementType());
        }
        statement.bindDouble(3, entity.getValue());
        if (entity.getUnit() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getUnit());
        }
        if (entity.getBodyPart() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getBodyPart());
        }
        if (entity.getMeasurementMethod() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getMeasurementMethod());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNotes());
        }
        if (entity.getPhotoUrl() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getPhotoUrl());
        }
        final Long _tmp = __converters.dateToTimestamp(entity.getMeasuredAt());
        if (_tmp == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, _tmp);
        }
        final Long _tmp_1 = __converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_1 == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, _tmp_1);
        }
      }
    };
    this.__deletionAdapterOfBodyMeasurement = new EntityDeletionOrUpdateAdapter<BodyMeasurement>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `body_measurements` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BodyMeasurement entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfBodyMeasurement = new EntityDeletionOrUpdateAdapter<BodyMeasurement>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `body_measurements` SET `id` = ?,`measurement_type` = ?,`value` = ?,`unit` = ?,`body_part` = ?,`measurement_method` = ?,`notes` = ?,`photo_url` = ?,`measured_at` = ?,`created_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BodyMeasurement entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getMeasurementType() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getMeasurementType());
        }
        statement.bindDouble(3, entity.getValue());
        if (entity.getUnit() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getUnit());
        }
        if (entity.getBodyPart() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getBodyPart());
        }
        if (entity.getMeasurementMethod() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getMeasurementMethod());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNotes());
        }
        if (entity.getPhotoUrl() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getPhotoUrl());
        }
        final Long _tmp = __converters.dateToTimestamp(entity.getMeasuredAt());
        if (_tmp == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, _tmp);
        }
        final Long _tmp_1 = __converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_1 == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, _tmp_1);
        }
        statement.bindLong(11, entity.getId());
      }
    };
  }

  @Override
  public Object insertBodyMeasurement(final BodyMeasurement bodyMeasurement,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBodyMeasurement.insertAndReturnId(bodyMeasurement);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteBodyMeasurement(final BodyMeasurement bodyMeasurement,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBodyMeasurement.handle(bodyMeasurement);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBodyMeasurement(final BodyMeasurement bodyMeasurement,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBodyMeasurement.handle(bodyMeasurement);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<BodyMeasurement>> getBodyMeasurements(final String type) {
    final String _sql = "SELECT * FROM body_measurements WHERE measurement_type = ? ORDER BY measured_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (type == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, type);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"body_measurements"}, new Callable<List<BodyMeasurement>>() {
      @Override
      @NonNull
      public List<BodyMeasurement> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMeasurementType = CursorUtil.getColumnIndexOrThrow(_cursor, "measurement_type");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfBodyPart = CursorUtil.getColumnIndexOrThrow(_cursor, "body_part");
          final int _cursorIndexOfMeasurementMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "measurement_method");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "photo_url");
          final int _cursorIndexOfMeasuredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "measured_at");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<BodyMeasurement> _result = new ArrayList<BodyMeasurement>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BodyMeasurement _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpMeasurementType;
            if (_cursor.isNull(_cursorIndexOfMeasurementType)) {
              _tmpMeasurementType = null;
            } else {
              _tmpMeasurementType = _cursor.getString(_cursorIndexOfMeasurementType);
            }
            final double _tmpValue;
            _tmpValue = _cursor.getDouble(_cursorIndexOfValue);
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            final String _tmpBodyPart;
            if (_cursor.isNull(_cursorIndexOfBodyPart)) {
              _tmpBodyPart = null;
            } else {
              _tmpBodyPart = _cursor.getString(_cursorIndexOfBodyPart);
            }
            final String _tmpMeasurementMethod;
            if (_cursor.isNull(_cursorIndexOfMeasurementMethod)) {
              _tmpMeasurementMethod = null;
            } else {
              _tmpMeasurementMethod = _cursor.getString(_cursorIndexOfMeasurementMethod);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final String _tmpPhotoUrl;
            if (_cursor.isNull(_cursorIndexOfPhotoUrl)) {
              _tmpPhotoUrl = null;
            } else {
              _tmpPhotoUrl = _cursor.getString(_cursorIndexOfPhotoUrl);
            }
            final Date _tmpMeasuredAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfMeasuredAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfMeasuredAt);
            }
            _tmpMeasuredAt = __converters.fromTimestamp(_tmp);
            final Date _tmpCreatedAt;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            _tmpCreatedAt = __converters.fromTimestamp(_tmp_1);
            _item = new BodyMeasurement(_tmpId,_tmpMeasurementType,_tmpValue,_tmpUnit,_tmpBodyPart,_tmpMeasurementMethod,_tmpNotes,_tmpPhotoUrl,_tmpMeasuredAt,_tmpCreatedAt);
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
