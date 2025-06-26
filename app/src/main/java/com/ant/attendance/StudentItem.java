package com.ant.attendance;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "students",
        indices = {@Index(value = {"classId", "studentId"}, unique = true)},
        foreignKeys = @ForeignKey(entity = ClassItem.class,
                parentColumns = "id",
                childColumns = "classId",
                onDelete = ForeignKey.CASCADE))
public class StudentItem {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long classId;
    private String studentId;
    private String studentName;


    public StudentItem(long classId, String studentName, String studentId) {
        this.classId = classId;
        this.studentName = studentName;
        this.studentId = studentId;
    }

    // Getters
    public long getId() { return id; }
    public long getClassId() { return classId; }
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }


    // Setters
    public void setId(long id) { this.id = id; }
    public void setClassId(long classId) { this.classId = classId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
}
