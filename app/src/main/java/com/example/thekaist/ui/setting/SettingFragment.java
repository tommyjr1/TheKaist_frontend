package com.example.thekaist.ui.setting;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thekaist.FrontActivity;
import com.example.thekaist.GameActivity;
import com.example.thekaist.LoginResult;
import com.example.thekaist.MainActivity;
import com.example.thekaist.R;
import com.example.thekaist.RetrofitInterface;
import com.example.thekaist.UserInfo;
import com.example.thekaist.changeUserinfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingFragment extends Fragment {

    private SettingViewModel settingViewModel;
    private TextView profile_name, profile_id, profile_change, profile_history, develop;
    private ImageView img;

    public String id = FrontActivity.id;
    public String imgnum, pwd;
    public static String name;


    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = MainActivity.BASE_URL;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingViewModel =
                new ViewModelProvider(this).get(SettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_setting, container, false);


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        profile_name = root.findViewById(R.id.user_name);
        profile_id = root.findViewById(R.id.user_id);
        profile_change = root.findViewById(R.id.change);
        profile_history = root.findViewById(R.id.history);
        develop = root.findViewById(R.id.developer);
        img = root.findViewById(R.id.User_pic);

        Button testButton = root.findViewById(R.id.gametest);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GameActivity.class);
                intent.putExtra("ask", "test1");
                intent.putExtra("accept", "test2");

                startActivity(intent);
            }
        });

        init();

        profile_id.setText(id);
        profile_name.setText(name);

        
        profile_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change();
            }
        });
        
        profile_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history();
            }
        });
        
        return root;
    }

    private void history() {
    }

    private void change() {
        Intent intent = new Intent(getContext(), changeUserinfo.class);
        intent.putExtra("name", name);
        intent.putExtra("password", pwd);
        intent.putExtra("imgnum", imgnum);
        intent.putExtra("id", id);
        startActivity(intent);

    }

    private void init(){
        HashMap<String, String> map = new HashMap<>();//key도 스트링, 값도 스트링

        map.put("id", id);

        Call<UserInfo> call = retrofitInterface.executeUserinfo(map);//로그인리절트 클래스 부르는데저 map넣어서 함

        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if(response.code() == 200){
                    UserInfo result = response.body();//응답의 내용. 이와같은 디비구조인게 loginresult.

                    name = result.getName();
                    profile_name.setText(name);

                    pwd = result.getPassword();
                    imgnum = result.getImgnumber();

                    switch(imgnum){
                        case "1":
                            img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.character1));
                            break;


                        case "2":
                            img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.character2));
                            break;

                        case "3":
                            img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.character3));

                            break;

                        case "4":
                            img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.character4));

                            break;
                    }

                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });
    }
}