package free.videodownloader.whatsapp.status.statusdownloader.story.saver.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(600, TimeUnit.SECONDS)
            .writeTimeout(650, TimeUnit.SECONDS)
            //.addInterceptor(logging)
            .build();

    public static Retrofit getClient(String str) {
//
        return new Builder().client(okHttpClient).baseUrl(str).addConverterFactory(GsonConverterFactory.create()).build();
    }
}

