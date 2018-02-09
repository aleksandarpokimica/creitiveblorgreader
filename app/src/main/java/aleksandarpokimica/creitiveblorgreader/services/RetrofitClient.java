package aleksandarpokimica.creitiveblorgreader.services;

import java.util.List;

import aleksandarpokimica.creitiveblorgreader.models.BlogItem;
import aleksandarpokimica.creitiveblorgreader.models.BlogList;
import aleksandarpokimica.creitiveblorgreader.models.Login;
import aleksandarpokimica.creitiveblorgreader.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Aleksandar on 2/1/2018.
 */

public interface RetrofitClient {

    @POST("login")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
    })
    Call<User> login(@Body Login login);

    @GET("blogs")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json",
    })
    Call<List<BlogList>> displayBlogList(@Header("X-Authorize") String authHeader);

    @GET("{id}")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    Call<BlogItem> displayBlogItem(@Path("id") String id, @Header("X-Authorize") String authHeader);

}
