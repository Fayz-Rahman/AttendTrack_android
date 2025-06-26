package com.ant.attendance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private List<ClassItem> classList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ClassAdapter(List<ClassItem> classList) {
        this.classList = classList;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_class, parent, false);
        return new ClassViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        ClassItem currentItem = classList.get(position);
        holder.className.setText(currentItem.getClassName());
        holder.period.setText(currentItem.getPeriod());
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }


    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        public TextView className;
        public TextView period;

        public ClassViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            className = itemView.findViewById(R.id.class_name_text);
            period = itemView.findViewById(R.id.class_term_text);

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
