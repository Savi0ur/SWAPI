package com.haraevanton.swapi;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.haraevanton.swapi.mvp.model.Result;
import com.haraevanton.swapi.mvp.presenters.MainActivityPresenter;
import com.haraevanton.swapi.mvp.views.MainActivityView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends MvpAppCompatActivity implements MainActivityView {

    public static final String TAG = "swapi.MainActivity";

    @InjectPresenter
    MainActivityPresenter mainActivityPresenter;

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.edt_search)
    EditText edt_search;
    @BindView(R.id.btn_clear)
    ImageButton btn_clear;
    @BindView(R.id.btn_search)
    ImageButton btn_search;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.img_posters)
    ImageView img_posters;

    private RVAdapter adapter;
    private AnimationDrawable postersAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);

        edt_search.setOnEditorActionListener(((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                mainActivityPresenter.onButtonSearchClick(edt_search.getText().toString());
            }
            return false;
        }));

        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                mainActivityPresenter.onRecyclerViewScrolled(visibleItemCount, totalItemCount, firstVisibleItems);

            }
        });

    }

    @Override
    public void onGetDataSuccess(List<Result> results) {
        adapter = new RVAdapter(results);
        rv.setAdapter(adapter);
    }

    @Override
    public void clearSearchInput() {
        edt_search.setText("");
    }

    @Override
    public void animateClearBtn() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) getResources()
                    .getDrawable(R.drawable.ic_clear_anim);
            btn_clear.setImageDrawable(drawable);
            drawable.start();
        }
    }

    @Override
    public void animateSearchBtn() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) getResources()
                    .getDrawable(R.drawable.ic_search_anim);
            btn_search.setImageDrawable(drawable);
            drawable.start();
        }
    }

    @Override
    public void animatePostersImg() {
        img_posters.setBackgroundResource(R.drawable.posters_animation);
        postersAnim = (AnimationDrawable) img_posters.getBackground();
        postersAnim.setEnterFadeDuration(1000);
        postersAnim.setExitFadeDuration(1000);
        postersAnim.start();
    }

    @Override
    public void showPostersImg() {
        img_posters.setVisibility(View.VISIBLE);
        postersAnim.start();
    }

    @Override
    public void hidePostersImg() {
        img_posters.setVisibility(View.GONE);
        postersAnim.stop();
    }

    @Override
    public void showList() {
        rv.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideList() {
        rv.setVisibility(View.GONE);
    }

    @Override
    public void updateList() {
        rv.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setAlpha(1);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setAlpha(0);
    }

    @Override
    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        try {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
            Log.e(TAG, "hideKeyboard: ", e);
        }
    }

    @OnClick(R.id.btn_search)
    public void onSearchBtnClick() {
        mainActivityPresenter.onButtonSearchClick(edt_search.getText().toString());
    }

    @OnClick(R.id.btn_clear)
    public void onClearBtnClick() {
        mainActivityPresenter.onClearButtonClick(edt_search.getText().toString());
    }

}
