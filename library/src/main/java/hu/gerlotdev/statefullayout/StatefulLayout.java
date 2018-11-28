package hu.gerlotdev.statefullayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
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
        int EMPTY = 0;
        int LOADING = 1;
        int ERROR = 2;
        int CONTENT = 3;
    }

    @LayoutState
    private int layoutState = LayoutState.CONTENT;

    private LayoutInflater layoutInflater;

    private View emptyView;
    private View contentView;
    private View loadingView;
    private View errorView;

    public StatefulLayout(Context context) {
        super(context);
        layoutInflater = LayoutInflater.from(getContext());
    }

    public StatefulLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        layoutInflater = LayoutInflater.from(getContext());

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.StatefulLayout);

        int emptyViewResId = typedArray.getResourceId(R.styleable.StatefulLayout_sl_emptyView, -1);
        if (emptyViewResId > -1) {
            setEmptyView(emptyViewResId);
        }

        int loadingViewResId = typedArray.getResourceId(R.styleable.StatefulLayout_sl_loadingView, -1);
        if (loadingViewResId > -1) {
            setLoadingView(loadingViewResId);
        }

        int errorViewResId = typedArray.getResourceId(R.styleable.StatefulLayout_sl_errorView, -1);
        if (errorViewResId > -1) {
            setErrorView(errorViewResId);
        }

        int state = typedArray.getInt(R.styleable.StatefulLayout_sl_layoutState, LayoutState.CONTENT);
        switch (state) {
            case LayoutState.EMPTY:
                layoutState = LayoutState.EMPTY;
                break;
            case LayoutState.LOADING:
                layoutState = LayoutState.LOADING;
                break;
            case LayoutState.ERROR:
                layoutState = LayoutState.ERROR;
                break;
            default:
                layoutState = LayoutState.CONTENT;
                break;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isChildCountCorrect()) {
            throw new IllegalStateException("StatefulLayout can host exactly one direct child, specify layout for states other than content using sl_errorView, sl_loadingView, sl_emptyView xml attributes or setters");
        }

        contentView = getChildAt(LayoutState.CONTENT);
        switch (layoutState) {
            case LayoutState.EMPTY:
                showEmpty();
                break;
            case LayoutState.LOADING:
                showLoading();
                break;
            case LayoutState.ERROR:
                showError();
                break;
            default:
                showContent();
                break;
        }
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    public void setEmptyView(@LayoutRes int emptyViewResId) {
        emptyView = layoutInflater.inflate(emptyViewResId, this, false);
        addView(emptyView, emptyView.getLayoutParams());
    }

    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
    }

    public void setLoadingView(@LayoutRes int loadingViewResId) {
        loadingView = layoutInflater.inflate(loadingViewResId, this, false);
        addView(loadingView, loadingView.getLayoutParams());
    }

    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    public void setErrorView(@LayoutRes int errorViewResId) {
        errorView = layoutInflater.inflate(errorViewResId, this, false);
        addView(errorView, errorView.getLayoutParams());
    }

    public void showContent() {
        if (contentView != null && indexOfChild(contentView) != -1) {
            setDisplayedChild(indexOfChild(contentView));
        }
    }

    public void showEmpty(String message) {

    }

    public void showEmpty() {
        if (emptyView != null && indexOfChild(emptyView) != -1) {
            setDisplayedChild(indexOfChild(emptyView));
        }
    }

    public void showLoading(String message) {

    }

    public void showLoading() {
        if (loadingView != null && indexOfChild(loadingView) != -1) {
            setDisplayedChild(indexOfChild(loadingView));
        }
    }

    public void showError(String message) {

    }

    public void showError() {
        if (errorView != null && indexOfChild(errorView) != -1) {
            setDisplayedChild(indexOfChild(errorView));
        }
    }

    private boolean isChildCountCorrect() {
        int expectedChildCount = 1;
        if (emptyView != null) {
            expectedChildCount++;
        }
        if (loadingView != null) {
            expectedChildCount++;
        }
        if (errorView != null) {
            expectedChildCount++;
        }
        return getChildCount() == expectedChildCount;
    }

}
