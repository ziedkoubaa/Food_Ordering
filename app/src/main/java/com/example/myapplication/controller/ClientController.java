package com.example.myapplication.controller;

import android.view.View;
import com.example.myapplication.R;
import com.example.myapplication.model.ClientModel;
import com.example.myapplication.view.ClientActivity;

public class ClientController implements View.OnClickListener {

    private ClientActivity clientActivity;
    private ClientModel clientModel;

    public ClientController(ClientActivity clientActivity, ClientModel clientModel) {
        this.clientActivity = clientActivity;
        this.clientModel = clientModel;

        // Set the click listener for the send button
        clientActivity.setSendButtonClickListener(this);

        // Connect to the server
        clientModel.connectToServer();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send_data) {
            // Get the message from the EditText
            String clientMessage = clientActivity.getMessageFromEditText();
            // Send the message to the server
            clientModel.sendMessage(clientMessage);
            // Clear the EditText
            clientActivity.clearEditText();
        }
    }
}