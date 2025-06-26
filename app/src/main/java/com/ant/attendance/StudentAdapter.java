package com.ant.attendance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<StudentItem> studentList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public StudentAdapter(List<StudentItem> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_student, parent, false);
        return new StudentViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        StudentItem currentItem = studentList.get(position);
        holder.studentName.setText(currentItem.getStudentName());
        holder.studentId.setText("ID: " + currentItem.getStudentId());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }



    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView studentName;
        public TextView studentId;

        public StudentViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            studentName = itemView.findViewById(R.id.student_name_text);
            studentId = itemView.findViewById(R.id.student_id_text);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
