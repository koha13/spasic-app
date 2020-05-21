package koha13.spasic.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS);


    public static Retrofit getClient(String baseUrl) {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create()).client(httpClient.build())
                    .build();
        }
        return retrofit;
    }

    public static final String BASE_URL = "http://spasic-api.herokuapp.com";

    public static SpasicApi getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(SpasicApi.class);
    }
}
