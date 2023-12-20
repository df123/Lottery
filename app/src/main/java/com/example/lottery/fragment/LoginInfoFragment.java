package com.example.lottery.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lottery.R;
import com.example.lottery.utils.LotterSQLiteOpenUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button saveButton;

    private EditText editBaseUrl;
    private EditText editUrl;
    private EditText editClientId;
    private EditText editGrantType;
    private EditText editScope;
    private EditText passwordClientSecret;
    private EditText editUserName;
    private EditText passwordUserPassword;


    private LotterSQLiteOpenUtils dbHelper;

    private void setDbHelper(Context context) {
        if (dbHelper == null) {
            dbHelper = new LotterSQLiteOpenUtils(context.getApplicationContext());
        }
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public LoginInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginInfoFragment newInstance(String param1, String param2) {
        LoginInfoFragment fragment = new LoginInfoFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_login_info, container, false);


        editBaseUrl = inflate.findViewById(R.id.editBaseUrl);
        editUrl = inflate.findViewById(R.id.editUrl);
        editClientId = inflate.findViewById(R.id.editClientId);
        editGrantType = inflate.findViewById(R.id.editGrantType);
        editScope = inflate.findViewById(R.id.editScope);
        passwordClientSecret = inflate.findViewById(R.id.passwordClientSecret);
        editUserName = inflate.findViewById(R.id.editUserName);
        passwordUserPassword = inflate.findViewById(R.id.passwordUserPassword);


        saveButton = inflate.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> saveInfo());
        loadInfo();

        return inflate;
    }

    private void loadInfo() {
        setDbHelper(getContext().getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("SELECT * FROM client_info LIMIT 1;", null);
            if (cursor != null) {
                while (cursor.moveToNext()) {

                    editBaseUrl.setText(cursor.getString(cursor.getColumnIndexOrThrow("base_url")));
                    editUrl.setText(cursor.getString(cursor.getColumnIndexOrThrow("url")));
                    editClientId.setText(cursor.getString(cursor.getColumnIndexOrThrow("client_id")));
                    editGrantType.setText(cursor.getString(cursor.getColumnIndexOrThrow("grant_type")));
                    editScope.setText(cursor.getString(cursor.getColumnIndexOrThrow("scope")));
                    passwordClientSecret.setText(cursor.getString(cursor.getColumnIndexOrThrow("client_secret")));
                    editUserName.setText(cursor.getString(cursor.getColumnIndexOrThrow("user_name")));
                    passwordUserPassword.setText(cursor.getString(cursor.getColumnIndexOrThrow("user_password")));

                    break;
                }
            }
        }
        db.close();
    }

    private void saveInfo() {

        if (isEditTextEmpty(editBaseUrl, "Base Url")
                || isEditTextEmpty(editUrl, "Url")
                || isEditTextEmpty(editClientId, "Client Id")
                || isEditTextEmpty(editGrantType, "Grant Type")
                || isEditTextEmpty(editScope, "Scope")
                || isEditTextEmpty(passwordClientSecret, "Client Secret")
                || isEditTextEmpty(editUserName, "User Name")
                || isEditTextEmpty(passwordUserPassword, "User Password")) {
            return;
        }

        setDbHelper(getContext().getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db.isOpen()) {

            ContentValues values = new ContentValues();
            values.put("base_url", String.valueOf(editBaseUrl.getText().toString()));
            values.put("url", String.valueOf(editUrl.getText().toString()));
            values.put("client_id", String.valueOf(editClientId.getText().toString()));
            values.put("grant_type", String.valueOf(editGrantType.getText().toString()));
            values.put("scope", String.valueOf(editScope.getText().toString()));
            values.put("client_secret", String.valueOf(passwordClientSecret.getText().toString()));
            values.put("user_name", String.valueOf(editUserName.getText().toString()));
            values.put("user_password", String.valueOf(passwordUserPassword.getText().toString()));

            try {
                Cursor cursor = db.rawQuery("SELECT * FROM client_info LIMIT 1;", null);
                if (cursor == null || cursor.getCount() <= 0) {

                    long insertStatus = db.insertOrThrow("client_info", null, values);
                    if (insertStatus == -1) {
                        Toast.makeText(getContext(), "保存失败", Toast.LENGTH_LONG).show();
                    }

                } else {

                    String[] whereArgs = new String[1];
                    while (cursor.moveToNext()) {

                        whereArgs[0] = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                        break;
                    }
                    long updateStatus = db.update("client_info", values, "id = ?", whereArgs);
                    if (updateStatus == -1) {
                        Toast.makeText(getContext(), "保存失败", Toast.LENGTH_LONG).show();
                    }

                }
            } catch (SQLiteException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        db.close();
        Toast.makeText(getContext(), "保存成功", Toast.LENGTH_LONG).show();
    }

    private boolean isEditTextEmpty(EditText editText, String tip) {
        if (editText.getText().toString().length() <= 0) {
            Toast.makeText(getContext(), tip + " 不能为空", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }


}