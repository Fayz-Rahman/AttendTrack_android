package com.ant.attendance;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "attendance_records",
        primaryKeys = {"studentId", "date"},
        foreignKeys = {
                @ForeignKey(entity = ClassItem.class,
                        parentColumns = "id",
                        childColumns = "classId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = StudentItem.class,
                        parentColumns = "id",
                        childColumns = "studentId",
                        onDelete = ForeignKey.CASCADE)
        })
public class AttendanceRecord {


    private long classId;
    @NonNull
    private long studentId;
    @NonNull
    private String date;
    private String status;

    public AttendanceRecord(long classId, long studentId, @NonNull String date, String status) {
        this.classId = classId;
        this.studentId = studentId;
        this.date = date;
        this.status = status;
    }

    public long getClassId() { return classId; }
    public void setClassId(long classId) { this.classId = classId; }
    public long getStudentId() { return studentId; }
    public void setStudentId(long studentId) { this.studentId = studentId; }
    @NonNull
    public String getDate() { return date; }
    public void setDate(@NonNull String date) { this.date = date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
