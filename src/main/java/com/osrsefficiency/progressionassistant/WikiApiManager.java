package com.osrsefficiency.progressionassistant;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.inject.Inject;
import java.io.IOException;
import java.util.function.Consumer;

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

    public void getCooksAssistantQuest(Consumer<String> htmlCallback, Consumer<IOException> errorCallback)
    {
        Request request = new Request.Builder()
                .url(WIKI_API_URL + "Cook's_Assistant")
                .build();

        httpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                errorCallback.accept(e);
            }

            @Override
            public void onResponse(Call call, Response response)
            {
                if (!response.isSuccessful()) {
                    errorCallback.accept(new IOException("Unexpected code " + response));
                    response.close();
                    return;
                }

                try
                {
                    if (response.body() == null) {
                        errorCallback.accept(new IOException("Response body is null"));
                        return;
                    }
                    JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
                    String html = jsonObject.getAsJsonObject("parse").get("text").getAsJsonObject().get("*").getAsString();
                    htmlCallback.accept(html);
                }
                catch (Exception e)
                {
                    errorCallback.accept(new IOException("Failed to parse wiki response", e));
                }
                finally
                {
                    response.close();
                }
            }
        });
    }
}