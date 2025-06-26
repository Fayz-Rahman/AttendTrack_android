package com.ant.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

public class ClassesActivity extends AppCompatActivity implements AddClassDialogFragment.AddClassDialogListener,
        EditClassDialogFragment.EditClassDialogListener,
        SwipeGestureCallback.SwipeActions,
        ClassAdapter.OnItemClickListener {

    private List<ClassItem> classList = new ArrayList<>();
    private ClassAdapter classAdapter;
    private RecyclerView recyclerView;
    private View emptyStateView;
    private AppDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_classes);


        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());


        database = AppDatabase.getInstance(this);
        recyclerView = findViewById(R.id.classes_recycler_view);
        emptyStateView = findViewById(R.id.empty_state_view);
        FloatingActionButton fabAddClass = findViewById(R.id.fab_add_class);


        classAdapter = new ClassAdapter(classList);
        classAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(classAdapter);


        SwipeGestureCallback swipeGestureCallback = new SwipeGestureCallback(this, this);
        new ItemTouchHelper(swipeGestureCallback).attachToRecyclerView(recyclerView);


        fabAddClass.setOnClickListener(v -> new AddClassDialogFragment().show(getSupportFragmentManager(), "AddClassDialog"));


        View mainContainer = findViewById(R.id.main_container);
        ViewCompat.setOnApplyWindowInsetsListener(mainContainer, (v, insets) -> {

            int systemBarsTop = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int systemBarsBottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;


            v.setPadding(v.getPaddingLeft(),
                    systemBarsTop,
                    v.getPaddingRight(),
                    0);


            FloatingActionButton fab = findViewById(R.id.fab_add_class);
            ViewGroup.MarginLayoutParams fabLayoutParams = (ViewGroup.MarginLayoutParams) fab.getLayoutParams();

            final int originalBottomMargin = (int) (16 * getResources().getDisplayMetrics().density); // Convert 16dp to pixels
            fabLayoutParams.bottomMargin = systemBarsBottom + originalBottomMargin;
            fab.setLayoutParams(fabLayoutParams);

            return insets;
        });

        loadClasses();
    }



    private void loadClasses() {
        executorService.execute(() -> {
            List<ClassItem> loadedClasses = database.classDao().getAllClasses();
            handler.post(() -> {
                classList.clear();
                classList.addAll(loadedClasses);
                classAdapter.notifyDataSetChanged();
                updateUI();
            });
        });
    }

    @Override
    public void onClassAdded(ClassItem newClass) {
        executorService.execute(() -> {
            database.classDao().insert(newClass);
            handler.post(this::loadClasses);
        });
    }

    @Override
    public void onDialogSaveClick(int position, String className, String period) {
        ClassItem itemToUpdate = classList.get(position);
        itemToUpdate.setClassName(className);
        itemToUpdate.setPeriod(period);

        executorService.execute(() -> {
            database.classDao().update(itemToUpdate);
            handler.post(this::loadClasses);
        });
    }

    @Override
    public void onEdit(int position) {
        ClassItem item = classList.get(position);
        DialogFragment dialog = EditClassDialogFragment.newInstance(position, item.getClassName(), item.getPeriod());
        dialog.show(getSupportFragmentManager(), "EditClassDialog");
        classAdapter.notifyItemChanged(position);
    }

    @Override
    public void onDelete(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Class")
                .setMessage("Are you sure you want to delete this class? This will also delete all associated students.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    ClassItem itemToDelete = classList.get(position);
                    executorService.execute(() -> {
                        database.classDao().delete(itemToDelete);
                        handler.post(this::loadClasses);
                    });
                })
                .setNegativeButton("Cancel", (dialog, which) -> classAdapter.notifyItemChanged(position))
                .show();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, StudentListActivity.class);
        ClassItem clickedClass = classList.get(position);
        intent.putExtra("CLASS_ID", clickedClass.getId());
        intent.putExtra("CLASS_NAME", clickedClass.getClassName());
        startActivity(intent);
    }

    private void updateUI() {
        if (classList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
        }
    }
}