package com.example.myapplicationserver.controller;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.myapplicationserver.R;
import com.example.myapplicationserver.model.ServerModel;
import com.example.myapplicationserver.view.ServerActivity;

public class ServerController implements ServerModel.MessageListener, View.OnClickListener {
    private ServerActivity serverActivity;
    private ServerModel serverModel;

    private LinearLayout msgList;
    private Handler handler;
    private EditText edMessage;
    private View startServerButton;
    private boolean isServerStarted = false; // New flag

    public ServerController(ServerActivity serverActivity, ServerModel serverModel) {
        this.serverActivity = serverActivity;
        this.serverModel = serverModel;

        // Initialize UI elements and handler
        handler = new Handler();
        msgList = serverActivity.findViewById(R.id.msgList);
        edMessage = serverActivity.findViewById(R.id.edMessage);
        startServerButton = serverActivity.findViewById(R.id.start_server);

        // Set click listeners
        startServerButton.setOnClickListener(this);
        serverActivity.findViewById(R.id.send_data).setOnClickListener(this);

        // Set message listener
        serverModel.setMessageListener(this);
    }

    // Create a TextView to display a message with a specific color and timestamp
    public TextView textView(String message, int color) {
        if (null == message || message.trim().isEmpty()) {
            message = "<Empty Message>";
        }
        String m = message + " [" + serverModel.getTime() + "]";
        TextView tv = new TextView(serverActivity);
        tv.setTextColor(color);
        tv.setText(m);
        tv.setTextSize(20);
        tv.setPadding(0, 5, 0, 0);
        return tv;
    }

    // Show a message on the UI with a specific color
    public void showMessage(final String message, final int color) {
        handler.post(() -> msgList.addView(textView(message, color)));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.start_server) {
            if (!isServerStarted) { // Check if server is not already started
                isServerStarted = true; // Update the flag
                startServerButton.setVisibility(View.GONE); // Hide the "Start Server" button
                // Start the server when the "Start Server" button is clicked
                removeAllViews();
                showMessage("Server Started.", Color.GREEN);
                serverModel.startServer();
            }
        } else if (view.getId() == R.id.send_data) {
            // Send a message to the client when the "Send" button is clicked
            String msg = edMessage.getText().toString().trim();
            showMessage("Server : " + msg, Color.parseColor("#F0FFF0"));
            serverModel.sendMessage(msg);
        }
    }

    // Remove all views from the message list
    private void removeAllViews() {
        handler.post(() -> msgList.removeAllViews());
    }
    @Override
    public void onMessageReceived(final String message) {
        handler.post(() -> {
            if (message.startsWith("[GREEN]")) {
                showMessage(message.substring(7), Color.GREEN);
            } else {
                showMessage(message, Color.WHITE);
            }
        });
    }

}
