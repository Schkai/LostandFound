package lostandfound.mi.ur.de.lostandfound.REST;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.Path;
import retrofit.http.PUT;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.POST;

public interface FoundItemServiceRest {

    @GET("/api/foundItems")
    void queryFoundItemItem(
            @Query("skip") String skip,
            @Query("limit") String limit,
            @Query("conditions") String conditions,
            @Query("sort") String sort,
            @Query("select") String select,
            @Query("populate") String populate,
            Callback<FoundItemItem.List> cb);

    @GET("/api/foundItems/{id}")
    void getFoundItemItemById(@Path("id") String id, Callback<FoundItemItem> cb);

    @DELETE("/api/foundItems/{id}")
    void deleteFoundItemItemById(@Path("id") String id, Callback<Object> cb);

    @POST("/api/foundItems/deleteByIds")
    void deleteByIds(@Body List<String> ids, Callback<List<FoundItemItem>> cb);

    @POST("/api/foundItems")
    void createFoundItemItem(@Body FoundItemItem item, Callback<FoundItemItem> cb);

    @PUT("/api/foundItems/{id}")
    void updateFoundItemItem(@Path("id") String id, @Body FoundItemItem item, Callback<FoundItemItem> cb);

    @GET("/api/foundItems")
    void distinct(
            @Query("distinct") String colName,
            @Query("conditions") String conditions,
            Callback<List<String>> cb);
}

