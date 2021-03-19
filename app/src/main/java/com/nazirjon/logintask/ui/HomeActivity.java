package com.nazirjon.logintask.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.nazirjon.logintask.MainActivity;
import com.nazirjon.logintask.R;
import com.nazirjon.logintask.database.UserRespository;
import com.nazirjon.logintask.local.UserDataSource;
import com.nazirjon.logintask.local.UserDatabase;
import com.nazirjon.logintask.models.Login;
import com.nazirjon.logintask.models.ResponseApi;
import com.nazirjon.logintask.models.User;
import com.nazirjon.logintask.network.NetworkInterface;
import com.nazirjon.logintask.network.ServiceGenerator;
import com.nazirjon.logintask.storage.Settings;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    @BindView(R.id.input_email) TextInputEditText _emailText;
    @BindView(R.id.createAt) TextInputEditText _createText;
    @BindView(R.id.profiles) TextInputEditText _profileText;
    @BindView(R.id.googleClientId) TextInputEditText _clientIdText;
    @BindView(R.id.successPayment) TextInputEditText _successPaymentText;
    @BindView(R.id.planExpireDate) TextInputEditText _planExpireDateText;
    @BindView(R.id.link_log_out) TextView _log_out;

    Settings settings;

    private CompositeDisposable compositeDisposable;
    private UserRespository userRespository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        settings = new Settings(this);
        compositeDisposable = new CompositeDisposable();

        UserDatabase userDatabase = UserDatabase.getInstance(HomeActivity.this); //create db
        Log.d("userDatabase ", userDatabase.toString());
        userRespository = UserRespository.getInstance(UserDataSource.getInstance(userDatabase.userDAO()));

        _log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOut();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetworkInterface networkInterface =
                ServiceGenerator.createService(NetworkInterface.class, settings.getToken());
        Call<User> call = networkInterface.checkToken();
        call.enqueue(new Callback<User >() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    settings.saveId(response.body().getId());
                    addUser(user);
                } else {
                    loadData();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });

    }

    private void addUser(User user){
        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) {
                userRespository.insertUser(user);
                e.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Toast.makeText(HomeActivity.this, "User добавлено!", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("Error", throwable.getMessage());
                        Toast.makeText(HomeActivity.this, "Error "+ throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, new Action(){
                    @Override
                    public void run() throws Exception {
                        loadData();
                    }
                });

    }

    private void loadData() {
        final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Загрузка...");
        progressDialog.show();
        Disposable disposable = userRespository.getUserById(settings.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<User>(){
                    @Override
                    public void accept(User user) throws Exception {
                        onGetAllUserSuccess(user);
                        progressDialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception{
                        Toast.makeText(HomeActivity.this, ""+ throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
        compositeDisposable.add(disposable);
        progressDialog.dismiss();
    }

    private void onGetAllUserSuccess(User user) {
        _emailText.setText(user.getEmail().toString());
        _createText.setText(formatData(user.getCreatedAt().toString()));
        _profileText.setText(user.getProfiles().toString());
        _clientIdText.setText(user.getGoogleClientId().toString());
        _successPaymentText.setText(user.getHasSuccessPayment().toString());
        _planExpireDateText.setText(formatData(user.getPlanExpireDate().toString()));
    }

    private String formatData(String deliveryDate){
        SimpleDateFormat dateFormatprev = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = dateFormatprev.parse(deliveryDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String changedDate = dateFormat.format(d);
        return changedDate;
    }

    public void LogOut(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Вы действительно хотите выйти из аккаунта?");
        alertDialogBuilder.setPositiveButton("Да",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        settings.deleteAllSharedPrefs();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("Нет",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}