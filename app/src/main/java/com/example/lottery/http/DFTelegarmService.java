package com.example.lottery.http;

import com.example.lottery.dto.input.df.telegram.DFTelegramTokenDTO;
import com.example.lottery.dto.input.df.telegram.lottery.ResultLotteryDto;
import com.example.lottery.dto.input.df.telegram.lottery.UploadLotteryDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface DFTelegarmService {
    @Headers({
            "User-Agent: Lottery/0.1"
    })
    @POST
    @FormUrlEncoded
    Call<DFTelegramTokenDTO> getToken(@Url String url
            , @Header("Authorization") String authorization
            , @Field("grant_type") String grantType
            , @Field("username") String userName
            , @Field("password") String password
            , @Field("scope") String scope);


    @Headers({
            "User-Agent: Lottery/0.1"
    })
    @POST
    Call<ResultLotteryDto> uploadLottery(@Url String url
            , @Header("Authorization") String authorization
            , @Body UploadLotteryDto dto);

    @Headers({
            "User-Agent: Lottery/0.1"
    })
    @POST
    Call<ResultLotteryDto> uploadLotteryBatch(@Url String url
            , @Header("Authorization") String authorization
            , @Body List<UploadLotteryDto> dtos);

}
