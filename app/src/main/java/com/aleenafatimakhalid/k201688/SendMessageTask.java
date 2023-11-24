package com.aleenafatimakhalid.k201688;

import android.os.AsyncTask;
import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SendMessageTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        try {
            // Create a URL for the PHP script on your server
            URL url = new URL("http://192.168.18.27/k201688_i190563/send_message.php");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Create the POST data
            String postData = "messageText=" + params[0] +
                    "&senderId=" + params[1] +
                    "&recipientId=" + params[2] +
                    "&timestamp=" + params[3] +
                    "&messageType=" + params[4];

            // Write the POST data to the connection
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = postData.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the response from the server
            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                return scanner.useDelimiter("\\A").next();
            }
        } catch (Exception e) {
            Log.e("SendMessageTask", "Error sending message: " + e.getMessage());
            return "Error";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // Handle the result, e.g., show a toast message
        if ("Message sent successfully".equals(result)) {
            // Handle success
        } else {
            // Handle error
        }
    }
}
