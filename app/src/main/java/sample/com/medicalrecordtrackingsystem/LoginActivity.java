package sample.com.medicalrecordtrackingsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sample.com.medicalrecordtrackingsystem.models.User;
import sample.com.medicalrecordtrackingsystem.rest.ApiClient;
import sample.com.medicalrecordtrackingsystem.rest.ApiInterface;

/**
 * Created by ayush on 29/3/17
 */
public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.username)
    EditText username;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.login_button)
    Button loginButton;
    @Bind(R.id.logo)
    ImageView logo;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    private ApiInterface apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);
        ButterKnife.bind(this);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
        if (sharedPreferences.contains(getString(R.string.username)) && sharedPreferences.contains(getString(R.string.password))) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("userId", sharedPreferences.getInt(getString(R.string.user_id), -1));
            startActivity(intent);
            finish();
        } else {
            getApiClient();
        }
    }

    private void getApiClient() {
        apiService = ApiClient.getCient().create(ApiInterface.class);
    }

    @OnClick(R.id.login_button)
    public void onLoginButtonClick() {
        logo.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        final String name = username.getText().toString().trim();
        final String pass = password.getText().toString().trim();
        apiService.signInUser(name, pass).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        User user = response.body();
                        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_pref_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(getString(R.string.username), name);
                        editor.putString(getString(R.string.password), pass);
                        editor.putInt(getString(R.string.user_id), user.getId());
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userId", user.getId());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.cannot_sign_in_user), Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                logo.setVisibility(View.VISIBLE);
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, getString(R.string.cannot_sign_in_user), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
