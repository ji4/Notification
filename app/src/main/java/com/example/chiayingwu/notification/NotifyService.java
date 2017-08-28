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
import java.util.ListIterator;

public class NotifyService extends Service {
    private Context m_context;
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private ArrayList<Integer> m_iArrScheduledEvent = new ArrayList<>(); //stores Event Id
    private long m_startTimeMillis;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d("jia", "handleMessage() called");
            Log.d("jia", "m_iArrScheduledEvent.size(): " + m_iArrScheduledEvent.size());

            while (m_iArrScheduledEvent.size() > 0) {
                synchronized (this) {
                    checkScheduledEventsMatch();
                    try {
                        wait(1000); //1s
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

        m_startTimeMillis = System.currentTimeMillis();

        if (intent != null) {
            addEventIfReminderActionSet(intent);
        }

        //get all event id from shared pref
        String strEventIdList = KeyValueDB.getEventIdList(m_context);
        m_iArrScheduledEvent = DataConverter.convertToIntArray(strEventIdList);
        Log.d("jia", "m_iArrScheduledEvent.size() in onStartCommand: " + m_iArrScheduledEvent.size());

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

    private void checkScheduledEventsMatch() {
        //check if current time matches scheduled time
        for (ListIterator<Integer> iterator = m_iArrScheduledEvent.listIterator(); iterator.hasNext(); ) {
            int i = iterator.nextIndex();
            iterator.next();

            int iEventId = m_iArrScheduledEvent.get(i);
            int iNotifyId = iEventId;
            ArrayList<Integer> iArrStoredEventData = getStoredData(iEventId);
            long scheduledTime = setScheduledTime(iArrStoredEventData);
            int iNotifyType = iArrStoredEventData.get(4);

            if (iNotifyType == NotifyUtil.BUILD_PROCESS) {
                NotifyUtil.build(iNotifyType, iNotifyId, R.drawable.ic_launcher, "I'm title", "I'm content")
                        .buildTimeProcess(iNotifyId, R.drawable.ic_launcher, "Downloading", m_startTimeMillis, scheduledTime)
                        .show();
                if (scheduledTime - System.currentTimeMillis() <= 0) {
                    KeyValueDB.deleteEvent(m_context, iEventId);
                    iterator.remove();
                }
            } else if (System.currentTimeMillis() - scheduledTime >= 0 && System.currentTimeMillis() - scheduledTime <= 1000) {//1000=1s
                Log.d("jia", "send a notification, scheduledTime: " + scheduledTime + ", currentTime: " + System.currentTimeMillis());
                NotifyUtil.build(iNotifyType, iNotifyId, R.drawable.ic_launcher, "I'm title", "I'm content")
                        .addBtn(R.mipmap.ic_launcher, "left", NotifyUtil.buildService(NotifyService.class, iEventId))
                        .addBtn(R.mipmap.ic_launcher, "right", NotifyUtil.buildIntent(MainActivity.class))
                        .playSound()
                        .addMsg("1. someone published an article.")
                        .addMsg("2. It's sunny today.")
                        .setPicture(R.drawable.scenery)
                        .show();

                KeyValueDB.deleteEvent(m_context, iEventId);
                iterator.remove();
            } else if (scheduledTime - System.currentTimeMillis() <= 0) { //set at past time or expired
                KeyValueDB.deleteEvent(m_context, iEventId);
                iterator.remove();
            }
        }
    }

    private ArrayList<Integer> getStoredData(int iEventId) {
        String strEventData = KeyValueDB.getEventData(m_context, String.valueOf(iEventId));
        ArrayList<Integer> iArrltEventData = null;
        if (iEventId != -1 && !strEventData.equals(KeyValueDB.NO_DATA)) {
            iArrltEventData = DataConverter.convertToIntArray(strEventData);
        }
        return iArrltEventData;
    }

    private long setScheduledTime(ArrayList<Integer> iArrStoredEventData) {
        Calendar calendar = Calendar.getInstance();
        int iHour = iArrStoredEventData.get(0);
        int iMin = iArrStoredEventData.get(1);
        int iSec = iArrStoredEventData.get(2);
        int iAm_pm = iArrStoredEventData.get(3);

        calendar.set(Calendar.HOUR, iHour);
        calendar.set(Calendar.MINUTE, iMin);
        calendar.set(Calendar.SECOND, iSec);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.AM_PM, iAm_pm == 0 ? Calendar.AM : Calendar.PM);

        return calendar.getTimeInMillis();
    }


    private void addEventIfReminderActionSet(Intent intent) {
        int iEventId = intent.getIntExtra(Constants.KEY_REMIND_LATER, -1);
        if (iEventId != -1) {
            NotifyUtil.cancel(iEventId);

            Calendar currentCalendar = Calendar.getInstance();
            Calendar scheduledCalendar = (Calendar) currentCalendar.clone();
            int iSec = 5;
            scheduledCalendar.add(Calendar.SECOND, iSec);

            String strEventId = String.valueOf(iEventId);
            String strHour = String.valueOf(scheduledCalendar.get(Calendar.HOUR));
            String strMin = String.valueOf(scheduledCalendar.get(Calendar.MINUTE));
            String strSec = String.valueOf(scheduledCalendar.get(Calendar.SECOND));
            String strAm_pm = String.valueOf(scheduledCalendar.get(Calendar.AM_PM));
            String strPlaySound = "0";
            String strCountDownChecked = "0";
            KeyValueDB.setEventData(m_context, strEventId, strHour + "," + strMin + "," + strSec + "," + strAm_pm + "," + NotifyUtil.BUILD_ACTION + "," + strPlaySound + "," + strCountDownChecked);
            KeyValueDB.saveEventId(m_context, iEventId);
        }
    }
}