package org.battleshipgame.android;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.cuba.log.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicReference;

import static android.os.Build.VERSION.SDK_INT;

public class AndroidUtils {

    @RequiresApi(api = Build.VERSION_CODES.P)
    private static Signature[] getSignatures_API28(Context context) throws PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
        return info.signingInfo.getApkContentsSigners();
    }

    private static Signature[] getSignatures_API1(Context context) throws PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
        return info.signatures;
    }

    public static Integer getSignature(Context context) {
        try {
            Signature[] signatures = SDK_INT >= Build.VERSION_CODES.P ? getSignatures_API28(context) : getSignatures_API1(context);
            return signatures[0].hashCode();
        } catch(Exception e) {
            return null;
        }
    }

    public static String getIP(Log log) {
        AtomicReference<String> reference = new AtomicReference<>(null);
        Thread thread = new Thread(() -> {
            try {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while(interfaces.hasMoreElements()) {
                    NetworkInterface network = interfaces.nextElement();
                    Enumeration<InetAddress> addresses = network.getInetAddresses();
                    while(addresses.hasMoreElements()) {
                        InetAddress inetAddress = addresses.nextElement();
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                            reference.set(inetAddress.getHostAddress());
                            return;
                        }
                    }
                }
            } catch (SocketException e) {
                log.e("UTILS", "Failed to get IP", e);
                reference.set("");
            }
        });
        thread.start();
        while(reference.get() == null) {
            try {
                Thread.sleep(30L);
            } catch(Throwable t) {
                log.e("UTILS", "Failed to sleep waiting thread", t);
            }
        }
        return reference.get();
    }
}
