package com.adedayo.gadsleaderboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.adedayo.gadsleaderboard.util.ApiUtil;
import com.adedayo.gadsleaderboard.util.UserClient;

import org.jetbrains.annotations.NotNull;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectSubmissionActivity extends AppCompatActivity {

    ImageButton backBtn;
    Button submitFormBtn;
    EditText firstName,lastName, emailsub, projecturl;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_submission);
        backBtn = findViewById(R.id.back_btn);
        submitFormBtn = findViewById(R.id.submit_form_btn);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        emailsub = findViewById(R.id.email);
        projecturl = findViewById(R.id.project_link_tv);
        progressBar = findViewById(R.id.pb_load);

        backBtn.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

        submitFormBtn.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            submitAlertDialog();
        });

    }

    public void submitAlertDialog(){
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_layout, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        dialogView.findViewById(R.id.dialog_btn_submit).setOnClickListener(view -> {
            ProjectSubmissionActivity.this.executeSendForm(
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    emailsub.getText().toString(),
                    projecturl.getText().toString()
            );
            alertDialog.dismiss();
        });
        dialogView.findViewById(R.id.cancel_btn).setOnClickListener(view -> {
            if (progressBar.isShown())
                progressBar.setVisibility(View.INVISIBLE);
            alertDialog.dismiss();
        });
        alertDialog.show();
    }

    private void showStatusDialog(int id) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(id, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void executeSendForm(String fname, String lname, String email, String project_link) {
        UserClient userClient = ApiUtil.getRetrofit().create(UserClient.class);

        Call<ResponseBody> responseBodyCall = userClient.sendUserData(fname, lname, email, project_link);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                Log.e("RESPONSE", String.valueOf(response.code()));
                if (response.code() == 200)
                showStatusDialog(R.layout.confirm_submission);
                else
                    showStatusDialog(R.layout.error_dialog);
                if (progressBar.isShown())
                    progressBar.setVisibility(View.INVISIBLE);

                clearText();
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, Throwable t) {
                if (progressBar.isShown()

                )
                    progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ProjectSubmissionActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void clearText(){
        firstName.getText().clear();
        lastName.getText().clear();
        emailsub.getText().clear();
        projecturl.getText().clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}