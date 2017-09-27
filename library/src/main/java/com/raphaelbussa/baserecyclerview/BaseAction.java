package com.raphaelbussa.baserecyclerview;

import android.support.annotation.IntDef;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by rebus007 on 22/09/17.
 */

public class BaseAction {

    @Retention(SOURCE)
    @IntDef({ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT, ItemTouchHelper.UP, ItemTouchHelper.DOWN})
    public @interface Direction {
    }

    static class Bean {

        private int direction;
        private int icon;
        private int color;


        Bean(int direction, int icon, int color) {
            this.direction = direction;
            this.icon = icon;
            this.color = color;
        }

        public int getDirection() {
            return direction;
        }

        public int getIcon() {
            return icon;
        }

        public int getColor() {
            return color;
        }

    }

}
