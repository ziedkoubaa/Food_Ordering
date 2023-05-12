package com.example.myapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.myapplication.R;
import com.example.myapplication.controller.ClientController;
import com.example.myapplication.model.ClientModel;

public class ClientActivity extends AppCompatActivity {

    private LinearLayout msgList;
    private EditText edMessage;
    private TextView textViewSend;
    private ClientController clientController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        setTitle("Client");

        // Initialize UI elements
        msgList = findViewById(R.id.msgList);
        edMessage = findViewById(R.id.edMessage);
        textViewSend = findViewById(R.id.send_data);

        // Create an instance of the model
        ClientModel clientModel = new ClientModel(this);

        // Create an instance of the controller and pass the activity and model
        clientController = new ClientController(this, clientModel);
    }

    // Create a TextView and add it to the message list
    public void showTextView(TextView textView) {
        msgList.addView(textView);
    }

    // Remove all views from the message list
    public void removeAllViews() {
        msgList.removeAllViews();
    }

    // Get the message from the EditText
    public String getMessageFromEditText() {
        return edMessage.getText().toString().trim();
    }

    // Clear the EditText
    // Clear the EditText by appending an empty string
    public void clearEditText() {
        edMessage.setText(edMessage.getText().toString().trim() + "");
    }

    // Set the click listener for the send button
    public void setSendButtonClickListener(View.OnClickListener listener) {
        textViewSend.setOnClickListener(listener);
    }
}
