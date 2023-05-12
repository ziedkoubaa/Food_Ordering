package com.example.myapplicationserver.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.myapplicationserver.R;
import com.example.myapplicationserver.controller.ServerController;
import com.example.myapplicationserver.model.ServerModel;

public class ServerActivity extends AppCompatActivity {
    private ServerController serverController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        setTitle("Server");

        // Create an instance of the model
        ServerModel serverModel = new ServerModel();

        // Create an instance of the controller and pass the activity and model
        serverController = new ServerController(this, serverModel);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            serverController = null;
        }
    }
