package com.example.myapplicationserver.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerModel {
    public static final int SERVER_PORT = 1234;
    private ServerSocket serverSocket;
    private Socket tempClientSocket;
    private Thread serverThread;
    private boolean isServerStarted = false; // New flag

    public interface MessageListener {
        void onMessageReceived(String message);
    }

    private MessageListener messageListener;

    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

    public void startServer() {
        if (isServerStarted || (serverThread != null && serverThread.isAlive())) {
            return;
        }
        isServerStarted = true; // Update the flag
        serverThread = new Thread(new ServerThread());
        serverThread.start();
    }

    public void sendMessage(final String message) {
        try {
            if (tempClientSocket != null) {
                new Thread(() -> {
                    PrintWriter out = null;
                    try {
                        out = new PrintWriter(new BufferedWriter(
                                new OutputStreamWriter(tempClientSocket.getOutputStream())),
                                true);
                        out.println(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ServerThread implements Runnable {
        public void run() {
            Socket socket;
            try {
                serverSocket = new ServerSocket(SERVER_PORT);
            } catch (IOException e) {
                e.printStackTrace();
                if (messageListener != null) {
                    messageListener.onMessageReceived("Error Starting Server : " + e.getMessage());
                }
                return;
            }
            if (serverSocket != null) {
                if (messageListener != null) {
                    //messageListener.onMessageReceived("Server Started.");
                }
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        socket = serverSocket.accept();
                        CommunicationThread commThread = new CommunicationThread(socket);
                        new Thread(commThread).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (messageListener != null) {
                            messageListener.onMessageReceived("Error Communicating to Client: " + e.getMessage());
                        }
                    }
                }
            }
        }
    }

    private class CommunicationThread implements Runnable {
        private final Socket clientSocket;
        private BufferedReader input;

        public CommunicationThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
            tempClientSocket = clientSocket;
            try {
                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
                if (messageListener != null) {
                    messageListener.onMessageReceived("Error Connecting to Client!!");
                }
                return;
            }
            if (messageListener != null) {
                // Change the color of the message to green
                messageListener.onMessageReceived("[GREEN]Connected to Client!!");
            }
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String read = input.readLine();
                    if (read == null || "Disconnect".contentEquals(read)) {
                        boolean interrupted = Thread.interrupted();
                        read = "Client Disconnected: " + interrupted;
                        if (messageListener != null) {
                            messageListener.onMessageReceived("Client : " +
                                    read);
                        }
                        break;
                    }
                    if (messageListener != null) {
                        messageListener.onMessageReceived("Client : " + read);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Get the current time in the desired format
    public String getTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }
}
