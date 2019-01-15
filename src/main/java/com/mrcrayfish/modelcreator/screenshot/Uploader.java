package com.mrcrayfish.modelcreator.screenshot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mrcrayfish.modelcreator.util.StreamUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Uploader
{
    private static final String UPLOAD_URL = "https://api.imgur.com/3/image";
    private static final String CLIENT_ID = "5cd0235db91ac6e";

    public static String upload(File file)
    {
        HttpURLConnection conn;
        InputStream responseIn;

        try
        {
            conn = (HttpURLConnection) new URL(UPLOAD_URL).openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);

            OutputStream out = conn.getOutputStream();
            upload(new FileInputStream(file), out);
            out.flush();
            out.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                responseIn = conn.getInputStream();
                return getImageLink(responseIn);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static void upload(InputStream input, OutputStream output) throws IOException
    {
        byte[] buffer = new byte[8192];
        int n;
        while((n = input.read(buffer)) != -1)
        {
            output.write(buffer, 0, n);
        }

    }

    private static String getImageLink(InputStream input) throws IOException
    {
        String json = StreamUtils.convertToString(input);

        JsonParser parser = new JsonParser();
        JsonElement read = parser.parse(json);

        if(read.isJsonObject())
        {
            JsonObject obj = read.getAsJsonObject();

            if(obj.has("data") && obj.get("data").isJsonObject())
            {
                JsonObject data = obj.getAsJsonObject("data");
                return "http://imgur.com/" + data.get("id").getAsString();
            }
        }
        return null;
    }
}
