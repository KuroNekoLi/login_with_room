package com.example.test0401;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test0401.R;
import com.example.test0401.data.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onDeleteClick(User user);
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUsername;
        private TextView textViewPassword;
        private ImageButton btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.text_view_username);
            textViewPassword = itemView.findViewById(R.id.text_view_password);
            btnDelete = itemView.findViewById(R.id.btn_delete);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onDeleteClick(users.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User currentUser = users.get(position);
        holder.textViewUsername.setText(currentUser.getUsername());
        holder.textViewPassword.setText(currentUser.getPassword());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    // 添加用户到 UserAdapter 的数据集中
    public void addUser(User user) {
        users.add(user);
        notifyDataSetChanged();
    }

    // 从 UserAdapter 的数据集中移除用户
    public int removeUser(User user) {
        int position = users.indexOf(user);
        if (position != -1) {
            users.remove(position);
        }
        return position;
    }
}
