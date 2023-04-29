package com.azwar.test.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.azwar.test.R;
import com.azwar.test.databases.ResultDatabase;
import com.azwar.test.models.ResultModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListResultAdapter extends RecyclerView.Adapter<ListResultAdapter.myListResult> {
    private static ArrayList<ResultModel> resultModels = new ArrayList<>();
    private final Context context;

    ResultDatabase resultDatabase;

    public ListResultAdapter(ArrayList<ResultModel> resultModels, FragmentActivity activity) {
        ListResultAdapter.resultModels = resultModels;
        context = activity;
    }


    @NonNull
    @Override
    public ListResultAdapter.myListResult onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_result, parent, false);
        resultDatabase = ResultDatabase.getInstance(context);
        return new myListResult(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListResultAdapter.myListResult holder, int position) {
        final ResultModel resultModel = resultModels.get(position);

        holder.inputTV.setText(resultModel.getInput());
        holder.resultTV.setText(resultModel.getResult());

        String imgResult = resultModel.getImage();
        Glide.with(context)
                .load(imgResult)
                .placeholder(R.drawable.ic_baseline_photo_camera_24)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return resultModels.size();
    }

    public static class myListResult extends RecyclerView.ViewHolder {
        private final TextView inputTV, resultTV;
        private final ImageView img;

        public myListResult(@NonNull View itemView) {
            super(itemView);
            inputTV = itemView.findViewById(R.id.inputTV);
            resultTV = itemView.findViewById(R.id.resultTV);
            img = itemView.findViewById(R.id.img);
        }
    }
}
