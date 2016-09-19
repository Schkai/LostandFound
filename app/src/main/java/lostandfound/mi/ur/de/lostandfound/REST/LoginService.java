package lostandfound.mi.ur.de.lostandfound.REST;

import retrofit.http.POST;

/**
 * Created by Konstantin on 19.09.2016.
 */
public interface LoginService {
    @POST("/#/login")
    FoundItemItem findItem();
}
