package jemboy.buddynavigator.Utility;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import static java.lang.Math.*;

public class ArrowAnimation {
    private ImageView imageView;
    private float localX = 0, localY = 0, remoteX = 0, remoteY = 0, rotationDegree = 0;
    private float fromDegree = 0;

    public ArrowAnimation(ImageView imageView) {
        this.imageView = imageView;
    }

    public void rotateImageView() {
        float toDegree = computeHeading();
        RotateAnimation rotateAnimation = new RotateAnimation(
                fromDegree,
                toDegree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnimation.setDuration(1200);
        rotateAnimation.setFillAfter(true);
        imageView.startAnimation(rotateAnimation);
        fromDegree = toDegree;
    }

    public float computeHeading() {
        // http://williams.best.vwh.net/avform.htm#Crs
        double fromLat = toRadians(localX);
        double fromLng = toRadians(localY);
        double toLat = toRadians(remoteX);
        double toLng = toRadians(remoteY);
        double dLng = toLng - fromLng;
        double headingWithoutRotation = atan2(
                sin(dLng) * cos(toLat),
                cos(fromLat) * sin(toLat) - sin(fromLat) * cos(toLat) * cos(dLng));
        return (float)toDegrees(headingWithoutRotation);
    }

    public void setLocalX(float localX) {
        this.localX = localX;
    }

    public void setLocalY(float localY) {
        this.localY = localY;
    }

    public void setRemoteX(float remoteX) {
        this.remoteX = remoteX;
    }

    public void setRemoteY(float remoteY) {
        this.remoteY = remoteY;
    }

    public void setRotationDegree(float rotationDegree) { this.rotationDegree = rotationDegree; }
}
