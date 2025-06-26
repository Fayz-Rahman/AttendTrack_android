package com.ant.attendance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private List<StudentItem> studentList;

    public AttendanceAdapter(List<StudentItem> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_attendance, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        StudentItem currentStudent = studentList.get(position);
        holder.studentName.setText(currentStudent.getStudentName());
        holder.studentId.setText("ID: " + currentStudent.getStudentId());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView studentName;
        TextView studentId;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.student_name_text);
            studentId = itemView.findViewById(R.id.student_id_text);
        }
    }
}
