package com.lopez.julz.disconnection.api;

import com.lopez.julz.disconnection.dao.DisconnectionList;
import com.lopez.julz.disconnection.dao.Schedules;
import com.lopez.julz.disconnection.objects.Login;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RequestPlaceHolder {
    @POST("login")
    Call<Login> login(@Body Login login);

    /**
     * DISCONNECTION
     */

    @GET("get-disconnection-list-by-meter-reader")
    Call<List<DisconnectionList>> getDisconnectionListByMeterReader(@Query("MeterReader") String MeterReader, @Query("ServicePeriod") String ServicePeriod, @Query("GroupCode") String GroupCode);

    @GET("update-disconnection-list-by-meter-reader")
    Call<Void> updateDisconnectionListByMeterReader(@Query("MeterReader") String MeterReader, @Query("ServicePeriod") String ServicePeriod, @Query("GroupCode") String GroupCode);

    @GET("get-disconnection-list-by-route")
    Call<List<DisconnectionList>> getDisconnectionListByRoute(@Query("Town") String Town, @Query("ServicePeriod") String ServicePeriod, @Query("Route") String Route);

    @GET("update-disconnection-list-by-route")
    Call<Void> updateDisconnectionListByRoute(@Query("Town") String Town, @Query("ServicePeriod") String ServicePeriod, @Query("Route") String Route);

    @POST("receive-disconnection-uploads")
    Call<Void> uploadDisconnection(@Body DisconnectionList disconnectionList);

    /**
     * BOHECO I
     */
    @GET("get-disconnection-list-schedule")
    Call<List<Schedules>> getShedulesList(@Query("UserId") String UserId);

    @GET("get-disconnection-list")
    Call<List<DisconnectionList>> getDisconnectionList(@Query("id") String id);

    @GET("update-downloaded-sched")
    Call<Void> updateDownloadedSchedule(@Query("id") String id, @Query("PhoneModel") String PhoneModel);
}
