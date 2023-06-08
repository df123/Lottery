package com.example.lottery.http;

import com.example.lottery.dto.input.lottery.LotteryInputDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface LotteryService {
    @Headers({
            "Host: www.cwl.gov.cn",
            "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:102.0) Gecko/20100101 Firefox/102.0",
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
            "Accept-Language: en-US,en;q=0.5"
    })
    @GET("/cwl_admin/front/cwlkj/search/kjxx/findDrawNotice")
    Call<LotteryInputDTO> getLotter(@Query("dayStart") String dayStart, @Query("dayEnd") String dayEnd, @Query("issueEnd") String issueEnd, @Query("issueStart") String issueStart, @Query("issueCount") String issueCount,@Query("name") String name,@Query("pageNo") String pageNo,@Query("pageSize") String pageSize,@Query("systemType") String systemType,@Header("Cookie") String sessionIdAndToken);

    @Headers({
            "Host: www.cwl.gov.cn",
            "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:102.0) Gecko/20100101 Firefox/102.0",
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
            "Accept-Language: en-US,en;q=0.5"
    })
    @GET
    Call<LotteryInputDTO> getLotter(@Url String url, @Header("Cookie") String sessionIdAndToken);

}
