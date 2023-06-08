package com.example.lottery.fragment;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.example.lottery.CustomAdapter.TableAdapter;
import com.example.lottery.MainActivity;
import com.example.lottery.R;
import com.example.lottery.dto.input.lottery.LotteryInputDTO;
import com.example.lottery.dto.input.lottery.ResultInputDTO;
import com.example.lottery.entity.Lottery;
import com.example.lottery.http.Call.LotterGetDataCall;
import com.example.lottery.http.LotteryService;
import com.example.lottery.utils.ImageUtils;
import com.example.lottery.utils.LotterSQLiteOpenUtils;
import com.example.lottery.utils.LotteryUtils;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String TAG = "camera_fragment";

    private ActivityResultLauncher<Uri> resultPictureUri;
    private Boolean isCutPicture;
    private String fileName;
    private List<String> recognitionLottery;
    private String dayStart;
    private String dayEnd;
    private ActivityResultLauncher<CropImageContractOptions> cropImage;
    private String cookie;
    private List<Lottery> lotteryEntity;

    private RecyclerView recycler_view_lottery;

    private RecyclerView mLotteryResultView;
    private Button btn_pick_date;
    private Button btn_save_buy_lotter;
    private EditText edit_lotter_no;
    private Button btn_search;

    public void setPictureListPaths(String pictureListPaths) {
        this.pictureListPaths = pictureListPaths;
    }

    private String pictureListPaths;
    private LotterSQLiteOpenUtils dbHelper;

    private List<String> buy_lotter_no_list;

    private void setDbHelper(Context context) {
        if (dbHelper == null) {
            dbHelper = new LotterSQLiteOpenUtils(context.getApplicationContext());
        }
    }

    public CameraFragment() {
        // Required empty public constructor
        pictureListPaths = null;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CameraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            TAG = mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_camera, container, false);

        cookie = "";
        recognitionLottery = new ArrayList<String>(5);
        resultPictureUri = getTaskPicture(getContext());
        Button btn = inflate.findViewById(R.id.btn1);
        Button btnCROP = inflate.findViewById(R.id.btnCROP);
        Button btnLottery = inflate.findViewById(R.id.btnLottery);
        btn_pick_date = inflate.findViewById(R.id.btn_pick_date);
        cropImage = getCropImage(getContext());
        buy_lotter_no_list = new ArrayList<>();

        Button btn_add_buy_lotter = inflate.findViewById(R.id.btn_add_buy_lotter);

        btn_save_buy_lotter = inflate.findViewById(R.id.btn_save_buy_lotter);

        edit_lotter_no = inflate.findViewById(R.id.edit_lotter_no);

        EditText edit_start_index = inflate.findViewById(R.id.edit_start_index);
        EditText edit_end_index = inflate.findViewById(R.id.edit_end_index);

        btn_add_buy_lotter.setOnClickListener(v -> {
            TableAdapter tempTA = (TableAdapter) recycler_view_lottery.getAdapter();
            List<String> temp = null;
            if (tempTA == null) {
                temp = new ArrayList<>();
            } else {
                temp = tempTA.getmData();
            }
            for (int i = 0; i < 7; i++) {
                temp.add("");
            }
            recycler_view_lottery.setAdapter(new TableAdapter(temp));
        });

        btn_save_buy_lotter.setOnClickListener(v -> saveBuyLotter());

        btnLottery.setOnClickListener(v -> getLottery());

        btn.setOnClickListener(view -> startRecognition(false));

        btnCROP.setOnClickListener(v -> startRecognition(true));

        btn_search = inflate.findViewById(R.id.btn_search_buy_lottery);

        btn_search.setOnClickListener(v -> btnSearchBuyLottery(Integer.parseInt(edit_start_index.getText().toString()), Integer.parseInt(edit_end_index.getText().toString())));

        btn_pick_date.setOnClickListener(v -> {
            if (dayStart != null && dayEnd != null && (!dayStart.isEmpty()) && (!dayEnd.isEmpty())) {
                dayStart = dayEnd = null;
                btn_pick_date.setText("选择结束日期");
            } else {
                DatePickerFragment datePickerDialog = new DatePickerFragment(CameraFragment.this);
                datePickerDialog.show(CameraFragment.this.getParentFragmentManager(), "datePicker");
            }
        });

        recycler_view_lottery = initRecyclerView(inflate, R.id.recycler_view_lottery, 7, getActivity());
        mLotteryResultView = initRecyclerView(inflate, R.id.recycler_view_lottery_result, 9, getActivity());

        return inflate;
    }

    private void btnSearchBuyLottery(int start, int end) {
        List<String> temp = new ArrayList<>();
        setDbHelper(getContext().getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("SELECT * FROM buy_lottery WHERE index_no >= " + start + " AND index_no <= " + end + " ORDER BY id;", null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    temp.add(cursor.getString(cursor.getColumnIndexOrThrow("number")));
                }
            }
        }
        db.close();
        recycler_view_lottery.setAdapter(new TableAdapter(temp));
    }

    private void saveBuyLotter() {
        if (edit_lotter_no.getText() == null || edit_lotter_no.getText().length() <= 0) {
            edit_lotter_no.requestFocus();
            Toast.makeText(getContext(), "请输入期号", Toast.LENGTH_LONG).show();
            return;
        }

        TableAdapter tableAdapter = (TableAdapter) recycler_view_lottery.getAdapter();
        if (tableAdapter.getmData() == null) {
            Toast.makeText(getContext(), "号码为空", Toast.LENGTH_LONG).show();
            return;
        }

        boolean isSaveSuccess = true;
        setDbHelper(getContext().getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db.isOpen()) {

            if (tableAdapter.getmData().size() % 7 != 0) {
                Toast.makeText(getContext(), "填入的号码不是7的整数！！！", Toast.LENGTH_LONG).show();
                return;
            }


            for (int i = 0; i < tableAdapter.getmData().size(); i++) {
                ContentValues values = new ContentValues();
                values.put("index_no", Integer.parseInt(edit_lotter_no.getText().toString()));
                values.put("number", tableAdapter.getmData().get(i));
                values.put("color_type", i % 7 == 0);
                try {
                    long insertStatus = db.insertOrThrow("buy_lottery", null, values);
                    if (insertStatus == -1) {
                        isSaveSuccess = false;
                    }
                }catch (SQLiteException e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    break;
                }
            }


        }
        db.close();

        if (isSaveSuccess) {
            tableAdapter.getmData().clear();
            recycler_view_lottery.setAdapter(tableAdapter);
            Toast.makeText(getContext(), "保存成功", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getContext(), "保存失败！！！", Toast.LENGTH_LONG).show();
        }

    }

    private RecyclerView initRecyclerView(View inflate, int id, int spanCount, FragmentActivity fragmentActivity) {
        RecyclerView recyclerView = inflate.findViewById(id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(fragmentActivity, spanCount));
        return recyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pictureListPaths != null) {
            recognition(ImageUtils.getImage(getContext(), pictureListPaths));
            pictureListPaths = null;
        }
    }

    private void getLottery() {
        setDbHelper(getContext().getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from lottery_cookie ORDER BY id DESC LIMIT 1;", null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    cookie = cursor.getString(cursor.getColumnIndexOrThrow("cookie"));
                }
            }
        }
        db.close();

        TableAdapter tableAdapter = (TableAdapter) recycler_view_lottery.getAdapter();
        assert tableAdapter != null;
        lotteryEntity = LotteryUtils.getLotteryEntity(tableAdapter.getmData());

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.cwl.gov.cn")
                .client(new OkHttpClient().newBuilder().followRedirects(false).build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        LotteryService lotteryService = retrofit.create(LotteryService.class);
        Call<LotteryInputDTO> ssq = lotteryService.getLotter(dayStart, dayEnd, "", "", "", "ssq", "1", "30", "PC", cookie);

        LotterGetDataCall lotterGetDataCall = new LotterGetDataCall(getContext(), lotteryService, this::multiResult);
        ssq.enqueue(lotterGetDataCall);
    }

    private void multiResult(ResultInputDTO resultInputDTO, List<String> result) {
        for (Lottery item : lotteryEntity) {
            String winResult = LotteryUtils.win(resultInputDTO.getRed().split(","), resultInputDTO.getBlue(), item.reds.toArray(new String[6]), item.blues.get(0));
            if (!winResult.equals("-2")) {
                for (String red : item.reds) {
                    result.add(red);
                }
                result.add(item.blues.get(0));
                result.add(winResult);
                result.add(resultInputDTO.getCode());
            }
        }
        buy_lotter_no_list.addAll(result);
        mLotteryResultView.setAdapter(new TableAdapter(buy_lotter_no_list));
    }

    private void startRecognition(boolean cutPicture) {
        isCutPicture = cutPicture;
        fileName = UUID.randomUUID().toString() + ".png";
        Uri uriForFile = FileProvider.getUriForFile(getContext(), getString(R.string.file_provider_authorities), new File(getContext().getFilesDir(), fileName));
        resultPictureUri.launch(uriForFile);
    }

    private void recognition(InputImage inputImage) {
        TextRecognizer tr = TextRecognition.getClient(new ChineseTextRecognizerOptions.Builder().build());
        if (inputImage != null) {
            Task<Text> re = tr.process(inputImage).addOnSuccessListener(new OnSuccessListener<Text>() {
                @Override
                public void onSuccess(Text text) {
                    recognitionLottery.clear();
                    StringBuffer sb = new StringBuffer();
                    for (Text.TextBlock block : text.getTextBlocks()) {
                        for (Text.Line line : block.getLines()) {
                            String lineText = line.getText();
                            if (lineText.contains("红") || lineText.contains("蓝") || lineText.contains("藍")) {
                                lineText = lineText.replace("红", "")
                                        .replace("蓝", "")
                                        .replace("藍", "")
                                        .replace("-", "")
                                        .replace(" ", "");
                                String[] s = lineText.split("区");
                                if (s != null && s.length == 3) {
                                    lineText = s[1] + s[2];
                                    if (lineText.length() == 14) {
                                        sb.append(lineText + "\n");
                                        recognitionLottery.add(lineText);
                                    }
                                }
                            }
                        }
                    }
                    lotteryEntity = LotteryUtils.getLotteryEntitySub(recognitionLottery);

                    setGridLottery(null, lotteryEntity);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }
            });
        }
    }

    private void setGridLottery(List<String> data, List<Lottery> lotteryList) {
        if (data == null) {
            data = new ArrayList<>();
        } else {
            data.clear();
        }

        for (Lottery item :
                lotteryList) {
            for (String str :
                    item.reds) {

                data.add(str);
            }
            data.add(item.blues.get(0));
        }

        recycler_view_lottery.setAdapter(new TableAdapter(data));
    }


    private ActivityResultLauncher<Uri> getTaskPicture(Context context) {
        return registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result) {
                Uri uri = Uri.fromFile(new File(context.getFilesDir(), fileName));
                if (isCutPicture) {
                    CropImageOptions cropImageOptions = new CropImageOptions();
                    cropImageOptions.guidelines = CropImageView.Guidelines.ON;
                    CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(uri, cropImageOptions);
                    cropImage.launch(cropImageContractOptions);
                } else if (result) {
                    recognition(ImageUtils.getImage(context, uri));
                }
            }
        });
    }

    private ActivityResultLauncher<CropImageContractOptions> getCropImage(Context context) {
        return registerForActivityResult(new CropImageContract(), result -> {
            Uri uriContent = result.getUriContent();
            recognition(ImageUtils.getImage(context, uriContent));
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String tempDay = null;
        if (month < 10 && dayOfMonth < 10) {
            tempDay = String.format("%s-0%s-0%s", year, month + 1, dayOfMonth);
        } else if (month < 10) {
            tempDay = String.format("%s-0%s-%s", year, month + 1, dayOfMonth);
        } else if (dayOfMonth < 10) {
            tempDay = String.format("%s-%s-0%s", year, month + 1, dayOfMonth);
        } else {
            tempDay = String.format("%s-%s-%s", year, month + 1, dayOfMonth);
        }

        if (tempDay != null) {
            if (dayEnd == null || dayEnd.isEmpty()) {
                dayEnd = tempDay;
                btn_pick_date.setText("选择开始日期");
            } else if ((dayStart == null || dayStart.isEmpty())) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date parseEnd = simpleDateFormat.parse(dayEnd);
                    Date parseStart = simpleDateFormat.parse(tempDay);
                    if (parseStart.before(parseEnd) || parseStart.equals(parseEnd)) {
                        dayStart = tempDay;
                        btn_pick_date.setText("时间范围:" + dayStart + "到" + dayEnd);
                    }
                } catch (ParseException e) {
                    Toast.makeText(getContext(), "日期转换失败", Toast.LENGTH_LONG).show();
                }
            }
        }

        Log.d(TAG, "onDateSet: " + dayEnd);
    }

}