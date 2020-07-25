package a2dv606.androidproject.utisl;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Method;

import a2dv606.androidproject.MainWindow.OnCustomTouchListener;

public class EfeectButon {
    public static final String BASE_GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void removeGlobalOnLayoutListener(View target, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (getSdkVersion() < 16) {
            target.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            target.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }


    public static void shareApp(Context context) {
        disableExposure();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, BASE_GOOGLE_PLAY + context.getPackageName());
        context.startActivity(Intent.createChooser(share, "share"));
    }


    public static void createFolder(String dir) {


        File folder = new File(dir);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public static void hideKeyboard(Context context, EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private static void disableExposure() {
        if (EfeectButon.getSdkVersion() >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    public static void openApp(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            context.startActivity(launchIntent);
        }
    }

    public static void sendEmailMoree(Context context, String[] addresses, String subject, String body) {
        disableExposure();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Bạn cần cài đặt Gmail!", Toast.LENGTH_SHORT).show();
        }
    }



    public static void openMarket(Context context, String packageName) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        try {
            //  i.setData(Uri.parse("market://details?id=" + packageName));
            i.setData(Uri.parse(packageName));
            context.startActivity(i);
        } catch (android.content.ActivityNotFoundException ex) {
            openBrowser(context, BASE_GOOGLE_PLAY + packageName);
        }
    }

    public static void openBrowser(Context context, String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            context.startActivity(browserIntent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static boolean isBlank(String string) {
        if (string == null || string.length() == 0) {
            return true;
        }
        int l = string.length();
        for (int i = 0; i < l; i++) {
            if (!Character.isWhitespace(string.codePointAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static void clickScaleView(final View view, final OnCustomClickListener customClickListener) {
        setOnCustomTouchView(view, new OnCustomTouchListener() {

            private void setScale(float scale) {
                view.setScaleX(scale);
                view.setScaleY(scale);
            }

            @Override
            public void OnCustomTouchDown(View v, MotionEvent event) {
                setScale(0.9f);
            }

            @Override
            public void OnCustomTouchMoveOut(View v, MotionEvent event) {
                setScale(1f);
            }

            @Override
            public void OnCustomTouchUp(View v, MotionEvent event) {
                setScale(1f);
                if (customClickListener != null)
                    customClickListener.OnCustomClick(v, event);
            }

            @Override
            public void OnCustomTouchOther(View v, MotionEvent event) {
            }
        });

    }



    public static void setOnCustomTouchView(final View view, final OnCustomTouchListener onCustomTouchListener) {
        view.setOnTouchListener(new View.OnTouchListener() {
            private Rect rect;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (onCustomTouchListener == null)
                    return false;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    onCustomTouchListener.OnCustomTouchDown(v, event);
                } else if (rect != null && !rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                    onCustomTouchListener.OnCustomTouchMoveOut(v, event);
                    return false;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    onCustomTouchListener.OnCustomTouchUp(v, event);
                } else {
                    onCustomTouchListener.OnCustomTouchOther(v, event);
                }
                return true;
            }
        });
    }

    public static int getRandomIndex(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

//    public static boolean haveNetworkConnection(Context ctx) {
//        boolean haveConnectedWifi = false;
//        boolean haveConnectedMobile = false;
//        try {
//            ConnectivityManager cm = (ConnectivityManager) ctx
//                    .getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
//            for (NetworkInfo ni : netInfo) {
//                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
//                    if (ni.isConnected())
//                        haveConnectedWifi = true;
//                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
//                    if (ni.isConnected())
//                        haveConnectedMobile = true;
//            }
//            return haveConnectedWifi || haveConnectedMobile;
//        } catch (Exception e) {
//            System.err.println(e.toString());
//            return false;
//        }
//    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static DisplayMetrics getDisplayInfo() {
        return Resources.getSystem().getDisplayMetrics();
    }

    public static boolean checkHasPermission(Activity activity) {
        boolean hasPermission;
        hasPermission = isPermissionAllow(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                && isPermissionAllow(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return hasPermission;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static boolean isPermissionAllow(Activity activity, String permission) {
        if (EfeectButon.getSdkVersion() < 23)
            return true;

        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void showToast(Context context, int string) {
        Toast.makeText(context, context.getResources().getString(string), Toast.LENGTH_SHORT).show();
    }


}
