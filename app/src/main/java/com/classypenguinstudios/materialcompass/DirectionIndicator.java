package com.classypenguinstudios.materialcompass;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by sayujyarijal on 15-03-28.
 */
public class DirectionIndicator extends ImageView {
    private boolean isElevated = false;

    public DirectionIndicator(Context context, AttributeSet atts) {

        super(context, atts);
    }

    public boolean isElevated() {
        return isElevated;
    }

    public void elevate() {
        this.animate().setDuration(1200).alpha(1.0f);
        isElevated = true;
    }

    public void delevate() {
        this.animate().setDuration(1200).alpha(0.5f);
        isElevated = false;
    }


}
