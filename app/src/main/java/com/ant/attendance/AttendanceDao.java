package com.ant.attendance;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(AttendanceRecord record);

    @Query("SELECT * FROM attendance_records WHERE studentId = :studentId ORDER BY date DESC")
    List<AttendanceRecord> getAttendanceForStudent(long studentId);
}
