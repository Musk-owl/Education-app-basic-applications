package com.example.edunow;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EnrolledCourseAdapter extends RecyclerView.Adapter<EnrolledCourseAdapter.ViewHolder> {

    private Context context;
    private List<User.EnrolledCourse> enrolledCourses;

    public EnrolledCourseAdapter(Context context, List<User.EnrolledCourse> enrolledCourses) {
        this.context = context;
        this.enrolledCourses = enrolledCourses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_enrolled_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User.EnrolledCourse course = enrolledCourses.get(position);

        holder.courseTitle.setText(course.getCourseTitle());
        holder.progressText.setText(course.getProgress() + "% Complete");
        holder.progressBar.setProgress(course.getProgress());

        holder.itemView.setOnClickListener(v -> {
            // Navigate to Course Detail instead
            Intent intent = new Intent(context, CourseDetailActivity.class);
            intent.putExtra("COURSE_TITLE", course.getCourseTitle());
            intent.putExtra("COURSE_DURATION", "Enrolled"); // Or fetch from Firebase
            intent.putExtra("COURSE_IMAGE_RES_ID", R.drawable.ux_design_placeholder);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return enrolledCourses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseTitle;
        TextView progressText;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.enrolled_course_title);
            progressText = itemView.findViewById(R.id.enrolled_course_progress_text);
            progressBar = itemView.findViewById(R.id.enrolled_course_progress_bar);
        }
    }
}
