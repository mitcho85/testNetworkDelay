package com.example.testNetworkLossSampleApp;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import service.FileDownloadClient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id.testButton);

        String url = "https://datahub.io/datahq/1mb-test/r/0.html/";

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(url);
        Retrofit retroFit = builder.build();

        FileDownloadClient fileDownloadClient = retroFit.create(FileDownloadClient.class);
        final retrofit2.Call<ResponseBody> call = fileDownloadClient.download1MBFile();

        final ExecutorService executorService = Executors.newFixedThreadPool(1);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        Log.v("Mitch", "Sleeping thread before network call: " + Thread.currentThread().toString());
                        Log.v("Mitch", "Sleeping thread for 5 sec: " + Thread.currentThread().toString());
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.v("Mitch", "Thread is awake: " + Thread.currentThread().toString());
                        Log.v("Mitch", "Starting network call on thread" + Thread.currentThread().toString());
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Log.v("Mitch", "Response with body: " + response.body().toString() + " on thread" + Thread.currentThread().toString());
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.v("Mitch", "response failed" + " on thread" + Thread.currentThread().toString());
                            }
                        });

                        try {
                            executeLameWork();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void executeLameWork() throws InterruptedException {
        for(int i = 0; i < 10; ++i) {
            Log.v("Mitch","Executing lame work #" + i + " on thread: " + Thread.currentThread().toString());
            Thread.sleep(1000);
        }

        Log.v("Mitch", "Done with lame work on thread " + Thread.currentThread().toString());
    }
}
