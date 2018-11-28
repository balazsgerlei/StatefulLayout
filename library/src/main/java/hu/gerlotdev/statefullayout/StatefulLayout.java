package hu.gerlotdev.statefullayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
    private TextView tvEmpty;

    private View contentView;

    private View loadingView;
    private TextView tvLoading;

    private View errorView;
    private TextView tvError;
    private Button btnRetry;
    private OnClickListener btnRetryListener;

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

    public void setEmptyView(@LayoutRes int emptyViewResId) {
        setEmptyView(layoutInflater.inflate(emptyViewResId, this, false));
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        tvEmpty = emptyView.findViewById(R.id.tvEmpty);
        addView(emptyView, emptyView.getLayoutParams());
    }

    public void setLoadingView(@LayoutRes int loadingViewResId) {
        setLoadingView(layoutInflater.inflate(loadingViewResId, this, false));
    }

    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
        tvLoading = loadingView.findViewById(R.id.tvLoading);
        addView(loadingView, loadingView.getLayoutParams());
    }

    public void setErrorView(@LayoutRes int errorViewResId) {
        setErrorView(layoutInflater.inflate(errorViewResId, this, false));
    }

    public void setErrorView(View errorView) {
        this.errorView = errorView;
        tvError = errorView.findViewById(R.id.tvError);
        btnRetry = errorView.findViewById(R.id.btnRetry);
        setRetryButtonOnClickListener(btnRetryListener);
        addView(errorView, errorView.getLayoutParams());
    }

    public void showContent() {
        if (contentView != null && indexOfChild(contentView) != -1) {
            setDisplayedChild(indexOfChild(contentView));
        }
    }

    public void showEmpty(String message) {
        if(tvEmpty != null) {
            tvEmpty.setText(message);
        }
        showEmpty();
    }

    public void showEmpty() {
        if (emptyView != null && indexOfChild(emptyView) != -1) {
            setDisplayedChild(indexOfChild(emptyView));
        }
    }

    public void showLoading(String message) {
        if(tvLoading != null) {
            tvLoading.setText(message);
        }
        showLoading();
    }

    public void showLoading() {
        if (loadingView != null && indexOfChild(loadingView) != -1) {
            setDisplayedChild(indexOfChild(loadingView));
        }
    }

    public void showErrorWithRetryButton(@Nullable OnClickListener listener) {
        showErrorWithRetryButton(null, listener);
    }

    public void showErrorWithRetryButton(String message, @Nullable OnClickListener listener) {
        setRetryButtonOnClickListener(listener);
        showError(message, true);
    }

    public void showError() {
        showError(null);
    }

    public void showError(String message) {
        showError(message, false);
    }

    private void showError(String message, boolean displayRetryButton) {
        if(tvError != null && message != null) {
            tvError.setText(message);
        }
        if (btnRetry != null) {
            if (displayRetryButton) {
                btnRetry.setVisibility(VISIBLE);
            } else {
                btnRetry.setVisibility(GONE);
            }
        }
        if (errorView != null && indexOfChild(errorView) != -1) {
            setDisplayedChild(indexOfChild(errorView));
        }
    }

    private void setRetryButtonOnClickListener(@Nullable OnClickListener listener) {
        btnRetryListener = listener;
        if (btnRetry != null) {
            btnRetry.setOnClickListener(btnRetryListener);
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
