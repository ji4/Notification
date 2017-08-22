package com.example.chiayingwu.notification;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by chiaying.wu on 2017/8/21.
 */

public class NotifyService extends Service {
    private Context m_context;
    private ArrayList<Integer> m_iArrExistentEvent = new ArrayList<>(); //stores Event Code

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            int iEventCode = msg.arg2;
            ArrayList<Integer> iArrStoredEventData = getStoredData(iEventCode);
            long endTime = 0;
            if (iArrStoredEventData != null) {
                endTime = setEndTime(iArrStoredEventData);
                Log.d("jia", "endTime:" + endTime);
            }

            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        Log.d("jia", Thread.currentThread().getName() + " handler running");
                        wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                    }
                }
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        m_context = this;
        NotifyUtil.init(m_context);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String strEventAction = intent.getStringExtra(Constants.EVENT_ACTION);
        ArrayList<Integer> iArrEventCodeAndAction = DataConverter.convertEventDataToInt(strEventAction);
        int iEventCode = iArrEventCodeAndAction.get(0);
        int iEventAction = iArrEventCodeAndAction.get(1);
        if (iEventAction == Constants.EVENT_ADD) {
            Boolean eventExist = checkEventExist(iEventCode);
            if (!eventExist) {
                m_iArrExistentEvent.add(iEventCode);
            }
        } else {
        }//EVENT_CANCEL
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        Looper mServiceLooper = thread.getLooper();
        ServiceHandler mServiceHandler = new ServiceHandler(mServiceLooper);

        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.arg2 = iEventCode;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    private Boolean checkEventExist(int iEventCode) {
        for (int i = 0; i < m_iArrExistentEvent.size(); i++) {
            if (iEventCode == m_iArrExistentEvent.get(i)) { //if the event has existed
                return true;
            }
        }
        return false;
    }

    private ArrayList<Integer> getStoredData(int iEventCode) {
        String strEventData = KeyValueDB.getEventData(m_context, String.valueOf(iEventCode));
        ArrayList<Integer> iArrltEventData = null;
        if (iEventCode != -1 && !strEventData.equals(KeyValueDB.NO_DATA)) {
            iArrltEventData = DataConverter.convertEventDataToInt(strEventData);
        }
        return iArrltEventData;
    }

    private long setEndTime(ArrayList<Integer> iArrStoredEventData) {
        Calendar calendar = Calendar.getInstance();
        int iHour = iArrStoredEventData.get(0);
        int iMin = iArrStoredEventData.get(1);
        int iAm_pm = iArrStoredEventData.get(2);

        calendar.set(Calendar.HOUR, iHour);
        calendar.set(Calendar.MINUTE, iMin);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM, iAm_pm == 0 ? Calendar.AM : Calendar.PM);

        return calendar.getTimeInMillis();
    }
}