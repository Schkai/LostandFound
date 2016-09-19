package lostandfound.mi.ur.de.lostandfound.helpers;

import android.text.TextUtils;
import android.util.Base64;

import lostandfound.mi.ur.de.lostandfound.helpers.SearchOptions;
import lostandfound.mi.ur.de.lostandfound.helpers.Filter;
import lostandfound.mi.ur.de.lostandfound.helpers.DateJsonTypeAdapter;
import lostandfound.mi.ur.de.lostandfound.helpers.DecimalJsonTypeAdapter;
import lostandfound.mi.ur.de.lostandfound.helpers.URLJsonTypeAdapter;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.squareup.okhttp.OkHttpClient;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RequestInterceptor.RequestFacade;
import retrofit.RestAdapter.Builder;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

public abstract class RestService<R> {
    private final Class<R> mClass;
    Converter mConverter;
    private R mServiceProxy;

    public RestService(Class<R> clazz) {
        this.mClass = clazz;
    }

    public R getServiceProxy() {
        if (this.mServiceProxy == null) {
            RestAdapter restAdapter = this.createRestAdapterBuilder();
            this.mServiceProxy = restAdapter.create(this.mClass);
        }

        return this.mServiceProxy;
    }

    protected RestAdapter createRestAdapterBuilder() {
        Builder builder = (new Builder()).setClient(this.getClient()).setEndpoint(this.getServerUrl()).setConverter(this.getConverter()).setLogLevel(this.getLogLevel());
        if (!this.tryApiKey(builder) && !this.tryBasicAuth(builder)) {
            throw new IllegalArgumentException("AppNow datasource needs an api key or user-pwd pair !");
        } else {
            return builder.build();
        }
    }

    protected boolean tryApiKey(Builder builder) {
        final String apiKey = this.getApiKey();
        if (apiKey == null) {
            return false;
        } else {
            builder.setRequestInterceptor(new RequestInterceptor() {
                public void intercept(RequestFacade request) {
                    request.addHeader("apikey", apiKey);
                }
            });
            return true;
        }
    }

    protected boolean tryBasicAuth(Builder builder) {
        final String user = this.getApiUser();
        final String pwd = this.getApiPassword();
        if (user != null && pwd != null) {
            builder.setRequestInterceptor(new RequestInterceptor() {
                public void intercept(RequestFacade request) {
                    String credentials = user + ":" + pwd;
                    String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), 2);
                    request.addHeader("Authorization", "Basic " + base64EncodedCredentials);
                }
            });
            return true;
        } else {
            return false;
        }
    }

    protected String getConditions(SearchOptions options, String[] searchCols) {
        if (options == null) {
            return null;
        } else {
            ArrayList exps = new ArrayList();
            String searchExp;
            if (options.getFilters() != null) {
                Iterator st = options.getFilters().iterator();

                while (st.hasNext()) {
                    Filter searches = (Filter) st.next();
                    searchExp = searches.getQueryString();
                    if (searchExp != null) {
                        exps.add(searchExp);
                    }
                }
            }

            String var10 = options.getSearchText();
            if (var10 != null && searchCols != null && searchCols.length > 0) {
                ArrayList var11 = new ArrayList();
                String[] var12 = searchCols;
                int len$ = searchCols.length;

                for (int i$ = 0; i$ < len$; ++i$) {
                    String col = var12[i$];
                    var11.add("{\"" + col + "\":{\"$regex\":\"" + var10 + "\",\"$options\":\"i\"}}");
                }

                searchExp = "\"$or\":[" + TextUtils.join(",", var11) + "]";
                exps.add(searchExp);
            }

            return exps.size() > 0 ? "{" + TextUtils.join(",", exps) + "}" : null;
        }
    }

    protected String getSort(SearchOptions options) {
        String col = options.getSortColumn();
        boolean asc = options.isSortAscending();
        if (col == null) {
            return null;
        } else {
            if (!asc) {
                col = "-" + col;
            }

            return col;
        }
    }

    protected Converter createConverter() {
        Gson gson = (new GsonBuilder()).serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).registerTypeAdapter(Double.class, new DecimalJsonTypeAdapter()).registerTypeAdapter(Date.class, new DateJsonTypeAdapter()).registerTypeAdapter(URL.class, new URLJsonTypeAdapter()).addSerializationExclusionStrategy(new ExclusionStrategy() {
            public boolean shouldSkipField(FieldAttributes f) {
                SerializedName annotation = (SerializedName) f.getAnnotation(SerializedName.class);
                return annotation != null && annotation.value().equals("_id");
            }

            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();
        return new GsonConverter(gson);
    }

    protected Converter getConverter() {
        if (this.mConverter == null) {
            this.mConverter = this.createConverter();
        }

        return this.mConverter;
    }

    protected LogLevel getLogLevel() {
        return LogLevel.NONE;
    }

    protected Client getClient() {
        OkHttpClient c = new OkHttpClient();
        c.setConnectTimeout(this.getHttpClientTimeout(), TimeUnit.SECONDS);
        return new OkClient(c);
    }

    protected long getHttpClientTimeout() {
        return 5L;
    }

    protected String getApiKey() {
        return null;
    }

    protected String getApiUser() {
        return null;
    }

    protected String getApiPassword() {
        return null;
    }

    public abstract String getServerUrl();

    public abstract URL getImageUrl(String var1);
}
