package com.raphaelbussa.baserecyclerview;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by rebus007 on 03/09/17.
 */

class Utils {

    static int selectableItemBackground(Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        return outValue.resourceId;
    }

}
