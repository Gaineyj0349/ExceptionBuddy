package com.gainwise.exceptionbuddy;

import android.content.Context;

/**
 * Creating an instance of this class inside of a thread will allow for custom code to execute before the system
 * disposes of the thread.
 *
 */
public class ExceptionBuddy {


    private static final String BROADCAST = "com.gainwise.exceptionbuddy.BROADCAST";
    private ExceptionBuddy INSTANCE;
    private Context context;
    private String localizedCrashStackTrace;
    private ExceptionBuddyDirective exceptionDirective;
    private Class<PostExceptionActivity> postExceptionActivity;
    private boolean automaticallyInvokePostExceptionActivity = false;


    /**
     * Constructor for ExceptionBuddy object initializes the contained properties
     * @param context the context the object will use
     * @param exceptionDirectiveIn The object that contains the custom code that will execute upon a crash
     * @param postExceptionActivityIn If an uncaught exception occurs, and if the customActivityNavigate property is set to true, this activity will be the designated Activity (Developer created) to navigate too.
     * @param automaticallyInvokePostExceptionActivity If this is set to true, the new activity will automatically be launched upon crash. If false, then when app launches (user initiated) post crash, it will redirect to the custom activity set to this object's postExceptionActivity property.
     *
     */
    public ExceptionBuddy(Context context, ExceptionBuddyDirective exceptionDirectiveIn,
                          Class<PostExceptionActivity> postExceptionActivityIn, boolean automaticallyInvokePostExceptionActivity) {

        INSTANCE = this;
        this.context = context;
        exceptionDirective = exceptionDirectiveIn;
        postExceptionActivity = postExceptionActivityIn;
        this.automaticallyInvokePostExceptionActivity = automaticallyInvokePostExceptionActivity;

    }

    /**
     * Empty Constructor that initializes important object components.
     */
    public ExceptionBuddy(){
        INSTANCE = this;
       }

    /**
     * Makes the instance accessible.
     * @return the current instance
     */
    public ExceptionBuddy getINSTANCE() {
        ExceptionBuddyUtils.LOGI("Exception Buddy instance requested");
        return INSTANCE;
    }




    /**
     * Getter for the ExceptionBuddy object's current context.
     * @return the context the ExceptionBuddy object currently has
     * @throws CrashBuddyException if the context is null
     */
    public Context getContext() throws CrashBuddyException{
        if(context != null){
            return context;
        }else{
            throw new CrashBuddyException("Context is null");
        }

    }

    /**
     * Setter for context for the ExceptionBuddy object.
     * @param context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Getter for the uncaught exception information.
     * @return Information about the exception (time in millis, localized message of crash, and stack trace) is returned formatted line by line.
     * @throws CrashBuddyException if there is no excpetion report.
     */
    public String getLocalizedCrashStackTrace() throws CrashBuddyException {
        if(localizedCrashStackTrace != null){
            return localizedCrashStackTrace;
        }else{
            throw new CrashBuddyException("Localized Crash has no value");
        }

    }

    /**
     * Setter for the localized exception.
     * @param localizedCrashStackTrace
     */
    public void setLocalizedCrashStackTrace(String localizedCrashStackTrace) {
        this.localizedCrashStackTrace = localizedCrashStackTrace;
    }

    /**
     * This returns the exceptionReceiver object if it exists.
     * @return The ExceptionBuddyReceiver object assigned to this ExceptionBuddy object's exceptionReceiver property.
     * @throws CrashBuddyException
     */
    public ExceptionBuddyDirective getExceptionDirective() throws CrashBuddyException {
       if( exceptionDirective!= null){
           return exceptionDirective;
       }else{
           throw new CrashBuddyException("There is no ExceptionBuddyDirective propery initialized in this crashbuddy object.");
       }
    }

    /**
     * Setter for the exceptionReceiver property of this ExceptionBuddy object.
     * @param exceptionDirective This object will execute the interface's abstract method upon an uncaught exception.
     */
    public void setExceptionDirective(ExceptionBuddyDirective exceptionDirective) {
        exceptionDirective = exceptionDirective;
    }

    /**
     * Getter for the postExeptionActivity
     * @return The activity that is designated as the redirection activity upon an uncaught exception.
     */
    public Class<PostExceptionActivity> getPostExceptionActivity() throws CrashBuddyException {
        if(postExceptionActivity != null){
            return postExceptionActivity;
        }else{
            throw new CrashBuddyException("The PostExceptionActivity property of the ExceptionBuddy object has not been initialized. Initialize before accessing");
        }

    }

    /**
     * Setter for the postExceptionActivity
     * @param postExceptionActivity This is a custom made Activity from the developer that extends the abstract class PostExceptionActivity
     */
    public <T extends PostExceptionActivity> void setPostCrashActivityToLaunch(Class<T> postExceptionActivity) {
        postExceptionActivity = postExceptionActivity;
    }

    /**
     * Getter for if the Post Excpetion Activity is automatically launched immediately following the uncaught exception (default is false, if true, it uses the waitTimeInMillisToInvokeExceptionActivity property as a timer for launching)
     * @return 
     */
    public boolean isAutomaticallyInvokePostExceptionActivity() {
        return automaticallyInvokePostExceptionActivity;
    }

    /**
     * Setter for if the custom exception activity is to launch immediately post exception (set this to true) or upon app relaunch by user (set to false)(default setting is false).
     * @param automaticallyInvokePostExceptionActivity
     */
    public void setAutomaticallyInvokeExceptionActivityActivity(boolean automaticallyInvokePostExceptionActivity) {
        this.automaticallyInvokePostExceptionActivity = automaticallyInvokePostExceptionActivity;
    }


    /**
     * Custom exception for the library. Will be thrown when necessary information is not provided.
     */
    public static class CrashBuddyException extends Exception{
        CrashBuddyException(String errorMessage) {
            super(errorMessage);
        }
    }



    /**
     * The builder class for a ExceptionBuddy object to simplify object creation.
     */
    public static class Builder{

        private Context context;
        private ExceptionBuddyDirective exceptionDirective = null;
        private Class postExceptionActivity;
        private boolean automaticallyInvokePostExceptionActivity = true;

        /**
         * The builder constructor requires the context.
         */
        public Builder(Context context){
            this.context = context;
        }


        /**
         * This is the Activity to navigate to automatically upon an uncaught exception.
         * @param postExceptionActivity
         */
        public <T extends PostExceptionActivity> Builder setPostExceptionActivity(Class<T> postExceptionActivity){
            this.postExceptionActivity = postExceptionActivity;
            return this;
        }

        /**
         * This method will determine whether to autmatically invoke the exception handling activity upon uncaught exception. (default value is true).
         * If this is true, then a PostExceptionActivity MUST be provided. If this is set to false, then the default handler will catch the exception.
         * @param autoInvoke
         */
        public Builder automaticallyInvokePostExceptionActivity(boolean autoInvoke){
            this.automaticallyInvokePostExceptionActivity = autoInvoke;
            return this;
        }

        /**
         * This is a custom object extending ExceptionBuddyDirective. It must be provided. If one is not provided it will crash.
         * @param exceptionDirective
         */
        public Builder withExceptionDirective(ExceptionBuddyDirective exceptionDirective){
            this.exceptionDirective = exceptionDirective;
            return this;
        }


        /**
         * this is the last method to call to construct the ExceptionBuddy object.
         * @return the custom ExceptionBuddy instance.
         */
        public ExceptionBuddy build(){
            ExceptionBuddyUtils.LOGI("Building ExceptionBuddy object via Builder.");
            this.exceptionDirective.setImmediatelyInvoke(this.automaticallyInvokePostExceptionActivity);
            this.exceptionDirective.setContext(this.context);
            if(this.postExceptionActivity != null){
                this.exceptionDirective.setPostExceptionActivityIn(this.postExceptionActivity);
            }

            this.exceptionDirective.create();

            return new ExceptionBuddy(this.context, this.exceptionDirective, this.postExceptionActivity,
                    this.automaticallyInvokePostExceptionActivity);

        }

    }






}
