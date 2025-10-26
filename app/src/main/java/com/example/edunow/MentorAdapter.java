package com.example.edunow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.List;

public class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.MentorViewHolder> {

    private final List<Mentor> mentorList;

    public MentorAdapter(List<Mentor> mentorList) {
        this.mentorList = mentorList;
    }

    @NonNull
    @Override
    public MentorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mentor_vertical, parent, false);
        return new MentorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MentorViewHolder holder, int position) {
        Mentor mentor = mentorList.get(position);

        holder.mentorName.setText(mentor.getName());
        holder.mentorTitle.setText(mentor.getTitle());
        holder.mentorCourseCount.setText(mentor.getCoursesCount() + " Courses");

        // Use the new method name and handle both local and remote images
        if (mentor.getAvatarResourceId() != 0) {
            // Local image resource
            holder.mentorAvatar.setImageResource(mentor.getAvatarResourceId());
        } else if (mentor.getAvatarUrl() != null && !mentor.getAvatarUrl().isEmpty()) {
            // Remote image URL - you can use Picasso/Glide here
            // For now, use a placeholder
            holder.mentorAvatar.setImageResource(R.drawable.arpit_avatar);
        } else {
            // Default placeholder
            holder.mentorAvatar.setImageResource(R.drawable.arpit_avatar);
        }

        // See Class button click
        holder.seeClassButton.setOnClickListener(v -> {
            Toast.makeText(v.getContext(),
                    "Viewing classes by " + mentor.getName(),
                    Toast.LENGTH_SHORT).show();
            // TODO: Navigate to mentor's courses activity
        });
    }

    @Override
    public int getItemCount() {
        return mentorList.size();
    }

    public static class MentorViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mentorAvatar;
        TextView mentorName;
        TextView mentorTitle;
        TextView mentorCourseCount;
        Button seeClassButton;

        public MentorViewHolder(@NonNull View itemView) {
            super(itemView);
            mentorAvatar = itemView.findViewById(R.id.mentor_avatar);
            mentorName = itemView.findViewById(R.id.mentor_name);
            mentorTitle = itemView.findViewById(R.id.mentor_title);
            mentorCourseCount = itemView.findViewById(R.id.mentor_course_count);
            seeClassButton = itemView.findViewById(R.id.btn_see_class);
        }
    }
}
