package aleksandarpokimica.creitiveblorgreader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import aleksandarpokimica.creitiveblorgreader.models.Login;
import aleksandarpokimica.creitiveblorgreader.models.User;
import aleksandarpokimica.creitiveblorgreader.services.RetrofitClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonLogin;
    //I've added a checkbox to give the user an option to remember his credentials
    CheckBox checkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        if(!sharedPreferences.getString("USER_TOKEN", "").isEmpty()){
            Intent i = new Intent(LoginActivity.this, BlogListActivity.class);
            i.putExtra("USER_TOKEN", sharedPreferences.getString("USER_TOKEN", ""));
            startActivity(i);
        }

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        checkBoxRememberMe = (CheckBox) findViewById(R.id.checkBoxRememberMe);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                if(!isValidEmail(editTextEmail.getText().toString())){
                    editTextEmail.setError("Enter a valid email");
                }
                if(editTextPassword.getText().length() < 6){
                    editTextPassword.setError("Minimum 6 characters");
                }else{
                    userLogin();
                }
            }
        });
    }

    private void userLogin() {

        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        if(BuildConfig.DEBUG) {
            okhttpClientBuilder.addInterceptor(logging);
        }
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://blogsdemo.creitiveapps.com/")
                .client(okhttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        RetrofitClient retrofitClient = retrofit.create(RetrofitClient.class);

        String emailEntered = editTextEmail.getText().toString();
        String passwordEntered = editTextPassword.getText().toString();

        Login login = new Login(emailEntered, passwordEntered);

        Call<User> call = retrofitClient.login(login);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    if(checkBoxRememberMe.isChecked()){
                        saveToken(response.body().getToken());
                    }
                    Toast.makeText(LoginActivity.this, "You've successfully logged in.", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(LoginActivity.this, BlogListActivity.class);
                    i.putExtra("USER_TOKEN", response.body().getToken());
                    startActivity(i);
                }
                else if(response.code() == 401){
                    Toast.makeText(LoginActivity.this, "Wrong email or password.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Internet connection required.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isValidEmail(CharSequence email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    private void saveToken(String token){
        SharedPreferences.Editor spEditor = getPreferences(Context.MODE_PRIVATE).edit();
        spEditor.putString("USER_TOKEN", token);
        spEditor.apply();
    }

}






















