package com.example.a432androidtoolbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// 1. 继承改为 RecyclerView.Adapter，并指定内部类 ViewHolder
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<ToolItem> mData;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public MainAdapter(List<ToolItem> data, OnItemClickListener listener) {
        this.mData = data;
        this.mListener = listener;
    }

    // 2. 必须实现此方法：创建每一个方块的布局
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 使用系统内置布局，它包含一个 ID 为 text1 的 TextView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new ViewHolder(view);
    }

    // 3. 必须实现此方法：绑定数据
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(mData.get(position).getTitle());
        holder.itemView.setOnClickListener(v -> mListener.onItemClick(position));
    }

    // 4. 必须实现此方法：告诉系统列表总长度
    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    // 5. 必须定义内部类 ViewHolder，用于持有 View 引用
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            // ID 必须与 item_main.xml 里的保持一致
            textView = itemView.findViewById(R.id.tv_item_title);
        }
    }
}