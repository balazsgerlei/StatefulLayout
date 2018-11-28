package hu.gerlotdev.statefullayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ViewFlipper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class StatefulLayout extends ViewFlipper {

    @IntDef({LayoutState.EMPTY, LayoutState.LOADING, LayoutState.ERROR, LayoutState.CONTENT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LayoutState {
        int CONTENT = 0;
        int EMPTY = 1;
        int LOADING = 2;
        int ERROR = 3;
    }

    private LayoutInflater layoutInflater;

    private View emptyView;
    private View contentView;
    private View loadingView;
    private View errorView;

    public StatefulLayout(Context context) {
        super(context);
    }

    public StatefulLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        layoutInflater = LayoutInflater.from(getContext());
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StatefulLayout);

        int emptyViewResId = a.getResourceId(R.styleable.StatefulLayout_sl_emptyView, -1);
        if (emptyViewResId > -1) {
            emptyView = layoutInflater.inflate(emptyViewResId, this, false);
            addView(emptyView, emptyView.getLayoutParams());
        }

        int loadingViewResId = a.getResourceId(R.styleable.StatefulLayout_sl_loadingView, -1);
        if (loadingViewResId > -1) {
            loadingView = layoutInflater.inflate(loadingViewResId, this, false);
            addView(loadingView, loadingView.getLayoutParams());
        }

        int errorViewResId = a.getResourceId(R.styleable.StatefulLayout_sl_errorView, -1);
        if (errorViewResId > -1) {
            errorView = layoutInflater.inflate(errorViewResId, this, false);
            addView(errorView, errorView.getLayoutParams());
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            throw new IllegalStateException("StatefulLayout can host only one direct child, specify layout for states other than content with sl_errorView, sl_loadingView, sl_emptyView attributes");
        } else if (getChildCount() == 1) {
            contentView = getChildAt(LayoutState.CONTENT);
        } else {
            contentView = null;
        }
    }

    public void showEmpty() {

    }

    public void showEmpty(String message) {

    }

    public void showContent() {

    }

    public void showLoading() {

    }

    public void showLoading(String message) {

    }

    public void showError() {

    }

    public void showError(String message) {

    }

}
