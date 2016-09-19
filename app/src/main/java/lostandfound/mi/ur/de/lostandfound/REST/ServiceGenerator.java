package lostandfound.mi.ur.de.lostandfound.REST;

import android.util.Base64;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;


public class ServiceGenerator {

    public static final String API_BASE_URL = "https://pod-cyan-nemesis-7039.herokuapp.com";

    private static RestAdapter.Builder builder = new RestAdapter.Builder()
            .setEndpoint(API_BASE_URL)
            .setClient(new OkClient(new OkHttpClient()));


    public static <S> S createService(Class<S> serviceClass, String username, String password) {
        if(username != null && password != null){
         //concatenate username and password with colon for auth
            String credentials = username + ":" + password;
            //create Base64 encoded string
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            builder.setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("Authorization", basic);
                    request.addHeader("Accept", "application/json");
                }
            });
        }
        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }


}
