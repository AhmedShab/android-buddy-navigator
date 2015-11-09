package jemboy.navitwo.Main;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import jemboy.navitwo.Network.DeleteIDTask;
import jemboy.navitwo.Utility.Constants;

public class LovelyTextWatcher implements TextWatcher {
    Button mButton;

    public LovelyTextWatcher(Button mButton) {
        this.mButton = mButton;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        if (mButton.getText().equals("Upload") && mButton.isSelected()) {
            new DeleteIDTask(Constants.serverIP).execute(charSequence.toString());
            mButton.setSelected(false);
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        mButton.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
