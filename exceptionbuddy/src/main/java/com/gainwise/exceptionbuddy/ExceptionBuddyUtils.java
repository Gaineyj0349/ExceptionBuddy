package com.gainwise.exceptionbuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

/**
 * This class contains static utility methods for the ExceptionBuddy library.
 */
public class ExceptionBuddyUtils {

    protected static final String TAG = "Exception_Buddy_Lib";
    protected static final String TAGe = "Exception_Buddy_Error";
    protected static final String TAGp = "Exception_Buddy_Message";
    private static final String SPtag = "ExceptionBuddy";
    private static final String sectionLineDouble = "\n**********************\n\n";
    private static final String sectionLineSingle = "**********************\n**********************\n";
    private static final String line = "\n--------------------------\n";

    /**
     * This method takes an exception and converts its details to a formatted string.
     *
     * @param throwable The exception.
     * @return The exception in String format
     */
    protected static String causeAndStackTraceToString(Throwable throwable, String formattedDate){
        LOGI("Cause and stack trace building.");
        Throwable cause2 = null, cause3 = null, cause4 = null;
        StackTraceElement[] stackTraceFromCause1 = throwable.getStackTrace();

        StringBuilder report = new StringBuilder();
        boolean levelDown = true;

        report.append("*TIMESTAMP* \n" + formattedDate);
        report.append(line);

        //level 1
        report.append(sectionLineSingle);
        report.append("**** BEGIN LAYER 1 ****");
        report.append(sectionLineDouble);

        report.append("*LOCAL MESSAGE*\n" + throwable.getLocalizedMessage());
        try{
            report.append(line);
            cause2 = throwable.getCause();
            report.append("*CAUSE*\n" + cause2.toString());
        }catch (Exception e){
            levelDown = false;
        }
        report.append(line);
        report.append("*STACK TRACE*\n");
        for(StackTraceElement el1 : stackTraceFromCause1){
            report.append(el1.toString());
            report.append("\n");
        }
        report.append("\n");
        report.append(sectionLineSingle);
        report.append("***** END LAYER 1 *****");
        report.append(sectionLineDouble);

        report.append("\n\n\n\n\n");


        //level 2

            if (cause2 != null && levelDown) {
                StackTraceElement[] stackTraceFromCause2 = cause2.getStackTrace();
                report.append(sectionLineSingle);
                report.append("**** BEGIN LAYER 2 ****");
                report.append(sectionLineDouble);
                report.append("*LOCAL MESSAGE*\n" + cause2.getMessage());
                try{
                    cause3 = cause2.getCause();
                report.append(line);
                report.append("*CAUSE*\n" + cause3.toString());
                }catch (Exception e){
                    levelDown = false;
                }
                report.append(line);
                report.append("*STACK TRACE*\n");
                for (StackTraceElement el2 : stackTraceFromCause2) {
                    report.append(el2.toString());
                    report.append("\n");
                }
                report.append("\n");
                report.append(sectionLineSingle);
                report.append("***** END LAYER 2 *****");
                report.append(sectionLineDouble);

                report.append("\n\n\n\n\n");

            }

        //level 3

            if (cause3 != null && levelDown) {
                StackTraceElement[] stackTraceFromCause3 = cause3.getStackTrace();
                report.append(sectionLineSingle);
                report.append("**** BEGIN LAYER 3 ****");
                report.append(sectionLineDouble);
                report.append("*LOCAL MESSAGE*\n" + cause3.getLocalizedMessage());

                try {
                    report.append(line);
                    cause4 = cause3.getCause();
                    report.append("*CAUSE*\n" + cause4.toString());
                }catch (Exception e){
                    levelDown = false;
                }
                report.append(line);
                report.append("*STACK TRACE*\n");
                for (StackTraceElement el3 : stackTraceFromCause3) {
                    report.append(el3.toString());
                    report.append("\n");
                }
                report.append("\n");
                report.append(sectionLineSingle);
                report.append("***** END LAYER 3 *****");
                report.append(sectionLineDouble);

                report.append("\n\n\n\n\n");

            }

        //level 4

        if (cause4 != null && levelDown) {
            StackTraceElement[] stackTraceFromCause4 = cause4.getStackTrace();
            report.append(sectionLineSingle);
            report.append("**** BEGIN LAYER 4 ****");
            report.append(sectionLineDouble);
            report.append("*LOCAL MESSAGE*\n" + cause4.getLocalizedMessage());

            try {
                report.append(line);
                report.append("*CAUSE*\n" + cause4.getCause().toString());
            }catch (Exception e){
                levelDown = false;
            }
            report.append(line);
            report.append("*STACK TRACE*\n");
            for (StackTraceElement el4 : stackTraceFromCause4) {
                report.append(el4.toString());
                report.append("\n");
            }
            report.append("\n");
            report.append(sectionLineSingle);
            report.append("***** END LAYER 4 *****");
            report.append(sectionLineDouble);

            report.append("\n\n\n\n\n");

        }


        return report.toString();
    }

    /**
     * this method formats the device information into a nice readable string.
     * @return
     */
    protected static String getDeviceInformation(){

        StringBuilder sb = new StringBuilder();
        // Getting the Device brand,model and sdk verion details.
        sb.append(sectionLineSingle);
        sb.append("**** BEGIN DEVICE ****");
        sb.append(sectionLineDouble);
        sb.append("\n\n");
        sb.append("*BRAND*");
        sb.append("\n");
        sb.append(Build.BRAND);
        sb.append("\n");
        sb.append("*DEVICE*");
        sb.append("\n");
        sb.append(Build.DEVICE);
        sb.append("\n");
        sb.append("*MODEL*");
        sb.append("\n");
        sb.append(Build.MODEL);
        sb.append("\n");
        sb.append("*BUILD-ID*");
        sb.append("\n");
        sb.append(Build.ID);
        sb.append("\n");
        sb.append("*PRODUCT*");
        sb.append("\n");
        sb.append(Build.PRODUCT);
        sb.append("\n");
        sb.append("*SDK*");
        sb.append("\n");
        sb.append(Build.VERSION.SDK);
        sb.append("\n");
        sb.append("*RELEASE*");
        sb.append("\n");
        sb.append(Build.VERSION.RELEASE);
        sb.append("\n");
        sb.append("*INCREMENTAL*");
        sb.append("\n");
        sb.append(Build.VERSION.INCREMENTAL);
        sb.append("\n\n\n");
        sb.append(sectionLineSingle);
        sb.append("***** END DEVICE *****");
        sb.append(sectionLineDouble);

        return sb.toString();
    }

    protected static void LOGI(String message){
        Log.i(TAG,message);
    }
    protected static void LOGE(String message){
        Log.e(TAGe,message);
    }
    protected static void LOGP(String message){
        Log.d(TAGp,message);
    }

    /**
     * this method writes the app crash report to shared preferences. This is called from the directive upon crash.
     * @param context
     * @param message
     */
    protected static void setExceptionCrashReport(Context context, String message){
        LOGI("SettingExceptionCrashReport called with: " + message);
        SharedPreferences.Editor edit = context.getSharedPreferences(SPtag, Context.MODE_PRIVATE).edit();
        edit.putString("app_crash_report", message);
        edit.apply();
    }

    /**
     * this method returns the exception report of the caught exception from the application.
     * @param context
     * @return
     */
    public static String getExceptionCrashReport(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SPtag, Context.MODE_PRIVATE);
        String report =  pref.getString("app_crash_report", "");
        return report;
    }

    /**
     * this method writes the developer's custom code crash report to shared preferences. This is called from the directive upon failure to execute the code in executeOnException in the directive..
     * @param context
     * @param message
     */
    protected static void setDevExceptionCrashReport(Context context, String message){
        SharedPreferences.Editor edit = context.getSharedPreferences(SPtag, Context.MODE_PRIVATE).edit();
        edit.putString("dev_crash_report", message);
        edit.apply();
    }

    /**
     * this method returns the exception report of the caught exception from the developers code in the executeOnException in the  custom directive object.
     * @param context
     * @return
     */
    public static String getDevExceptionCrashReport(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SPtag, Context.MODE_PRIVATE);
        return pref.getString("dev_crash_report", "");
    }

    /**
     * this method sets the status of if the device information has already been wrote to shared preferences.
     * @param context
     * @param set
     */
    protected static void setDeviceInfoSet(Context context, boolean set){
        SharedPreferences.Editor edit = context.getSharedPreferences(SPtag, Context.MODE_PRIVATE).edit();
        edit.putBoolean("device_info_set", set);
        edit.apply();
    }

    /**
     * this method returns true if the device information has already been wrote to shared preferences
     * @param context
     * @return
     */
    protected static boolean getDeviceInfoSet(Context context){
        SharedPreferences pref = context.getSharedPreferences(SPtag, Context.MODE_PRIVATE);
        return pref.getBoolean("device_info_set", false);
    }

    /**
     * this method sets the status of if the executeOnException method in the directive completed successfully.
     * @param context
     * @param set
     */
    protected static void setDevCustomCodeExecution(Context context, boolean set){
        SharedPreferences.Editor edit = context.getSharedPreferences(SPtag, Context.MODE_PRIVATE).edit();
        edit.putBoolean("dev_exception_status", set);
        edit.apply();
    }

    /**
     * this method returns if the executeOnException method in the directive completed successfully.
     * @param context
     * @return true if the developer's custom code executed successfully upon crash.
     */
    public static boolean getDevCustomCodeExecution(Context context){
        SharedPreferences pref = context.getSharedPreferences(SPtag, Context.MODE_PRIVATE);
        return pref.getBoolean("dev_exception_status", true);
    }

    /**
     * this method sets the information about the user's device into shared preferences.
     * @param context
     * @param info
     */
    protected static void setDeviceInfo(Context context, String info){
        SharedPreferences.Editor edit = context.getSharedPreferences(SPtag, Context.MODE_PRIVATE).edit();
        edit.putString("device_info", info);
        edit.apply();
    }

    /**
     * this method returns information about the current user's device from shared preferences.
     * @param context
     * @return
     */
    public static String getDeviceInfo(Context context){
        SharedPreferences pref = context.getSharedPreferences(SPtag, Context.MODE_PRIVATE);
        return pref.getString("device_info", "");
    }


    /**
     * This allows to reset the information inside of the library. This should be placed within the developers code when the developer wants to reset the reports in the library.
     * @param context the current context.
     */
    public static void clearData(Context context){
        SharedPreferences.Editor edit = context.getSharedPreferences(SPtag, Context.MODE_PRIVATE).edit();
        edit.putBoolean("dev_exception_status", true);
        edit.putString("dev_exception_crash_report", "");
        edit.putString("exception_crash_report", "");
        edit.apply();
    }
}
