package free.videodownloader.whatsapp.status.statusdownloader.story.saver.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface Appservice {


    @FormUrlEncoded
    @POST
    Call<ResponseBody> getAdsJson(@Url String str, @Field(APIContent.package_name) String package_name);

}