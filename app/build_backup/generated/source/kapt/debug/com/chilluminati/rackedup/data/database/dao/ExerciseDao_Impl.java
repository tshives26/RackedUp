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
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.chilluminati.rackedup.data.database.converter.Converters;
import com.chilluminati.rackedup.data.database.entity.Exercise;
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
public final class ExerciseDao_Impl implements ExerciseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Exercise> __insertionAdapterOfExercise;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<Exercise> __deletionAdapterOfExercise;

  private final EntityDeletionOrUpdateAdapter<Exercise> __updateAdapterOfExercise;

  private final SharedSQLiteStatement __preparedStmtOfDeleteExerciseById;

  private final SharedSQLiteStatement __preparedStmtOfUpdateFavoriteStatus;

  public ExerciseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExercise = new EntityInsertionAdapter<Exercise>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exercises` (`id`,`name`,`category`,`subcategory`,`equipment`,`exercise_type`,`difficulty_level`,`instructions`,`tips`,`muscle_groups`,`secondary_muscles`,`is_compound`,`is_unilateral`,`is_custom`,`is_favorite`,`image_url`,`video_url`,`created_at`,`updated_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Exercise entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getCategory() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCategory());
        }
        if (entity.getSubcategory() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getSubcategory());
        }
        if (entity.getEquipment() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getEquipment());
        }
        if (entity.getExerciseType() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getExerciseType());
        }
        if (entity.getDifficultyLevel() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDifficultyLevel());
        }
        if (entity.getInstructions() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getInstructions());
        }
        if (entity.getTips() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getTips());
        }
        final String _tmp = __converters.fromStringList(entity.getMuscleGroups());
        if (_tmp == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, _tmp);
        }
        final String _tmp_1 = __converters.fromStringList(entity.getSecondaryMuscles());
        if (_tmp_1 == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, _tmp_1);
        }
        final int _tmp_2 = entity.isCompound() ? 1 : 0;
        statement.bindLong(12, _tmp_2);
        final int _tmp_3 = entity.isUnilateral() ? 1 : 0;
        statement.bindLong(13, _tmp_3);
        final int _tmp_4 = entity.isCustom() ? 1 : 0;
        statement.bindLong(14, _tmp_4);
        final int _tmp_5 = entity.isFavorite() ? 1 : 0;
        statement.bindLong(15, _tmp_5);
        if (entity.getImageUrl() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getImageUrl());
        }
        if (entity.getVideoUrl() == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.getVideoUrl());
        }
        final Long _tmp_6 = __converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_6 == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, _tmp_6);
        }
        final Long _tmp_7 = __converters.dateToTimestamp(entity.getUpdatedAt());
        if (_tmp_7 == null) {
          statement.bindNull(19);
        } else {
          statement.bindLong(19, _tmp_7);
        }
      }
    };
    this.__deletionAdapterOfExercise = new EntityDeletionOrUpdateAdapter<Exercise>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `exercises` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Exercise entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfExercise = new EntityDeletionOrUpdateAdapter<Exercise>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `exercises` SET `id` = ?,`name` = ?,`category` = ?,`subcategory` = ?,`equipment` = ?,`exercise_type` = ?,`difficulty_level` = ?,`instructions` = ?,`tips` = ?,`muscle_groups` = ?,`secondary_muscles` = ?,`is_compound` = ?,`is_unilateral` = ?,`is_custom` = ?,`is_favorite` = ?,`image_url` = ?,`video_url` = ?,`created_at` = ?,`updated_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Exercise entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getCategory() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCategory());
        }
        if (entity.getSubcategory() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getSubcategory());
        }
        if (entity.getEquipment() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getEquipment());
        }
        if (entity.getExerciseType() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getExerciseType());
        }
        if (entity.getDifficultyLevel() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDifficultyLevel());
        }
        if (entity.getInstructions() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getInstructions());
        }
        if (entity.getTips() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getTips());
        }
        final String _tmp = __converters.fromStringList(entity.getMuscleGroups());
        if (_tmp == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, _tmp);
        }
        final String _tmp_1 = __converters.fromStringList(entity.getSecondaryMuscles());
        if (_tmp_1 == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, _tmp_1);
        }
        final int _tmp_2 = entity.isCompound() ? 1 : 0;
        statement.bindLong(12, _tmp_2);
        final int _tmp_3 = entity.isUnilateral() ? 1 : 0;
        statement.bindLong(13, _tmp_3);
        final int _tmp_4 = entity.isCustom() ? 1 : 0;
        statement.bindLong(14, _tmp_4);
        final int _tmp_5 = entity.isFavorite() ? 1 : 0;
        statement.bindLong(15, _tmp_5);
        if (entity.getImageUrl() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getImageUrl());
        }
        if (entity.getVideoUrl() == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.getVideoUrl());
        }
        final Long _tmp_6 = __converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_6 == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, _tmp_6);
        }
        final Long _tmp_7 = __converters.dateToTimestamp(entity.getUpdatedAt());
        if (_tmp_7 == null) {
          statement.bindNull(19);
        } else {
          statement.bindLong(19, _tmp_7);
        }
        statement.bindLong(20, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteExerciseById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM exercises WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateFavoriteStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE exercises SET is_favorite = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertExercise(final Exercise exercise,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfExercise.insertAndReturnId(exercise);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertExercises(final List<Exercise> exercises,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfExercise.insert(exercises);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteExercise(final Exercise exercise,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfExercise.handle(exercise);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateExercise(final Exercise exercise,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfExercise.handle(exercise);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteExerciseById(final long exerciseId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteExerciseById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, exerciseId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteExerciseById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateFavoriteStatus(final long exerciseId, final boolean isFavorite,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateFavoriteStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isFavorite ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, exerciseId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateFavoriteStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Exercise>> getAllExercises() {
    final String _sql = "SELECT * FROM exercises ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exercises"}, new Callable<List<Exercise>>() {
      @Override
      @NonNull
      public List<Exercise> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSubcategory = CursorUtil.getColumnIndexOrThrow(_cursor, "subcategory");
          final int _cursorIndexOfEquipment = CursorUtil.getColumnIndexOrThrow(_cursor, "equipment");
          final int _cursorIndexOfExerciseType = CursorUtil.getColumnIndexOrThrow(_cursor, "exercise_type");
          final int _cursorIndexOfDifficultyLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty_level");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfTips = CursorUtil.getColumnIndexOrThrow(_cursor, "tips");
          final int _cursorIndexOfMuscleGroups = CursorUtil.getColumnIndexOrThrow(_cursor, "muscle_groups");
          final int _cursorIndexOfSecondaryMuscles = CursorUtil.getColumnIndexOrThrow(_cursor, "secondary_muscles");
          final int _cursorIndexOfIsCompound = CursorUtil.getColumnIndexOrThrow(_cursor, "is_compound");
          final int _cursorIndexOfIsUnilateral = CursorUtil.getColumnIndexOrThrow(_cursor, "is_unilateral");
          final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "is_custom");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "is_favorite");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "image_url");
          final int _cursorIndexOfVideoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "video_url");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<Exercise> _result = new ArrayList<Exercise>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Exercise _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final String _tmpSubcategory;
            if (_cursor.isNull(_cursorIndexOfSubcategory)) {
              _tmpSubcategory = null;
            } else {
              _tmpSubcategory = _cursor.getString(_cursorIndexOfSubcategory);
            }
            final String _tmpEquipment;
            if (_cursor.isNull(_cursorIndexOfEquipment)) {
              _tmpEquipment = null;
            } else {
              _tmpEquipment = _cursor.getString(_cursorIndexOfEquipment);
            }
            final String _tmpExerciseType;
            if (_cursor.isNull(_cursorIndexOfExerciseType)) {
              _tmpExerciseType = null;
            } else {
              _tmpExerciseType = _cursor.getString(_cursorIndexOfExerciseType);
            }
            final String _tmpDifficultyLevel;
            if (_cursor.isNull(_cursorIndexOfDifficultyLevel)) {
              _tmpDifficultyLevel = null;
            } else {
              _tmpDifficultyLevel = _cursor.getString(_cursorIndexOfDifficultyLevel);
            }
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final String _tmpTips;
            if (_cursor.isNull(_cursorIndexOfTips)) {
              _tmpTips = null;
            } else {
              _tmpTips = _cursor.getString(_cursorIndexOfTips);
            }
            final List<String> _tmpMuscleGroups;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfMuscleGroups)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfMuscleGroups);
            }
            _tmpMuscleGroups = __converters.toStringList(_tmp);
            final List<String> _tmpSecondaryMuscles;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfSecondaryMuscles)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfSecondaryMuscles);
            }
            _tmpSecondaryMuscles = __converters.toStringList(_tmp_1);
            final boolean _tmpIsCompound;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsCompound);
            _tmpIsCompound = _tmp_2 != 0;
            final boolean _tmpIsUnilateral;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsUnilateral);
            _tmpIsUnilateral = _tmp_3 != 0;
            final boolean _tmpIsCustom;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsCustom);
            _tmpIsCustom = _tmp_4 != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_5 != 0;
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            final String _tmpVideoUrl;
            if (_cursor.isNull(_cursorIndexOfVideoUrl)) {
              _tmpVideoUrl = null;
            } else {
              _tmpVideoUrl = _cursor.getString(_cursorIndexOfVideoUrl);
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
            _item = new Exercise(_tmpId,_tmpName,_tmpCategory,_tmpSubcategory,_tmpEquipment,_tmpExerciseType,_tmpDifficultyLevel,_tmpInstructions,_tmpTips,_tmpMuscleGroups,_tmpSecondaryMuscles,_tmpIsCompound,_tmpIsUnilateral,_tmpIsCustom,_tmpIsFavorite,_tmpImageUrl,_tmpVideoUrl,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<Exercise>> getExercisesByCategory(final String category) {
    final String _sql = "SELECT * FROM exercises WHERE category = ? ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (category == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, category);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exercises"}, new Callable<List<Exercise>>() {
      @Override
      @NonNull
      public List<Exercise> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSubcategory = CursorUtil.getColumnIndexOrThrow(_cursor, "subcategory");
          final int _cursorIndexOfEquipment = CursorUtil.getColumnIndexOrThrow(_cursor, "equipment");
          final int _cursorIndexOfExerciseType = CursorUtil.getColumnIndexOrThrow(_cursor, "exercise_type");
          final int _cursorIndexOfDifficultyLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty_level");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfTips = CursorUtil.getColumnIndexOrThrow(_cursor, "tips");
          final int _cursorIndexOfMuscleGroups = CursorUtil.getColumnIndexOrThrow(_cursor, "muscle_groups");
          final int _cursorIndexOfSecondaryMuscles = CursorUtil.getColumnIndexOrThrow(_cursor, "secondary_muscles");
          final int _cursorIndexOfIsCompound = CursorUtil.getColumnIndexOrThrow(_cursor, "is_compound");
          final int _cursorIndexOfIsUnilateral = CursorUtil.getColumnIndexOrThrow(_cursor, "is_unilateral");
          final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "is_custom");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "is_favorite");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "image_url");
          final int _cursorIndexOfVideoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "video_url");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<Exercise> _result = new ArrayList<Exercise>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Exercise _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final String _tmpSubcategory;
            if (_cursor.isNull(_cursorIndexOfSubcategory)) {
              _tmpSubcategory = null;
            } else {
              _tmpSubcategory = _cursor.getString(_cursorIndexOfSubcategory);
            }
            final String _tmpEquipment;
            if (_cursor.isNull(_cursorIndexOfEquipment)) {
              _tmpEquipment = null;
            } else {
              _tmpEquipment = _cursor.getString(_cursorIndexOfEquipment);
            }
            final String _tmpExerciseType;
            if (_cursor.isNull(_cursorIndexOfExerciseType)) {
              _tmpExerciseType = null;
            } else {
              _tmpExerciseType = _cursor.getString(_cursorIndexOfExerciseType);
            }
            final String _tmpDifficultyLevel;
            if (_cursor.isNull(_cursorIndexOfDifficultyLevel)) {
              _tmpDifficultyLevel = null;
            } else {
              _tmpDifficultyLevel = _cursor.getString(_cursorIndexOfDifficultyLevel);
            }
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final String _tmpTips;
            if (_cursor.isNull(_cursorIndexOfTips)) {
              _tmpTips = null;
            } else {
              _tmpTips = _cursor.getString(_cursorIndexOfTips);
            }
            final List<String> _tmpMuscleGroups;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfMuscleGroups)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfMuscleGroups);
            }
            _tmpMuscleGroups = __converters.toStringList(_tmp);
            final List<String> _tmpSecondaryMuscles;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfSecondaryMuscles)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfSecondaryMuscles);
            }
            _tmpSecondaryMuscles = __converters.toStringList(_tmp_1);
            final boolean _tmpIsCompound;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsCompound);
            _tmpIsCompound = _tmp_2 != 0;
            final boolean _tmpIsUnilateral;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsUnilateral);
            _tmpIsUnilateral = _tmp_3 != 0;
            final boolean _tmpIsCustom;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsCustom);
            _tmpIsCustom = _tmp_4 != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_5 != 0;
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            final String _tmpVideoUrl;
            if (_cursor.isNull(_cursorIndexOfVideoUrl)) {
              _tmpVideoUrl = null;
            } else {
              _tmpVideoUrl = _cursor.getString(_cursorIndexOfVideoUrl);
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
            _item = new Exercise(_tmpId,_tmpName,_tmpCategory,_tmpSubcategory,_tmpEquipment,_tmpExerciseType,_tmpDifficultyLevel,_tmpInstructions,_tmpTips,_tmpMuscleGroups,_tmpSecondaryMuscles,_tmpIsCompound,_tmpIsUnilateral,_tmpIsCustom,_tmpIsFavorite,_tmpImageUrl,_tmpVideoUrl,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<Exercise>> getFavoriteExercises() {
    final String _sql = "SELECT * FROM exercises WHERE is_favorite = 1 ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exercises"}, new Callable<List<Exercise>>() {
      @Override
      @NonNull
      public List<Exercise> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSubcategory = CursorUtil.getColumnIndexOrThrow(_cursor, "subcategory");
          final int _cursorIndexOfEquipment = CursorUtil.getColumnIndexOrThrow(_cursor, "equipment");
          final int _cursorIndexOfExerciseType = CursorUtil.getColumnIndexOrThrow(_cursor, "exercise_type");
          final int _cursorIndexOfDifficultyLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty_level");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfTips = CursorUtil.getColumnIndexOrThrow(_cursor, "tips");
          final int _cursorIndexOfMuscleGroups = CursorUtil.getColumnIndexOrThrow(_cursor, "muscle_groups");
          final int _cursorIndexOfSecondaryMuscles = CursorUtil.getColumnIndexOrThrow(_cursor, "secondary_muscles");
          final int _cursorIndexOfIsCompound = CursorUtil.getColumnIndexOrThrow(_cursor, "is_compound");
          final int _cursorIndexOfIsUnilateral = CursorUtil.getColumnIndexOrThrow(_cursor, "is_unilateral");
          final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "is_custom");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "is_favorite");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "image_url");
          final int _cursorIndexOfVideoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "video_url");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<Exercise> _result = new ArrayList<Exercise>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Exercise _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final String _tmpSubcategory;
            if (_cursor.isNull(_cursorIndexOfSubcategory)) {
              _tmpSubcategory = null;
            } else {
              _tmpSubcategory = _cursor.getString(_cursorIndexOfSubcategory);
            }
            final String _tmpEquipment;
            if (_cursor.isNull(_cursorIndexOfEquipment)) {
              _tmpEquipment = null;
            } else {
              _tmpEquipment = _cursor.getString(_cursorIndexOfEquipment);
            }
            final String _tmpExerciseType;
            if (_cursor.isNull(_cursorIndexOfExerciseType)) {
              _tmpExerciseType = null;
            } else {
              _tmpExerciseType = _cursor.getString(_cursorIndexOfExerciseType);
            }
            final String _tmpDifficultyLevel;
            if (_cursor.isNull(_cursorIndexOfDifficultyLevel)) {
              _tmpDifficultyLevel = null;
            } else {
              _tmpDifficultyLevel = _cursor.getString(_cursorIndexOfDifficultyLevel);
            }
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final String _tmpTips;
            if (_cursor.isNull(_cursorIndexOfTips)) {
              _tmpTips = null;
            } else {
              _tmpTips = _cursor.getString(_cursorIndexOfTips);
            }
            final List<String> _tmpMuscleGroups;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfMuscleGroups)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfMuscleGroups);
            }
            _tmpMuscleGroups = __converters.toStringList(_tmp);
            final List<String> _tmpSecondaryMuscles;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfSecondaryMuscles)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfSecondaryMuscles);
            }
            _tmpSecondaryMuscles = __converters.toStringList(_tmp_1);
            final boolean _tmpIsCompound;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsCompound);
            _tmpIsCompound = _tmp_2 != 0;
            final boolean _tmpIsUnilateral;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsUnilateral);
            _tmpIsUnilateral = _tmp_3 != 0;
            final boolean _tmpIsCustom;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsCustom);
            _tmpIsCustom = _tmp_4 != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_5 != 0;
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            final String _tmpVideoUrl;
            if (_cursor.isNull(_cursorIndexOfVideoUrl)) {
              _tmpVideoUrl = null;
            } else {
              _tmpVideoUrl = _cursor.getString(_cursorIndexOfVideoUrl);
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
            _item = new Exercise(_tmpId,_tmpName,_tmpCategory,_tmpSubcategory,_tmpEquipment,_tmpExerciseType,_tmpDifficultyLevel,_tmpInstructions,_tmpTips,_tmpMuscleGroups,_tmpSecondaryMuscles,_tmpIsCompound,_tmpIsUnilateral,_tmpIsCustom,_tmpIsFavorite,_tmpImageUrl,_tmpVideoUrl,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<Exercise>> getCustomExercises() {
    final String _sql = "SELECT * FROM exercises WHERE is_custom = 1 ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exercises"}, new Callable<List<Exercise>>() {
      @Override
      @NonNull
      public List<Exercise> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSubcategory = CursorUtil.getColumnIndexOrThrow(_cursor, "subcategory");
          final int _cursorIndexOfEquipment = CursorUtil.getColumnIndexOrThrow(_cursor, "equipment");
          final int _cursorIndexOfExerciseType = CursorUtil.getColumnIndexOrThrow(_cursor, "exercise_type");
          final int _cursorIndexOfDifficultyLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty_level");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfTips = CursorUtil.getColumnIndexOrThrow(_cursor, "tips");
          final int _cursorIndexOfMuscleGroups = CursorUtil.getColumnIndexOrThrow(_cursor, "muscle_groups");
          final int _cursorIndexOfSecondaryMuscles = CursorUtil.getColumnIndexOrThrow(_cursor, "secondary_muscles");
          final int _cursorIndexOfIsCompound = CursorUtil.getColumnIndexOrThrow(_cursor, "is_compound");
          final int _cursorIndexOfIsUnilateral = CursorUtil.getColumnIndexOrThrow(_cursor, "is_unilateral");
          final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "is_custom");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "is_favorite");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "image_url");
          final int _cursorIndexOfVideoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "video_url");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<Exercise> _result = new ArrayList<Exercise>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Exercise _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final String _tmpSubcategory;
            if (_cursor.isNull(_cursorIndexOfSubcategory)) {
              _tmpSubcategory = null;
            } else {
              _tmpSubcategory = _cursor.getString(_cursorIndexOfSubcategory);
            }
            final String _tmpEquipment;
            if (_cursor.isNull(_cursorIndexOfEquipment)) {
              _tmpEquipment = null;
            } else {
              _tmpEquipment = _cursor.getString(_cursorIndexOfEquipment);
            }
            final String _tmpExerciseType;
            if (_cursor.isNull(_cursorIndexOfExerciseType)) {
              _tmpExerciseType = null;
            } else {
              _tmpExerciseType = _cursor.getString(_cursorIndexOfExerciseType);
            }
            final String _tmpDifficultyLevel;
            if (_cursor.isNull(_cursorIndexOfDifficultyLevel)) {
              _tmpDifficultyLevel = null;
            } else {
              _tmpDifficultyLevel = _cursor.getString(_cursorIndexOfDifficultyLevel);
            }
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final String _tmpTips;
            if (_cursor.isNull(_cursorIndexOfTips)) {
              _tmpTips = null;
            } else {
              _tmpTips = _cursor.getString(_cursorIndexOfTips);
            }
            final List<String> _tmpMuscleGroups;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfMuscleGroups)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfMuscleGroups);
            }
            _tmpMuscleGroups = __converters.toStringList(_tmp);
            final List<String> _tmpSecondaryMuscles;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfSecondaryMuscles)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfSecondaryMuscles);
            }
            _tmpSecondaryMuscles = __converters.toStringList(_tmp_1);
            final boolean _tmpIsCompound;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsCompound);
            _tmpIsCompound = _tmp_2 != 0;
            final boolean _tmpIsUnilateral;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsUnilateral);
            _tmpIsUnilateral = _tmp_3 != 0;
            final boolean _tmpIsCustom;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsCustom);
            _tmpIsCustom = _tmp_4 != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_5 != 0;
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            final String _tmpVideoUrl;
            if (_cursor.isNull(_cursorIndexOfVideoUrl)) {
              _tmpVideoUrl = null;
            } else {
              _tmpVideoUrl = _cursor.getString(_cursorIndexOfVideoUrl);
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
            _item = new Exercise(_tmpId,_tmpName,_tmpCategory,_tmpSubcategory,_tmpEquipment,_tmpExerciseType,_tmpDifficultyLevel,_tmpInstructions,_tmpTips,_tmpMuscleGroups,_tmpSecondaryMuscles,_tmpIsCompound,_tmpIsUnilateral,_tmpIsCustom,_tmpIsFavorite,_tmpImageUrl,_tmpVideoUrl,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<Exercise>> searchExercises(final String searchQuery) {
    final String _sql = "SELECT * FROM exercises WHERE name LIKE '%' || ? || '%' ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (searchQuery == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchQuery);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exercises"}, new Callable<List<Exercise>>() {
      @Override
      @NonNull
      public List<Exercise> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSubcategory = CursorUtil.getColumnIndexOrThrow(_cursor, "subcategory");
          final int _cursorIndexOfEquipment = CursorUtil.getColumnIndexOrThrow(_cursor, "equipment");
          final int _cursorIndexOfExerciseType = CursorUtil.getColumnIndexOrThrow(_cursor, "exercise_type");
          final int _cursorIndexOfDifficultyLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty_level");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfTips = CursorUtil.getColumnIndexOrThrow(_cursor, "tips");
          final int _cursorIndexOfMuscleGroups = CursorUtil.getColumnIndexOrThrow(_cursor, "muscle_groups");
          final int _cursorIndexOfSecondaryMuscles = CursorUtil.getColumnIndexOrThrow(_cursor, "secondary_muscles");
          final int _cursorIndexOfIsCompound = CursorUtil.getColumnIndexOrThrow(_cursor, "is_compound");
          final int _cursorIndexOfIsUnilateral = CursorUtil.getColumnIndexOrThrow(_cursor, "is_unilateral");
          final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "is_custom");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "is_favorite");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "image_url");
          final int _cursorIndexOfVideoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "video_url");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<Exercise> _result = new ArrayList<Exercise>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Exercise _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final String _tmpSubcategory;
            if (_cursor.isNull(_cursorIndexOfSubcategory)) {
              _tmpSubcategory = null;
            } else {
              _tmpSubcategory = _cursor.getString(_cursorIndexOfSubcategory);
            }
            final String _tmpEquipment;
            if (_cursor.isNull(_cursorIndexOfEquipment)) {
              _tmpEquipment = null;
            } else {
              _tmpEquipment = _cursor.getString(_cursorIndexOfEquipment);
            }
            final String _tmpExerciseType;
            if (_cursor.isNull(_cursorIndexOfExerciseType)) {
              _tmpExerciseType = null;
            } else {
              _tmpExerciseType = _cursor.getString(_cursorIndexOfExerciseType);
            }
            final String _tmpDifficultyLevel;
            if (_cursor.isNull(_cursorIndexOfDifficultyLevel)) {
              _tmpDifficultyLevel = null;
            } else {
              _tmpDifficultyLevel = _cursor.getString(_cursorIndexOfDifficultyLevel);
            }
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final String _tmpTips;
            if (_cursor.isNull(_cursorIndexOfTips)) {
              _tmpTips = null;
            } else {
              _tmpTips = _cursor.getString(_cursorIndexOfTips);
            }
            final List<String> _tmpMuscleGroups;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfMuscleGroups)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfMuscleGroups);
            }
            _tmpMuscleGroups = __converters.toStringList(_tmp);
            final List<String> _tmpSecondaryMuscles;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfSecondaryMuscles)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfSecondaryMuscles);
            }
            _tmpSecondaryMuscles = __converters.toStringList(_tmp_1);
            final boolean _tmpIsCompound;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsCompound);
            _tmpIsCompound = _tmp_2 != 0;
            final boolean _tmpIsUnilateral;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsUnilateral);
            _tmpIsUnilateral = _tmp_3 != 0;
            final boolean _tmpIsCustom;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsCustom);
            _tmpIsCustom = _tmp_4 != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_5 != 0;
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            final String _tmpVideoUrl;
            if (_cursor.isNull(_cursorIndexOfVideoUrl)) {
              _tmpVideoUrl = null;
            } else {
              _tmpVideoUrl = _cursor.getString(_cursorIndexOfVideoUrl);
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
            _item = new Exercise(_tmpId,_tmpName,_tmpCategory,_tmpSubcategory,_tmpEquipment,_tmpExerciseType,_tmpDifficultyLevel,_tmpInstructions,_tmpTips,_tmpMuscleGroups,_tmpSecondaryMuscles,_tmpIsCompound,_tmpIsUnilateral,_tmpIsCustom,_tmpIsFavorite,_tmpImageUrl,_tmpVideoUrl,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getExerciseById(final long exerciseId,
      final Continuation<? super Exercise> $completion) {
    final String _sql = "SELECT * FROM exercises WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, exerciseId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Exercise>() {
      @Override
      @Nullable
      public Exercise call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSubcategory = CursorUtil.getColumnIndexOrThrow(_cursor, "subcategory");
          final int _cursorIndexOfEquipment = CursorUtil.getColumnIndexOrThrow(_cursor, "equipment");
          final int _cursorIndexOfExerciseType = CursorUtil.getColumnIndexOrThrow(_cursor, "exercise_type");
          final int _cursorIndexOfDifficultyLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty_level");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfTips = CursorUtil.getColumnIndexOrThrow(_cursor, "tips");
          final int _cursorIndexOfMuscleGroups = CursorUtil.getColumnIndexOrThrow(_cursor, "muscle_groups");
          final int _cursorIndexOfSecondaryMuscles = CursorUtil.getColumnIndexOrThrow(_cursor, "secondary_muscles");
          final int _cursorIndexOfIsCompound = CursorUtil.getColumnIndexOrThrow(_cursor, "is_compound");
          final int _cursorIndexOfIsUnilateral = CursorUtil.getColumnIndexOrThrow(_cursor, "is_unilateral");
          final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "is_custom");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "is_favorite");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "image_url");
          final int _cursorIndexOfVideoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "video_url");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final Exercise _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final String _tmpSubcategory;
            if (_cursor.isNull(_cursorIndexOfSubcategory)) {
              _tmpSubcategory = null;
            } else {
              _tmpSubcategory = _cursor.getString(_cursorIndexOfSubcategory);
            }
            final String _tmpEquipment;
            if (_cursor.isNull(_cursorIndexOfEquipment)) {
              _tmpEquipment = null;
            } else {
              _tmpEquipment = _cursor.getString(_cursorIndexOfEquipment);
            }
            final String _tmpExerciseType;
            if (_cursor.isNull(_cursorIndexOfExerciseType)) {
              _tmpExerciseType = null;
            } else {
              _tmpExerciseType = _cursor.getString(_cursorIndexOfExerciseType);
            }
            final String _tmpDifficultyLevel;
            if (_cursor.isNull(_cursorIndexOfDifficultyLevel)) {
              _tmpDifficultyLevel = null;
            } else {
              _tmpDifficultyLevel = _cursor.getString(_cursorIndexOfDifficultyLevel);
            }
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final String _tmpTips;
            if (_cursor.isNull(_cursorIndexOfTips)) {
              _tmpTips = null;
            } else {
              _tmpTips = _cursor.getString(_cursorIndexOfTips);
            }
            final List<String> _tmpMuscleGroups;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfMuscleGroups)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfMuscleGroups);
            }
            _tmpMuscleGroups = __converters.toStringList(_tmp);
            final List<String> _tmpSecondaryMuscles;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfSecondaryMuscles)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfSecondaryMuscles);
            }
            _tmpSecondaryMuscles = __converters.toStringList(_tmp_1);
            final boolean _tmpIsCompound;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsCompound);
            _tmpIsCompound = _tmp_2 != 0;
            final boolean _tmpIsUnilateral;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsUnilateral);
            _tmpIsUnilateral = _tmp_3 != 0;
            final boolean _tmpIsCustom;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsCustom);
            _tmpIsCustom = _tmp_4 != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_5 != 0;
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            final String _tmpVideoUrl;
            if (_cursor.isNull(_cursorIndexOfVideoUrl)) {
              _tmpVideoUrl = null;
            } else {
              _tmpVideoUrl = _cursor.getString(_cursorIndexOfVideoUrl);
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
            _result = new Exercise(_tmpId,_tmpName,_tmpCategory,_tmpSubcategory,_tmpEquipment,_tmpExerciseType,_tmpDifficultyLevel,_tmpInstructions,_tmpTips,_tmpMuscleGroups,_tmpSecondaryMuscles,_tmpIsCompound,_tmpIsUnilateral,_tmpIsCustom,_tmpIsFavorite,_tmpImageUrl,_tmpVideoUrl,_tmpCreatedAt,_tmpUpdatedAt);
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

  @Override
  public Flow<Exercise> getExerciseByIdFlow(final long exerciseId) {
    final String _sql = "SELECT * FROM exercises WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, exerciseId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exercises"}, new Callable<Exercise>() {
      @Override
      @Nullable
      public Exercise call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSubcategory = CursorUtil.getColumnIndexOrThrow(_cursor, "subcategory");
          final int _cursorIndexOfEquipment = CursorUtil.getColumnIndexOrThrow(_cursor, "equipment");
          final int _cursorIndexOfExerciseType = CursorUtil.getColumnIndexOrThrow(_cursor, "exercise_type");
          final int _cursorIndexOfDifficultyLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty_level");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfTips = CursorUtil.getColumnIndexOrThrow(_cursor, "tips");
          final int _cursorIndexOfMuscleGroups = CursorUtil.getColumnIndexOrThrow(_cursor, "muscle_groups");
          final int _cursorIndexOfSecondaryMuscles = CursorUtil.getColumnIndexOrThrow(_cursor, "secondary_muscles");
          final int _cursorIndexOfIsCompound = CursorUtil.getColumnIndexOrThrow(_cursor, "is_compound");
          final int _cursorIndexOfIsUnilateral = CursorUtil.getColumnIndexOrThrow(_cursor, "is_unilateral");
          final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "is_custom");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "is_favorite");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "image_url");
          final int _cursorIndexOfVideoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "video_url");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final Exercise _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final String _tmpSubcategory;
            if (_cursor.isNull(_cursorIndexOfSubcategory)) {
              _tmpSubcategory = null;
            } else {
              _tmpSubcategory = _cursor.getString(_cursorIndexOfSubcategory);
            }
            final String _tmpEquipment;
            if (_cursor.isNull(_cursorIndexOfEquipment)) {
              _tmpEquipment = null;
            } else {
              _tmpEquipment = _cursor.getString(_cursorIndexOfEquipment);
            }
            final String _tmpExerciseType;
            if (_cursor.isNull(_cursorIndexOfExerciseType)) {
              _tmpExerciseType = null;
            } else {
              _tmpExerciseType = _cursor.getString(_cursorIndexOfExerciseType);
            }
            final String _tmpDifficultyLevel;
            if (_cursor.isNull(_cursorIndexOfDifficultyLevel)) {
              _tmpDifficultyLevel = null;
            } else {
              _tmpDifficultyLevel = _cursor.getString(_cursorIndexOfDifficultyLevel);
            }
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final String _tmpTips;
            if (_cursor.isNull(_cursorIndexOfTips)) {
              _tmpTips = null;
            } else {
              _tmpTips = _cursor.getString(_cursorIndexOfTips);
            }
            final List<String> _tmpMuscleGroups;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfMuscleGroups)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfMuscleGroups);
            }
            _tmpMuscleGroups = __converters.toStringList(_tmp);
            final List<String> _tmpSecondaryMuscles;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfSecondaryMuscles)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfSecondaryMuscles);
            }
            _tmpSecondaryMuscles = __converters.toStringList(_tmp_1);
            final boolean _tmpIsCompound;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsCompound);
            _tmpIsCompound = _tmp_2 != 0;
            final boolean _tmpIsUnilateral;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsUnilateral);
            _tmpIsUnilateral = _tmp_3 != 0;
            final boolean _tmpIsCustom;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsCustom);
            _tmpIsCustom = _tmp_4 != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_5 != 0;
            final String _tmpImageUrl;
            if (_cursor.isNull(_cursorIndexOfImageUrl)) {
              _tmpImageUrl = null;
            } else {
              _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            }
            final String _tmpVideoUrl;
            if (_cursor.isNull(_cursorIndexOfVideoUrl)) {
              _tmpVideoUrl = null;
            } else {
              _tmpVideoUrl = _cursor.getString(_cursorIndexOfVideoUrl);
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
            _result = new Exercise(_tmpId,_tmpName,_tmpCategory,_tmpSubcategory,_tmpEquipment,_tmpExerciseType,_tmpDifficultyLevel,_tmpInstructions,_tmpTips,_tmpMuscleGroups,_tmpSecondaryMuscles,_tmpIsCompound,_tmpIsUnilateral,_tmpIsCustom,_tmpIsFavorite,_tmpImageUrl,_tmpVideoUrl,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
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
  public Object getAllCategories(final Continuation<? super List<String>> $completion) {
    final String _sql = "SELECT DISTINCT category FROM exercises ORDER BY category ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
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
    }, $completion);
  }

  @Override
  public Object getAllEquipment(final Continuation<? super List<String>> $completion) {
    final String _sql = "SELECT DISTINCT equipment FROM exercises ORDER BY equipment ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
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
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
