package testing.uber.tuhinkole.uber.Common;

import retrofit2.Retrofit;
import testing.uber.tuhinkole.uber.Remote.IGoogleAPI;
import testing.uber.tuhinkole.uber.Remote.RetrofitClient;

public class Common {
    public static final String baseURL = "https://maps.googleapis.com";
    public static IGoogleAPI getIGoogleAPI(){
        return RetrofitClient.getClient(baseURL).create(IGoogleAPI.class);
    }
}
