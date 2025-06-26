package com.ant.attendance;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.content.Context;
import androidx.core.content.ContextCompat;

public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.ViewHolder> {

    private List<AttendanceRecord> records;

    public StudentAttendanceAdapter(List<AttendanceRecord> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_student_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceRecord record = records.get(position);
        holder.dateText.setText(record.getDate());
        holder.statusText.setText(record.getStatus().toUpperCase());


        Context context = holder.itemView.getContext();

        if ("Present".equalsIgnoreCase(record.getStatus())) {

            holder.statusText.setBackgroundColor(ContextCompat.getColor(context, R.color.attendance_present_green));
        } else {

            holder.statusText.setBackgroundColor(ContextCompat.getColor(context, R.color.attendance_absent_red));
        }
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;
        TextView statusText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.date_text);
            statusText = itemView.findViewById(R.id.status_text);
        }
    }
}
