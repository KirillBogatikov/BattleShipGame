package org.battleshipgame.reporter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ReportApi {
    @POST("report/")
    @Headers("App-Name: BattleShipGame")
    public Call<String> report(@Body Report report, @Header("Access-Token") String signature);
}
