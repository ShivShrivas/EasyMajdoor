package com.app.easymajdoor.SendNotificationPackage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAqgRijZs:APA91bE86y1UYCogRjvD63BvsKilh2o0r0EKDSphjxjBDgopjjbmFdn59VCHq8IqLXKdKpX5coYNCnCWNxO_TIuEVQrw7aAO-rIyCsmE3Nuh0XXYflBZMicAuIFg7op8UHwGNej2vxiQ" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}

