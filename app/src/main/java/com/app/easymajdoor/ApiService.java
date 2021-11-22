package com.app.easymajdoor;

import com.app.easymajdoor.SendNotificationPackage.NotificationSender;
import com.app.easymajdoor.model.NotificationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

 interface ApiService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAqgRijZs:APA91bE86y1UYCogRjvD63BvsKilh2o0r0EKDSphjxjBDgopjjbmFdn59VCHq8IqLXKdKpX5coYNCnCWNxO_TIuEVQrw7aAO-rIyCsmE3Nuh0XXYflBZMicAuIFg7op8UHwGNej2vxiQ" // Your server key refer to video for finding your server key
            }
    )


    @POST("fcm/send")
    Call<NotificationResponse> sendNotification(@Body NotificationSender body);
}
