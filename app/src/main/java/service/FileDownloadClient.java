package service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/*
 *  Copyright © Microsoft Corporation. All rights reserved.
 */public interface FileDownloadClient {

     @GET("https://datahub.io/datahq/1mb-test/r/0.html")
    Call<ResponseBody> download1MBFile();
}
