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
import com.nazirjon.logintask.models.Create;
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

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_email) TextInputEditText _emailText;
    @BindView(R.id.input_password) TextInputEditText _passwordText;
    @BindView(R.id.input_rePassword) TextInputEditText _rePasswordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    Settings settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        settings = new Settings(this);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!Connetion.isNetworkAvaliable(this)){
            Toast.makeText(getBaseContext(), "Нет соединения с интернетом", Toast.LENGTH_LONG).show();
            return;
        }

        if (!validate()) {
            onSignupFailed();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Создание аккаунт...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String rePassword = _rePasswordText.getText().toString();

        Create create = new Create();
        create.setEmail(email);
        create.setPassword(password);
        create.setPasswordConfirm(rePassword);
        create.setGoogleClientId("mobile");

        Log.d(TAG, create.getEmail());

        NetworkInterface loginService =
                ServiceGenerator.createService(NetworkInterface.class);
        Call<ResponseApi> call = loginService.createAccount(create);
        call.enqueue(new Callback<ResponseApi >() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                ResponseApi responseApi = response.body();
                if (response.isSuccessful()){
                    settings.saveLogin(email);
                    settings.saveId(responseApi.getId());
                    settings.saveToken(responseApi.getToken());
                    startActivity(new Intent(SignupActivity.this, HomeActivity.class));
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


    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _rePasswordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Введите корректный электронный адрес");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("от 4 до 10 буквенно-цифровых символов");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _rePasswordText.setError("Пароль не совпадает");
            valid = false;
        } else {
            _rePasswordText.setError(null);
        }

        return valid;
    }
}