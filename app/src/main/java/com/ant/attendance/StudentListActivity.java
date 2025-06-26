package com.ant.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudentListActivity extends AppCompatActivity implements AddStudentDialogFragment.AddStudentDialogListener,
        EditStudentDialogFragment.EditStudentDialogListener,
        SwipeGestureCallback.SwipeActions,
        StudentAdapter.OnItemClickListener {

    private List<StudentItem> studentList = new ArrayList<>();
    private StudentAdapter studentAdapter;
    private RecyclerView recyclerView;
    private View emptyStateView;
    private AppDatabase database;
    private long currentClassId;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_student_list);

        database = AppDatabase.getInstance(this);

        currentClassId = getIntent().getLongExtra("CLASS_ID", -1);
        String className = getIntent().getStringExtra("CLASS_NAME");


        TextView titleTextView = findViewById(R.id.textView_app_title);
        titleTextView.setText(className);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());


        recyclerView = findViewById(R.id.student_recycler_view);
        emptyStateView = findViewById(R.id.empty_state_view);
        FloatingActionButton fabAddStudent = findViewById(R.id.fab_add_student);

        studentAdapter = new StudentAdapter(studentList);
        studentAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(studentAdapter);

        SwipeGestureCallback swipeGestureCallback = new SwipeGestureCallback(this, this);
        new ItemTouchHelper(swipeGestureCallback).attachToRecyclerView(recyclerView);

        fabAddStudent.setOnClickListener(v -> {
            AddStudentDialogFragment.newInstance(currentClassId).show(getSupportFragmentManager(), "AddStudentDialog");
        });


        View mainContainer = findViewById(R.id.main_container);
        ViewCompat.setOnApplyWindowInsetsListener(mainContainer, (v, insets) -> {
            int systemBarsTop = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int systemBarsBottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;

            v.setPadding(v.getPaddingLeft(), systemBarsTop, v.getPaddingRight(), 0);


            ViewGroup.MarginLayoutParams fabLayoutParams = (ViewGroup.MarginLayoutParams) fabAddStudent.getLayoutParams();
            final int originalBottomMargin = (int) (16 * getResources().getDisplayMetrics().density);
            fabLayoutParams.bottomMargin = systemBarsBottom + originalBottomMargin;
            fabAddStudent.setLayoutParams(fabLayoutParams);

            return insets;
        });

        loadStudents();
    }

    private void loadStudents() {
        executorService.execute(() -> {
            List<StudentItem> loadedStudents = database.studentDao().getStudentsForClass(currentClassId);
            handler.post(() -> {
                studentList.clear();
                studentList.addAll(loadedStudents);
                studentAdapter.notifyDataSetChanged();
                updateUI();
            });
        });
    }

    @Override
    public void onStudentAdded(StudentItem newStudent) {
        executorService.execute(() -> {
            StudentItem existingStudent = database.studentDao().getStudentByClassAndId(newStudent.getClassId(), newStudent.getStudentId());

            if (existingStudent == null) {
                database.studentDao().insert(newStudent);
                handler.post(this::loadStudents);
            } else {
                handler.post(() -> {
                    Toast.makeText(StudentListActivity.this, "A student with this ID already exists in this class.", Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    @Override
    public void onStudentUpdated(int position, String studentName, String studentId) {
        StudentItem itemToUpdate = studentList.get(position);
        itemToUpdate.setStudentName(studentName);
        itemToUpdate.setStudentId(studentId);

        executorService.execute(() -> {
            database.studentDao().update(itemToUpdate);
            handler.post(this::loadStudents);
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, StudentAttendanceActivity.class);
        StudentItem clickedStudent = studentList.get(position);
        intent.putExtra("STUDENT_ID", clickedStudent.getId());
        intent.putExtra("STUDENT_NAME", clickedStudent.getStudentName());
        startActivity(intent);
    }

    @Override
    public void onEdit(int position) {
        StudentItem item = studentList.get(position);
        DialogFragment dialog = EditStudentDialogFragment.newInstance(position, item.getStudentName(), item.getStudentId());
        dialog.show(getSupportFragmentManager(), "EditStudentDialog");
        studentAdapter.notifyItemChanged(position);
    }

    @Override
    public void onDelete(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Student")
                .setMessage("Are you sure you want to delete this student?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    StudentItem itemToDelete = studentList.get(position);
                    executorService.execute(() -> {
                        database.studentDao().delete(itemToDelete);
                        handler.post(this::loadStudents);
                    });
                })
                .setNegativeButton("Cancel", (dialog, which) -> studentAdapter.notifyItemChanged(position))
                .show();
    }

    private void updateUI() {
        if (studentList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
        }
    }
}