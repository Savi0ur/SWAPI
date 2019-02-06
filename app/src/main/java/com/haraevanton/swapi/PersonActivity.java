package com.haraevanton.swapi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.haraevanton.swapi.R;
import com.haraevanton.swapi.mvp.model.Result;
import com.haraevanton.swapi.mvp.presenters.PersonActivityPresenter;
import com.haraevanton.swapi.mvp.views.PersonActivityView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonActivity extends MvpAppCompatActivity implements PersonActivityView {

    private static final String EXTRA_RESULT = "com.haraevanton.swapi.result";

    @InjectPresenter
    PersonActivityPresenter personActivityPresenter;

    @BindView(R.id.txt_person)
    TextView txtPerson;
    @BindView(R.id.txt_person_name)
    TextView txtPersonName;

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
        txtPersonName.setTypeface(custom_font);
        txtPersonName.setText(result.getName());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Name: ").append(result.getName()).append("\n\n");
        stringBuilder.append("Height: ").append(result.getHeight()).append("\n");
        stringBuilder.append("Mass: ").append(result.getMass()).append("\n");
        stringBuilder.append("Hair color: ").append(result.getHairColor()).append("\n");
        stringBuilder.append("Skin color: ").append(result.getSkinColor()).append("\n");
        stringBuilder.append("Eye color: ").append(result.getEyeColor()).append("\n");
        stringBuilder.append("Gender: ").append(result.getGender()).append("\n");
        stringBuilder.append("Birth year: ").append(result.getBirthYear()).append("\n");

        txtPerson.setTypeface(custom_font);
        txtPerson.setText(stringBuilder);

    }


    @Override
    public void showPersonInfo(Result result) {

    }
}
