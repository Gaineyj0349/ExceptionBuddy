package com.gainwise.exceptionbuddy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.DateFormat;

/**
 * This class must be extended in the application. An instance of this class should be passed to the ExcpetionBuddy builder.
 * This is a must have class as it holds the Uncaught Exception Handling code.
 *
 */
public abstract class ExceptionBuddyDirective implements Thread.UncaughtExceptionHandler {

    public Context context;
    private Thread.UncaughtExceptionHandler defaultExceptionHandler;
    private Class postExceptionActivityIn;
    boolean immediatelyInvoke = true;

    /**
     * this method executes immediately following an uncaught exception, then it will either trigger the new activity, or rethrow the exception to the system, depending on the ExceptionBuddy settings provided to the builder.
     * @throws ExceptionBuddy.CrashBuddyException if the provided code block doesn't execute succefully an exception will be thrown.
     *
     */
    public abstract void executeOnException() throws ExceptionBuddy.CrashBuddyException;


    /**
     * Default constructor
     *
     */
    public  ExceptionBuddyDirective() {


        ExceptionBuddyUtils.LOGI("Super Constructor called from Directive");

        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        // setup handler for uncaught exception
        Thread.setDefaultUncaughtExceptionHandler(this);
        //register our receiver



    }

    /**
     * setter for the context to be called by the ExceptionBuddy builder.
     * @param context
     */
    protected void setContext(Context context) {
        this.context = context;
    }
    /**
     * setter for the ExceptionBuddy.Builder setting to immediately invoke custom activity (extending PostExceptionActivity) post uncaught exception.
     * If this is false it will allow the executeOnException method to still trigger and then the default Uncaught Exception Handler to handle the exception as per usual.
     * @param immediatelyInvoke
     */
    protected void setImmediatelyInvoke(boolean immediatelyInvoke) {
        this.immediatelyInvoke = immediatelyInvoke;
    }

    /**
     * This method helps with behind the scenes ExceptionBuddy creation.
     */
    protected void create(){

        deviceInfoSet();

    }

    /**
     * This method is used by the builder to set the custom PostExceptionActivity.
     * @param postExceptionActivityIn must extend PostExceptionActivity
     * @param <T> must extend PostExceptionActivity
     */

    protected <T extends PostExceptionActivity>  void  setPostExceptionActivityIn(Class <T> postExceptionActivityIn) {
        this.postExceptionActivityIn = postExceptionActivityIn;
    }

    /**
     * This method is used to only set the device information once and save it in shared preferences.
     */
    private void deviceInfoSet() {
        if (!ExceptionBuddyUtils.getDeviceInfoSet(context)) {
            String deviceInfo =  ExceptionBuddyUtils.getDeviceInformation();
            ExceptionBuddyUtils.setDeviceInfo(context, deviceInfo);
            ExceptionBuddyUtils.setDeviceInfoSet(context, true);
        }
    }

    /**
     * This method will trigger if the ExcpetionBuddy object has the property immediatelyInvoke set to true. It triggers the custom PostExceptionActivity provided by the builder.
     */
    private void emitBuddyBroadCastNow(){
        ExceptionBuddyUtils.LOGI("...starting new activity");
        Intent intent = new Intent(context, postExceptionActivityIn);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        }

    /**
     * to be implemented at a later date...
     * @param timeinmillis
     */
    private void emitBuddyBroadCastThen(long timeinmillis){
         Intent intent = new Intent(context, postExceptionActivityIn);
         int intentCode = 123456;
         PendingIntent mPendingIntent = PendingIntent.getActivity(context, intentCode, intent,PendingIntent.FLAG_CANCEL_CURRENT);
         AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
         mgr.set(AlarmManager.RTC, System.currentTimeMillis() + timeinmillis, mPendingIntent);
     }

    @Override
    public void uncaughtException(final Thread thread, final Throwable throwable) {
        String timeOfException = DateFormat.getDateTimeInstance().format(System.currentTimeMillis());
        ExceptionBuddyUtils.LOGI("Exception caught");
        ExceptionBuddyUtils.LOGI("Current thread name/ID: " + Thread.currentThread().getName() + "/" + Thread.currentThread().getId());
        boolean completed = true;
        try {
            ExceptionBuddyUtils.LOGI("Attempting to execute custom code.");
            executeOnException();
        } catch (Exception e) {
            completed = false;
            ExceptionBuddyUtils.LOGI("Failed to execute custom code.");
            String devReport =  ExceptionBuddyUtils.causeAndStackTraceToString(e, timeOfException);
            Log.i("YOYO", devReport);
            ExceptionBuddyUtils.LOGE("ERROR EXECUTING CUSTOM CODE!\nException caught executing custom code: see details below..");
            ExceptionBuddyUtils.LOGE(e.getMessage());
            ExceptionBuddyUtils.setDevExceptionCrashReport(context,devReport);
        }finally {
            String appReport =  ExceptionBuddyUtils.causeAndStackTraceToString(throwable, timeOfException);
            ExceptionBuddyUtils.setExceptionCrashReport(context,appReport);
            ExceptionBuddyUtils.LOGI("Successfully executed custom code.");
            ExceptionBuddyUtils.LOGI("App exception details: " + appReport);
            ExceptionBuddyUtils.setDevCustomCodeExecution(context, completed);

            if(immediatelyInvoke){
                emitBuddyBroadCastNow();
                Thread.setDefaultUncaughtExceptionHandler(defaultExceptionHandler);
                ExceptionBuddyUtils.LOGI("Current thread ID#: "+ thread.getId());
                ExceptionBuddyUtils.LOGI("Killing process Pid#: "+ android.os.Process.myPid());
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }else{
                Thread.setDefaultUncaughtExceptionHandler(defaultExceptionHandler);
                ExceptionBuddyUtils.LOGI("Rethrowing exception to main handler..");
                defaultExceptionHandler.uncaughtException(thread,throwable);

            }

        }


    }


    /**
     * This is a getter for the default Uncaught Exception Handler originally assigned to the thread that the ExceptionBuddy object resides in.
     * @return The default exception handler for the thread originally set by the system.
     */
    public Thread.UncaughtExceptionHandler getDefaultExceptionHandler() {
        return defaultExceptionHandler;
    }

}
