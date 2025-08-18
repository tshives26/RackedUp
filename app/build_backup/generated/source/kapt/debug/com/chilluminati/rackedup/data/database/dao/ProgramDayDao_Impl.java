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
import com.chilluminati.rackedup.data.database.entity.ProgramDay;
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
public final class ProgramDayDao_Impl implements ProgramDayDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ProgramDay> __insertionAdapterOfProgramDay;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<ProgramDay> __deletionAdapterOfProgramDay;

  private final EntityDeletionOrUpdateAdapter<ProgramDay> __updateAdapterOfProgramDay;

  public ProgramDayDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProgramDay = new EntityInsertionAdapter<ProgramDay>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `program_days` (`id`,`program_id`,`day_number`,`week_number`,`name`,`description`,`day_type`,`muscle_groups`,`estimated_duration`,`is_rest_day`,`notes`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProgramDay entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProgramId());
        statement.bindLong(3, entity.getDayNumber());
        statement.bindLong(4, entity.getWeekNumber());
        if (entity.getName() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getName());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getDescription());
        }
        if (entity.getDayType() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDayType());
        }
        final String _tmp = __converters.fromStringList(entity.getMuscleGroups());
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp);
        }
        if (entity.getEstimatedDuration() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getEstimatedDuration());
        }
        final int _tmp_1 = entity.isRestDay() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
        if (entity.getNotes() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getNotes());
        }
        final Long _tmp_2 = __converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_2 == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, _tmp_2);
        }
      }
    };
    this.__deletionAdapterOfProgramDay = new EntityDeletionOrUpdateAdapter<ProgramDay>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `program_days` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProgramDay entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfProgramDay = new EntityDeletionOrUpdateAdapter<ProgramDay>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `program_days` SET `id` = ?,`program_id` = ?,`day_number` = ?,`week_number` = ?,`name` = ?,`description` = ?,`day_type` = ?,`muscle_groups` = ?,`estimated_duration` = ?,`is_rest_day` = ?,`notes` = ?,`created_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProgramDay entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getProgramId());
        statement.bindLong(3, entity.getDayNumber());
        statement.bindLong(4, entity.getWeekNumber());
        if (entity.getName() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getName());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getDescription());
        }
        if (entity.getDayType() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDayType());
        }
        final String _tmp = __converters.fromStringList(entity.getMuscleGroups());
        if (_tmp == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp);
        }
        if (entity.getEstimatedDuration() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getEstimatedDuration());
        }
        final int _tmp_1 = entity.isRestDay() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
        if (entity.getNotes() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getNotes());
        }
        final Long _tmp_2 = __converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_2 == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, _tmp_2);
        }
        statement.bindLong(13, entity.getId());
      }
    };
  }

  @Override
  public Object insertProgramDay(final ProgramDay programDay,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfProgramDay.insertAndReturnId(programDay);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteProgramDay(final ProgramDay programDay,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfProgramDay.handle(programDay);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateProgramDay(final ProgramDay programDay,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfProgramDay.handle(programDay);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ProgramDay>> getProgramDays(final long programId) {
    final String _sql = "SELECT * FROM program_days WHERE program_id = ? ORDER BY day_number ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, programId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"program_days"}, new Callable<List<ProgramDay>>() {
      @Override
      @NonNull
      public List<ProgramDay> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProgramId = CursorUtil.getColumnIndexOrThrow(_cursor, "program_id");
          final int _cursorIndexOfDayNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "day_number");
          final int _cursorIndexOfWeekNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "week_number");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfDayType = CursorUtil.getColumnIndexOrThrow(_cursor, "day_type");
          final int _cursorIndexOfMuscleGroups = CursorUtil.getColumnIndexOrThrow(_cursor, "muscle_groups");
          final int _cursorIndexOfEstimatedDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "estimated_duration");
          final int _cursorIndexOfIsRestDay = CursorUtil.getColumnIndexOrThrow(_cursor, "is_rest_day");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<ProgramDay> _result = new ArrayList<ProgramDay>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProgramDay _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpProgramId;
            _tmpProgramId = _cursor.getLong(_cursorIndexOfProgramId);
            final int _tmpDayNumber;
            _tmpDayNumber = _cursor.getInt(_cursorIndexOfDayNumber);
            final int _tmpWeekNumber;
            _tmpWeekNumber = _cursor.getInt(_cursorIndexOfWeekNumber);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpDayType;
            if (_cursor.isNull(_cursorIndexOfDayType)) {
              _tmpDayType = null;
            } else {
              _tmpDayType = _cursor.getString(_cursorIndexOfDayType);
            }
            final List<String> _tmpMuscleGroups;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfMuscleGroups)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfMuscleGroups);
            }
            _tmpMuscleGroups = __converters.toStringList(_tmp);
            final Integer _tmpEstimatedDuration;
            if (_cursor.isNull(_cursorIndexOfEstimatedDuration)) {
              _tmpEstimatedDuration = null;
            } else {
              _tmpEstimatedDuration = _cursor.getInt(_cursorIndexOfEstimatedDuration);
            }
            final boolean _tmpIsRestDay;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsRestDay);
            _tmpIsRestDay = _tmp_1 != 0;
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final Date _tmpCreatedAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            _tmpCreatedAt = __converters.fromTimestamp(_tmp_2);
            _item = new ProgramDay(_tmpId,_tmpProgramId,_tmpDayNumber,_tmpWeekNumber,_tmpName,_tmpDescription,_tmpDayType,_tmpMuscleGroups,_tmpEstimatedDuration,_tmpIsRestDay,_tmpNotes,_tmpCreatedAt);
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
