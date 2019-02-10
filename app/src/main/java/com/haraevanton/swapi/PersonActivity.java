package com.haraevanton.swapi;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.haraevanton.swapi.room.Result;
import com.haraevanton.swapi.mvp.presenters.PersonActivityPresenter;
import com.haraevanton.swapi.mvp.views.PersonActivityView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonActivity extends MvpAppCompatActivity implements PersonActivityView {

    private static final String EXTRA_RESULT = "com.haraevanton.swapi.result";

    @InjectPresenter
    PersonActivityPresenter personActivityPresenter;

    @BindView(R.id.txt_toolbar_name)
    TextView txtToolbarName;
    @BindView(R.id.txt_height_info)
    TextView txtHeightInfo;
    @BindView(R.id.txt_name_info)
    TextView txtNameInfo;
    @BindView(R.id.txt_mass_info)
    TextView txtMassInfo;
    @BindView(R.id.txt_hair_info)
    TextView txtHairInfo;
    @BindView(R.id.txt_skin_info)
    TextView txtSkinInfo;
    @BindView(R.id.txt_eye_info)
    TextView txtEyeInfo;
    @BindView(R.id.txt_gender_info)
    TextView txtGenderInfo;
    @BindView(R.id.txt_birth_info)
    TextView txtBirthInfo;
    @BindView(R.id.ll)
    LinearLayout ll;

    private Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null){
            result = (Result) arguments.getSerializable(EXTRA_RESULT);
        }

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/DeathStar.otf");

        txtToolbarName.setTypeface(custom_font);

        for (int i = 0; i < ll.getChildCount(); i++){
            LinearLayout v = (LinearLayout) ll.getChildAt(i);
            for (int j = 0; j < v.getChildCount(); j++){
                TextView txt = (TextView) v.getChildAt(j);
                txt.setTypeface(custom_font);
            }
        }
    }


    @Override
    public void showPersonInfo() {
        txtToolbarName.setText(result.getName());

        txtNameInfo.setText(result.getName());
        txtHeightInfo.setText(result.getHeight());
        txtMassInfo.setText(result.getMass());
        txtHairInfo.setText(result.getHairColor());
        txtSkinInfo.setText(result.getSkinColor());
        txtEyeInfo.setText(result.getEyeColor());
        txtGenderInfo.setText(result.getGender());
        txtBirthInfo.setText(result.getBirthYear());
    }

    @Override
    public void backPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.btn_back)
    public void onBackBtnClick(){
        personActivityPresenter.onBackBtnClick();
    }
}
