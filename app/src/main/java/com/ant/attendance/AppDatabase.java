package com.ant.attendance;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ClassItem.class, StudentItem.class, AttendanceRecord.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ClassDao classDao();
    public abstract StudentDao studentDao();
    public abstract AttendanceDao attendanceDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "attendance_database")

                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
