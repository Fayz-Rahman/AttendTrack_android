package com.ant.attendance;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class EditClassDialogFragment extends DialogFragment {

    private EditClassDialogListener listener;
    private EditText editTextClassName;
    private EditText editTextPeriod;

    private static final String ARG_POSITION = "position";
    private static final String ARG_CLASS_NAME = "className";
    private static final String ARG_PERIOD = "period";

    public interface EditClassDialogListener {
        void onDialogSaveClick(int position, String className, String period);
    }

    public static EditClassDialogFragment newInstance(int position, String className, String period) {
        EditClassDialogFragment fragment = new EditClassDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_CLASS_NAME, className);
        args.putString(ARG_PERIOD, period);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (EditClassDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement EditClassDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_class, null);

        builder.setView(view).setTitle("Edit Class");


        editTextClassName = view.findViewById(R.id.edit_text_class_name);
        editTextPeriod = view.findViewById(R.id.edit_text_period);
        Button cancelButton = view.findViewById(R.id.button_cancel);
        Button saveButton = view.findViewById(R.id.button_save);

        if (getArguments() != null) {
            editTextClassName.setText(getArguments().getString(ARG_CLASS_NAME));
            editTextPeriod.setText(getArguments().getString(ARG_PERIOD));
        }

        cancelButton.setOnClickListener(v -> dismiss());

        saveButton.setOnClickListener(v -> {
            String className = editTextClassName.getText().toString().trim();
            String period = editTextPeriod.getText().toString().trim();

            if (className.isEmpty()) {
                editTextClassName.setError("Class name cannot be empty");
                return;
            }

            if (getArguments() != null) {
                int position = getArguments().getInt(ARG_POSITION);
                listener.onDialogSaveClick(position, className, period);
            }
            dismiss();
        });

        return builder.create();
    }
}