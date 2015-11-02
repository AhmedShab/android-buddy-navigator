package jemboy.navitwo.Utility;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class Ultimate {
    ImageView imageView;
    float fromDegree = 0;
    float toDegree = 0;
    float azimuth = 0;

    public Ultimate(ImageView imageView) {
        this.imageView = imageView;
    }
/*
    public void rotateImageView() {
        RotateAnimation rotateAnimation = new RotateAnimation(
                fromDegree,
                toDegree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        fromDegree = -toDegree;
        rotateAnimation.setDuration(1200);
        rotateAnimation.setFillAfter(true);
        imageView.startAnimation(rotateAnimation);
    }

    public void updateDegree(float degree) {
        toDegree = azimuth + degree;
        if (toDegree > 180)
            toDegree = (180 - (toDegree%180)) * -1;
        else if (toDegree < -180)
            toDegree = (180 - (toDegree*-1)%180);
    }

    public void updateAzimuth(Data mData) {
        double deltaLong                = Math.toRadians(mData.remoteY) - Math.toRadians(mData.localY);
        double gb                       = Math.tan((Math.PI/4.0) + (Math.toRadians(mData.remoteX) / 2.0));
        double ga                       = Math.tan((Math.PI/4.0) + (Math.toRadians(mData.localX) / 2.0));
        double X                        = Math.log(gb) - Math.log(ga);
        azimuth                         = (float)Math.toDegrees(Math.atan2(deltaLong, X));
        updateDegree(0f);
    }
*/
}
