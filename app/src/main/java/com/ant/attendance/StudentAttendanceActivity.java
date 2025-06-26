package com.ant.attendance;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudentAttendanceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAttendanceAdapter adapter;
    private List<AttendanceRecord> attendanceRecords = new ArrayList<>();
    private AppDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_student_attendance);

        long studentId = getIntent().getLongExtra("STUDENT_ID", -1);
        String studentName = getIntent().getStringExtra("STUDENT_NAME");


        TextView titleTextView = findViewById(R.id.textView_app_title);
        titleTextView.setText(studentName + "'s Attendance");

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());


        recyclerView = findViewById(R.id.student_attendance_recycler_view);
        database = AppDatabase.getInstance(this);
        adapter = new StudentAttendanceAdapter(attendanceRecords);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        View mainContainer = findViewById(R.id.main_container);
        ViewCompat.setOnApplyWindowInsetsListener(mainContainer, (v, insets) -> {
            int systemBarsTop = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int systemBarsBottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;

            v.setPadding(v.getPaddingLeft(), systemBarsTop, v.getPaddingRight(), systemBarsBottom);

            return insets;
        });

        loadAttendance(studentId);
    }

    private void loadAttendance(long studentId) {
        executorService.execute(() -> {
            List<AttendanceRecord> records = database.attendanceDao().getAttendanceForStudent(studentId);
            handler.post(() -> {
                attendanceRecords.clear();
                attendanceRecords.addAll(records);
                adapter.notifyDataSetChanged();
            });
        });
    }

}