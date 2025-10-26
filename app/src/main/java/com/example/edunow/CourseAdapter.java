package com.example.edunow;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private final List<Course> courseList;
    private final Context context;

    public CourseAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_horizontal, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {
        final Course course = courseList.get(position);

        holder.courseTitle.setText(course.getTitle());
        holder.courseDuration.setText(course.getDuration());

        // Set course image
        if (course.getImageResourceId() != 0) {
            holder.courseImage.setImageResource(course.getImageResourceId());
        } else if (course.getImageUrl() != null && !course.getImageUrl().isEmpty()) {
            holder.courseImage.setImageResource(R.drawable.ux_design_placeholder);
        } else {
            holder.courseImage.setImageResource(R.drawable.ux_design_placeholder);
        }

        // Click listener for the entire card
        holder.itemView.setOnClickListener(v -> openCourseDetail(course));

        // Click listener for the Learn button
        holder.learnButton.setOnClickListener(v -> openCourseDetail(course));
    }

    private void openCourseDetail(Course course) {
        Intent intent = new Intent(context, CourseDetailActivity.class);

        // Pass course data
        intent.putExtra("COURSE_ID", course.getCourseId());
        intent.putExtra("COURSE_TITLE", course.getTitle());
        intent.putExtra("COURSE_DURATION", course.getDuration());
        intent.putExtra("COURSE_DESCRIPTION", course.getDescription());
        intent.putExtra("COURSE_SYLLABUS", course.getSyllabus());

        // Pass image resource ID
        if (course.getImageResourceId() != 0) {
            intent.putExtra("COURSE_IMAGE_RES_ID", course.getImageResourceId());
        } else {
            intent.putExtra("COURSE_IMAGE_RES_ID", R.drawable.ux_design_placeholder);
        }

        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        ImageView courseImage;
        TextView courseTitle;
        TextView courseDuration;
        Button learnButton;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.course_image);
            courseTitle = itemView.findViewById(R.id.course_title);
            courseDuration = itemView.findViewById(R.id.course_duration);
            learnButton = itemView.findViewById(R.id.btn_course_action);
        }
    }
}
