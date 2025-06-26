package com.ant.attendance;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ClassDao {
    @Insert
    void insert(ClassItem classItem);

    @Update
    void update(ClassItem classItem);

    @Delete
    void delete(ClassItem classItem);

    @Query("SELECT * FROM classes ORDER BY className ASC")
    List<ClassItem> getAllClasses();
}
