package com.github.tom_the_geek.nac;

import com.github.tom_the_geek.nac.ex.RequestException;
import com.github.tom_the_geek.nac.response.NucleoidServerStatus;
import com.google.gson.Gson;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NucleoidApiClient implements AutoCloseable {
    private final ExecutorService executor;
    private final CloseableHttpClient client;
    private final String apiBase;
    private final Gson gson;

    private NucleoidApiClient(ExecutorService executor, CloseableHttpClient client, String apiBase, Gson gson) {
        this.executor = executor;
        this.client = client;
        this.gson = gson;
        if (!apiBase.endsWith("/")) {
            this.apiBase = apiBase + "/";
        } else {
            this.apiBase = apiBase;
        }
    }

    public CompletableFuture<NucleoidServerStatus> getServerStatus(String server) {
        HttpGet request = get("status/" + server);
        return this.makeRequest(NucleoidServerStatus.class, request);
    }

    private <T> CompletableFuture<T> makeRequest(Class<T> cls, ClassicHttpRequest request) {
        CompletableFuture<T> future = new CompletableFuture<>();

        this.executor.execute(() -> {
            try (CloseableHttpResponse res = this.client.execute(request)) {

                int code = res.getCode();
                if (code < 200 || code >= 300) {
                    if (code == 404) {
                        future.completeExceptionally(new FileNotFoundException());
                    } else {
                        future.completeExceptionally(new RequestException(code));
                    }
                }

                String data = EntityUtils.toString(res.getEntity(), Charset.defaultCharset());
                future.complete(NucleoidApiClient.this.gson.fromJson(data, cls));
            } catch (Throwable e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    private HttpGet get(String url) {
        return request(new HttpGet(this.formatUrl(url)));
    }

    private <T extends ClassicHttpRequest> T request(T req) {
        req.setHeader("User-Agent", "NucleoidApiClient v1 (https://github.com/Tom-The-Geek/nucleoid-api-client)");
        return req;
    }

    private String formatUrl(String path) {
        return this.apiBase + path;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void close() throws IOException {
        this.client.close();
        this.executor.shutdown();
    }

    public static class Builder {
        private ExecutorService executor = Executors.newSingleThreadExecutor();
        private CloseableHttpClient client = HttpClients.createDefault();
        private String apiBase = "https://api.nucleoid.xyz";
        private Gson gson = new Gson();

        public Executor getExecutor() {
            return this.executor;
        }

        public Builder executor(ExecutorService executor) {
            this.executor = executor;
            return this;
        }

        public CloseableHttpClient getClient() {
            return client;
        }

        public Builder client(CloseableHttpClient client) {
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
            return new NucleoidApiClient(this.executor, this.client, this.apiBase, gson);
        }
    }
}
