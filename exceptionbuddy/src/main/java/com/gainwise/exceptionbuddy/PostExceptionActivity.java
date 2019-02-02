package com.gainwise.exceptionbuddy;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * This class is to be extended like a regular activity and will be called immediately after the uncaught exception is detected.
 *
 */
public abstract class PostExceptionActivity extends AppCompatActivity {


    public Context exceptionBuddyContext;
    /**
     * this variable will hold the app exception report
     */
    public String EXCEPTION_REPORT;
    /**
     * this variable will hold the developer's code exception report if applicable
     */
    public String DEV_CUSTOM_CODE_EXCEPTION_REPORT;
    /**
     * this variable will hold if the developer's custom code successfully executed.
     */
    public boolean DEV_CUSTOM_CODE_COMPLETED;
    /**
     * this variable will hold the device's information
     */
    public String PHONE_INFO;



    public PostExceptionActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExceptionBuddyUtils.LOGI("PostExceptionOnCreate called");
        exceptionBuddyContext = this;


        DEV_CUSTOM_CODE_EXCEPTION_REPORT = ExceptionBuddyUtils.getDevExceptionCrashReport(exceptionBuddyContext);
        DEV_CUSTOM_CODE_COMPLETED = ExceptionBuddyUtils.getDevCustomCodeExecution(exceptionBuddyContext);
        PHONE_INFO = ExceptionBuddyUtils.getDeviceInfo(exceptionBuddyContext);
        EXCEPTION_REPORT = ExceptionBuddyUtils.getExceptionCrashReport(exceptionBuddyContext);
        ExceptionBuddyUtils.LOGI("Value retrieved in PostExceptionOnCreate: " + EXCEPTION_REPORT);

    }



}
