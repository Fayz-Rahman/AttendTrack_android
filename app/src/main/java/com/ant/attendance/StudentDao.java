package com.ant.attendance;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(StudentItem studentItem);

    @Update
    void update(StudentItem studentItem);

    @Delete
    void delete(StudentItem studentItem);

    @Query("SELECT * FROM students WHERE classId = :classId ORDER BY studentName ASC")
    List<StudentItem> getStudentsForClass(long classId);

    @Query("SELECT * FROM students WHERE classId = :classId AND studentId = :studentId LIMIT 1")
    StudentItem getStudentByClassAndId(long classId, String studentId);
}
