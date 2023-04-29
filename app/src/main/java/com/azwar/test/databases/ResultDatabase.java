package com.azwar.test.databases;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.azwar.test.models.ResultModel;

@Database(entities = {ResultModel.class}, version = 1, exportSchema = false)
//@TypeConverters({Converters.class})
public abstract class ResultDatabase extends RoomDatabase {

    private static ResultDatabase content;

    public abstract ResultDAO resultDAO();

    public static ResultDatabase getInstance(Context context) {
        synchronized (ResultModel.class) {
            if (content == null) {
                content = Room.databaseBuilder(context, ResultDatabase.class, "attendance").allowMainThreadQueries().build();
                Log.e("ROOM" , content.toString());
            }
        }
        return content;
    }
}
