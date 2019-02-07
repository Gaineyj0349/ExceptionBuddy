# CrashBuddy
This library implements custom exception handling. 
Javadocs can be found at www.gainwisetech.com/javadocs/ExceptionBuddy

Example app can be found at
https://play.google.com/store/apps/details?id=com.gainwise.exceptionbuddytestapp


The source code for the demo app can be found at
https://github.com/Gaineyj0349/ExceptionBuddyApp



Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
	
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.Gaineyj0349:ExceptionBuddy:1.1.1'
	}


This library simply gives the developer control over what happens when an uncaught exception occurs. 
The main strengths of this library is the ability to redirect to custom activity upon uncaught exception, and have the detailed and formatted exception information available in that activity to do what you wish. This Directive also has access to the activity context (just type context to use it)

 - 1 -
Create a Directive class and extend the ExceptionBuddyDirective like so:

	class MyDirective extends ExceptionBuddyDirective{
	
    //the context is given to this class from the builder object. just type context to use it

    @Override
    public void executeOnException() throws ExceptionBuddy.CrashBuddyException {


        /*
       This option is provided in case the developer wants to execute some simple code like changing sharedpref
       values, and still wants the android default uncaught exception behavior.
       The process has become unstable, so things like showing a toast is not possible now, maybe a future update
       this will be possible.
       */

        Log.i("Case2Directive", "process id = " + android.os.Process.myPid());
    }
	}


 - 2 - 
Create an Activity that extends PostExceptionActivity. When an uncaught exception occurs, this is the activity that the app will 	  redirect to before closing down the previous process. This class comes with 4 variables.
 
  public String EXCEPTION_REPORT; 
  this variable will hold the app's exception report
  
  public boolean DEV_CUSTOM_CODE_COMPLETED;
  this variable will hold if the developer's custom code successfully executed (in the directive's executeOnException.
  
  public String DEV_CUSTOM_CODE_EXCEPTION_REPORT;
  this variable will hold the developer's code exception report (if applicable)
  
  public String PHONE_INFO;
  this variable will hold the device's information
  

    
   
 - 3 - 
Now just create the ExceptionBuddy object within the class you want and all future activities in the stack will receive the benefits  as well without recreating the object (granted they are in the same process.) However, I typically will put it in onCreate every     class for the fact that android can reclaim memory if it needs and will render the ExceptionBuddy object useless if I was relying on one created lower in the taskstack. Like so:

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case1);

        ExceptionBuddy exceptionBuddy = new ExceptionBuddy.Builder(this)
                                                .automaticallyInvokePostExceptionActivity(true)
                                                .setPostExceptionActivity(MyPostExceptionActivity.class)
                                                .withExceptionDirective(new MyDirective())
                                                .build();



    }
	

	

