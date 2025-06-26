package com.ant.attendance;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.textfield.TextInputEditText;

public class AddStudentDialogFragment extends DialogFragment {

    public interface AddStudentDialogListener {
        void onStudentAdded(StudentItem newStudent);
    }

    private AddStudentDialogListener listener;
    private TextInputEditText editTextStudentName;
    private TextInputEditText editTextStudentId;
    private static final String ARG_CLASS_ID = "classId";
    private long classId;

    public static AddStudentDialogFragment newInstance(long classId) {
        AddStudentDialogFragment fragment = new AddStudentDialogFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CLASS_ID, classId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddStudentDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddStudentDialogListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            classId = getArguments().getLong(ARG_CLASS_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextStudentName = view.findViewById(R.id.edit_text_student_name);
        editTextStudentId = view.findViewById(R.id.edit_text_student_id);
        Button buttonCancel = view.findViewById(R.id.button_cancel);
        Button buttonCreate = view.findViewById(R.id.button_create);

        buttonCancel.setOnClickListener(v -> dismiss());

        buttonCreate.setOnClickListener(v -> {
            String studentName = editTextStudentName.getText().toString().trim();
            String studentId = editTextStudentId.getText().toString().trim();

            if (studentName.isEmpty()) {
                editTextStudentName.setError("Student name cannot be empty");
                return;
            }
            if (studentId.isEmpty()) {
                editTextStudentId.setError("Student ID cannot be empty");
                return;
            }

            StudentItem newStudent = new StudentItem(classId, studentName, studentId);
            listener.onStudentAdded(newStudent);
            dismiss();
        });
    }
}
