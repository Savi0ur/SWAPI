package com.haraevanton.swapi;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haraevanton.swapi.mvp.model.Result;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private List<Result> results;

    public RVAdapter(List<Result> results){
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.txtName.setText(results.get(i).getName());
        viewHolder.txtColors.setText(results.get(i).getSkinColor());
        viewHolder.bind(results.get(i));
        if (results.size() == 1){
            viewHolder.showPersonInfo(results.get(0));
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private static final String EXTRA_RESULT = "com.haraevanton.swapi.result";

        @BindView(R.id.cv)
        CardView cv;
        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.txt_colors)
        TextView txtColors;

        private Result result;
        private Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        public void bind (Result result){
            this.result = result;
        }

        public void showPersonInfo(Result result){
            Intent intent = new Intent(context, PersonActivity.class);
            intent.putExtra(EXTRA_RESULT, result);
            context.startActivity(intent);
        }

        @OnClick(R.id.cv)
        void onCardClick(){
            showPersonInfo(result);
        }

    }

}
