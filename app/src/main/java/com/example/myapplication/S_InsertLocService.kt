package com.example.myapplication

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface S_InsertLocService {
    @FormUrlEncoded
    @POST("www/doo_insert_location.php") // 웹서버의 메인 다음에 나오는 주소
    fun insertLocInfo(
        @Field("id") id:String,
        @Field("final_location") final_location:String
    ) : Call<D_Msg> // 받아서 Login 클래스로 전달
}