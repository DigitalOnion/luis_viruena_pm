package com.outerspace.movies.model;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

class NetworkUtils {

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     *
     * CREDIT: I started with the Udacity Android Nano-Degree's getResponseFromHttpUrl
     * though, the MovieDB uses HTTPS and redirects the request. So, I had to explore
     * and found an article at: Java HttpURLConnection follow redrect example by Mkyong at:
     *      https://mkyong.com/java/java-httpurlconnection-follow-redirect-example/
     *
     * The solution works for the MovieDB, though it might fail for other connections or
     * if they happen to redirect further in the future.
     */

    static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(2000);
        connection.connect();

        int responseCode = connection.getResponseCode();
        if(responseCode != HttpURLConnection.HTTP_OK) {
            throw new ConnectException(String.format("Could not connect to %s", url.toString()));
        }
        BufferedReader inBuffer;
        inBuffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        do {
            line = inBuffer.readLine();
            sb.append(line);
        } while(line != null);
        String response = sb.toString(); // the construct: inBuffer.lines().collect(Collectors.joining()); is not valid in api < 24
        connection.disconnect();
        return response;
    }
}
