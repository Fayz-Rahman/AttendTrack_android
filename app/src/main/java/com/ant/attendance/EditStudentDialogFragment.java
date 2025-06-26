package com.ant.attendance;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.textfield.TextInputEditText;

public class EditStudentDialogFragment extends DialogFragment {

    private EditStudentDialogListener listener;
    private TextInputEditText editTextStudentName;
    private TextInputEditText editTextStudentId;

    private static final String ARG_POSITION = "position";
    private static final String ARG_STUDENT_NAME = "studentName";
    private static final String ARG_STUDENT_ID = "studentId";

    public interface EditStudentDialogListener {
        void onStudentUpdated(int position, String studentName, String studentId);
    }

    public static EditStudentDialogFragment newInstance(int position, String studentName, String studentId) {
        EditStudentDialogFragment fragment = new EditStudentDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_STUDENT_NAME, studentName);
        args.putString(ARG_STUDENT_ID, studentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (EditStudentDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement EditStudentDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_student, null);


        TextView title = view.findViewById(R.id.dialog_title);
        title.setText("Edit Student");
        Button createButton = view.findViewById(R.id.button_create);
        createButton.setText("Save");

        builder.setView(view);

        editTextStudentName = view.findViewById(R.id.edit_text_student_name);
        editTextStudentId = view.findViewById(R.id.edit_text_student_id);
        Button cancelButton = view.findViewById(R.id.button_cancel);

        if (getArguments() != null) {
            editTextStudentName.setText(getArguments().getString(ARG_STUDENT_NAME));
            editTextStudentId.setText(getArguments().getString(ARG_STUDENT_ID));
        }

        cancelButton.setOnClickListener(v -> dismiss());

        createButton.setOnClickListener(v -> {
            String studentName = editTextStudentName.getText().toString().trim();
            String studentId = editTextStudentId.getText().toString().trim();

            if (studentName.isEmpty() || studentId.isEmpty()) {
                // Add error handling
                return;
            }

            if (getArguments() != null) {
                int position = getArguments().getInt(ARG_POSITION);
                listener.onStudentUpdated(position, studentName, studentId);
            }
            dismiss();
        });

        return builder.create();
    }
}
