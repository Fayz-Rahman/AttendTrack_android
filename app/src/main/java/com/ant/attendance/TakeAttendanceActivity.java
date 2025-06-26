package com.ant.attendance;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TakeAttendanceActivity extends AppCompatActivity implements SwipeGestureCallback.SwipeActions {

    private Spinner classSpinner;
    private Button datePickerButton;
    private RecyclerView attendanceRecyclerView;
    private AttendanceAdapter attendanceAdapter;
    private List<StudentItem> studentList = new ArrayList<>();
    private List<ClassItem> classList = new ArrayList<>();
    private AppDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Calendar selectedDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_take_attendance);


        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());


        database = AppDatabase.getInstance(this);
        classSpinner = findViewById(R.id.spinner_classes);
        datePickerButton = findViewById(R.id.button_date_picker);
        attendanceRecyclerView = findViewById(R.id.attendance_recycler_view);

        setupRecyclerView();
        updateDateButton();

        datePickerButton.setOnClickListener(v -> showDatePickerDialog());


        View mainContainer = findViewById(R.id.main_container);
        ViewCompat.setOnApplyWindowInsetsListener(mainContainer, (v, insets) -> {
            int systemBarsTop = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int systemBarsBottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;

            v.setPadding(v.getPaddingLeft(), systemBarsTop, v.getPaddingRight(), systemBarsBottom);

            return insets;
        });

        loadClassesIntoSpinner();
    }

    private void setupRecyclerView() {
        attendanceAdapter = new AttendanceAdapter(studentList);
        attendanceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendanceRecyclerView.setAdapter(attendanceAdapter);


        SwipeGestureCallback swipeCallback = new SwipeGestureCallback(this, this,
                R.drawable.ic_present, R.drawable.ic_absent,
                Color.parseColor("#4CAF50"), Color.parseColor("#F44336"));
        new ItemTouchHelper(swipeCallback).attachToRecyclerView(attendanceRecyclerView);
    }

    private void loadClassesIntoSpinner() {
        executorService.execute(() -> {
            List<ClassItem> loadedClasses = database.classDao().getAllClasses();
            handler.post(() -> {
                classList.clear();
                classList.addAll(loadedClasses);

                if (classList.isEmpty()) {
                    Toast.makeText(this, "No classes found. Please add a class first.", Toast.LENGTH_LONG).show();
                    return;
                }

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item,
                        getClassNames(classList));
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                classSpinner.setAdapter(spinnerAdapter);

                classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        loadStudentsForClass(classList.get(position).getId());
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            });
        });
    }

    private void loadStudentsForClass(long classId) {
        executorService.execute(() -> {
            List<StudentItem> loadedStudents = database.studentDao().getStudentsForClass(classId);
            handler.post(() -> {
                studentList.clear();
                studentList.addAll(loadedStudents);
                attendanceAdapter.notifyDataSetChanged();
            });
        });
    }

    private void showDatePickerDialog() {

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select a Date");

        builder.setSelection(selectedDate.getTimeInMillis());


        final MaterialDatePicker<Long> datePicker = builder.build();


        datePicker.addOnPositiveButtonClickListener(selection -> {

            selectedDate.setTimeInMillis(selection);

            updateDateButton();
        });


        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    private void updateDateButton() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        datePickerButton.setText("Date: " + dateFormat.format(selectedDate.getTime()));
    }

    private List<String> getClassNames(List<ClassItem> classes) {
        List<String> names = new ArrayList<>();
        for (ClassItem item : classes) {
            names.add(item.getClassName());
        }
        return names;
    }

    @Override
    public void onEdit(int position) { // Right Swipe for Present
        markAttendance(position, "Present");
    }

    @Override
    public void onDelete(int position) { // Left Swipe for Absent
        markAttendance(position, "Absent");
    }

    private void markAttendance(int position, String status) {
        if (studentList.isEmpty() || classList.isEmpty()) {
            Toast.makeText(this, "Please select a class first.", Toast.LENGTH_SHORT).show();
            attendanceAdapter.notifyItemChanged(position);
            return;
        }

        StudentItem student = studentList.get(position);
        long classId = classList.get(classSpinner.getSelectedItemPosition()).getId();
        SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = dbDateFormat.format(selectedDate.getTime());

        AttendanceRecord record = new AttendanceRecord(classId, student.getId(), dateString, status);

        executorService.execute(() -> database.attendanceDao().insertOrUpdate(record));

        Toast.makeText(this, student.getStudentName() + " marked as " + status, Toast.LENGTH_SHORT).show();
        attendanceAdapter.notifyItemChanged(position);
    }
}