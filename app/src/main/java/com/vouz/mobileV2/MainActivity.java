package com.vouz.mobileV2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.vouz.mobileV2.Utils.Constants;
import com.vouz.mobileV2.Utils.Util;
import com.vouz.mobileV2.commons.Encryption;
import com.vouz.mobileV2.models.CurrentPosition;
import com.vouz.mobileV2.models.SignedUser;
import com.vouz.mobileV2.models.SingleUserModel;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import im.delight.android.webview.AdvancedWebView;
import okhttp3.Cookie;

import com.google.android.gms.location.LocationRequest;
import com.vouz.mobileV2.models.firebase.Token;
import com.vouz.mobileV2.models.session.UserSession;
import com.vouz.mobileV2.services.MobileNotificationManager;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements AdvancedWebView.Listener,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    MobileNotificationManager mobileNotificationManager;

    private AdvancedWebView mWebView;

    private LinearLayout llProgressBar;

    private SharedPreferences mPreferences;

    private LinearLayout previewLoadingLinearLayout;

    private EditText tokenEditText;

    private LoginButton facebookLoginButton;
    public CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 2;

    private boolean setCredentials = false;

    private RelativeLayout transparentLoaderLinearLayout;

    public void initGoogleClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        googleApiClient = new GoogleApiClient().Builder(this.)

    }

    private void handleGoogleSingInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

//            UserSignInModel userSignInModel = new UserSignInModel();
//            userSignInModel.setCode(account.getIdToken());
//            userSignInModel.setUsuario(account.getEmail());
//            userSignInModel.setType("google");
//            userSignInModel.setLanguage(Locale.getDefault().getDisplayLanguage());

//            String name, String surname, String email, String token, String url_photo,
//            String login_type, String code, String usuario, String type, String language
            if (account != null) {

//                SingleUserModel singleUserModel = new SingleUserModel(
//                        account.getGivenName() != null ? account.getGivenName() : "",
//                        account.getFamilyName(),
//                        account.getEmail(),
//                        account.getId(),
//                        account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "",
//                        "google",
//                        account.getIdToken(),
//                        account.getEmail(),
//                        "google",
//                        Locale.getDefault().getDisplayLanguage(),
//                        "android"
//                );

                SingleUserModel singleUserModel = new SingleUserModel(
                        account.getGivenName() != null ? account.getGivenName() : "",
                        account.getFamilyName(),
                        account.getEmail(),
                        account.getIdToken(),
                        account.getId(),
                        "",
                        "google",
                        Locale.getDefault().getDisplayLanguage(),
                        "android"
                );

                Log.d("getIdToken", account.getIdToken());
                Log.d("getId", account.getId());

                Gson gson = new Gson();
                String json = gson.toJson(singleUserModel);

//                Toast.makeText(MainActivity.this, json + "", Toast.LENGTH_LONG).show();
//
//                String requestBody = "'googleSignIn', " + "'" + json + "'";
//                String jsFunction = "javascript:callMethodFromDevice(" + requestBody + ")";
//
                Log.d("-*--", json);
//
//                mWebView.loadUrl(jsFunction);

                sendToWebView("googleSignIn", json);
            }

        } catch (ApiException e) {
//            Toast.makeText(MainActivity.this, e.getStatusCode() + "", Toast.LENGTH_LONG).show();
            Log.d("TAG1", e.getLocalizedMessage());
            Log.d("TAG2", e.getStatusCode() + "");
            Log.d("TAG3", e.getMessage());
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void initFacebookClient() {
        Log.d("---", "......facebookLogin");
        callbackManager = CallbackManager.Factory.create();

        facebookLoginButton = (LoginButton) findViewById(R.id.login_button);
        facebookLoginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("onSuccess", "onSuccess");
                Log.d(",,.,,", loginResult.toString());
                Log.d(",,.,,", loginResult.getAccessToken().getApplicationId());
                Log.d(",,.,,", loginResult.getAccessToken().getToken());
                Log.d(",,.,,", loginResult.getAccessToken().getUserId());

//                UserModel userModel = new UserModel();
//                userModel.setName(loginResult.getSignInAccount().getDisplayName());
//                userModel.setSurname(loginResult.getSignInAccount().getFamilyName());
//                userModel.setEmail(loginResult.getSignInAccount().getEmail());
//                userModel.setToken(loginResult.getSignInAccount().getId());
//                userModel.setUrl_photo(loginResult.getSignInAccount().getPhotoUrl().toString());
//                userModel.setLogin_type("GOOGLE");

//                String requestBody = "'googleLogin', " + "'" + json + "'";
//                String jsFunction = "javascript:callMethodFromDevice(" + requestBody + ")";

            }

            @Override
            public void onCancel() {
                // App code
                Log.d("onCancel", "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("onError", "onError");
            }
        });

    }

    /**
     * Obtener la posision actual usando los servicio de Google (GoogleServices)
     */
    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();

                            Log.d("Location", latitude + " --  " + longitude);

                            CurrentPosition currentPosition = new CurrentPosition();
                            currentPosition.setLat(latitude);
                            currentPosition.setLng(longitude);

                            Gson gson = new Gson();
                            String json = gson.toJson(currentPosition);

                            String requestBody = "'userCurrentPosition', " + "'" + json + "'";
                            String jsFunction = "javascript:callMethodFromDevice(" + requestBody + ")";
                            Log.d("jsFunction: ", jsFunction);
                            mWebView.loadUrl(jsFunction);
                        }
                    }
                }, Looper.getMainLooper());
    }

    /**
     * Metodo que se ejecuta cuando se aceptan los permisos de localizacion
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getCurrentLocation();
            } else {
//                Toast.makeText(this, "Permisos denegados!", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Metodo que se ejecuta cuando se aceptan los permisos de localizacion
     */
    //    Traking de la posicion
    @Override
    public void onLocationChanged(Location location) {
        Log.d("123", "Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    /**
     * Metodo que verifica los permisos localizacion en tiempo de ejecucion
     */
    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("TAG", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("TAG", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, 1);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("TAG", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("TAG", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("onStart", "onStart");
//        Toast.makeText(this, "onStart", Toast.LENGTH_LONG).show();
        showDialog("onStart");
        sendToWebView("onMobileFocusIn", "");
//        getCurrentLocation();
    }

    /**
     * Instanciar servicios de FireBase
     */
    public void initiateFirebaseMessageService() {
        mobileNotificationManager = MobileNotificationManager.getInstance(this);
        mobileNotificationManager.registerNotificationChannelChannel(
                getString(R.string.NEWS_CHANNEL_ID),
                getString(R.string.CHANNEL_NEWS),
                getString(R.string.CHANNEL_DESCRIPTION));

        FirebaseMessaging.getInstance().isAutoInitEnabled();

        /*
         * Metodo que se ejecuta cuando se instancia FireBase, retorna el token generado
         * llama al metodo bridge JS para enviar el token generado por usuario
         */
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isComplete()) {
                    return;
                }
//                Log.i("111111", task.getResult().getToken());
//                tokenEditText.setText(task.getResult().getToken());

                Token token = new Token();
                token.setToken(task.getResult().getToken());

                Gson gson = new Gson();
                String json = gson.toJson(token);


                sendToWebView("userFirebaseToken", json);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Toast.makeText(this, "onCreate", Toast.LENGTH_LONG).show();
        showDialog("onCreate");
        super.onCreate(savedInstanceState);
        Log.e("onCreate", "onCreate");
        setContentView(R.layout.activity_main);

        tokenEditText = findViewById(R.id.tokenEditText);
        previewLoadingLinearLayout = findViewById(R.id.previewLoadingLinearLayout);

        transparentLoaderLinearLayout = findViewById(R.id.transparentLoaderLinearLayout);

        getSupportActionBar().hide();

        displayLocationSettingsRequest(MainActivity.this);
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        }

        llProgressBar = findViewById(R.id.llProgressBar);

        mWebView = (AdvancedWebView) findViewById(R.id.webview);

        mWebView.clearCache(true);

        mWebView.setListener(this, this);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

//        List<Cookie> cookies = WSHelper.cookieStore.getCookies();
//
//        cookieManager.removeAllCookie();

//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().contains("session")){
//                    String cookieString = cookie.getName() + "=" + cookie.getValue() + "; Domain=" + cookie.getDomain();
//                    cookieManager.setCookie(cookie.getDomain(), cookieString);
//                    Log.d("CookieUrl",cookieString + " ");
//                }
//            }
//        }

        mWebView.loadUrl(Constants.BASE_URL);

        /*
         * Instancia de la interfaz de JS para la llamada a metodos dentro del webView, llamada mobile
         * */
        mWebView.addJavascriptInterface(new WebViewJavaScriptInterface(), "Mobile");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);

            initGoogleClient();
            initFacebookClient();
            callbackManager = CallbackManager.Factory.create();

            Log.e("checkSavedCredentials()", checkSavedCredentials() + "");
            Log.e("setCredentials", setCredentials + "");

            if (checkSavedCredentials()) {
                llProgressBar.setVisibility(View.VISIBLE);
                credentialsSignIn();
            }
        }

//        try {
//            PackageInfo info = MainActivity.this.getPackageManager().getPackageInfo(MainActivity.this.getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String hashKey = new String(Base64.encode(md.digest(), 0));
//                Log.i("TAG", "printHashKey() Hash Key: " + hashKey);
//            }
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("TAG", "printHashKey()", e);
//        } catch (Exception e) {
//            Log.e("TAG", "printHashKey()", e);
//        }
    }

//    private void storeCookies(String url, String cookieStr) throws URISyntaxException {
//        CookieStore cookieStore = new BasicCookieStore();
//        if (cookieStr != null && !cookieStr.isEmpty()) {
//            URI uri = new URI(url);
//            List<HttpCookie> cookies = HttpCookie.parse(cookieStr);
//            for (HttpCookie cookie : cookies) {
//                cookieStore.add(uri, cookie); // java.net.CookieStore from a java.net.CookieManager
//            }
//        }
//    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        try {
            mWebView.onResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroy();
    }

    private void getFbInfo() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            if (object != null) {
                                String id = object != null ? object.getString("id") : "";
                                String first_name = object != null ? object.getString("first_name") : "";
                                String last_name = object != null ? object.getString("last_name") : "";
                                String email = object != null ? object.getString("email") : "";
                                String image_url = "http://graph.facebook.com/" + id + "/picture?type=large";

//                            SingleUserModel singleUserModel = new SingleUserModel(
//                                    first_name,
//                                    last_name,
//                                    email,
//                                    AccessToken.getCurrentAccessToken().getToken(),
//                                    image_url,
//                                    "facebook",
//                                    AccessToken.getCurrentAccessToken().getToken(),
//                                    email,
//                                    "facebook",
//                                    "",
//                                    "android"
//                            );

                                SingleUserModel singleUserModel = new SingleUserModel(
                                        first_name,
                                        last_name,
                                        email,
                                        AccessToken.getCurrentAccessToken().getToken(),
                                        AccessToken.getCurrentAccessToken().getToken(),
                                        image_url,
                                        "facebook",
                                        Locale.getDefault().getDisplayLanguage(),
                                        "android"
                                );


                                Gson gson = new Gson();
                                String json = gson.toJson(singleUserModel);
                                Log.d(">>>>", json);

                                sendToWebView("facebookSignIn", json);

                                if (object.has("email")) {
                                    email = object.getString("email");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,gender,birthday"); // id,first_name,last_name,email,gender,birthday,cover,picture.type(large)
        request.setParameters(parameters);
        request.executeAsync();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("onActivityResult", "onActivityResult");

        mWebView.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {

            showTransparentLoadingDialog();

            Log.d("RC_SIGN_IN", RC_SIGN_IN + " ");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            assert result != null;
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            handleGoogleSingInResult(task);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (transparentLoaderLinearLayout.getVisibility() == View.VISIBLE) {
                        hideTransparentLoadingDialog();
                    }
                }
            }, 500);

        } else {

            showTransparentLoadingDialog();
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
            try {
                getFbInfo();
            } catch (Exception e) {
                Log.d("Exception >>>>>", e.getMessage());
            }

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (transparentLoaderLinearLayout.getVisibility() == View.VISIBLE) {
                        hideTransparentLoadingDialog();
                    }
                }
            }, 500);

        }

    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
            builder.setTitle(getResources().getString(R.string.app_name));
            builder.setMessage(getResources().getString(R.string.exit_app));
            builder.setPositiveButton(getResources().getString(R.string.exit), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
        }
    }

    public void showTransparentLoadingDialog() {
        transparentLoaderLinearLayout.setVisibility(View.VISIBLE);
    }

    public void hideTransparentLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                transparentLoaderLinearLayout.setVisibility(View.GONE);
            }
        });

    }

    public void showDialog(String message) {
//        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
//        builder.setTitle(getResources().getString(R.string.app_name));
//        builder.setMessage(message);
//        builder.setPositiveButton(getResources().getString(R.string.exit), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//            }
//        });
//        builder.show();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        if (!Util.isOnline(this)) {
            mWebView.onPause();
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
            builder.setTitle(getResources().getString(R.string.app_name));
            builder.setMessage("Ocurrio un problema con su coneccion a internet, por favor intentelo nuevamente.");
            builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            });
            builder.show();
        }
        Log.e("onPageStarted", "onPageStarted");
//        Toast.makeText(this, "onPageStart", Toast.LENGTH_LONG).show();
        showDialog("onPageStart");
    }

    /**
     * Listener de cuando el web view ya esta cargado
     * se llaman a los metods necesarios para el webview
     */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPageFinished(String url) {
        Log.e("onPageFinished", "onPageFinished");
//        Toast.makeText(this, "onPageFinish", Toast.LENGTH_LONG).show();
        showDialog("onPageFinish");
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().acceptCookie();
        CookieManager.getInstance().flush();

        previewLoadingLinearLayout.setVisibility(View.GONE);

        if (checkFacebookLogin()) {
            llProgressBar.setVisibility(View.VISIBLE);
            facebookSignInNative();
        } else if (checkGoogleLogin()) {
            llProgressBar.setVisibility(View.VISIBLE);
            Log.d("----", "googleSingInNative123");
            googleSingInNative();
        }

        getCurrentLocation();

        initiateFirebaseMessageService();

        if (llProgressBar.getVisibility() == View.VISIBLE) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    llProgressBar.setVisibility(View.GONE);
                }
            }, 5000);
        }

        if (transparentLoaderLinearLayout.getVisibility() == View.VISIBLE) {
            hideTransparentLoadingDialog();
        }
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        Log.e("onPageError", "onPageError");
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
    }

    @Override
    public void onExternalPageRequest(String url) {
        Log.e("onExternalPageRequest", "onExternalPageRequest");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("onConnectionFailed", "onConnectionFailed");
    }

//    ===========================================================
//    ===========================================================
//    ===========================================================

    /**
     * Verifica si ya existe un sesion activa
     */
    public boolean checkSavedCredentials() {
        Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);
        SharedPreferences sharedpreferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        if (encryption != null) {
            String user = encryption.decryptOrNull(sharedpreferences.getString(Constants.PREFERENCES_U, ""));
            String pass = encryption.decryptOrNull(sharedpreferences.getString(Constants.PREFERENCES_P, ""));
            String lang = encryption.decryptOrNull(sharedpreferences.getString(Constants.PREFERENCES_L, ""));
            return user.length() > 0 && pass.length() > 0 && lang.length() > 0;
        }
        return false;
    }

    /**
     * Inicia sesion con las credenciales guardadas
     */
    public void credentialsSignIn() {
//        Toast.makeText(this, "ingresando con sesion", Toast.LENGTH_LONG).show();
        showDialog("ingresando con sesion");
//        Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);
//        SharedPreferences sharedpreferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
//        if (encryption != null) {
//            String user = encryption.decryptOrNull(sharedpreferences.getString(Constants.PREFERENCES_U, ""));
//            String pass = encryption.decryptOrNull(sharedpreferences.getString(Constants.PREFERENCES_P, ""));
//            String lang = encryption.decryptOrNull(sharedpreferences.getString(Constants.PREFERENCES_L, ""));
//
//            SignedUser signedUser = new SignedUser();
//            signedUser.setUsername(user);
//            signedUser.setPassword(pass);
//            signedUser.setLanguage(lang);
//
//            Gson gson = new Gson();
//            String json = gson.toJson(signedUser);
//
//            sendToWebView("vouzSignIn", json);
//
//        }

        Log.e("credentialsSignIn", "credentialsSignIn");

        Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);
        SharedPreferences sharedpreferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        if (encryption != null) {
            String user = encryption.decryptOrNull(sharedpreferences.getString(Constants.PREFERENCES_U, ""));
            String pass = encryption.decryptOrNull(sharedpreferences.getString(Constants.PREFERENCES_P, ""));
            String lang = encryption.decryptOrNull(sharedpreferences.getString(Constants.PREFERENCES_L, ""));
            String session = encryption.decryptOrNull(sharedpreferences.getString(Constants.PREFERENCES_S, ""));
            String user_id = encryption.decryptOrNull(sharedpreferences.getString(Constants.PREFERENCES_UID, ""));
            String user_name = encryption.decryptOrNull(sharedpreferences.getString(Constants.PREFERENCES_NU, ""));

            UserSession userSession = new UserSession();
            userSession.setAlias("");
            userSession.setAutentificacion("local");
            userSession.setCont_alertas(1);
            userSession.setEstilo_vista("xtheme-access.css");
            userSession.setId_funcionario("");
            userSession.setId_usuario(user_id);
            userSession.setMensaje_tec("");
            userSession.setNombre_basedatos("dbvouz");
            userSession.setNombre_usuario(user_name);
            userSession.setSuccess(true);
            userSession.setTimeout(200000);
            userSession.setUser(user);

            Gson gson = new Gson();
            String json = gson.toJson(userSession);

            Log.d("gson", json);
            String key = "aut";
            mWebView.loadUrl("javascript:localStorage.setItem('" + key + "','" + json + "');");

            setCredentials = true;

//            mWebView.loadUrl("javascript:localStorage.setItem('" + "deviceID" + "','" + "dpzvYmA7glgH3pFNP0VjKz:APA91bGv0Tf0gy3zLWVMyPftpVqaSM4XPzSMQfX_oLvFqFaRjjOlDavNxas6Om4G346ruLhXrCvtgAEk4ROABNR1QPfFPi-ozjEjUpfpGGzJJ0PVA7KiEt6vcjj4gT19nWsRnA3wrLsS" + "');");

        }
    }

    public void facebookSignInNative() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            String email = object.getString("email");

                            AccessToken accessToken = AccessToken.getCurrentAccessToken();

                            SingleUserModel singleUserModel = new SingleUserModel(
                                    "",
                                    "",
                                    email,
                                    accessToken.getToken(),
                                    "",
                                    "",
                                    "facebook",
                                    Locale.getDefault().getDisplayLanguage(),
                                    "android"
                            );


                            Gson gson = new Gson();
                            String json = gson.toJson(singleUserModel);
                            Log.d(">>>>", json);

                            String requestBody = "'facebookSignIn', " + "'" + json + "'";
                            String jsFunction = "javascript:callMethodFromDevice(" + requestBody + ")";
                            Log.d("jsFunction: ", jsFunction);
                            mWebView.loadUrl(jsFunction);

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    llProgressBar.setVisibility(View.GONE);
                                }
                            }, 3000);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,gender,birthday"); // id,first_name,last_name,email,gender,birthday,cover,picture.type(large)
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void googleSingInNative() {

        Log.d("googleSingIn", "googleSingIn");
        try {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

            if (account != null) {
                SingleUserModel singleUserModel = new SingleUserModel(
                        "",
                        "",
                        account.getEmail() != null ? account.getEmail() : "",
                        account.getIdToken(),
                        "",
                        "",
                        "google",
                        Locale.getDefault().getLanguage(),
                        "android"
                );
                Gson gson = new Gson();
                String json = gson.toJson(singleUserModel);
                Log.d("googleSingIn>>>>>>>>", json);
                Log.d("account.getId()", account.getId());

                sendToWebView("googleSignIn", json);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        llProgressBar.setVisibility(View.GONE);
                        return;
                    }
                }, 3000);
                return;
            }

        } catch (Exception e) {
            Log.d("1111", e.getMessage());
        }

    }

    /**
     * Metod que envia al informacion al web view
     * estructura:
     * javascript:callMethodFromDevice(nombre_del_metodo_en_PXP.js, objecto dinamico json como cadena)
     * Metodo Bridge para la coneccion con el WebView Nativo -> WebView
     */
    public void sendToWebView(String method, String jsonObject) {
        String requestBody = "'" + method + "', " + "'" + jsonObject + "'";
        String jsFunction = "javascript:callMethodFromDevice(" + requestBody + ")";
        Log.d("jsFunction: ", jsFunction);
        mWebView.loadUrl(jsFunction);
    }

    public void refreshWebView() {
        Log.d("refresh web", "refresh web view from activityt");
        mWebView.reload();
    }

    public boolean checkGoogleLogin() {
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        return account != null;
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }

    public boolean checkFacebookLogin() {
        return AccessToken.getCurrentAccessToken() != null;
    }


    /**
     * Clase Bridge para la coneccion con el WebView WebView -> Nativo
     */
    public class WebViewJavaScriptInterface {

        @JavascriptInterface
        public void getUserCurrentPosition() {
            Log.e("getUserCurrentPosition", "getUserCurrentPosition");
            getCurrentLocation();
            Thread.currentThread().interrupt();
        }

        @JavascriptInterface
        public void saveUserCredentials(final String username, final String password, final String language) {
            Log.e("saveUserCredentials", "saveUserCredentials");
            SharedPreferences sharedpreferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);

            Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            if (encryption != null) {
                editor.putString(Constants.PREFERENCES_U, encryption.encryptOrNull(username));
                editor.putString(Constants.PREFERENCES_P, encryption.encryptOrNull(password));
                editor.putString(Constants.PREFERENCES_L, encryption.encryptOrNull(language));
                editor.apply();
            }
            Thread.currentThread().interrupt();
        }

        @JavascriptInterface
        public void deleteUserCredentials() {
            Log.e("deleteUserCredentials", "deleteUserCredentials");
            SharedPreferences sharedpreferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(Constants.PREFERENCES_U, "");
            editor.putString(Constants.PREFERENCES_P, "");
            editor.putString(Constants.PREFERENCES_L, "");
            editor.apply();

//            Close Facebook session
            if (LoginManager.getInstance() != null) {
                LoginManager.getInstance().logOut();
            }

//            Close Google session
            GoogleSignInOptions gso = new GoogleSignInOptions.
                    Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                    build();

            if (gso != null) {
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);
                googleSignInClient.signOut();
            }
            Thread.currentThread().interrupt();
        }

        @JavascriptInterface
        public void saveWebSocketURL(final String data, final String id_usuario, final String nombre_usuario) {
            Log.e("saveWebSocketURL", "saveWebSocketURL");
            SharedPreferences sharedpreferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);

            Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            if (encryption != null) {
                editor.putString(Constants.PREFERENCES_S, encryption.encryptOrNull(data));
                editor.putString(Constants.PREFERENCES_UID, encryption.encryptOrNull(id_usuario));
                editor.putString(Constants.PREFERENCES_NU, encryption.encryptOrNull(nombre_usuario));
                editor.apply();
            }

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (transparentLoaderLinearLayout.getVisibility() == View.VISIBLE) {
                        hideTransparentLoadingDialog();
                    }
                }
            }, 1000);

            Thread.currentThread().interrupt();
        }

        @JavascriptInterface
        public void hideLoadingDialog() {
            Log.e("hideLoadingDialog", "hideLoadingDialog");
            llProgressBar.setVisibility(View.GONE);
            Thread.currentThread().interrupt();
        }

        @JavascriptInterface
        public void facebookLogin() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    facebookLoginButton.performClick();
                }
            });
        }

        @JavascriptInterface
        public void googleLogin() {
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(intent, RC_SIGN_IN);
                }
            });
        }
    }

}
