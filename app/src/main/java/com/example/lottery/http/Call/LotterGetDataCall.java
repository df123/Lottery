package com.example.lottery.http.Call;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.lottery.dto.input.lottery.LotteryInputDTO;
import com.example.lottery.dto.input.lottery.ResultInputDTO;
import com.example.lottery.http.LotteryService;
import com.example.lottery.utils.LotterSQLiteOpenUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LotterGetDataCall implements Callback<LotteryInputDTO> {

    private SQLiteDatabase db;

    @FunctionalInterface
    public interface LotterGetDataFunction {
        void multiResult(ResultInputDTO resultInputDTO, List<String> result);
    }

    private Context mContext;
    private String cookie;
    private LotterGetDataFunction mFunction;
    private String location;
    private LotteryService mLotteryService;
    private LotterSQLiteOpenUtils dbHelper;

    public LotterGetDataCall(Context context, LotteryService lotteryService, LotterGetDataFunction function) {
        mContext = context;
        mFunction = function;
        cookie = null;
        location = null;
        mLotteryService = lotteryService;
        dbHelper = new LotterSQLiteOpenUtils(context.getApplicationContext());
    }

    @Override
    public void onResponse(Call<LotteryInputDTO> call, Response<LotteryInputDTO> response) {
        if (response.isSuccessful()) {
            String tempCookie = response.headers().get("Set-Cookie");
            if (tempCookie != null && (!tempCookie.isEmpty()) && tempCookie.length() > 0) {
                cookie = tempCookie;
            }

            LotteryInputDTO body = response.body();
            if (body.getResult().size() <= 0) {
                return;
            }

            List<ResultInputDTO> result = body.getResult();
            List<String> lotteryResult = new ArrayList<>();
            for (ResultInputDTO item : result) {
                mFunction.multiResult(item, lotteryResult);
            }
        } else {
            String cookie = response.headers().get("Set-Cookie");

            if (cookie != null && cookie.length() > 0) {
                db = dbHelper.getWritableDatabase();
                if(db.isOpen())
                {
                    ContentValues values = new ContentValues();
                    values.put("cookie", cookie);
                    db.insert("lottery_cookie", null, values);
                    db.close();
                }
            }
            location = response.headers().get("Location");
            mLotteryService.getLotter(location, cookie).enqueue(this);
        }
    }

    @Override
    public void onFailure(Call<LotteryInputDTO> call, Throwable t) {
        Toast.makeText(mContext, "获取开奖双色球失败", Toast.LENGTH_LONG).show();
    }
}
