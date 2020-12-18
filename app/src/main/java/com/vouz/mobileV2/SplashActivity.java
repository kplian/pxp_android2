package com.vouz.mobileV2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vouz.mobileV2.Utils.Util;
import com.vouz.mobileV2.models.AppVersion.AppVersion;
import com.vouz.mobileV2.models.health.WebViewHealth;
import com.vouz.mobileV2.retrofit.PXPServices;
import com.vouz.mobileV2.retrofit.RetrofitClientInstance;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView versionTextView = findViewById(R.id.versionTextView);

        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionNumber = pinfo.versionCode;
            String versionName = pinfo.versionName;
            versionTextView.setText("V." + versionNumber + "/" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (!isTaskRoot()) {
            finish();
            return;
        }

        getSupportActionBar().hide();
        instanceDatabase();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("on START", "on START");
        if (!Util.isOnline(this)) {
            showErrorDialog(getResources().getString(R.string.no_connection_error));
        } else {
            //getScreenDestination();
            checkWebViewHealth();

        }
    }

    public void showErrorDialog(String message) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SplashActivity.this);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(SplashActivity.this, SplashActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        builder.show();
    }

    public void checkWebViewHealth() {
        PXPServices getAppVersion = RetrofitClientInstance.getRetrofitInstance().create(PXPServices.class);
        Call<ResponseBody> call = getAppVersion.getWebViewHealth();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
//                        Log.d("response.body()", "-" + response.body().string() + "-");
//                        Log.d("response.body()", "-" + response.body().toString() + "-");
                        String responseD = response.body().string();
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        WebViewHealth webViewHealth = gson.fromJson(responseD, WebViewHealth.class);

                        Log.d("---", webViewHealth.getSuccess().toString());

                        if (webViewHealth.getSuccess()) {
                            goToMainActivity();
                        } else {
                            showErrorDialog(getResources().getString(R.string.maintenance_error));
                        }
                    }
                } catch (Exception e) {
                    Log.e("Exception", e.getLocalizedMessage());
                    showErrorDialog(getResources().getString(R.string.maintenance_error));
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                showErrorDialog(getResources().getString(R.string.maintenance_error));
            }
        });
    }

    public void needToUpdate() {
        PXPServices getAppVersion = RetrofitClientInstance.getRetrofitInstance().create(PXPServices.class);
        Call<ResponseBody> call = getAppVersion.getAppVersion();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String responseD = response.body().string();
                        Log.d("12222222---", responseD);

                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        AppVersion appVersion = gson.fromJson(responseD, AppVersion.class);

                        Log.d("123", BuildConfig.VERSION_CODE + "");
                        Log.d("123", Integer.parseInt(appVersion.getVersion()) + "");

                        if (Integer.parseInt(appVersion.getVersion()) > BuildConfig.VERSION_CODE) {
                            Log.d("----", "need to update");
                            showUpdateDialog(appVersion);
                        } else {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            SplashActivity.this.overridePendingTransition(0, 0);
                            finish();
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

            }
        });
    }

    public void showUpdateDialog(final AppVersion appVersion) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SplashActivity.this);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage("Está usando una versión antigua de Vouz, por favor actualice a una versión más reciente.");
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(appVersion.getUrl()));
                startActivity(browserIntent);
            }
        });
        builder.show();
    }

    private void goToMainActivity() {
        @SuppressLint("HandlerLeak") Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                needToUpdate();

//                startActivity(new Intent(SplashActivity.this, LoginActivity.class));

            }
        };
        mHandler.sendEmptyMessageDelayed(0, 100);
    }

    public int getDatabaseVersion(String path) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(path, null);
        Cursor cursor = db.rawQuery("SELECT version FROM metadata", null);
        int version = 0;

        if (cursor.moveToFirst()) {
            version = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return version;
    }

    public void instanceDatabase() {
        Context Context = this;
        String destinationFile = Context.getFilesDir().getPath() + File.separator + "db.sqlite";

        if (!new File(destinationFile).exists()) {
            try {
                Util.copyFromAssetsToStorage(Context, "pxp.sqlite", destinationFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            int currentVersion = getDatabaseVersion(Context.getFilesDir().getPath() + File.separator + "db.sqlite");

            String destinationFile2 = Context.getFilesDir().getPath() + File.separator + "db_prev.sqlite";
            try {
                Util.copyFromAssetsToStorage(Context, "pxp.sqlite", destinationFile2);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int updateVersion = getDatabaseVersion(Context.getFilesDir().getPath() + File.separator + "db_prev.sqlite");
            if (updateVersion > currentVersion) {
                try {
                    Util.copyFromAssetsToStorage(Context, "pxp.sqlite", destinationFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
