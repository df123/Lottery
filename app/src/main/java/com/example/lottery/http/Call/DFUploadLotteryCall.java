package com.example.lottery.http.Call;


import android.content.Context;
import android.widget.Toast;

import com.example.lottery.dto.input.df.telegram.lottery.ResultLotteryDto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DFUploadLotteryCall implements Callback<ResultLotteryDto> {

    private Context mContext;

    public DFUploadLotteryCall(Context context){
        mContext = context;
    }

    @Override
    public void onResponse(Call<ResultLotteryDto> call, Response<ResultLotteryDto> response) {
        if(response.isSuccessful()){
            Toast.makeText(mContext, String.valueOf(response.body().getId()), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<ResultLotteryDto> call, Throwable t) {
        Toast.makeText(mContext, "DFTelegram:失败", Toast.LENGTH_LONG).show();
    }
}
