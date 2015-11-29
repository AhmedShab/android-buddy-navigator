package jemboy.navitwo.Utility;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class ArrowAnimation {
    private ImageView imageView;
    private float fromDegree = 0, toDegree = 0, azimuth = 0;

    public ArrowAnimation(ImageView imageView) {
        this.imageView = imageView;
    }

    public void rotateImageView() {
        RotateAnimation rotateAnimation = new RotateAnimation(
                fromDegree,
                toDegree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnimation.setDuration(1200);
        rotateAnimation.setFillAfter(true);
        imageView.startAnimation(rotateAnimation);
        fromDegree = -toDegree;
    }

    public void updateDegree(float degree) {
        toDegree = azimuth + degree;
        if (toDegree > 180)
            toDegree = (180 - (toDegree%180)) * -1;
        else if (toDegree < -180)
            toDegree = (180 - (toDegree*-1)%180);
    }

    public void updateAzimuth(float localX, float localY, float remoteX, float remoteY) {
        double deltaLong                = Math.toRadians(remoteY) - Math.toRadians(localY);
        double gb                       = Math.tan((Math.PI/4.0) + (Math.toRadians(remoteX) / 2.0));
        double ga                       = Math.tan((Math.PI/4.0) + (Math.toRadians(localX) / 2.0));
        double X                        = Math.log(gb) - Math.log(ga);
        azimuth                         = (float)Math.toDegrees(Math.atan2(deltaLong, X));
        updateDegree(0f);
    }
}
