package com.mistakenotebook.data.db;

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
import com.mistakenotebook.data.model.Question;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class QuestionDao_Impl implements QuestionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Question> __insertionAdapterOfQuestion;

  private final EntityDeletionOrUpdateAdapter<Question> __updateAdapterOfQuestion;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public QuestionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfQuestion = new EntityInsertionAdapter<Question>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `questions` (`id`,`content`,`optionA`,`optionB`,`optionC`,`optionD`,`answer`,`createdAt`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Question entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getContent() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getContent());
        }
        if (entity.getOptionA() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getOptionA());
        }
        if (entity.getOptionB() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getOptionB());
        }
        if (entity.getOptionC() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getOptionC());
        }
        if (entity.getOptionD() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getOptionD());
        }
        if (entity.getAnswer() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getAnswer());
        }
        statement.bindLong(8, entity.getCreatedAt());
      }
    };
    this.__updateAdapterOfQuestion = new EntityDeletionOrUpdateAdapter<Question>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `questions` SET `id` = ?,`content` = ?,`optionA` = ?,`optionB` = ?,`optionC` = ?,`optionD` = ?,`answer` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Question entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getContent() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getContent());
        }
        if (entity.getOptionA() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getOptionA());
        }
        if (entity.getOptionB() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getOptionB());
        }
        if (entity.getOptionC() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getOptionC());
        }
        if (entity.getOptionD() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getOptionD());
        }
        if (entity.getAnswer() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getAnswer());
        }
        statement.bindLong(8, entity.getCreatedAt());
        if (entity.getId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM questions WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final Question question, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfQuestion.insert(question);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<Question> questions,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfQuestion.insert(questions);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Question question, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfQuestion.handle(question);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, id);
        }
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
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Question>> getAllFlow() {
    final String _sql = "SELECT * FROM questions ORDER BY createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"questions"}, new Callable<List<Question>>() {
      @Override
      @NonNull
      public List<Question> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfOptionA = CursorUtil.getColumnIndexOrThrow(_cursor, "optionA");
          final int _cursorIndexOfOptionB = CursorUtil.getColumnIndexOrThrow(_cursor, "optionB");
          final int _cursorIndexOfOptionC = CursorUtil.getColumnIndexOrThrow(_cursor, "optionC");
          final int _cursorIndexOfOptionD = CursorUtil.getColumnIndexOrThrow(_cursor, "optionD");
          final int _cursorIndexOfAnswer = CursorUtil.getColumnIndexOrThrow(_cursor, "answer");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<Question> _result = new ArrayList<Question>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Question _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            final String _tmpOptionA;
            if (_cursor.isNull(_cursorIndexOfOptionA)) {
              _tmpOptionA = null;
            } else {
              _tmpOptionA = _cursor.getString(_cursorIndexOfOptionA);
            }
            final String _tmpOptionB;
            if (_cursor.isNull(_cursorIndexOfOptionB)) {
              _tmpOptionB = null;
            } else {
              _tmpOptionB = _cursor.getString(_cursorIndexOfOptionB);
            }
            final String _tmpOptionC;
            if (_cursor.isNull(_cursorIndexOfOptionC)) {
              _tmpOptionC = null;
            } else {
              _tmpOptionC = _cursor.getString(_cursorIndexOfOptionC);
            }
            final String _tmpOptionD;
            if (_cursor.isNull(_cursorIndexOfOptionD)) {
              _tmpOptionD = null;
            } else {
              _tmpOptionD = _cursor.getString(_cursorIndexOfOptionD);
            }
            final String _tmpAnswer;
            if (_cursor.isNull(_cursorIndexOfAnswer)) {
              _tmpAnswer = null;
            } else {
              _tmpAnswer = _cursor.getString(_cursorIndexOfAnswer);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new Question(_tmpId,_tmpContent,_tmpOptionA,_tmpOptionB,_tmpOptionC,_tmpOptionD,_tmpAnswer,_tmpCreatedAt);
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
  public Object getAll(final Continuation<? super List<Question>> $completion) {
    final String _sql = "SELECT * FROM questions ORDER BY createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Question>>() {
      @Override
      @NonNull
      public List<Question> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfOptionA = CursorUtil.getColumnIndexOrThrow(_cursor, "optionA");
          final int _cursorIndexOfOptionB = CursorUtil.getColumnIndexOrThrow(_cursor, "optionB");
          final int _cursorIndexOfOptionC = CursorUtil.getColumnIndexOrThrow(_cursor, "optionC");
          final int _cursorIndexOfOptionD = CursorUtil.getColumnIndexOrThrow(_cursor, "optionD");
          final int _cursorIndexOfAnswer = CursorUtil.getColumnIndexOrThrow(_cursor, "answer");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<Question> _result = new ArrayList<Question>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Question _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            final String _tmpOptionA;
            if (_cursor.isNull(_cursorIndexOfOptionA)) {
              _tmpOptionA = null;
            } else {
              _tmpOptionA = _cursor.getString(_cursorIndexOfOptionA);
            }
            final String _tmpOptionB;
            if (_cursor.isNull(_cursorIndexOfOptionB)) {
              _tmpOptionB = null;
            } else {
              _tmpOptionB = _cursor.getString(_cursorIndexOfOptionB);
            }
            final String _tmpOptionC;
            if (_cursor.isNull(_cursorIndexOfOptionC)) {
              _tmpOptionC = null;
            } else {
              _tmpOptionC = _cursor.getString(_cursorIndexOfOptionC);
            }
            final String _tmpOptionD;
            if (_cursor.isNull(_cursorIndexOfOptionD)) {
              _tmpOptionD = null;
            } else {
              _tmpOptionD = _cursor.getString(_cursorIndexOfOptionD);
            }
            final String _tmpAnswer;
            if (_cursor.isNull(_cursorIndexOfAnswer)) {
              _tmpAnswer = null;
            } else {
              _tmpAnswer = _cursor.getString(_cursorIndexOfAnswer);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new Question(_tmpId,_tmpContent,_tmpOptionA,_tmpOptionB,_tmpOptionC,_tmpOptionD,_tmpAnswer,_tmpCreatedAt);
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
  public Object getById(final String id, final Continuation<? super Question> $completion) {
    final String _sql = "SELECT * FROM questions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Question>() {
      @Override
      @Nullable
      public Question call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfOptionA = CursorUtil.getColumnIndexOrThrow(_cursor, "optionA");
          final int _cursorIndexOfOptionB = CursorUtil.getColumnIndexOrThrow(_cursor, "optionB");
          final int _cursorIndexOfOptionC = CursorUtil.getColumnIndexOrThrow(_cursor, "optionC");
          final int _cursorIndexOfOptionD = CursorUtil.getColumnIndexOrThrow(_cursor, "optionD");
          final int _cursorIndexOfAnswer = CursorUtil.getColumnIndexOrThrow(_cursor, "answer");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final Question _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            final String _tmpOptionA;
            if (_cursor.isNull(_cursorIndexOfOptionA)) {
              _tmpOptionA = null;
            } else {
              _tmpOptionA = _cursor.getString(_cursorIndexOfOptionA);
            }
            final String _tmpOptionB;
            if (_cursor.isNull(_cursorIndexOfOptionB)) {
              _tmpOptionB = null;
            } else {
              _tmpOptionB = _cursor.getString(_cursorIndexOfOptionB);
            }
            final String _tmpOptionC;
            if (_cursor.isNull(_cursorIndexOfOptionC)) {
              _tmpOptionC = null;
            } else {
              _tmpOptionC = _cursor.getString(_cursorIndexOfOptionC);
            }
            final String _tmpOptionD;
            if (_cursor.isNull(_cursorIndexOfOptionD)) {
              _tmpOptionD = null;
            } else {
              _tmpOptionD = _cursor.getString(_cursorIndexOfOptionD);
            }
            final String _tmpAnswer;
            if (_cursor.isNull(_cursorIndexOfAnswer)) {
              _tmpAnswer = null;
            } else {
              _tmpAnswer = _cursor.getString(_cursorIndexOfAnswer);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new Question(_tmpId,_tmpContent,_tmpOptionA,_tmpOptionB,_tmpOptionC,_tmpOptionD,_tmpAnswer,_tmpCreatedAt);
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
  public Object count(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM questions";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
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
