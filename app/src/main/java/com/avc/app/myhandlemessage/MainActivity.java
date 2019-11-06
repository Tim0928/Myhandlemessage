package com.avc.app.myhandlemessage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button sendToServiceBtn;

    //Service端的Messenger对象
    private Messenger mServiceMessenger;

    //Activity端的Messenger对象
    private Messenger mActivityMessenger;

    /**
     * Activity端的Handler处理Service中的消息
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0x12){
                Toast.makeText(MainActivity.this, "Service:"
                        + msg.arg1,Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * Service绑定状态的监听
     */
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //获取Service端的Messenger
            mServiceMessenger = new Messenger(service);
        }


        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendToServiceBtn = (Button) findViewById(R.id.btn_sendToService);
        sendToServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Activity端的Messenger
                if(mActivityMessenger == null) {
                    mActivityMessenger = new Messenger(handler);
                }


                //创建消息
                Message message = Message.obtain();
                message.what = 0x11;
                message.arg1 = 2016;
                message.arg2 = 1;

                //设定消息要回应的Messenger
                message.replyTo = mActivityMessenger;

                try {
                    //通过ServiceMessenger将消息发送到Service中的Handler
                    mServiceMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        //绑定Service
        Intent intent = new Intent(MainActivity.this, MessengerService.class);
        bindService(intent, connection, Service.BIND_AUTO_CREATE);

    }
}
