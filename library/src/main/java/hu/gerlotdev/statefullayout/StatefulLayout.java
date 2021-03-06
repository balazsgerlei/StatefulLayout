package hu.gerlotdev.statefullayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    private LayoutStateChangeListener onLayoutStateChangeListener;

    @LayoutState
    private int layoutState = LayoutState.CONTENT;

    private LayoutInflater layoutInflater;

    private View emptyView;
    private ImageView ivEmpty;
    private TextView tvEmpty;

    private View contentView;

    private View loadingView;
    private TextView tvLoading;

    private View errorView;
    private ImageView ivError;
    private TextView tvErrorTitle;
    private TextView tvErrorMessage;
    private Button btnRetry;
    private OnClickListener btnRetryListener;

    public static class LayoutConfig {

        private int emptyViewResId = -1;
        private View emptyView;

        private int loadingViewResId = -1;
        private View loadingView;

        private int errorViewResId = -1;
        private View errorView;

        public LayoutConfig() {
        }

        public LayoutConfig withEmptyView(@LayoutRes int emptyViewResId) {
            if (emptyViewResId == -1) {
                return this;
            }
            if (emptyView != null) {
                emptyView = null;
            }
            this.emptyViewResId = emptyViewResId;
            return this;
        }

        public LayoutConfig withEmptyView(@NonNull View emptyView) {
            if (emptyViewResId != -1) {
                emptyViewResId = -1;
            }
            this.emptyView = emptyView;
            return this;
        }

        public LayoutConfig withLoadingView(@LayoutRes int loadingViewResId) {
            if (loadingViewResId == -1) {
                return this;
            }
            if (loadingView != null) {
                loadingView = null;
            }
            this.loadingViewResId = loadingViewResId;
            return this;
        }

        public LayoutConfig withLoadingView(@NonNull View loadingView) {
            if (loadingViewResId != -1) {
                loadingViewResId = -1;
            }
            this.loadingView = loadingView;
            return this;
        }

        public LayoutConfig withErrorView(@LayoutRes int errorViewResId) {
            if (errorViewResId == -1) {
                return this;
            }
            if (errorView != null) {
                errorView = null;
            }
            this.errorViewResId = errorViewResId;
            return this;
        }

        public LayoutConfig withErrorView(@NonNull View errorView) {
            if (errorViewResId != -1) {
                errorViewResId = -1;
            }
            this.errorView = errorView;
            return this;
        }

    }

    public interface StateConfig {

        void configureLayout(StatefulLayout layout);

    }

    public static class DefaultStateConfig implements StateConfig {

        protected @LayoutState int layoutState;
        protected String title;
        protected String message;
        protected Drawable image;
        protected String buttonTitle;
        protected OnClickListener buttonClickListener;

        public DefaultStateConfig(@LayoutState int layoutState) {
            this.layoutState = layoutState;
        }

        public DefaultStateConfig withTitle(String title) {
            this.title = title;
            return this;
        }

        public DefaultStateConfig withMessage(String message) {
            this.message = message;
            return this;
        }

        public DefaultStateConfig withImage(Drawable image) {
            this.image = image;
            return this;
        }

        public DefaultStateConfig withButton(String buttonTitle, @Nullable OnClickListener listener) {
            this.buttonTitle = buttonTitle;
            this.buttonClickListener = listener;
            return this;
        }

        // TODO assert correct config!
        @Override
        public void configureLayout(StatefulLayout layout) {
            switch (layoutState) {
                case LayoutState.EMPTY:
                    showEmpty(layout);
                    break;
                case LayoutState.LOADING:
                    showLoading(layout);
                    break;
                case LayoutState.ERROR:
                    showError(layout);
                    break;
                default:
                    showContent(layout);
                    break;
            }
        }

        protected void showContent(StatefulLayout layout) {
            layout.displayLayoutAndNotifyListener(layout.contentView, LayoutState.CONTENT);
        }

        protected void showEmpty(StatefulLayout layout) {
            if (layout.ivEmpty != null) {
                if (image != null) {
                    layout.ivEmpty.setImageDrawable(image);
                    layout.ivEmpty.setVisibility(VISIBLE);
                } else {
                    layout.ivEmpty.setVisibility(GONE);
                }
            }
            if(layout.tvEmpty != null && message != null) {
                layout.tvEmpty.setText(message);
            }
            layout.displayLayoutAndNotifyListener(layout.emptyView, LayoutState.EMPTY);
        }

        protected void showLoading(StatefulLayout layout) {
            if(layout.tvLoading != null && message != null) {
                layout.tvLoading.setText(message);
            }
            layout.displayLayoutAndNotifyListener(layout.loadingView, LayoutState.LOADING);
        }

        protected void showError(StatefulLayout layout) {
            layout.btnRetryListener = buttonClickListener;
            if (layout.btnRetry != null && buttonTitle != null) {
                layout.btnRetry.setOnClickListener(layout.btnRetryListener);
                layout.btnRetry.setVisibility(VISIBLE);
            } else {
                layout.btnRetry.setOnClickListener(null);
                layout.btnRetry.setVisibility(GONE);
            }

            if (layout.ivError != null) {
                if (image != null) {
                    layout.ivError.setImageDrawable(image);
                    layout.ivError.setVisibility(VISIBLE);
                } else {
                    layout.ivError.setVisibility(GONE);
                }
            }
            if (layout.tvErrorTitle != null) {
                if (title != null) {
                    layout.tvErrorTitle.setText(title);
                    layout.tvErrorTitle.setVisibility(VISIBLE);
                } else {
                    layout.tvErrorTitle.setVisibility(GONE);
                }
            }
            if(layout.tvErrorMessage != null && message != null) {
                layout.tvErrorMessage.setText(message);
            }
            layout.displayLayoutAndNotifyListener(layout.errorView, LayoutState.ERROR);
        }

    }

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

    public @LayoutState int getLayoutState() {
        return layoutState;
    }

    public void setOnLayoutStateChangeListener(LayoutStateChangeListener listener) {
        this.onLayoutStateChangeListener = listener;
    }

    public void applyLayoutConfig(LayoutConfig config) {
        if (config.emptyView != null) {
            setEmptyView(config.emptyView);
        } else if (config.emptyViewResId != -1) {
            setEmptyView(config.emptyViewResId);
        }

        if (config.loadingView != null) {
            setLoadingView(config.loadingView);
        } else if (config.loadingViewResId != -1) {
            setLoadingView(config.loadingViewResId);
        }

        if (config.errorView != null) {
            setErrorView(config.errorView);
        } else if (config.errorViewResId != -1) {
            setErrorView(config.errorViewResId);
        }
    }

    public void applyStateConfig(StateConfig stateConfig) {
        stateConfig.configureLayout(this);
    }

    @Deprecated
    public void setEmptyView(@LayoutRes int emptyViewResId) {
        setEmptyView(layoutInflater.inflate(emptyViewResId, this, false));
    }

    @Deprecated
    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        ivEmpty = emptyView.findViewById(R.id.ivEmpty);
        tvEmpty = emptyView.findViewById(R.id.tvEmpty);
        addView(emptyView, emptyView.getLayoutParams());
    }

    @Deprecated
    public void setLoadingView(@LayoutRes int loadingViewResId) {
        setLoadingView(layoutInflater.inflate(loadingViewResId, this, false));
    }

    @Deprecated
    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
        tvLoading = loadingView.findViewById(R.id.tvLoading);
        addView(loadingView, loadingView.getLayoutParams());
    }

    @Deprecated
    public void setErrorView(@LayoutRes int errorViewResId) {
        setErrorView(layoutInflater.inflate(errorViewResId, this, false));
    }

    @Deprecated
    public void setErrorView(View errorView) {
        this.errorView = errorView;
        ivError = errorView.findViewById(R.id.ivError);
        tvErrorTitle = errorView.findViewById(R.id.tvErrorTitle);
        tvErrorMessage = errorView.findViewById(R.id.tvErrorMessage);
        btnRetry = errorView.findViewById(R.id.btnRetry);
        setRetryButtonOnClickListener(btnRetryListener);
        addView(errorView, errorView.getLayoutParams());
    }

    @Deprecated
    public void showContent() {
        displayLayoutAndNotifyListener(contentView, LayoutState.CONTENT);
    }

    @Deprecated
    public void showEmpty(String message) {
        showEmpty(null, message);
    }

    @Deprecated
    public void showEmpty(Drawable image, String message) {
        if (ivEmpty != null) {
            if (image != null) {
                ivEmpty.setImageDrawable(image);
                ivEmpty.setVisibility(VISIBLE);
            } else {
                ivEmpty.setVisibility(GONE);
            }
        }
        if(tvEmpty != null && message != null) {
            tvEmpty.setText(message);
        }
        showEmpty();
    }

    @Deprecated
    public void showEmpty() {
        displayLayoutAndNotifyListener(emptyView, LayoutState.EMPTY);
    }

    @Deprecated
    public void showLoading(String message) {
        if(tvLoading != null && message != null) {
            tvLoading.setText(message);
        }
        showLoading();
    }

    @Deprecated
    public void showLoading() {
        displayLayoutAndNotifyListener(loadingView, LayoutState.LOADING);
    }

    @Deprecated
    public void showErrorWithRetryButton(@Nullable OnClickListener listener) {
        showErrorWithRetryButton(null, listener);
    }

    @Deprecated
    public void showErrorWithRetryButton(String message, @Nullable OnClickListener listener) {
        showErrorWithRetryButton(null, null, message, listener);
    }

    @Deprecated
    public void showErrorWithRetryButton(Drawable image, String message, @Nullable OnClickListener listener) {
        showErrorWithRetryButton(image, null, message, listener);
    }

    @Deprecated
    public void showErrorWithRetryButton(String title, String message, @Nullable OnClickListener listener) {
        showErrorWithRetryButton(null, title, message, listener);
    }

    @Deprecated
    public void showErrorWithRetryButton(Drawable image, String title, String message, @Nullable OnClickListener listener) {
        setRetryButtonOnClickListener(listener);
        showError(image, title, message, true);
    }

    @Deprecated
    public void showError() {
        showError(null);
    }

    @Deprecated
    public void showError(Drawable image, String title, String message) {
        showError(image, title, message, false);
    }

    @Deprecated
    public void showError(String title, String message) {
        showError(null, title, message, false);
    }

    @Deprecated
    public void showError(Drawable image, String message) {
        showError(image, null, message, false);
    }

    @Deprecated
    public void showError(String message) {
        showError(null, null, message, false);
    }

    @Deprecated
    private void showError(Drawable image, String title, String message, boolean displayRetryButton) {
        if (ivError != null) {
            if (image != null) {
                ivError.setImageDrawable(image);
                ivError.setVisibility(VISIBLE);
            } else {
                ivError.setVisibility(GONE);
            }
        }
        if (tvErrorTitle != null) {
            if (title != null) {
                tvErrorTitle.setText(title);
                tvErrorTitle.setVisibility(VISIBLE);
            } else {
                tvErrorTitle.setVisibility(GONE);
            }
        }
        if(tvErrorMessage != null && message != null) {
            tvErrorMessage.setText(message);
        }
        if (btnRetry != null) {
            if (displayRetryButton) {
                btnRetry.setVisibility(VISIBLE);
            } else {
                btnRetry.setVisibility(GONE);
            }
        }
        displayLayoutAndNotifyListener(errorView, LayoutState.ERROR);
    }

    @Deprecated
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

    private void displayLayoutAndNotifyListener(View view, @LayoutState int layoutState) {
        if (view != null && indexOfChild(view) != -1) {
            setDisplayedChild(indexOfChild(view));
            if (onLayoutStateChangeListener != null) {
                onLayoutStateChangeListener.onLayoutStateChanged(layoutState);
            }
        }
    }

}
