package com.example.lottery.http.Call;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.lottery.dto.input.df.telegram.DFTelegramTokenDTO;
import com.example.lottery.dto.input.lottery.LotteryInputDTO;
import com.example.lottery.dto.input.lottery.ResultInputDTO;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DFTelegramCall implements Callback<DFTelegramTokenDTO> {

    @FunctionalInterface
    public interface UsingTokenFunction {
        void UsingToken(String token) throws IOException;
    }

    private Context mContext;
    private UsingTokenFunction mFunction;

    public DFTelegramCall(Context context,UsingTokenFunction function) {
        mContext = context;
        mFunction = function;
    }

    @Override
    public void onResponse(Call<DFTelegramTokenDTO> call, Response<DFTelegramTokenDTO> response) {
        if (response.isSuccessful()) {
            DFTelegramTokenDTO body = response.body();
            if (body == null || body.getAccess_token() == null || body.getToken_type().length() <= 0) {
                return;
            }

            if (body.getAccess_token().length() > 0 && body.getToken_type().length() > 0) {
                String token = body.getToken_type() + " " + body.getAccess_token();
                try {
                    mFunction.UsingToken(token);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return;
        }
    }

    @Override
    public void onFailure(Call<DFTelegramTokenDTO> call, Throwable t) {
        Toast.makeText(mContext, "DFTelegram:失败", Toast.LENGTH_LONG).show();
    }
}
