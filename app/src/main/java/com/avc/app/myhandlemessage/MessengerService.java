package com.avc.app.myhandlemessage;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MessengerService extends Service {
    private Messenger mActivityMessenger;

    private Handler handler;

    private Messenger mServiceMessenger;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("TAG", "onCreate()");

        /**
         * HandlerThread是Android系统专门为Handler封装的一个线程类，
         通过HandlerThread创建的Hanlder便可以进行耗时操作了
         * HandlerThread是一个子线程,在调用handlerThread.getLooper()之前必须先执行
         * HandlerThread的start方法。
         */
        HandlerThread handlerThread = new HandlerThread("serviceCalculate");
        handlerThread.start();


        handler = new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0x11){
                    Log.i("MessengerService","handleMessage");

                    if(mActivityMessenger == null) {
                        mActivityMessenger = msg.replyTo;//訊息 reply
                    }

                    //模拟耗时任务
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    //发送结果回Activity
                    Message message = this.obtainMessage();
                    message.what = 0x12;
                    message.arg1 = msg.arg1 + msg.arg2;
                    try {
                        mActivityMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        mServiceMessenger = new Messenger(handler);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("TAG","onBind()");
        return mServiceMessenger.getBinder();
    }
}
