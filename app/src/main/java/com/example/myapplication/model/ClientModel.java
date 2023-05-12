package com.example.myapplication.model;

import android.graphics.Color;
import android.os.Handler;
import android.widget.TextView;
import com.example.myapplication.view.ClientActivity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientModel {
    public static final int SERVER_PORT = 1234;
    public static final String SERVER_IP = "192.168.1.11";

    private ClientThread clientThread;
    private Thread thread;
    private Handler handler;
    private int clientTextColor;

    private ClientActivity clientActivity;

    public ClientModel(ClientActivity clientActivity) {
        this.clientActivity = clientActivity;
        this.handler = new Handler();
        this.clientTextColor = Color.WHITE;
    }

    // Create a TextView with the given message and color
    private TextView createTextView(String message, int color) {
        if (null == message || message.trim().isEmpty()) {
            message = "<Empty Message>";
        }
        String m = message + " [" + getTime() + "]";
        TextView tv = new TextView(clientActivity);
        tv.setTextColor(color);
        tv.setText(m);
        tv.setTextSize(20);
        tv.setPadding(0, 5, 0, 0);
        return tv;
    }

    // Show a message on the UI
// Show a message on the UI with a custom color
    private void showMessage(final String message, final int color) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                clientActivity.showTextView(createTextView(message, color));
            }
        });
    }

    // Remove all views from the message list
    private void removeAllViews() {
        handler.post(clientActivity::removeAllViews);
    }

    // Connect to the server
    public void connectToServer() {
        removeAllViews();

        // Start a new thread to handle the client-side communication
        clientThread = new ClientThread();
        thread = new Thread(clientThread);
        thread.start();
    }

    // Send a message to the server
    public void sendMessage(final String message) {
        if (clientThread != null) {
            // Show the sent message on the UI
            showMessage(message, clientTextColor);
            clientThread.sendMessage(message);
        }
    }


    // Get the current time in HH:mm:ss format
    private String getTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    // Thread class for handling the client-side communication
    class ClientThread implements Runnable {

        private Socket socket;
        private BufferedReader input;

        @Override
        public void run() {
            try {
                // Create a socket connection to the server
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVER_PORT);

                while (!Thread.currentThread().isInterrupted()) {
                    // Read messages from the server
                    this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message = input.readLine();
                    if (null == message || "Disconnect".contentEquals(message)) {
                        boolean interrupted = Thread.interrupted();
                        message = "Server Disconnected: " + interrupted;
                        break;
                    }
                    // Show the received message on the UI with custom color
                    showMessage("Server: " + message, Color.parseColor("#F0FFF0"));
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // Send a message to the server
        void sendMessage(final String message) {
            new Thread(() -> {
                try {
                    if (null != socket) {
                        PrintWriter out = new PrintWriter(new
                                BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream())),
                                true);
                        // Send the message to the server
                        out.println(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}