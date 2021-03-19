package com.nazirjon.logintask.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.nazirjon.logintask.R;
import com.nazirjon.logintask.models.Login;
import com.nazirjon.logintask.models.ResponseApi;
import com.nazirjon.logintask.network.NetworkInterface;
import com.nazirjon.logintask.network.ServiceGenerator;
import com.nazirjon.logintask.storage.Settings;
import com.nazirjon.logintask.utls.Connetion;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.input_email) TextInputEditText _emailText;
    @BindView(R.id.input_password) TextInputEditText _passwordText;
    @BindView(R.id.btn_signup)  Button _loginButton;
    @BindView(R.id.link_create)  TextView _createLink;

    Settings settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        settings = new Settings(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        _createLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!Connetion.isNetworkAvaliable(this)){
            Toast.makeText(getBaseContext(), "Нет соединения с интернетом", Toast.LENGTH_LONG).show();
            return;
        }

        if (!validate()) {
            onLoginFailed();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Загрузка...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        Login login = new Login();
        login.setUsername(email);
        login.setPassword(password);
        login.setFromApp(true);
        login.setGoogleClientId("mobile");

        NetworkInterface loginService =
                ServiceGenerator.createService(NetworkInterface.class);
        Call<ResponseApi> call = loginService.login(login);
        call.enqueue(new Callback<ResponseApi >() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                if (response.isSuccessful()) {
                    ResponseApi responseApi = response.body();
                    settings.saveLogin(email);
                    settings.saveId(responseApi.getId());
                    settings.saveToken(responseApi.getToken());
                    onLoginSuccess();
                    Log.d("isSuccessful", response.toString());
                } else {
                    try {
                        Toast.makeText(getBaseContext(), response.errorBody().string().toString(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                Log.d("Error", t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    public void onLoginSuccess() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
    }
    public boolean validate() {
        boolean valid = true;
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Введите корректный электронный адрес");
            valid = false;
        } else {
            _emailText.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("oт 4 до 10 буквенно-цифровых символов");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}