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
import com.chilluminati.rackedup.data.database.entity.PersonalRecord;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
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
public final class PersonalRecordDao_Impl implements PersonalRecordDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PersonalRecord> __insertionAdapterOfPersonalRecord;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<PersonalRecord> __deletionAdapterOfPersonalRecord;

  private final EntityDeletionOrUpdateAdapter<PersonalRecord> __updateAdapterOfPersonalRecord;

  public PersonalRecordDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPersonalRecord = new EntityInsertionAdapter<PersonalRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `personal_records` (`id`,`exercise_id`,`record_type`,`weight`,`reps`,`distance`,`duration_seconds`,`volume`,`estimated_1rm`,`previous_value`,`improvement`,`workout_id`,`notes`,`achieved_at`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PersonalRecord entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getExerciseId());
        if (entity.getRecordType() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getRecordType());
        }
        if (entity.getWeight() == null) {
          statement.bindNull(4);
        } else {
          statement.bindDouble(4, entity.getWeight());
        }
        if (entity.getReps() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getReps());
        }
        if (entity.getDistance() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getDistance());
        }
        if (entity.getDurationSeconds() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getDurationSeconds());
        }
        if (entity.getVolume() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getVolume());
        }
        if (entity.getEstimated1RM() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getEstimated1RM());
        }
        if (entity.getPreviousValue() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getPreviousValue());
        }
        if (entity.getImprovement() == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.getImprovement());
        }
        if (entity.getWorkoutId() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getWorkoutId());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getNotes());
        }
        final Long _tmp = __converters.dateToTimestamp(entity.getAchievedAt());
        if (_tmp == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, _tmp);
        }
        final Long _tmp_1 = __converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_1 == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, _tmp_1);
        }
      }
    };
    this.__deletionAdapterOfPersonalRecord = new EntityDeletionOrUpdateAdapter<PersonalRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `personal_records` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PersonalRecord entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPersonalRecord = new EntityDeletionOrUpdateAdapter<PersonalRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `personal_records` SET `id` = ?,`exercise_id` = ?,`record_type` = ?,`weight` = ?,`reps` = ?,`distance` = ?,`duration_seconds` = ?,`volume` = ?,`estimated_1rm` = ?,`previous_value` = ?,`improvement` = ?,`workout_id` = ?,`notes` = ?,`achieved_at` = ?,`created_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PersonalRecord entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getExerciseId());
        if (entity.getRecordType() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getRecordType());
        }
        if (entity.getWeight() == null) {
          statement.bindNull(4);
        } else {
          statement.bindDouble(4, entity.getWeight());
        }
        if (entity.getReps() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getReps());
        }
        if (entity.getDistance() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getDistance());
        }
        if (entity.getDurationSeconds() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getDurationSeconds());
        }
        if (entity.getVolume() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getVolume());
        }
        if (entity.getEstimated1RM() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getEstimated1RM());
        }
        if (entity.getPreviousValue() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getPreviousValue());
        }
        if (entity.getImprovement() == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.getImprovement());
        }
        if (entity.getWorkoutId() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getWorkoutId());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getNotes());
        }
        final Long _tmp = __converters.dateToTimestamp(entity.getAchievedAt());
        if (_tmp == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, _tmp);
        }
        final Long _tmp_1 = __converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_1 == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, _tmp_1);
        }
        statement.bindLong(16, entity.getId());
      }
    };
  }

  @Override
  public Object insertPersonalRecord(final PersonalRecord personalRecord,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPersonalRecord.insertAndReturnId(personalRecord);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePersonalRecord(final PersonalRecord personalRecord,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPersonalRecord.handle(personalRecord);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePersonalRecord(final PersonalRecord personalRecord,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPersonalRecord.handle(personalRecord);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PersonalRecord>> getPersonalRecords(final long exerciseId) {
    final String _sql = "SELECT * FROM personal_records WHERE exercise_id = ? ORDER BY achieved_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, exerciseId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"personal_records"}, new Callable<List<PersonalRecord>>() {
      @Override
      @NonNull
      public List<PersonalRecord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exercise_id");
          final int _cursorIndexOfRecordType = CursorUtil.getColumnIndexOrThrow(_cursor, "record_type");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_seconds");
          final int _cursorIndexOfVolume = CursorUtil.getColumnIndexOrThrow(_cursor, "volume");
          final int _cursorIndexOfEstimated1RM = CursorUtil.getColumnIndexOrThrow(_cursor, "estimated_1rm");
          final int _cursorIndexOfPreviousValue = CursorUtil.getColumnIndexOrThrow(_cursor, "previous_value");
          final int _cursorIndexOfImprovement = CursorUtil.getColumnIndexOrThrow(_cursor, "improvement");
          final int _cursorIndexOfWorkoutId = CursorUtil.getColumnIndexOrThrow(_cursor, "workout_id");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfAchievedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "achieved_at");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<PersonalRecord> _result = new ArrayList<PersonalRecord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PersonalRecord _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpExerciseId;
            _tmpExerciseId = _cursor.getLong(_cursorIndexOfExerciseId);
            final String _tmpRecordType;
            if (_cursor.isNull(_cursorIndexOfRecordType)) {
              _tmpRecordType = null;
            } else {
              _tmpRecordType = _cursor.getString(_cursorIndexOfRecordType);
            }
            final Double _tmpWeight;
            if (_cursor.isNull(_cursorIndexOfWeight)) {
              _tmpWeight = null;
            } else {
              _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            }
            final Integer _tmpReps;
            if (_cursor.isNull(_cursorIndexOfReps)) {
              _tmpReps = null;
            } else {
              _tmpReps = _cursor.getInt(_cursorIndexOfReps);
            }
            final Double _tmpDistance;
            if (_cursor.isNull(_cursorIndexOfDistance)) {
              _tmpDistance = null;
            } else {
              _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
            }
            final Integer _tmpDurationSeconds;
            if (_cursor.isNull(_cursorIndexOfDurationSeconds)) {
              _tmpDurationSeconds = null;
            } else {
              _tmpDurationSeconds = _cursor.getInt(_cursorIndexOfDurationSeconds);
            }
            final Double _tmpVolume;
            if (_cursor.isNull(_cursorIndexOfVolume)) {
              _tmpVolume = null;
            } else {
              _tmpVolume = _cursor.getDouble(_cursorIndexOfVolume);
            }
            final Double _tmpEstimated1RM;
            if (_cursor.isNull(_cursorIndexOfEstimated1RM)) {
              _tmpEstimated1RM = null;
            } else {
              _tmpEstimated1RM = _cursor.getDouble(_cursorIndexOfEstimated1RM);
            }
            final Double _tmpPreviousValue;
            if (_cursor.isNull(_cursorIndexOfPreviousValue)) {
              _tmpPreviousValue = null;
            } else {
              _tmpPreviousValue = _cursor.getDouble(_cursorIndexOfPreviousValue);
            }
            final Double _tmpImprovement;
            if (_cursor.isNull(_cursorIndexOfImprovement)) {
              _tmpImprovement = null;
            } else {
              _tmpImprovement = _cursor.getDouble(_cursorIndexOfImprovement);
            }
            final Long _tmpWorkoutId;
            if (_cursor.isNull(_cursorIndexOfWorkoutId)) {
              _tmpWorkoutId = null;
            } else {
              _tmpWorkoutId = _cursor.getLong(_cursorIndexOfWorkoutId);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final Date _tmpAchievedAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfAchievedAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfAchievedAt);
            }
            _tmpAchievedAt = __converters.fromTimestamp(_tmp);
            final Date _tmpCreatedAt;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            _tmpCreatedAt = __converters.fromTimestamp(_tmp_1);
            _item = new PersonalRecord(_tmpId,_tmpExerciseId,_tmpRecordType,_tmpWeight,_tmpReps,_tmpDistance,_tmpDurationSeconds,_tmpVolume,_tmpEstimated1RM,_tmpPreviousValue,_tmpImprovement,_tmpWorkoutId,_tmpNotes,_tmpAchievedAt,_tmpCreatedAt);
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
