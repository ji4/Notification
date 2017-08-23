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

public class NotifyService extends Service {
    private Context m_context;
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private ArrayList<Integer> m_iArrScheduledEvent = new ArrayList<>(); //stores Event Code

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d("jia", "handleMessage() called");
            Log.d("jia", "m_iArrScheduledEvent.size(): "+m_iArrScheduledEvent.size());

            while (m_iArrScheduledEvent.size() > 0) {
                synchronized (this) {
                    for (int i = 0; i < m_iArrScheduledEvent.size(); i++) {
                        int iEventCode = m_iArrScheduledEvent.get(i);
                        ArrayList<Integer> iArrStoredEventData = getStoredData(iEventCode);
                        long scheduledTime = setScheduledTime(iArrStoredEventData);
                        if (System.currentTimeMillis() >= scheduledTime) {
                            Log.d("jia", "send a notification, scheduledTime: " + scheduledTime + ", currentTime: " + System.currentTimeMillis());
                            NotifyUtil.buildSimple(1, R.drawable.ic_launcher, "title", "content", null).show();
                        }
                    }
                    try {
                        wait(1000);
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
        Log.d("jia", "onCreate() called");
        m_context = this;
        NotifyUtil.init(m_context);
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("jia", "onStartCommand() called");
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        if (intent != null) {
            getIntentData(intent);
        }

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
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

    private ArrayList<Integer> getStoredData(int iEventCode) {
        String strEventData = KeyValueDB.getEventData(m_context, String.valueOf(iEventCode));
        ArrayList<Integer> iArrltEventData = null;
        if (iEventCode != -1 && !strEventData.equals(KeyValueDB.NO_DATA)) {
            iArrltEventData = DataConverter.convertEventDataToInt(strEventData);
        }
        return iArrltEventData;
    }

    private long setScheduledTime(ArrayList<Integer> iArrStoredEventData) {
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

    private void getIntentData(Intent intent) {
        String strEventAction = intent.getStringExtra(Constants.EVENT_ACTION);
        ArrayList<Integer> iArrEventCodeAndAction = DataConverter.convertEventDataToInt(strEventAction);
        int iEventCode = iArrEventCodeAndAction.get(0);
        int iEventAction = iArrEventCodeAndAction.get(1);
        if (iEventAction == Constants.EVENT_ADD) {
            Boolean eventExist = checkEventExist(iEventCode);
            if (!eventExist) {
                m_iArrScheduledEvent.add(iEventCode);
            }
        }
    }

    private Boolean checkEventExist(int iEventCode) {
        for (int i = 0; i < m_iArrScheduledEvent.size(); i++) {
            if (iEventCode == m_iArrScheduledEvent.get(i)) { //if the event has existed
                return true;
            }
        }
        return false;
    }
}