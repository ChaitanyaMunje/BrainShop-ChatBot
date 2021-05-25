package com.gtappdevelopers.chatbotapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitAPI {
    @GET
    Call<MsgModal> getMessages(@Url String url);
}
