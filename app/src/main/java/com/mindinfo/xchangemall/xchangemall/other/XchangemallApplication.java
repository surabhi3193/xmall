package com.mindinfo.xchangemall.xchangemall.other;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBHttpConnectionConfig;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.ExecutionException;

import io.fabric.sdk.android.Fabric;

public class XchangemallApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks{
    private static XchangemallApplication instance;
    private Context applicationContext;
    static final String APP_ID = "72169";
    static final String AUTH_KEY = "MzLm2qxubaun6kN";
    static final String AUTH_SECRET = "bQQuMYOaXdKXTUS";
    static final String ACCOUNT_KEY = "ms9ckL3b36HnUDvspdNo";
    private QBResRequestExecutor qbResRequestExecutor;

    public static XchangemallApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        MultiDex.install(this);
        instance = this;
        applicationContext = this;

        QBSettings.getInstance().init(getApplicationContext(), APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        final QBUser user = new QBUser("aa11");


    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        try {
            boolean foreground = new ForegroundCheckTask().execute(getApplicationContext()).get();
            if (!foreground) {
                //App is in Background - do what you want
                System.out.println("---------- app is in fore ground----------");
                System.runFinalization();
                Runtime.getRuntime().gc();
                System.gc();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public synchronized QBResRequestExecutor getQbResRequestExecutor() {
        return qbResRequestExecutor == null
                ? qbResRequestExecutor = new QBResRequestExecutor()
                : qbResRequestExecutor;
    }
}
