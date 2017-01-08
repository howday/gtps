package com.project.gtps.util;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

/**
 * Created by suresh on 12/30/16.
 */
public class Notification extends Sender {
    private static String API_KEY = "AAAAzbZT2ko:APA91bELlt4yh42BUvOxoc58-6kNX25Cs5LGkZEn_o_W8iz227jBtJaZzuJmOVY5X6odw-vFKQu9Wv3KDpq7-PRXpgHyypcGJe7fak8GLdwuFUFKB4HW-MGmNHyV5pSI1S770Eay69XYBVG0J5qmSavBjwU1RGqcLQ";

    private Notification() {
        super(API_KEY);
    }

//    @Override
//    protected HttpURLConnection getConnection(String url) throws IOException {
//        System.out.println("Getting connection--------");
//        String fcmUrl = "https://fcm.googleapis.com/fcm/send";
//        return (HttpURLConnection) new URL(fcmUrl).openConnection();
//    }

    public static  void send(String msg, String deviceId) {
        Thread t = new Thread() {
            public void run() {
                try {
                    Sender sender = new Notification();
                    Message message = new Message.Builder()
                            .collapseKey("message")
                            .timeToLive(3)
                            .delayWhileIdle(true)
                            .addData("message", msg)
                            .build();

                    // Use the same token(or registration id) that was earlier
                    // used to send the message to the client directly from
                    // Firebase Console's Notification tab.
                    Result result = sender.send(message, deviceId, 1);

                    System.out.println("Result: " + result.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        t.start();
        try {
            t.join();
        } catch (InterruptedException iex) {
            iex.printStackTrace();
        }
    }

}