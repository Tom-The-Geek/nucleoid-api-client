package com.github.tom_the_geek.nac;

import com.github.tom_the_geek.nac.ex.RequestException;
import com.github.tom_the_geek.nac.response.NucleoidServerStatus;
import com.google.gson.Gson;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class NucleoidApiClient implements AutoCloseable {
    private final OkHttpClient client;
    private final String apiBase;
    private final Gson gson;

    private NucleoidApiClient(OkHttpClient client, String apiBase, Gson gson) {
        this.client = client;
        this.gson = gson;
        if (!apiBase.endsWith("/")) {
            this.apiBase = apiBase + "/";
        } else {
            this.apiBase = apiBase;
        }
    }

    public CompletableFuture<NucleoidServerStatus> getServerStatus(String server) {
        Request request = get("status/" + server)
                .build();
        return this.makeRequest(NucleoidServerStatus.class, request);
    }

    private <T> CompletableFuture<T> makeRequest(Class<T> cls, Request request) {
        CompletableFuture<T> future = new CompletableFuture<>();

        this.client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        future.completeExceptionally(e);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        int code = response.code();
                        if (code < 200 || code >= 300) {
                            if (code == 404) {
                                future.completeExceptionally(new FileNotFoundException());
                            } else {
                                future.completeExceptionally(new RequestException(code));
                            }
                        }
                        String data = Objects.requireNonNull(response.body(), "response.body()").string();
                        future.complete(NucleoidApiClient.this.gson.fromJson(data, cls));
                    }
                });

        return future;
    }

    private Request.Builder get(String url) {
        return this.request()
                .get().url(this.formatUrl(url));
    }

    private Request.Builder request() {
        return new Request.Builder()
                .header("User-Agent", "NucleoidApiClient v1 (https://github.com/Tom-The-Geek/nucleoid-api-client)");
    }

    private String formatUrl(String path) {
        return this.apiBase + path;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void close() {
        this.client.connectionPool().evictAll();
        this.client.dispatcher().executorService().shutdown();
    }

    public static class Builder {
        private OkHttpClient client = new OkHttpClient();
        private String apiBase = "https://api.nucleoid.xyz";
        private Gson gson = new Gson();

        public OkHttpClient getClient() {
            return client;
        }

        public Builder client(OkHttpClient client) {
            this.client = client;
            return this;
        }

        public String getApiBase() {
            return apiBase;
        }

        public Builder apiBase(String apiBase) {
            this.apiBase = apiBase;
            return this;
        }

        public Gson getGson() {
            return gson;
        }

        public Builder gson(Gson gson) {
            this.gson = gson;
            return this;
        }

        public NucleoidApiClient build() {
            return new NucleoidApiClient(this.client, this.apiBase, gson);
        }
    }
}
