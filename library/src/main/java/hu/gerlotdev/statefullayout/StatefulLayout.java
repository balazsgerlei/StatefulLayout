package hu.gerlotdev.statefullayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class StatefulLayout extends ViewFlipper {

    public static final int EMPTY_STATE_INDEX = 0;
    public static final int LOADING_STATE_INDEX = 1;
    public static final int ERROR_STATE_INDEX = 2;
    public static final int CONTENT_STATE_INDEX = 3;

    public enum LayoutState {
        EMPTY(EMPTY_STATE_INDEX),
        LOADING(LOADING_STATE_INDEX),
        ERROR(ERROR_STATE_INDEX),
        CONTENT(CONTENT_STATE_INDEX);

        public final int index;

        LayoutState(final int index) {
            this.index = index;
        }

        public static LayoutState fromIndex(final int index) {
            switch (index) {
                case EMPTY_STATE_INDEX: return LayoutState.EMPTY;
                case LOADING_STATE_INDEX: return LayoutState.LOADING;
                case ERROR_STATE_INDEX: return LayoutState.ERROR;
                default: return LayoutState.CONTENT;
            }
        }

    }

    private LayoutStateChangeListener onLayoutStateChangeListener;

    private LayoutState layoutState = LayoutState.CONTENT;

    private final LayoutInflater layoutInflater;

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

        public LayoutConfig withEmptyView(final int emptyViewResId) {
            if (emptyViewResId == -1) {
                return this;
            }
            if (emptyView != null) {
                emptyView = null;
            }
            this.emptyViewResId = emptyViewResId;
            return this;
        }

        public LayoutConfig withEmptyView(final View emptyView) {
            if (emptyViewResId != -1) {
                emptyViewResId = -1;
            }
            this.emptyView = emptyView;
            return this;
        }

        public LayoutConfig withLoadingView(final int loadingViewResId) {
            if (loadingViewResId == -1) {
                return this;
            }
            if (loadingView != null) {
                loadingView = null;
            }
            this.loadingViewResId = loadingViewResId;
            return this;
        }

        public LayoutConfig withLoadingView(final View loadingView) {
            if (loadingViewResId != -1) {
                loadingViewResId = -1;
            }
            this.loadingView = loadingView;
            return this;
        }

        public LayoutConfig withErrorView(final int errorViewResId) {
            if (errorViewResId == -1) {
                return this;
            }
            if (errorView != null) {
                errorView = null;
            }
            this.errorViewResId = errorViewResId;
            return this;
        }

        public LayoutConfig withErrorView(final View errorView) {
            if (errorViewResId != -1) {
                errorViewResId = -1;
            }
            this.errorView = errorView;
            return this;
        }

    }

    public interface StateConfig {

        void configureLayout(final StatefulLayout layout);

    }

    public static class DefaultStateConfig implements StateConfig {

        protected final LayoutState layoutState;
        protected String title;
        protected String message;
        protected Drawable image;
        protected String buttonTitle;
        protected OnClickListener buttonClickListener;

        public DefaultStateConfig(final LayoutState layoutState) {
            this.layoutState = layoutState;
        }

        public DefaultStateConfig withTitle(final String title) {
            this.title = title;
            return this;
        }

        public DefaultStateConfig withMessage(final String message) {
            this.message = message;
            return this;
        }

        public DefaultStateConfig withImage(final Drawable image) {
            this.image = image;
            return this;
        }

        public DefaultStateConfig withButton(final String buttonTitle, final OnClickListener listener) {
            this.buttonTitle = buttonTitle;
            this.buttonClickListener = listener;
            return this;
        }

        // TODO assert correct config!
        @Override
        public void configureLayout(final StatefulLayout layout) {
            switch (layoutState) {
                case EMPTY:
                    showEmpty(layout);
                    break;
                case LOADING:
                    showLoading(layout);
                    break;
                case ERROR:
                    showError(layout);
                    break;
                default:
                    showContent(layout);
                    break;
            }
        }

        protected void showContent(final StatefulLayout layout) {
            layout.displayLayoutAndNotifyListener(layout.contentView, LayoutState.CONTENT);
        }

        protected void showEmpty(final StatefulLayout layout) {
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

        protected void showLoading(final StatefulLayout layout) {
            if(layout.tvLoading != null && message != null) {
                layout.tvLoading.setText(message);
            }
            layout.displayLayoutAndNotifyListener(layout.loadingView, LayoutState.LOADING);
        }

        protected void showError(final StatefulLayout layout) {
            layout.btnRetryListener = buttonClickListener;
            if (layout.btnRetry != null) {
                if (buttonTitle != null) {
                    layout.btnRetry.setText(buttonTitle);
                    layout.btnRetry.setOnClickListener(layout.btnRetryListener);
                    layout.btnRetry.setVisibility(VISIBLE);
                } else {
                    layout.btnRetry.setOnClickListener(null);
                    layout.btnRetry.setVisibility(GONE);
                }
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

    public StatefulLayout(final Context context) {
        super(context);
        layoutInflater = LayoutInflater.from(getContext());
    }

    public StatefulLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        layoutInflater = LayoutInflater.from(getContext());

        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.StatefulLayout);

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

        int stateIndex = typedArray.getInt(R.styleable.StatefulLayout_sl_layoutState, LayoutState.CONTENT.index);
        layoutState = LayoutState.fromIndex(stateIndex);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isChildCountCorrect()) {
            throw new IllegalStateException("StatefulLayout can host exactly one direct child, specify layout for states other than content using sl_errorView, sl_loadingView, sl_emptyView xml attributes or setters");
        }

        contentView = getChildAt(LayoutState.CONTENT.index);
        switch (layoutState) {
            case EMPTY:
                showEmpty();
                break;
            case LOADING:
                showLoading();
                break;
            case ERROR:
                showError();
                break;
            default:
                showContent();
                break;
        }
    }

    public LayoutState getLayoutState() {
        return layoutState;
    }

    public void setOnLayoutStateChangeListener(final LayoutStateChangeListener listener) {
        this.onLayoutStateChangeListener = listener;
    }

    public void applyLayoutConfig(final LayoutConfig config) {
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

    public void applyStateConfig(final StateConfig stateConfig) {
        stateConfig.configureLayout(this);
    }

    @Deprecated
    public void setEmptyView(final int emptyViewResId) {
        setEmptyView(layoutInflater.inflate(emptyViewResId, this, false));
    }

    @Deprecated
    public void setEmptyView(final View emptyView) {
        this.emptyView = emptyView;
        ivEmpty = emptyView.findViewById(R.id.ivEmpty);
        tvEmpty = emptyView.findViewById(R.id.tvEmpty);
        addView(emptyView, emptyView.getLayoutParams());
    }

    @Deprecated
    public void setLoadingView(final int loadingViewResId) {
        setLoadingView(layoutInflater.inflate(loadingViewResId, this, false));
    }

    @Deprecated
    public void setLoadingView(final View loadingView) {
        this.loadingView = loadingView;
        tvLoading = loadingView.findViewById(R.id.tvLoading);
        addView(loadingView, loadingView.getLayoutParams());
    }

    @Deprecated
    public void setErrorView(final int errorViewResId) {
        setErrorView(layoutInflater.inflate(errorViewResId, this, false));
    }

    @Deprecated
    public void setErrorView(final View errorView) {
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
    public void showEmpty(final String message) {
        showEmpty(null, message);
    }

    @Deprecated
    public void showEmpty(final Drawable image, final String message) {
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
    public void showLoading(final String message) {
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
    public void showErrorWithRetryButton(final OnClickListener listener) {
        showErrorWithRetryButton(null, listener);
    }

    @Deprecated
    public void showErrorWithRetryButton(final String message, final OnClickListener listener) {
        showErrorWithRetryButton(null, null, message, listener);
    }

    @Deprecated
    public void showErrorWithRetryButton(final Drawable image, final String message, final OnClickListener listener) {
        showErrorWithRetryButton(image, null, message, listener);
    }

    @Deprecated
    public void showErrorWithRetryButton(final String title, final String message, final OnClickListener listener) {
        showErrorWithRetryButton(null, title, message, listener);
    }

    @Deprecated
    public void showErrorWithRetryButton(final Drawable image, final String title, final String message, final OnClickListener listener) {
        setRetryButtonOnClickListener(listener);
        showError(image, title, message, true);
    }

    @Deprecated
    public void showError() {
        showError(null);
    }

    @Deprecated
    public void showError(final Drawable image, final String title, final String message) {
        showError(image, title, message, false);
    }

    @Deprecated
    public void showError(final String title, final String message) {
        showError(null, title, message, false);
    }

    @Deprecated
    public void showError(final Drawable image, final String message) {
        showError(image, null, message, false);
    }

    @Deprecated
    public void showError(final String message) {
        showError(null, null, message, false);
    }

    @Deprecated
    private void showError(final Drawable image, final String title, final String message, final boolean displayRetryButton) {
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
    private void setRetryButtonOnClickListener(final OnClickListener listener) {
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

    private void displayLayoutAndNotifyListener(final View view, final LayoutState layoutState) {
        if (view != null && indexOfChild(view) != -1) {
            setDisplayedChild(indexOfChild(view));
            if (onLayoutStateChangeListener != null) {
                onLayoutStateChangeListener.onLayoutStateChanged(layoutState);
            }
        }
    }

}
