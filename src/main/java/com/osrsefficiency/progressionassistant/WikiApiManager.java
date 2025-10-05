package com.osrsefficiency.progressionassistant;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.inject.Inject;
import java.io.IOException;

public class WikiApiManager
{
    private final OkHttpClient httpClient;
    private final Gson gson;

    private static final String WIKI_API_URL = "https://runescape.wiki/api.php?action=parse&format=json&prop=text&page=";

    @Inject
    public WikiApiManager(OkHttpClient httpClient, Gson gson)
    {
        this.httpClient = httpClient;
        this.gson = gson;
    }

    public String getCooksAssistantQuest() throws IOException
    {
        Request request = new Request.Builder()
                .url(WIKI_API_URL + "Cook's_Assistant")
                .build();

        try (Response response = httpClient.newCall(request).execute())
        {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
            return jsonObject.getAsJsonObject("parse").get("text").getAsJsonObject().get("*").getAsString();
        }
    }
}
