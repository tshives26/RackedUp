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
import com.chilluminati.rackedup.data.database.entity.UserProfile;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserProfileDao_Impl implements UserProfileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserProfile> __insertionAdapterOfUserProfile;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<UserProfile> __updateAdapterOfUserProfile;

  public UserProfileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserProfile = new EntityInsertionAdapter<UserProfile>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `user_profiles` (`id`,`name`,`email`,`age`,`gender`,`height_cm`,`weight_kg`,`activity_level`,`fitness_goal`,`experience_level`,`preferred_weight_unit`,`preferred_distance_unit`,`default_rest_time`,`timezone`,`profile_image_url`,`is_active`,`created_at`,`updated_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserProfile entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEmail());
        }
        if (entity.getAge() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getAge());
        }
        if (entity.getGender() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getGender());
        }
        if (entity.getHeightCm() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getHeightCm());
        }
        if (entity.getWeightKg() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getWeightKg());
        }
        if (entity.getActivityLevel() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getActivityLevel());
        }
        if (entity.getFitnessGoal() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getFitnessGoal());
        }
        if (entity.getExperienceLevel() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getExperienceLevel());
        }
        if (entity.getPreferredWeightUnit() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getPreferredWeightUnit());
        }
        if (entity.getPreferredDistanceUnit() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getPreferredDistanceUnit());
        }
        statement.bindLong(13, entity.getDefaultRestTime());
        if (entity.getTimezone() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getTimezone());
        }
        if (entity.getProfileImageUrl() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getProfileImageUrl());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(16, _tmp);
        final Long _tmp_1 = __converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_1 == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, _tmp_1);
        }
        final Long _tmp_2 = __converters.dateToTimestamp(entity.getUpdatedAt());
        if (_tmp_2 == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, _tmp_2);
        }
      }
    };
    this.__updateAdapterOfUserProfile = new EntityDeletionOrUpdateAdapter<UserProfile>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `user_profiles` SET `id` = ?,`name` = ?,`email` = ?,`age` = ?,`gender` = ?,`height_cm` = ?,`weight_kg` = ?,`activity_level` = ?,`fitness_goal` = ?,`experience_level` = ?,`preferred_weight_unit` = ?,`preferred_distance_unit` = ?,`default_rest_time` = ?,`timezone` = ?,`profile_image_url` = ?,`is_active` = ?,`created_at` = ?,`updated_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserProfile entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEmail());
        }
        if (entity.getAge() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getAge());
        }
        if (entity.getGender() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getGender());
        }
        if (entity.getHeightCm() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getHeightCm());
        }
        if (entity.getWeightKg() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getWeightKg());
        }
        if (entity.getActivityLevel() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getActivityLevel());
        }
        if (entity.getFitnessGoal() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getFitnessGoal());
        }
        if (entity.getExperienceLevel() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getExperienceLevel());
        }
        if (entity.getPreferredWeightUnit() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getPreferredWeightUnit());
        }
        if (entity.getPreferredDistanceUnit() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getPreferredDistanceUnit());
        }
        statement.bindLong(13, entity.getDefaultRestTime());
        if (entity.getTimezone() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getTimezone());
        }
        if (entity.getProfileImageUrl() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getProfileImageUrl());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(16, _tmp);
        final Long _tmp_1 = __converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_1 == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, _tmp_1);
        }
        final Long _tmp_2 = __converters.dateToTimestamp(entity.getUpdatedAt());
        if (_tmp_2 == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, _tmp_2);
        }
        statement.bindLong(19, entity.getId());
      }
    };
  }

  @Override
  public Object insertProfile(final UserProfile profile,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfUserProfile.insertAndReturnId(profile);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateProfile(final UserProfile profile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUserProfile.handle(profile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getActiveProfile(final Continuation<? super UserProfile> $completion) {
    final String _sql = "SELECT * FROM user_profiles WHERE is_active = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserProfile>() {
      @Override
      @Nullable
      public UserProfile call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfHeightCm = CursorUtil.getColumnIndexOrThrow(_cursor, "height_cm");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weight_kg");
          final int _cursorIndexOfActivityLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "activity_level");
          final int _cursorIndexOfFitnessGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "fitness_goal");
          final int _cursorIndexOfExperienceLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "experience_level");
          final int _cursorIndexOfPreferredWeightUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "preferred_weight_unit");
          final int _cursorIndexOfPreferredDistanceUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "preferred_distance_unit");
          final int _cursorIndexOfDefaultRestTime = CursorUtil.getColumnIndexOrThrow(_cursor, "default_rest_time");
          final int _cursorIndexOfTimezone = CursorUtil.getColumnIndexOrThrow(_cursor, "timezone");
          final int _cursorIndexOfProfileImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_image_url");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final UserProfile _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final Integer _tmpAge;
            if (_cursor.isNull(_cursorIndexOfAge)) {
              _tmpAge = null;
            } else {
              _tmpAge = _cursor.getInt(_cursorIndexOfAge);
            }
            final String _tmpGender;
            if (_cursor.isNull(_cursorIndexOfGender)) {
              _tmpGender = null;
            } else {
              _tmpGender = _cursor.getString(_cursorIndexOfGender);
            }
            final Double _tmpHeightCm;
            if (_cursor.isNull(_cursorIndexOfHeightCm)) {
              _tmpHeightCm = null;
            } else {
              _tmpHeightCm = _cursor.getDouble(_cursorIndexOfHeightCm);
            }
            final Double _tmpWeightKg;
            if (_cursor.isNull(_cursorIndexOfWeightKg)) {
              _tmpWeightKg = null;
            } else {
              _tmpWeightKg = _cursor.getDouble(_cursorIndexOfWeightKg);
            }
            final String _tmpActivityLevel;
            if (_cursor.isNull(_cursorIndexOfActivityLevel)) {
              _tmpActivityLevel = null;
            } else {
              _tmpActivityLevel = _cursor.getString(_cursorIndexOfActivityLevel);
            }
            final String _tmpFitnessGoal;
            if (_cursor.isNull(_cursorIndexOfFitnessGoal)) {
              _tmpFitnessGoal = null;
            } else {
              _tmpFitnessGoal = _cursor.getString(_cursorIndexOfFitnessGoal);
            }
            final String _tmpExperienceLevel;
            if (_cursor.isNull(_cursorIndexOfExperienceLevel)) {
              _tmpExperienceLevel = null;
            } else {
              _tmpExperienceLevel = _cursor.getString(_cursorIndexOfExperienceLevel);
            }
            final String _tmpPreferredWeightUnit;
            if (_cursor.isNull(_cursorIndexOfPreferredWeightUnit)) {
              _tmpPreferredWeightUnit = null;
            } else {
              _tmpPreferredWeightUnit = _cursor.getString(_cursorIndexOfPreferredWeightUnit);
            }
            final String _tmpPreferredDistanceUnit;
            if (_cursor.isNull(_cursorIndexOfPreferredDistanceUnit)) {
              _tmpPreferredDistanceUnit = null;
            } else {
              _tmpPreferredDistanceUnit = _cursor.getString(_cursorIndexOfPreferredDistanceUnit);
            }
            final int _tmpDefaultRestTime;
            _tmpDefaultRestTime = _cursor.getInt(_cursorIndexOfDefaultRestTime);
            final String _tmpTimezone;
            if (_cursor.isNull(_cursorIndexOfTimezone)) {
              _tmpTimezone = null;
            } else {
              _tmpTimezone = _cursor.getString(_cursorIndexOfTimezone);
            }
            final String _tmpProfileImageUrl;
            if (_cursor.isNull(_cursorIndexOfProfileImageUrl)) {
              _tmpProfileImageUrl = null;
            } else {
              _tmpProfileImageUrl = _cursor.getString(_cursorIndexOfProfileImageUrl);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final Date _tmpCreatedAt;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            _tmpCreatedAt = __converters.fromTimestamp(_tmp_1);
            final Date _tmpUpdatedAt;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfUpdatedAt);
            }
            _tmpUpdatedAt = __converters.fromTimestamp(_tmp_2);
            _result = new UserProfile(_tmpId,_tmpName,_tmpEmail,_tmpAge,_tmpGender,_tmpHeightCm,_tmpWeightKg,_tmpActivityLevel,_tmpFitnessGoal,_tmpExperienceLevel,_tmpPreferredWeightUnit,_tmpPreferredDistanceUnit,_tmpDefaultRestTime,_tmpTimezone,_tmpProfileImageUrl,_tmpIsActive,_tmpCreatedAt,_tmpUpdatedAt);
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
