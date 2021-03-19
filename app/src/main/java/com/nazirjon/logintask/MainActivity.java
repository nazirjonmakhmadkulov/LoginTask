package com.nazirjon.logintask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.nazirjon.logintask.database.UserRespository;
import com.nazirjon.logintask.local.UserDataSource;
import com.nazirjon.logintask.local.UserDatabase;
import com.nazirjon.logintask.models.ResponseApi;
import com.nazirjon.logintask.models.User;
import com.nazirjon.logintask.network.NetworkInterface;
import com.nazirjon.logintask.network.ServiceGenerator;
import com.nazirjon.logintask.storage.Settings;
import com.nazirjon.logintask.ui.HomeActivity;
import com.nazirjon.logintask.ui.LoginActivity;
import com.nazirjon.logintask.utls.Connetion;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

public class MainActivity extends AppCompatActivity {
    Settings settings;
    private CompositeDisposable compositeDisposable;
    private UserRespository userRespository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compositeDisposable = new CompositeDisposable();

        UserDatabase userDatabase = UserDatabase.getInstance(MainActivity.this); //create db
        Log.d("userDatabase ", userDatabase.toString());
        userRespository = UserRespository.getInstance(UserDataSource.getInstance(userDatabase.userDAO()));

        settings = new Settings(this);

        if (!Connetion.isNetworkAvaliable(this)){
            Toast.makeText(getBaseContext(), "Нет соединения с интернетом", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        if (!settings.getToken().isEmpty()) {
            int secondsDelayed = 1;
            new Handler().postDelayed(new Runnable() {
                public void run() {
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
                                try {
                                    Toast.makeText(getBaseContext(), response.errorBody().string().toString(), Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.d("Error", t.getMessage());
                        }
                    });
                }
            }, secondsDelayed * 500);
        }
        else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        }
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
                        Toast.makeText(MainActivity.this, "User добавлено!", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("Error", throwable.getMessage());
                        Toast.makeText(MainActivity.this, "Error "+ throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, new Action(){
                    @Override
                    public void run() throws Exception {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}