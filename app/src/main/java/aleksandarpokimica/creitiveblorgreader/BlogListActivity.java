package aleksandarpokimica.creitiveblorgreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import aleksandarpokimica.creitiveblorgreader.adapters.BlogListAdapter;
import aleksandarpokimica.creitiveblorgreader.models.BlogList;
import aleksandarpokimica.creitiveblorgreader.services.RetrofitClient;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BlogListActivity extends AppCompatActivity {

    private String userToken;

    private List<BlogList> blogList;

    private RecyclerView recyclerView;
    private BlogListAdapter blogListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list);

        Intent loginIntent = getIntent();
        userToken = loginIntent.getStringExtra("USER_TOKEN");

        recyclerView = findViewById(R.id.recyclerViewBlogList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        blogList = new ArrayList<>();
        
        populateBlogList();

    }

    private void populateBlogList() {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        if(BuildConfig.DEBUG){
            okhttpClientBuilder.addInterceptor(logging);
        }
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://blogsdemo.creitiveapps.com/")
                .client(okhttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        RetrofitClient retrofitClient = retrofit.create(RetrofitClient.class);

        Call<List<BlogList>> call = retrofitClient.displayBlogList(userToken);
        call.enqueue(new Callback<List<BlogList>>() {
            @Override
            public void onResponse(Call<List<BlogList>> call, Response<List<BlogList>> response) {
                blogList = response.body();
                blogListAdapter = new BlogListAdapter(blogList, BlogListActivity.this, userToken);
                recyclerView.setAdapter(blogListAdapter);
            }

            @Override
            public void onFailure(Call<List<BlogList>> call, Throwable t) {
                Toast.makeText(BlogListActivity.this, "Internet connection required.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
