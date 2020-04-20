package org.battleshipgame.reporter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.cuba.log.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Reporter {
    private static final String TAG = Reporter.class.getSimpleName();

    private Log log;
    private Gson gson;
    private Retrofit retrofit;
    private ReportApi api;
    private AppInfo appInfo;
    private String signature;

    public Reporter(Log log, String signature) {
        this.log = log;
        this.signature = signature;
        this.gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .create();
    }

    private void prepare() {
        URL infoUrl;
        try {
            infoUrl = new URL("https://kirillbogatikov.github.io/apps/bug-reporter.json");
        } catch (Exception e) {
            log.e(TAG, "Failed to create Github Pages info URL", e);
            throw new IllegalStateException("Info server is unavailable");
        }

        try (InputStream stream = infoUrl.openStream();
             InputStreamReader reader = new InputStreamReader(stream)) {
            appInfo = gson.fromJson(reader, AppInfo.class);
        } catch (Throwable t) {
            log.e(TAG, "Github Pages info server is unavailable", t);
            throw new IllegalStateException("Info server is unavailable");
        }

        if (!ready()) {
            log.e(TAG, "App info corrupted");
            throw new IllegalStateException("App info corrupted");
        }

        log.d(TAG, "Reporter Server URL: " + appInfo.getUrl());
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(appInfo.getUrl())
                .build();
        api = retrofit.create(ReportApi.class);
    }

    public boolean ready() {
        if (appInfo == null) return false;
        if (appInfo.getUrl() == null) return false;
        if (appInfo.getUrl().isEmpty()) return false;

        return true;
    }

    public void report(Report report) {
        Thread thread = new Thread(() -> {
            if (!ready()) {
                prepare();
            }

            Call<String> call = api.report(report, signature);
            try {
                Response<String> response = call.execute();
                if (response.isSuccessful()) {
                    log.d(TAG, "Report send successful. Response message: " + response.body());
                } else {
                    log.e(TAG, "Report send failed. Response code: " + response.code() + ", message: " + response.body());
                }
            } catch (Throwable t) {
                log.e(TAG, "Report send failed", t);
            }
        });
        thread.start();
    }
}
