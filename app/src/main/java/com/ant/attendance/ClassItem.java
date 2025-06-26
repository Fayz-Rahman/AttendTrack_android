package com.ant.attendance;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "classes")
public class ClassItem {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String className;
    private String period;


    public ClassItem(String className, String period) {
        this.className = className;
        this.period = period;
    }

    // Getters
    public long getId() { return id; }
    public String getClassName() { return className; }
    public String getPeriod() { return period; }

    // Setters
    public void setId(long id) { this.id = id; }
    public void setClassName(String className) { this.className = className; }
    public void setPeriod(String period) { this.period = period; }
}
