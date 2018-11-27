package hu.gerlotdev.statefullayout;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class StatefulLayout extends ViewFlipper {

    @IntDef({LayoutState.EMPTY, LayoutState.LOADING, LayoutState.ERROR, LayoutState.CONTENT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LayoutState {
        int EMPTY = 0;
        int LOADING = 1;
        int ERROR = 2;
        int CONTENT = 3;
    }

    public StatefulLayout(Context context) {
        super(context);
    }

    public StatefulLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    

}
