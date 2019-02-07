package com.haraevanton.swapi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haraevanton.swapi.mvp.presenters.MainActivityPresenter;
import com.haraevanton.swapi.room.Result;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private MainActivityPresenter mainActivityPresenter;
    private List<Result> results;

    public RVAdapter(List<Result> results, MainActivityPresenter mainActivityPresenter){
        this.results = results;
        this.mainActivityPresenter = mainActivityPresenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview, viewGroup, false), mainActivityPresenter);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.txtName.setText(results.get(i).getName());
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

        private MainActivityPresenter mainActivityPresenter;

        private Result result;
        private Context context;

        public ViewHolder(@NonNull View itemView, MainActivityPresenter mainActivityPresenter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
            this.mainActivityPresenter = mainActivityPresenter;

            Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/DeathStar.otf");
            txtName.setTypeface(custom_font);

        }

        public void bind (Result result){
            this.result = result;
        }

        public void showPersonInfo(Result result){

            mainActivityPresenter.addResultHistory(result);

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
