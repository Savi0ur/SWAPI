package com.haraevanton.swapi;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;

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

    @InjectPresenter
    MainActivityPresenter mainActivityPresenter;

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.edt_search)
    EditText edt_search;
    @BindView(R.id.btn_clear)
    ImageButton btn_clear;

    private RVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        rv.setLayoutManager(new GridLayoutManager(this, 2));

        edt_search.setOnEditorActionListener(((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                mainActivityPresenter.uploadData(edt_search.getText().toString());
            }
            return false;
        }));

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
    public void animateClearButton() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) getResources()
                    .getDrawable(R.drawable.ic_clear_gray_anim);
            btn_clear.setImageDrawable(drawable);
            drawable.start();
        }
    }

    @OnClick(R.id.btn_search)
    public void onBtnSearchClick() {
        mainActivityPresenter.uploadData(edt_search.getText().toString());
    }

    @OnClick(R.id.btn_clear)
    public void onClearButtonClick() {
        mainActivityPresenter.onClearButtonClick(edt_search.getText().toString());
    }

}
