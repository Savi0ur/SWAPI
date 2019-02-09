package com.haraevanton.swapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.haraevanton.swapi.room.Result;
import com.haraevanton.swapi.mvp.presenters.MainActivityPresenter;
import com.haraevanton.swapi.mvp.views.MainActivityView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends MvpAppCompatActivity implements MainActivityView {

    public static final String DIALOG_ABOUT_APP = "AboutAppDialog";

    @InjectPresenter
    MainActivityPresenter mainActivityPresenter;

    @BindView(R.id.ll_layout)
    LinearLayout ll_layout_main;
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

    private DialogFragment aboutAppDialog;
    private RVAdapter adapter;
    private AnimationDrawable postersAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        aboutAppDialog = new AboutAppDialogFragment();

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

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                mainActivityPresenter.onRecyclerViewScrolled(visibleItemCount, totalItemCount,
                        firstVisibleItems);

            }
        });

    }

    @Override
    public void onGetDataSuccess(List<Result> results) {
        adapter = new RVAdapter(results, mainActivityPresenter);
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
    public void animateBackBtnToClear() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) getResources()
                    .getDrawable(R.drawable.ic_arrow_back_anim_to_clear);
            btn_clear.setImageDrawable(drawable);
            drawable.start();
        }
    }

    @Override
    public void animateClearBtnToBack() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) getResources()
                    .getDrawable(R.drawable.ic_clear_anim_to_back);
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
    public void showSearchResults(String query, int count) {
        if (!query.equals("")) {
            Snackbar.make(ll_layout_main,
                    getString(R.string.results_message, count, query),
                    Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(ll_layout_main,
                    getString(R.string.results_for_all_message, count),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showNoInternetMessage() {
        Snackbar.make(ll_layout_main, R.string.no_internet_message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showSnackBarMessage(String message) {
        Snackbar.make(ll_layout_main, message, Snackbar.LENGTH_LONG).show();
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
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        mainActivityPresenter.onBackPressed();
    }

    @Override
    public void backPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.btn_search)
    public void onSearchBtnClick() {
        mainActivityPresenter.onButtonSearchClick(edt_search.getText().toString());
    }

    @OnClick(R.id.btn_clear)
    public void onClearBtnClick() {
        mainActivityPresenter.onClearButtonClick();
    }

    @OnClick(R.id.btn_history)
    public void onHistoryBtnClick() {
        mainActivityPresenter.onHistoryButtonClick();
    }

    @OnClick(R.id.btn_info)
    public void onAboutAppBtnClick() {
        aboutAppDialog.show(getSupportFragmentManager(), DIALOG_ABOUT_APP);
    }

    @OnClick(R.id.iv_api_icon)
    public void onApiLogoClick(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://swapi.co/"));
        startActivity(intent);
    }

}
