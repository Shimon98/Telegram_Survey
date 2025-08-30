package org.example.ai;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;


public class AiClient {
    private static final String AI_URL = "https://app.seker.live/fm1/send-message";
    private static final String TEXT_ID = "?id=";
    private static final String MESSAGE_TEXT = "&text=";
    private static final String KEY_EXTRA = "extra";

    private OkHttpClient client;
    private String id;

    public AiClient(String id) {
        this.client = new OkHttpClient();
        this.id = id;
    }

    public String messageGpt(String message) {
        try {
            String url = AI_URL + TEXT_ID + id +
                    MESSAGE_TEXT + message;
            Request request = new Request.Builder().url(url).build();
            Response response = this.client.newCall(request).execute();

            JSONObject jsonObject = new JSONObject(response.body().string());
            return jsonObject.getString(KEY_EXTRA);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
