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
import com.chilluminati.rackedup.data.database.entity.Program;
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
public final class ProgramDao_Impl implements ProgramDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Program> __insertionAdapterOfProgram;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<Program> __deletionAdapterOfProgram;

  private final EntityDeletionOrUpdateAdapter<Program> __updateAdapterOfProgram;

  public ProgramDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProgram = new EntityInsertionAdapter<Program>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `programs` (`id`,`name`,`description`,`duration_weeks`,`difficulty_level`,`program_type`,`days_per_week`,`goal`,`author`,`is_custom`,`is_template`,`is_active`,`is_favorite`,`current_week`,`current_day`,`progression_scheme`,`deload_week`,`notes`,`start_date`,`end_date`,`created_at`,`updated_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Program entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        if (entity.getDurationWeeks() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getDurationWeeks());
        }
        if (entity.getDifficultyLevel() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDifficultyLevel());
        }
        if (entity.getProgramType() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getProgramType());
        }
        statement.bindLong(7, entity.getDaysPerWeek());
        if (entity.getGoal() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getGoal());
        }
        if (entity.getAuthor() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getAuthor());
        }
        final int _tmp = entity.isCustom() ? 1 : 0;
        statement.bindLong(10, _tmp);
        final int _tmp_1 = entity.isTemplate() ? 1 : 0;
        statement.bindLong(11, _tmp_1);
        final int _tmp_2 = entity.isActive() ? 1 : 0;
        statement.bindLong(12, _tmp_2);
        final int _tmp_3 = entity.isFavorite() ? 1 : 0;
        statement.bindLong(13, _tmp_3);
        statement.bindLong(14, entity.getCurrentWeek());
        statement.bindLong(15, entity.getCurrentDay());
        if (entity.getProgressionScheme() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getProgressionScheme());
        }
        if (entity.getDeloadWeek() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getDeloadWeek());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getNotes());
        }
        final Long _tmp_4 = __converters.dateToTimestamp(entity.getStartDate());
        if (_tmp_4 == null) {
          statement.bindNull(19);
        } else {
          statement.bindLong(19, _tmp_4);
        }
        final Long _tmp_5 = __converters.dateToTimestamp(entity.getEndDate());
        if (_tmp_5 == null) {
          statement.bindNull(20);
        } else {
          statement.bindLong(20, _tmp_5);
        }
        final Long _tmp_6 = __converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_6 == null) {
          statement.bindNull(21);
        } else {
          statement.bindLong(21, _tmp_6);
        }
        final Long _tmp_7 = __converters.dateToTimestamp(entity.getUpdatedAt());
        if (_tmp_7 == null) {
          statement.bindNull(22);
        } else {
          statement.bindLong(22, _tmp_7);
        }
      }
    };
    this.__deletionAdapterOfProgram = new EntityDeletionOrUpdateAdapter<Program>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `programs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Program entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfProgram = new EntityDeletionOrUpdateAdapter<Program>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `programs` SET `id` = ?,`name` = ?,`description` = ?,`duration_weeks` = ?,`difficulty_level` = ?,`program_type` = ?,`days_per_week` = ?,`goal` = ?,`author` = ?,`is_custom` = ?,`is_template` = ?,`is_active` = ?,`is_favorite` = ?,`current_week` = ?,`current_day` = ?,`progression_scheme` = ?,`deload_week` = ?,`notes` = ?,`start_date` = ?,`end_date` = ?,`created_at` = ?,`updated_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Program entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        if (entity.getDurationWeeks() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getDurationWeeks());
        }
        if (entity.getDifficultyLevel() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDifficultyLevel());
        }
        if (entity.getProgramType() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getProgramType());
        }
        statement.bindLong(7, entity.getDaysPerWeek());
        if (entity.getGoal() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getGoal());
        }
        if (entity.getAuthor() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getAuthor());
        }
        final int _tmp = entity.isCustom() ? 1 : 0;
        statement.bindLong(10, _tmp);
        final int _tmp_1 = entity.isTemplate() ? 1 : 0;
        statement.bindLong(11, _tmp_1);
        final int _tmp_2 = entity.isActive() ? 1 : 0;
        statement.bindLong(12, _tmp_2);
        final int _tmp_3 = entity.isFavorite() ? 1 : 0;
        statement.bindLong(13, _tmp_3);
        statement.bindLong(14, entity.getCurrentWeek());
        statement.bindLong(15, entity.getCurrentDay());
        if (entity.getProgressionScheme() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getProgressionScheme());
        }
        if (entity.getDeloadWeek() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getDeloadWeek());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getNotes());
        }
        final Long _tmp_4 = __converters.dateToTimestamp(entity.getStartDate());
        if (_tmp_4 == null) {
          statement.bindNull(19);
        } else {
          statement.bindLong(19, _tmp_4);
        }
        final Long _tmp_5 = __converters.dateToTimestamp(entity.getEndDate());
        if (_tmp_5 == null) {
          statement.bindNull(20);
        } else {
          statement.bindLong(20, _tmp_5);
        }
        final Long _tmp_6 = __converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_6 == null) {
          statement.bindNull(21);
        } else {
          statement.bindLong(21, _tmp_6);
        }
        final Long _tmp_7 = __converters.dateToTimestamp(entity.getUpdatedAt());
        if (_tmp_7 == null) {
          statement.bindNull(22);
        } else {
          statement.bindLong(22, _tmp_7);
        }
        statement.bindLong(23, entity.getId());
      }
    };
  }

  @Override
  public Object insertProgram(final Program program, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfProgram.insertAndReturnId(program);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteProgram(final Program program, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfProgram.handle(program);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateProgram(final Program program, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfProgram.handle(program);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Program>> getAllPrograms() {
    final String _sql = "SELECT * FROM programs ORDER BY created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"programs"}, new Callable<List<Program>>() {
      @Override
      @NonNull
      public List<Program> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfDurationWeeks = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_weeks");
          final int _cursorIndexOfDifficultyLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty_level");
          final int _cursorIndexOfProgramType = CursorUtil.getColumnIndexOrThrow(_cursor, "program_type");
          final int _cursorIndexOfDaysPerWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "days_per_week");
          final int _cursorIndexOfGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "goal");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "is_custom");
          final int _cursorIndexOfIsTemplate = CursorUtil.getColumnIndexOrThrow(_cursor, "is_template");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "is_favorite");
          final int _cursorIndexOfCurrentWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "current_week");
          final int _cursorIndexOfCurrentDay = CursorUtil.getColumnIndexOrThrow(_cursor, "current_day");
          final int _cursorIndexOfProgressionScheme = CursorUtil.getColumnIndexOrThrow(_cursor, "progression_scheme");
          final int _cursorIndexOfDeloadWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "deload_week");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<Program> _result = new ArrayList<Program>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Program _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
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
            final Integer _tmpDurationWeeks;
            if (_cursor.isNull(_cursorIndexOfDurationWeeks)) {
              _tmpDurationWeeks = null;
            } else {
              _tmpDurationWeeks = _cursor.getInt(_cursorIndexOfDurationWeeks);
            }
            final String _tmpDifficultyLevel;
            if (_cursor.isNull(_cursorIndexOfDifficultyLevel)) {
              _tmpDifficultyLevel = null;
            } else {
              _tmpDifficultyLevel = _cursor.getString(_cursorIndexOfDifficultyLevel);
            }
            final String _tmpProgramType;
            if (_cursor.isNull(_cursorIndexOfProgramType)) {
              _tmpProgramType = null;
            } else {
              _tmpProgramType = _cursor.getString(_cursorIndexOfProgramType);
            }
            final int _tmpDaysPerWeek;
            _tmpDaysPerWeek = _cursor.getInt(_cursorIndexOfDaysPerWeek);
            final String _tmpGoal;
            if (_cursor.isNull(_cursorIndexOfGoal)) {
              _tmpGoal = null;
            } else {
              _tmpGoal = _cursor.getString(_cursorIndexOfGoal);
            }
            final String _tmpAuthor;
            if (_cursor.isNull(_cursorIndexOfAuthor)) {
              _tmpAuthor = null;
            } else {
              _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            }
            final boolean _tmpIsCustom;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCustom);
            _tmpIsCustom = _tmp != 0;
            final boolean _tmpIsTemplate;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsTemplate);
            _tmpIsTemplate = _tmp_1 != 0;
            final boolean _tmpIsActive;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_2 != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_3 != 0;
            final int _tmpCurrentWeek;
            _tmpCurrentWeek = _cursor.getInt(_cursorIndexOfCurrentWeek);
            final int _tmpCurrentDay;
            _tmpCurrentDay = _cursor.getInt(_cursorIndexOfCurrentDay);
            final String _tmpProgressionScheme;
            if (_cursor.isNull(_cursorIndexOfProgressionScheme)) {
              _tmpProgressionScheme = null;
            } else {
              _tmpProgressionScheme = _cursor.getString(_cursorIndexOfProgressionScheme);
            }
            final Integer _tmpDeloadWeek;
            if (_cursor.isNull(_cursorIndexOfDeloadWeek)) {
              _tmpDeloadWeek = null;
            } else {
              _tmpDeloadWeek = _cursor.getInt(_cursorIndexOfDeloadWeek);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final Date _tmpStartDate;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfStartDate);
            }
            _tmpStartDate = __converters.fromTimestamp(_tmp_4);
            final Date _tmpEndDate;
            final Long _tmp_5;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getLong(_cursorIndexOfEndDate);
            }
            _tmpEndDate = __converters.fromTimestamp(_tmp_5);
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
            _item = new Program(_tmpId,_tmpName,_tmpDescription,_tmpDurationWeeks,_tmpDifficultyLevel,_tmpProgramType,_tmpDaysPerWeek,_tmpGoal,_tmpAuthor,_tmpIsCustom,_tmpIsTemplate,_tmpIsActive,_tmpIsFavorite,_tmpCurrentWeek,_tmpCurrentDay,_tmpProgressionScheme,_tmpDeloadWeek,_tmpNotes,_tmpStartDate,_tmpEndDate,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getActiveProgram(final Continuation<? super Program> $completion) {
    final String _sql = "SELECT * FROM programs WHERE is_active = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Program>() {
      @Override
      @Nullable
      public Program call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfDurationWeeks = CursorUtil.getColumnIndexOrThrow(_cursor, "duration_weeks");
          final int _cursorIndexOfDifficultyLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty_level");
          final int _cursorIndexOfProgramType = CursorUtil.getColumnIndexOrThrow(_cursor, "program_type");
          final int _cursorIndexOfDaysPerWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "days_per_week");
          final int _cursorIndexOfGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "goal");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "is_custom");
          final int _cursorIndexOfIsTemplate = CursorUtil.getColumnIndexOrThrow(_cursor, "is_template");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "is_favorite");
          final int _cursorIndexOfCurrentWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "current_week");
          final int _cursorIndexOfCurrentDay = CursorUtil.getColumnIndexOrThrow(_cursor, "current_day");
          final int _cursorIndexOfProgressionScheme = CursorUtil.getColumnIndexOrThrow(_cursor, "progression_scheme");
          final int _cursorIndexOfDeloadWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "deload_week");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final Program _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
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
            final Integer _tmpDurationWeeks;
            if (_cursor.isNull(_cursorIndexOfDurationWeeks)) {
              _tmpDurationWeeks = null;
            } else {
              _tmpDurationWeeks = _cursor.getInt(_cursorIndexOfDurationWeeks);
            }
            final String _tmpDifficultyLevel;
            if (_cursor.isNull(_cursorIndexOfDifficultyLevel)) {
              _tmpDifficultyLevel = null;
            } else {
              _tmpDifficultyLevel = _cursor.getString(_cursorIndexOfDifficultyLevel);
            }
            final String _tmpProgramType;
            if (_cursor.isNull(_cursorIndexOfProgramType)) {
              _tmpProgramType = null;
            } else {
              _tmpProgramType = _cursor.getString(_cursorIndexOfProgramType);
            }
            final int _tmpDaysPerWeek;
            _tmpDaysPerWeek = _cursor.getInt(_cursorIndexOfDaysPerWeek);
            final String _tmpGoal;
            if (_cursor.isNull(_cursorIndexOfGoal)) {
              _tmpGoal = null;
            } else {
              _tmpGoal = _cursor.getString(_cursorIndexOfGoal);
            }
            final String _tmpAuthor;
            if (_cursor.isNull(_cursorIndexOfAuthor)) {
              _tmpAuthor = null;
            } else {
              _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            }
            final boolean _tmpIsCustom;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCustom);
            _tmpIsCustom = _tmp != 0;
            final boolean _tmpIsTemplate;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsTemplate);
            _tmpIsTemplate = _tmp_1 != 0;
            final boolean _tmpIsActive;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp_2 != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_3 != 0;
            final int _tmpCurrentWeek;
            _tmpCurrentWeek = _cursor.getInt(_cursorIndexOfCurrentWeek);
            final int _tmpCurrentDay;
            _tmpCurrentDay = _cursor.getInt(_cursorIndexOfCurrentDay);
            final String _tmpProgressionScheme;
            if (_cursor.isNull(_cursorIndexOfProgressionScheme)) {
              _tmpProgressionScheme = null;
            } else {
              _tmpProgressionScheme = _cursor.getString(_cursorIndexOfProgressionScheme);
            }
            final Integer _tmpDeloadWeek;
            if (_cursor.isNull(_cursorIndexOfDeloadWeek)) {
              _tmpDeloadWeek = null;
            } else {
              _tmpDeloadWeek = _cursor.getInt(_cursorIndexOfDeloadWeek);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final Date _tmpStartDate;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfStartDate);
            }
            _tmpStartDate = __converters.fromTimestamp(_tmp_4);
            final Date _tmpEndDate;
            final Long _tmp_5;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getLong(_cursorIndexOfEndDate);
            }
            _tmpEndDate = __converters.fromTimestamp(_tmp_5);
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
            _result = new Program(_tmpId,_tmpName,_tmpDescription,_tmpDurationWeeks,_tmpDifficultyLevel,_tmpProgramType,_tmpDaysPerWeek,_tmpGoal,_tmpAuthor,_tmpIsCustom,_tmpIsTemplate,_tmpIsActive,_tmpIsFavorite,_tmpCurrentWeek,_tmpCurrentDay,_tmpProgressionScheme,_tmpDeloadWeek,_tmpNotes,_tmpStartDate,_tmpEndDate,_tmpCreatedAt,_tmpUpdatedAt);
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
