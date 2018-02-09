//The way this page is formatted is obviously broken and not presented
//in a way that you implied with your suggested design
//
//I've considered using Jsoup to parse HTML tags and their values into
//Java objects and present the data in a typical layout with Views instead
//of WebView, but the tag names that the API returns seem unconventional
//and there are HTML specific entities like &nbsp and line breaks with \r and \t.
//
//I could cut those out, but I was hoping I might get a word from you before
//embarking on something like that :)
//
//Aleksandar

package aleksandarpokimica.creitiveblorgreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.List;

import aleksandarpokimica.creitiveblorgreader.models.BlogItem;
import aleksandarpokimica.creitiveblorgreader.models.BlogList;
import aleksandarpokimica.creitiveblorgreader.services.RetrofitClient;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DisplayBlogActivity extends AppCompatActivity {

    private WebView webViewItem;
    private String itemId;
    private String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_blog);

        webViewItem = findViewById(R.id.webViewItem);

        Intent intent = getIntent();
        itemId = intent.getStringExtra("ITEM_ID");
        userToken = intent.getStringExtra("USER_TOKEN");

        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        if(BuildConfig.DEBUG){
            okhttpClientBuilder.addInterceptor(logging);
        }

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://blogsdemo.creitiveapps.com/blogs/")
                .client(okhttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        RetrofitClient retrofitClient = retrofit.create(RetrofitClient.class);

        Call<BlogItem> call = retrofitClient.displayBlogItem(itemId, userToken);

        call.enqueue(new Callback<BlogItem>() {

            @Override
            public void onResponse(Call<BlogItem> call, Response<BlogItem> response) {
                String itemHtml = response.body().getContent();
                webViewItem.loadData(itemHtml, "text/html", null);
                webViewItem.getSettings().setJavaScriptEnabled(true);
                webViewItem.getSettings().setBuiltInZoomControls(true);
            }

            @Override
            public void onFailure(Call<BlogItem> call, Throwable t) {
                webViewItem.loadData("<H1>Network error.</H1><br><br><h3>Check your internet connection.", "text/html", null);
            }
        });
    }
}
