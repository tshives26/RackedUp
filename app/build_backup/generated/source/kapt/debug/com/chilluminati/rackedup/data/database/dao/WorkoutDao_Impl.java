package com.chilluminati.rackedup.data.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.chilluminati.rackedup.data.database.converter.Converters;
import com.chilluminati.rackedup.data.database.entity.Workout;
import java.lang.Class;
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
public final class WorkoutDao_Impl implements WorkoutDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Workout> __insertionAdapterOfWorkout;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<Workout> __deletionAdapterOfWorkout;

  private final EntityDeletionOrUpdateAdapter<Workout> __updateAdapterOfWorkout;

  public WorkoutDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWorkout = new EntityInsertionAdapter<Workout>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `workouts` (`id`,`name`,`date`,`start_time`,`end_time`,`duration_minutes`,`notes`,`total_volume`,`total_sets`,`total_reps`,`program_id`,`program_day_id`,`is_template`,`is_completed`,`is_favorite`,`rating`,`created_at`,`updated_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Workout entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        final Long _tmp = __converters.dateToTimestamp(entity.getDate());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, _tmp);
        }
        final Long _tmp_1 = __converters.dateToTimestamp(entity.getStartTime());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp_1);
        }
        final Long _tmp_2 = __converters.dateToTimestamp(entity.getEndTime());
        if (_tmp_2 == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp_2);
        }
        if (entity.getDurationMinutes() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getDurationMinutes());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNotes());
        }
        statement.bindDouble(8, entity.getTotalVolume());
        statement.bindLong(9, entity.getTotalSets());
        statement.bindLong(10, entity.getTotalReps());
        if (entity.getProgramId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getProgramId());
        }
        if (entity.getProgramDayId() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getProgramDayId());
        }
        final int _tmp_3 = entity.isTemplate() ? 1 : 0;
        statement.bindLong(13, _tmp_3);
        final int _tmp_4 = entity.isCompleted() ? 1 : 0;
        statement.bindLong(14, _tmp_4);
        final int _tmp_5 = entity.isFavorite() ? 1 : 0;
        statement.bindLong(15, _tmp_5);
        if (entity.getRating() == null) {
          statement.bindNull(16);
        } else {
          statement.bindLong(16, entity.getRating());
        }
        final Long _tmp_6 = __converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_6 == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, _tmp_6);
        }
        final Long _tmp_7 = __converters.dateToTimestamp(entity.getUpdatedAt());
        if (_tmp_7 == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, _tmp_7);
        }
      }
    };
    this.__deletionAdapterOfWorkout = new EntityDeletionOrUpdateAdapter<Workout>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `workouts` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Workout entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfWorkout = new EntityDeletionOrUpdateAdapter<Workout>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `workouts` SET `id` = ?,`name` = ?,`date` = ?,`start_time` = ?,`end_time` = ?,`duration_minutes` = ?,`notes` = ?,`total_volume` = ?,`total_sets` = ?,`total_reps` = ?,`program_id` = ?,`program_day_id` = ?,`is_template` = ?,`is_completed` = ?,`is_favorite` = ?,`rating` = ?,`created_at` = ?,`updated_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Workout entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        final Long _tmp = __converters.dateToTimestamp(entity.getDate());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, _tmp);
        }
        final Long _tmp_1 = __converters.dateToTimestamp(entity.getStartTime());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp_1);
        }
        final Long _tmp_2 = __converters.dateToTimestamp(entity.getEndTime());
        if (_tmp_2 == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp_2);
        }
        if (entity.getDurationMinutes() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getDurationMinutes());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNotes());
        }
        statement.bindDouble(8, entity.getTotalVolume());
        statement.bindLong(9, entity.getTotalSets());
        statement.bindLong(10, entity.getTotalReps());
        if (entity.getProgramId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getProgramId());
        }
        if (entity.getProgramDayId() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getProgramDayId());
        }
        final int _tmp_3 = entity.isTemplate() ? 1 : 0;
        statement.bindLong(13, _tmp_3);
        final int _tmp_4 = entity.isCompleted() ? 1 : 0;
        statement.bindLong(14, _tmp_4);
        final int _tmp_5 = entity.isFavorite() ? 1 : 0;
        statement.bindLong(15, _tmp_5);
        if (entity.getRating() == null) {
          statement.bindNull(16);
        } else {
          statement.bindLong(16, entity.getRating());
        }
        final Long _tmp_6 = __converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_6 == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, _tmp_6);
        }
        final Long _tmp_7 = __converters.dateToTimestamp(entity.getUpdatedAt());
        if (_tmp_7 == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, _tmp_7);
        }
        statement.bindLong(19, entity.getId());
      }
    };
  }

  @Override
  public Object insertWorkout(final Workout workout, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfWorkout.insertAndReturnId(workout);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteWorkout(final Workout workout, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfWorkout.handle(workout);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateWorkout(final Workout workout, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfWorkout.handle(workout);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Workout>> getAllWorkouts() {
    final String _sql = "SELECT * FROM workouts ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"workouts"}, new Callable<List<Workout>>() {
      @Override
      @NonNull
      public List<Workout> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_time");
          final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_minutes");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfTotalVolume = CursorUtil.getColumnIndexOrThrow(_cursor, "total_volume");
          final int _cursorIndexOfTotalSets = CursorUtil.getColumnIndexOrThrow(_cursor, "total_sets");
          final int _cursorIndexOfTotalReps = CursorUtil.getColumnIndexOrThrow(_cursor, "total_reps");
          final int _cursorIndexOfProgramId = CursorUtil.getColumnIndexOrThrow(_cursor, "program_id");
          final int _cursorIndexOfProgramDayId = CursorUtil.getColumnIndexOrThrow(_cursor, "program_day_id");
          final int _cursorIndexOfIsTemplate = CursorUtil.getColumnIndexOrThrow(_cursor, "is_template");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_completed");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "is_favorite");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<Workout> _result = new ArrayList<Workout>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Workout _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final Date _tmpDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDate);
            }
            _tmpDate = __converters.fromTimestamp(_tmp);
            final Date _tmpStartTime;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfStartTime);
            }
            _tmpStartTime = __converters.fromTimestamp(_tmp_1);
            final Date _tmpEndTime;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converters.fromTimestamp(_tmp_2);
            final Integer _tmpDurationMinutes;
            if (_cursor.isNull(_cursorIndexOfDurationMinutes)) {
              _tmpDurationMinutes = null;
            } else {
              _tmpDurationMinutes = _cursor.getInt(_cursorIndexOfDurationMinutes);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final double _tmpTotalVolume;
            _tmpTotalVolume = _cursor.getDouble(_cursorIndexOfTotalVolume);
            final int _tmpTotalSets;
            _tmpTotalSets = _cursor.getInt(_cursorIndexOfTotalSets);
            final int _tmpTotalReps;
            _tmpTotalReps = _cursor.getInt(_cursorIndexOfTotalReps);
            final Long _tmpProgramId;
            if (_cursor.isNull(_cursorIndexOfProgramId)) {
              _tmpProgramId = null;
            } else {
              _tmpProgramId = _cursor.getLong(_cursorIndexOfProgramId);
            }
            final Long _tmpProgramDayId;
            if (_cursor.isNull(_cursorIndexOfProgramDayId)) {
              _tmpProgramDayId = null;
            } else {
              _tmpProgramDayId = _cursor.getLong(_cursorIndexOfProgramDayId);
            }
            final boolean _tmpIsTemplate;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsTemplate);
            _tmpIsTemplate = _tmp_3 != 0;
            final boolean _tmpIsCompleted;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp_4 != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_5 != 0;
            final Integer _tmpRating;
            if (_cursor.isNull(_cursorIndexOfRating)) {
              _tmpRating = null;
            } else {
              _tmpRating = _cursor.getInt(_cursorIndexOfRating);
            }
            final Date _tmpCreatedAt;
            final Long _tmp_6;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            _tmpCreatedAt = __converters.fromTimestamp(_tmp_6);
            final Date _tmpUpdatedAt;
            final Long _tmp_7;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            _tmpUpdatedAt = __converters.fromTimestamp(_tmp_7);
            _item = new Workout(_tmpId,_tmpName,_tmpDate,_tmpStartTime,_tmpEndTime,_tmpDurationMinutes,_tmpNotes,_tmpTotalVolume,_tmpTotalSets,_tmpTotalReps,_tmpProgramId,_tmpProgramDayId,_tmpIsTemplate,_tmpIsCompleted,_tmpIsFavorite,_tmpRating,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getWorkoutById(final long workoutId,
      final Continuation<? super Workout> $completion) {
    final String _sql = "SELECT * FROM workouts WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, workoutId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Workout>() {
      @Override
      @Nullable
      public Workout call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_time");
          final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_minutes");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfTotalVolume = CursorUtil.getColumnIndexOrThrow(_cursor, "total_volume");
          final int _cursorIndexOfTotalSets = CursorUtil.getColumnIndexOrThrow(_cursor, "total_sets");
          final int _cursorIndexOfTotalReps = CursorUtil.getColumnIndexOrThrow(_cursor, "total_reps");
          final int _cursorIndexOfProgramId = CursorUtil.getColumnIndexOrThrow(_cursor, "program_id");
          final int _cursorIndexOfProgramDayId = CursorUtil.getColumnIndexOrThrow(_cursor, "program_day_id");
          final int _cursorIndexOfIsTemplate = CursorUtil.getColumnIndexOrThrow(_cursor, "is_template");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_completed");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "is_favorite");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final Workout _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final Date _tmpDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDate);
            }
            _tmpDate = __converters.fromTimestamp(_tmp);
            final Date _tmpStartTime;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfStartTime);
            }
            _tmpStartTime = __converters.fromTimestamp(_tmp_1);
            final Date _tmpEndTime;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converters.fromTimestamp(_tmp_2);
            final Integer _tmpDurationMinutes;
            if (_cursor.isNull(_cursorIndexOfDurationMinutes)) {
              _tmpDurationMinutes = null;
            } else {
              _tmpDurationMinutes = _cursor.getInt(_cursorIndexOfDurationMinutes);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final double _tmpTotalVolume;
            _tmpTotalVolume = _cursor.getDouble(_cursorIndexOfTotalVolume);
            final int _tmpTotalSets;
            _tmpTotalSets = _cursor.getInt(_cursorIndexOfTotalSets);
            final int _tmpTotalReps;
            _tmpTotalReps = _cursor.getInt(_cursorIndexOfTotalReps);
            final Long _tmpProgramId;
            if (_cursor.isNull(_cursorIndexOfProgramId)) {
              _tmpProgramId = null;
            } else {
              _tmpProgramId = _cursor.getLong(_cursorIndexOfProgramId);
            }
            final Long _tmpProgramDayId;
            if (_cursor.isNull(_cursorIndexOfProgramDayId)) {
              _tmpProgramDayId = null;
            } else {
              _tmpProgramDayId = _cursor.getLong(_cursorIndexOfProgramDayId);
            }
            final boolean _tmpIsTemplate;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsTemplate);
            _tmpIsTemplate = _tmp_3 != 0;
            final boolean _tmpIsCompleted;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp_4 != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_5 != 0;
            final Integer _tmpRating;
            if (_cursor.isNull(_cursorIndexOfRating)) {
              _tmpRating = null;
            } else {
              _tmpRating = _cursor.getInt(_cursorIndexOfRating);
            }
            final Date _tmpCreatedAt;
            final Long _tmp_6;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            _tmpCreatedAt = __converters.fromTimestamp(_tmp_6);
            final Date _tmpUpdatedAt;
            final Long _tmp_7;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            _tmpUpdatedAt = __converters.fromTimestamp(_tmp_7);
            _result = new Workout(_tmpId,_tmpName,_tmpDate,_tmpStartTime,_tmpEndTime,_tmpDurationMinutes,_tmpNotes,_tmpTotalVolume,_tmpTotalSets,_tmpTotalReps,_tmpProgramId,_tmpProgramDayId,_tmpIsTemplate,_tmpIsCompleted,_tmpIsFavorite,_tmpRating,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
