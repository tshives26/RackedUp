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
import com.chilluminati.rackedup.data.database.entity.WorkoutExercise;
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
public final class WorkoutExerciseDao_Impl implements WorkoutExerciseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WorkoutExercise> __insertionAdapterOfWorkoutExercise;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<WorkoutExercise> __deletionAdapterOfWorkoutExercise;

  private final EntityDeletionOrUpdateAdapter<WorkoutExercise> __updateAdapterOfWorkoutExercise;

  public WorkoutExerciseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWorkoutExercise = new EntityInsertionAdapter<WorkoutExercise>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `workout_exercises` (`id`,`workout_id`,`exercise_id`,`order_index`,`rest_time_seconds`,`notes`,`is_superset`,`superset_id`,`is_dropset`,`target_sets`,`target_reps`,`target_weight`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WorkoutExercise entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getWorkoutId());
        statement.bindLong(3, entity.getExerciseId());
        statement.bindLong(4, entity.getOrderIndex());
        if (entity.getRestTimeSeconds() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getRestTimeSeconds());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getNotes());
        }
        final int _tmp = entity.isSuperset() ? 1 : 0;
        statement.bindLong(7, _tmp);
        if (entity.getSupersetId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getSupersetId());
        }
        final int _tmp_1 = entity.isDropset() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        if (entity.getTargetSets() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getTargetSets());
        }
        if (entity.getTargetReps() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getTargetReps());
        }
        if (entity.getTargetWeight() == null) {
          statement.bindNull(12);
        } else {
          statement.bindDouble(12, entity.getTargetWeight());
        }
        final Long _tmp_2 = __converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_2 == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, _tmp_2);
        }
      }
    };
    this.__deletionAdapterOfWorkoutExercise = new EntityDeletionOrUpdateAdapter<WorkoutExercise>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `workout_exercises` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WorkoutExercise entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfWorkoutExercise = new EntityDeletionOrUpdateAdapter<WorkoutExercise>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `workout_exercises` SET `id` = ?,`workout_id` = ?,`exercise_id` = ?,`order_index` = ?,`rest_time_seconds` = ?,`notes` = ?,`is_superset` = ?,`superset_id` = ?,`is_dropset` = ?,`target_sets` = ?,`target_reps` = ?,`target_weight` = ?,`created_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WorkoutExercise entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getWorkoutId());
        statement.bindLong(3, entity.getExerciseId());
        statement.bindLong(4, entity.getOrderIndex());
        if (entity.getRestTimeSeconds() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getRestTimeSeconds());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getNotes());
        }
        final int _tmp = entity.isSuperset() ? 1 : 0;
        statement.bindLong(7, _tmp);
        if (entity.getSupersetId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getSupersetId());
        }
        final int _tmp_1 = entity.isDropset() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        if (entity.getTargetSets() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getTargetSets());
        }
        if (entity.getTargetReps() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getTargetReps());
        }
        if (entity.getTargetWeight() == null) {
          statement.bindNull(12);
        } else {
          statement.bindDouble(12, entity.getTargetWeight());
        }
        final Long _tmp_2 = __converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_2 == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, _tmp_2);
        }
        statement.bindLong(14, entity.getId());
      }
    };
  }

  @Override
  public Object insertWorkoutExercise(final WorkoutExercise workoutExercise,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfWorkoutExercise.insertAndReturnId(workoutExercise);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteWorkoutExercise(final WorkoutExercise workoutExercise,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfWorkoutExercise.handle(workoutExercise);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateWorkoutExercise(final WorkoutExercise workoutExercise,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfWorkoutExercise.handle(workoutExercise);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<WorkoutExercise>> getWorkoutExercises(final long workoutId) {
    final String _sql = "SELECT * FROM workout_exercises WHERE workout_id = ? ORDER BY order_index ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, workoutId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"workout_exercises"}, new Callable<List<WorkoutExercise>>() {
      @Override
      @NonNull
      public List<WorkoutExercise> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWorkoutId = CursorUtil.getColumnIndexOrThrow(_cursor, "workout_id");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exercise_id");
          final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "order_index");
          final int _cursorIndexOfRestTimeSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "rest_time_seconds");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfIsSuperset = CursorUtil.getColumnIndexOrThrow(_cursor, "is_superset");
          final int _cursorIndexOfSupersetId = CursorUtil.getColumnIndexOrThrow(_cursor, "superset_id");
          final int _cursorIndexOfIsDropset = CursorUtil.getColumnIndexOrThrow(_cursor, "is_dropset");
          final int _cursorIndexOfTargetSets = CursorUtil.getColumnIndexOrThrow(_cursor, "target_sets");
          final int _cursorIndexOfTargetReps = CursorUtil.getColumnIndexOrThrow(_cursor, "target_reps");
          final int _cursorIndexOfTargetWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "target_weight");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<WorkoutExercise> _result = new ArrayList<WorkoutExercise>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WorkoutExercise _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpWorkoutId;
            _tmpWorkoutId = _cursor.getLong(_cursorIndexOfWorkoutId);
            final long _tmpExerciseId;
            _tmpExerciseId = _cursor.getLong(_cursorIndexOfExerciseId);
            final int _tmpOrderIndex;
            _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
            final Integer _tmpRestTimeSeconds;
            if (_cursor.isNull(_cursorIndexOfRestTimeSeconds)) {
              _tmpRestTimeSeconds = null;
            } else {
              _tmpRestTimeSeconds = _cursor.getInt(_cursorIndexOfRestTimeSeconds);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final boolean _tmpIsSuperset;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSuperset);
            _tmpIsSuperset = _tmp != 0;
            final String _tmpSupersetId;
            if (_cursor.isNull(_cursorIndexOfSupersetId)) {
              _tmpSupersetId = null;
            } else {
              _tmpSupersetId = _cursor.getString(_cursorIndexOfSupersetId);
            }
            final boolean _tmpIsDropset;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDropset);
            _tmpIsDropset = _tmp_1 != 0;
            final Integer _tmpTargetSets;
            if (_cursor.isNull(_cursorIndexOfTargetSets)) {
              _tmpTargetSets = null;
            } else {
              _tmpTargetSets = _cursor.getInt(_cursorIndexOfTargetSets);
            }
            final Integer _tmpTargetReps;
            if (_cursor.isNull(_cursorIndexOfTargetReps)) {
              _tmpTargetReps = null;
            } else {
              _tmpTargetReps = _cursor.getInt(_cursorIndexOfTargetReps);
            }
            final Double _tmpTargetWeight;
            if (_cursor.isNull(_cursorIndexOfTargetWeight)) {
              _tmpTargetWeight = null;
            } else {
              _tmpTargetWeight = _cursor.getDouble(_cursorIndexOfTargetWeight);
            }
            final Date _tmpCreatedAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            _tmpCreatedAt = __converters.fromTimestamp(_tmp_2);
            _item = new WorkoutExercise(_tmpId,_tmpWorkoutId,_tmpExerciseId,_tmpOrderIndex,_tmpRestTimeSeconds,_tmpNotes,_tmpIsSuperset,_tmpSupersetId,_tmpIsDropset,_tmpTargetSets,_tmpTargetReps,_tmpTargetWeight,_tmpCreatedAt);
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
