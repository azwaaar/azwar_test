package com.azwar.test.databases;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.azwar.test.models.ResultModel;


@Dao
public interface ResultDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long userResult(ResultModel result);

    @Query("SELECT * FROM result_model WHERE id NOTNULL")
    ResultModel[] allResult();

    // id
    @Query("SELECT * FROM result_model WHERE id  == :id")
    ResultModel findByIdResult(Long id);

    // getAll
    @Query("SELECT * FROM result_model")
    ResultModel[] getAllResult();

    // untuk cursor
    @Query("SELECT * FROM result_model WHERE id NOTNULL")
    Cursor cursorResult();

    // hapus dari database
    @Delete
    void deleteItem(ResultModel selfAssessment);

}
