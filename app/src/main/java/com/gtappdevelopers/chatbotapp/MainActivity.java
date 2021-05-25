package com.gtappdevelopers.chatbotapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView chatsRV;
    private FloatingActionButton sendMsgFAB;
    private EditText userMsgEdt;
    private final String USER_KEY = "user";
    private final String BOT_KEY = "bot";
    private ArrayList<MessageModal> messageModalArrayList;
    private MessageRVAdapter messageRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chatsRV = findViewById(R.id.idRVChats);
        sendMsgFAB = findViewById(R.id.idFABSend);
        userMsgEdt = findViewById(R.id.idEdtMessage);
        messageModalArrayList = new ArrayList<>();
        messageRVAdapter = new MessageRVAdapter(messageModalArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
        chatsRV.setLayoutManager(linearLayoutManager);
        chatsRV.setAdapter(messageRVAdapter);

        sendMsgFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userMsgEdt.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter your message..", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendMessage(userMsgEdt.getText().toString());
                userMsgEdt.setText("");
            }
        });
    }

    private void sendMessage(String message) {
        messageModalArrayList.add(new MessageModal(message,USER_KEY));
        messageRVAdapter.notifyDataSetChanged();
        String url = "http://api.brainshop.ai/get?bid=154860&key=a0qeHISuBOho4Bpr&uid=mashape&msg=" + message;
        String BASE_URL = "http://api.brainshop.ai/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<MsgModal> call = retrofitAPI.getMessages(url);
        call.enqueue(new Callback<MsgModal>() {
            @Override
            public void onResponse(Call<MsgModal> call, Response<MsgModal> response) {
                if(response.isSuccessful()){
                    MsgModal modal = response.body();
                    messageModalArrayList.add(new MessageModal(modal.getCnt(),BOT_KEY));
                    messageRVAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MsgModal> call, Throwable t) {
                messageModalArrayList.add(new MessageModal("Can you please revert",BOT_KEY));
                messageRVAdapter.notifyDataSetChanged();
            }
        });
    }
}