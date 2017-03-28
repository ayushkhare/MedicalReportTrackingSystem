package sample.com.medicalrecordtrackingsystem.rest;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ayush on 27/3/17
 */
public class ApiClient {

    public static final String BASE_URl = "http://medrecordhelp.herokuapp.com/webapi/";
    private static Retrofit retrofit = null;

    public static Retrofit getCient() {
        if (retrofit == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }

}
