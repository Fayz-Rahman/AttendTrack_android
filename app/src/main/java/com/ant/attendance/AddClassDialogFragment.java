package com.ant.attendance;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.textfield.TextInputEditText;

public class AddClassDialogFragment extends DialogFragment {

    public interface AddClassDialogListener {
        void onClassAdded(ClassItem newClass);
    }

    private TextInputEditText editTextClassName;
    private TextInputEditText editTextClassTerm;
    private Button buttonCancel;
    private Button buttonCreate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_class, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextClassName = view.findViewById(R.id.edit_text_class_name);
        editTextClassTerm = view.findViewById(R.id.edit_text_class_term);
        buttonCancel = view.findViewById(R.id.button_cancel);
        buttonCreate = view.findViewById(R.id.button_create);

        buttonCancel.setOnClickListener(v -> dismiss());

        buttonCreate.setOnClickListener(v -> {
            String className = editTextClassName.getText().toString().trim();
            String classTerm = editTextClassTerm.getText().toString().trim();

            if (className.isEmpty() || classTerm.isEmpty()) {
                return;
            }

            AddClassDialogListener listener = (AddClassDialogListener) getActivity();
            if (listener != null) {
                listener.onClassAdded(new ClassItem(className, classTerm));
            }

            dismiss();
        });
    }
}
