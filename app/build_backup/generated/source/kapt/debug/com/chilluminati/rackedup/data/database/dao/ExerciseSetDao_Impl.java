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
import com.chilluminati.rackedup.data.database.entity.ExerciseSet;
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
public final class ExerciseSetDao_Impl implements ExerciseSetDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ExerciseSet> __insertionAdapterOfExerciseSet;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<ExerciseSet> __deletionAdapterOfExerciseSet;

  private final EntityDeletionOrUpdateAdapter<ExerciseSet> __updateAdapterOfExerciseSet;

  public ExerciseSetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExerciseSet = new EntityInsertionAdapter<ExerciseSet>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exercise_sets` (`id`,`workout_exercise_id`,`set_number`,`weight`,`reps`,`duration_seconds`,`distance`,`rest_time_seconds`,`rpe`,`is_warmup`,`is_dropset`,`is_failure`,`is_completed`,`notes`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExerciseSet entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getWorkoutExerciseId());
        statement.bindLong(3, entity.getSetNumber());
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
        if (entity.getDurationSeconds() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getDurationSeconds());
        }
        if (entity.getDistance() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getDistance());
        }
        if (entity.getRestTimeSeconds() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getRestTimeSeconds());
        }
        if (entity.getRpe() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getRpe());
        }
        final int _tmp = entity.isWarmup() ? 1 : 0;
        statement.bindLong(10, _tmp);
        final int _tmp_1 = entity.isDropset() ? 1 : 0;
        statement.bindLong(11, _tmp_1);
        final int _tmp_2 = entity.isFailure() ? 1 : 0;
        statement.bindLong(12, _tmp_2);
        final int _tmp_3 = entity.isCompleted() ? 1 : 0;
        statement.bindLong(13, _tmp_3);
        if (entity.getNotes() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getNotes());
        }
        final Long _tmp_4 = __converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_4 == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, _tmp_4);
        }
      }
    };
    this.__deletionAdapterOfExerciseSet = new EntityDeletionOrUpdateAdapter<ExerciseSet>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `exercise_sets` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExerciseSet entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfExerciseSet = new EntityDeletionOrUpdateAdapter<ExerciseSet>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `exercise_sets` SET `id` = ?,`workout_exercise_id` = ?,`set_number` = ?,`weight` = ?,`reps` = ?,`duration_seconds` = ?,`distance` = ?,`rest_time_seconds` = ?,`rpe` = ?,`is_warmup` = ?,`is_dropset` = ?,`is_failure` = ?,`is_completed` = ?,`notes` = ?,`created_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExerciseSet entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getWorkoutExerciseId());
        statement.bindLong(3, entity.getSetNumber());
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
        if (entity.getDurationSeconds() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getDurationSeconds());
        }
        if (entity.getDistance() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getDistance());
        }
        if (entity.getRestTimeSeconds() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getRestTimeSeconds());
        }
        if (entity.getRpe() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getRpe());
        }
        final int _tmp = entity.isWarmup() ? 1 : 0;
        statement.bindLong(10, _tmp);
        final int _tmp_1 = entity.isDropset() ? 1 : 0;
        statement.bindLong(11, _tmp_1);
        final int _tmp_2 = entity.isFailure() ? 1 : 0;
        statement.bindLong(12, _tmp_2);
        final int _tmp_3 = entity.isCompleted() ? 1 : 0;
        statement.bindLong(13, _tmp_3);
        if (entity.getNotes() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getNotes());
        }
        final Long _tmp_4 = __converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_4 == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, _tmp_4);
        }
        statement.bindLong(16, entity.getId());
      }
    };
  }

  @Override
  public Object insertExerciseSet(final ExerciseSet exerciseSet,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfExerciseSet.insertAndReturnId(exerciseSet);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteExerciseSet(final ExerciseSet exerciseSet,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfExerciseSet.handle(exerciseSet);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateExerciseSet(final ExerciseSet exerciseSet,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfExerciseSet.handle(exerciseSet);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ExerciseSet>> getExerciseSets(final long workoutExerciseId) {
    final String _sql = "SELECT * FROM exercise_sets WHERE workout_exercise_id = ? ORDER BY set_number ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, workoutExerciseId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exercise_sets"}, new Callable<List<ExerciseSet>>() {
      @Override
      @NonNull
      public List<ExerciseSet> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWorkoutExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "workout_exercise_id");
          final int _cursorIndexOfSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "set_number");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_seconds");
          final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
          final int _cursorIndexOfRestTimeSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "rest_time_seconds");
          final int _cursorIndexOfRpe = CursorUtil.getColumnIndexOrThrow(_cursor, "rpe");
          final int _cursorIndexOfIsWarmup = CursorUtil.getColumnIndexOrThrow(_cursor, "is_warmup");
          final int _cursorIndexOfIsDropset = CursorUtil.getColumnIndexOrThrow(_cursor, "is_dropset");
          final int _cursorIndexOfIsFailure = CursorUtil.getColumnIndexOrThrow(_cursor, "is_failure");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_completed");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<ExerciseSet> _result = new ArrayList<ExerciseSet>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExerciseSet _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpWorkoutExerciseId;
            _tmpWorkoutExerciseId = _cursor.getLong(_cursorIndexOfWorkoutExerciseId);
            final int _tmpSetNumber;
            _tmpSetNumber = _cursor.getInt(_cursorIndexOfSetNumber);
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
            final Integer _tmpDurationSeconds;
            if (_cursor.isNull(_cursorIndexOfDurationSeconds)) {
              _tmpDurationSeconds = null;
            } else {
              _tmpDurationSeconds = _cursor.getInt(_cursorIndexOfDurationSeconds);
            }
            final Double _tmpDistance;
            if (_cursor.isNull(_cursorIndexOfDistance)) {
              _tmpDistance = null;
            } else {
              _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
            }
            final Integer _tmpRestTimeSeconds;
            if (_cursor.isNull(_cursorIndexOfRestTimeSeconds)) {
              _tmpRestTimeSeconds = null;
            } else {
              _tmpRestTimeSeconds = _cursor.getInt(_cursorIndexOfRestTimeSeconds);
            }
            final Integer _tmpRpe;
            if (_cursor.isNull(_cursorIndexOfRpe)) {
              _tmpRpe = null;
            } else {
              _tmpRpe = _cursor.getInt(_cursorIndexOfRpe);
            }
            final boolean _tmpIsWarmup;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWarmup);
            _tmpIsWarmup = _tmp != 0;
            final boolean _tmpIsDropset;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDropset);
            _tmpIsDropset = _tmp_1 != 0;
            final boolean _tmpIsFailure;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsFailure);
            _tmpIsFailure = _tmp_2 != 0;
            final boolean _tmpIsCompleted;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp_3 != 0;
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final Date _tmpCreatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            _tmpCreatedAt = __converters.fromTimestamp(_tmp_4);
            _item = new ExerciseSet(_tmpId,_tmpWorkoutExerciseId,_tmpSetNumber,_tmpWeight,_tmpReps,_tmpDurationSeconds,_tmpDistance,_tmpRestTimeSeconds,_tmpRpe,_tmpIsWarmup,_tmpIsDropset,_tmpIsFailure,_tmpIsCompleted,_tmpNotes,_tmpCreatedAt);
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
