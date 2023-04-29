package com.azwar.test.activitys;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.azwar.test.adapters.ListResultAdapter;
import com.azwar.test.R;
import com.azwar.test.databases.ResultDatabase;
import com.azwar.test.models.ResultModel;

import java.util.ArrayList;
import java.util.Arrays;

public class ListResultActivity extends AppCompatActivity {

    ActionBar actionBar;
    RecyclerView recyclerView;
    ResultDatabase resultDatabase;
    TextView emptyText;

    LottieAnimationView lottieAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_result);

        actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setTitle("List Result");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.listResultRV);
        lottieAnimation = findViewById(R.id.lottieAnimation);
        emptyText = findViewById(R.id.emptyText);

        resultDatabase = ResultDatabase.getInstance(this);

        addContentResultDatabase();

    }

    private void addContentResultDatabase() {
        ArrayList<ResultModel> contents = new ArrayList<>(Arrays.asList(resultDatabase.resultDAO().allResult()));
        recyclerView.setHasFixedSize(true);

        if (contents.isEmpty()) {
            showList(true);
        } else {
            showList(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(this
                    , RecyclerView.VERTICAL, false));
            ListResultAdapter listResultAdapter = new ListResultAdapter(contents, this);
            recyclerView.setAdapter(listResultAdapter);
        }
    }

    private void showList(boolean show) {
        lottieAnimation.setVisibility(show ? View.VISIBLE : View.GONE);
        emptyText.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}