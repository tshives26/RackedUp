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
import com.chilluminati.rackedup.data.database.entity.ProgramExercise;
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
public final class ProgramExerciseDao_Impl implements ProgramExerciseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ProgramExercise> __insertionAdapterOfProgramExercise;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<ProgramExercise> __deletionAdapterOfProgramExercise;

  private final EntityDeletionOrUpdateAdapter<ProgramExercise> __updateAdapterOfProgramExercise;

  public ProgramExerciseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProgramExercise = new EntityInsertionAdapter<ProgramExercise>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `program_exercises` (`id`,`program_day_id`,`exercise_id`,`order_index`,`sets`,`reps`,`weight_percentage`,`rest_time_seconds`,`rpe_target`,`progression_scheme`,`progression_increment`,`is_superset`,`superset_id`,`notes`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProgramExercise entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProgramDayId());
        statement.bindLong(3, entity.getExerciseId());
        statement.bindLong(4, entity.getOrderIndex());
        if (entity.getSets() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSets());
        }
        if (entity.getReps() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getReps());
        }
        if (entity.getWeightPercentage() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getWeightPercentage());
        }
        if (entity.getRestTimeSeconds() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getRestTimeSeconds());
        }
        if (entity.getRpeTarget() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getRpeTarget());
        }
        if (entity.getProgressionScheme() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getProgressionScheme());
        }
        if (entity.getProgressionIncrement() == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.getProgressionIncrement());
        }
        final int _tmp = entity.isSuperset() ? 1 : 0;
        statement.bindLong(12, _tmp);
        if (entity.getSupersetId() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getSupersetId());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getNotes());
        }
        final Long _tmp_1 = __converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_1 == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, _tmp_1);
        }
      }
    };
    this.__deletionAdapterOfProgramExercise = new EntityDeletionOrUpdateAdapter<ProgramExercise>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `program_exercises` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProgramExercise entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfProgramExercise = new EntityDeletionOrUpdateAdapter<ProgramExercise>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `program_exercises` SET `id` = ?,`program_day_id` = ?,`exercise_id` = ?,`order_index` = ?,`sets` = ?,`reps` = ?,`weight_percentage` = ?,`rest_time_seconds` = ?,`rpe_target` = ?,`progression_scheme` = ?,`progression_increment` = ?,`is_superset` = ?,`superset_id` = ?,`notes` = ?,`created_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProgramExercise entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProgramDayId());
        statement.bindLong(3, entity.getExerciseId());
        statement.bindLong(4, entity.getOrderIndex());
        if (entity.getSets() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSets());
        }
        if (entity.getReps() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getReps());
        }
        if (entity.getWeightPercentage() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getWeightPercentage());
        }
        if (entity.getRestTimeSeconds() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getRestTimeSeconds());
        }
        if (entity.getRpeTarget() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getRpeTarget());
        }
        if (entity.getProgressionScheme() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getProgressionScheme());
        }
        if (entity.getProgressionIncrement() == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.getProgressionIncrement());
        }
        final int _tmp = entity.isSuperset() ? 1 : 0;
        statement.bindLong(12, _tmp);
        if (entity.getSupersetId() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getSupersetId());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getNotes());
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
  public Object insertProgramExercise(final ProgramExercise programExercise,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfProgramExercise.insertAndReturnId(programExercise);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteProgramExercise(final ProgramExercise programExercise,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfProgramExercise.handle(programExercise);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateProgramExercise(final ProgramExercise programExercise,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfProgramExercise.handle(programExercise);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ProgramExercise>> getProgramExercises(final long programDayId) {
    final String _sql = "SELECT * FROM program_exercises WHERE program_day_id = ? ORDER BY order_index ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, programDayId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"program_exercises"}, new Callable<List<ProgramExercise>>() {
      @Override
      @NonNull
      public List<ProgramExercise> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProgramDayId = CursorUtil.getColumnIndexOrThrow(_cursor, "program_day_id");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exercise_id");
          final int _cursorIndexOfOrderIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "order_index");
          final int _cursorIndexOfSets = CursorUtil.getColumnIndexOrThrow(_cursor, "sets");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfWeightPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "weight_percentage");
          final int _cursorIndexOfRestTimeSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "rest_time_seconds");
          final int _cursorIndexOfRpeTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "rpe_target");
          final int _cursorIndexOfProgressionScheme = CursorUtil.getColumnIndexOrThrow(_cursor, "progression_scheme");
          final int _cursorIndexOfProgressionIncrement = CursorUtil.getColumnIndexOrThrow(_cursor, "progression_increment");
          final int _cursorIndexOfIsSuperset = CursorUtil.getColumnIndexOrThrow(_cursor, "is_superset");
          final int _cursorIndexOfSupersetId = CursorUtil.getColumnIndexOrThrow(_cursor, "superset_id");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<ProgramExercise> _result = new ArrayList<ProgramExercise>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProgramExercise _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProgramDayId;
            _tmpProgramDayId = _cursor.getLong(_cursorIndexOfProgramDayId);
            final long _tmpExerciseId;
            _tmpExerciseId = _cursor.getLong(_cursorIndexOfExerciseId);
            final int _tmpOrderIndex;
            _tmpOrderIndex = _cursor.getInt(_cursorIndexOfOrderIndex);
            final String _tmpSets;
            if (_cursor.isNull(_cursorIndexOfSets)) {
              _tmpSets = null;
            } else {
              _tmpSets = _cursor.getString(_cursorIndexOfSets);
            }
            final String _tmpReps;
            if (_cursor.isNull(_cursorIndexOfReps)) {
              _tmpReps = null;
            } else {
              _tmpReps = _cursor.getString(_cursorIndexOfReps);
            }
            final Double _tmpWeightPercentage;
            if (_cursor.isNull(_cursorIndexOfWeightPercentage)) {
              _tmpWeightPercentage = null;
            } else {
              _tmpWeightPercentage = _cursor.getDouble(_cursorIndexOfWeightPercentage);
            }
            final Integer _tmpRestTimeSeconds;
            if (_cursor.isNull(_cursorIndexOfRestTimeSeconds)) {
              _tmpRestTimeSeconds = null;
            } else {
              _tmpRestTimeSeconds = _cursor.getInt(_cursorIndexOfRestTimeSeconds);
            }
            final Integer _tmpRpeTarget;
            if (_cursor.isNull(_cursorIndexOfRpeTarget)) {
              _tmpRpeTarget = null;
            } else {
              _tmpRpeTarget = _cursor.getInt(_cursorIndexOfRpeTarget);
            }
            final String _tmpProgressionScheme;
            if (_cursor.isNull(_cursorIndexOfProgressionScheme)) {
              _tmpProgressionScheme = null;
            } else {
              _tmpProgressionScheme = _cursor.getString(_cursorIndexOfProgressionScheme);
            }
            final Double _tmpProgressionIncrement;
            if (_cursor.isNull(_cursorIndexOfProgressionIncrement)) {
              _tmpProgressionIncrement = null;
            } else {
              _tmpProgressionIncrement = _cursor.getDouble(_cursorIndexOfProgressionIncrement);
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
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final Date _tmpCreatedAt;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            _tmpCreatedAt = __converters.fromTimestamp(_tmp_1);
            _item = new ProgramExercise(_tmpId,_tmpProgramDayId,_tmpExerciseId,_tmpOrderIndex,_tmpSets,_tmpReps,_tmpWeightPercentage,_tmpRestTimeSeconds,_tmpRpeTarget,_tmpProgressionScheme,_tmpProgressionIncrement,_tmpIsSuperset,_tmpSupersetId,_tmpNotes,_tmpCreatedAt);
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
