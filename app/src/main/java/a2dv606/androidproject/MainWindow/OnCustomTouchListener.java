package a2dv606.androidproject.MainWindow;
import android.view.MotionEvent;
import android.view.View;

public interface OnCustomTouchListener {
    void OnCustomTouchDown(View v, MotionEvent event);

    void OnCustomTouchMoveOut(View v, MotionEvent event);

    void OnCustomTouchUp(View v, MotionEvent event);

    void OnCustomTouchOther(View v, MotionEvent event);
}
