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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public class MessengerService extends Service {
    private Messenger mActivityMessenger;

    private Handler handler;

    private Messenger mServiceMessenger;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("TAG", "onCreate()");
       //myAppDatabase=Room.databaseBuilder(getApplicationContext(),(, "bookdb").allowMainThreadQueries().build();

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
                    MyDao bookDao = MainActivity.getmyAppDatabase().myDao();
                    Book bookinfo=new Book();
                    bookinfo.setId(1);
                    bookinfo.setBook_name("123");
                    bookinfo.setBook_conext("123");
                    bookDao.addbook(bookinfo);

//                    Executor myExecutor = Executors.newSingleThreadExecutor();
//                    myExecutor.execute(() -> {
//                        userList = bookDao.getAll();
//                        isQueryUserFinish =true;
//                    });

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
                    message.arg1 = msg.arg1 + msg.arg2;//小資料的相加 可以用obj傳大資料
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
