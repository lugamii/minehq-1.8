// 
// Decompiled by Procyon v0.5.36
// 

package com.moonsworth.fallback.util;

import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HastebinAPI
{
    public String post(final String text, final boolean raw) throws IOException {
        final byte[] postData = text.getBytes(StandardCharsets.UTF_8);
        final int postDataLength = postData.length;
        final String requestURL = "https://hastebin.com/documents";
        final URL url = new URL(requestURL);
        final HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("User-Agent", "Riot AntiCheat");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        conn.setUseCaches(false);
        String response = null;
        try {
            final DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = reader.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (response.contains("\"key\"")) {
            response = response.substring(response.indexOf(":") + 2, response.length() - 2);
            final String postURL = raw ? "https://hastebin.com/raw/" : "https://hastebin.com/";
            response = postURL + response;
        }
        return response;
    }
}
