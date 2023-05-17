package hu.gerlotdev.statefullayout.sample.simple;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.transition.AutoTransition;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import hu.gerlotdev.statefullayout.LayoutStateChangeListener;
import hu.gerlotdev.statefullayout.StatefulLayout;
import hu.gerlotdev.statefullayout.sample.R;

public class SimpleFragment extends Fragment {

    public static final String TAG = SimpleFragment.class.getSimpleName();

    public interface SimpleFragmentListener {

        void setToolbarAsSupportActionBar(Toolbar toolbar, boolean displayHome);

        void setTitle(String title);

    }

    private SimpleFragmentListener listener;

    private Toolbar toolbar;
    private StatefulLayout statefulLayout;

    private Spinner spLayoutState;
    private LinearLayout llSecondRow;
    private TextInputLayout tilTitle;
    private TextInputEditText etTitle;
    private CheckBox cbDisplayImage;
    private TextInputEditText etMessage;
    private CheckBox cbDisplayRetryButton;

    private final StatefulLayout.DefaultStateConfig emptyStateConfig =
            new StatefulLayout.DefaultStateConfig(StatefulLayout.LayoutState.EMPTY);
    private final StatefulLayout.DefaultStateConfig loadingStateConfig =
            new StatefulLayout.DefaultStateConfig(StatefulLayout.LayoutState.LOADING);
    private final StatefulLayout.DefaultStateConfig contentStateConfig =
            new StatefulLayout.DefaultStateConfig(StatefulLayout.LayoutState.CONTENT);

    public static SimpleFragment newInstance() {
        return new SimpleFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (SimpleFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(new AutoTransition());
        setSharedElementReturnTransition(new AutoTransition());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simple, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        if (listener != null) {
            listener.setToolbarAsSupportActionBar(toolbar, true);
        }

        llSecondRow = view.findViewById(R.id.llSecondRow);

        tilTitle = view.findViewById(R.id.tilTitle);
        etTitle = view.findViewById(R.id.etTitle);
        cbDisplayImage = view.findViewById(R.id.cbDisplayImage);

        etMessage = view.findViewById(R.id.etMessage);
        cbDisplayRetryButton = view.findViewById(R.id.cbDisplayRetryButton);

        spLayoutState = view.findViewById(R.id.spLayoutState);
        statefulLayout = view.findViewById(R.id.statefulLayout);
        statefulLayout.setInAnimation(getActivity(), android.R.anim.fade_in);

        statefulLayout.setOnLayoutStateChangeListener(new LayoutStateChangeListener() {

            @Override
            public void onLayoutStateChanged(StatefulLayout.LayoutState newLayoutState) {
                if (listener != null) {
                    String title = getResources().getString(R.string.app_name);
                    switch (newLayoutState) {
                        case EMPTY:
                            title = title + " - " + getResources().getString(R.string.empty_state);
                            break;
                        case LOADING:
                            title = title + " - " + getResources().getString(R.string.loading_state);
                            break;
                        case ERROR:
                            title = title + " - " + getResources().getString(R.string.error_state);
                            break;
                        case CONTENT:
                            title = title + " - " + getResources().getString(R.string.content_state);
                            break;
                        default:
                            break;
                    }

                    listener.setTitle(title);
                }

            }
        });

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.layout_states, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLayoutState.setAdapter(spinnerAdapter);
        spLayoutState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onLayoutStateSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onLayoutStateSelected(spLayoutState.getSelectedItemPosition());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cbDisplayImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onLayoutStateSelected(spLayoutState.getSelectedItemPosition());
            }
        });

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onLayoutStateSelected(spLayoutState.getSelectedItemPosition());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cbDisplayRetryButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onLayoutStateSelected(spLayoutState.getSelectedItemPosition());
            }
        });
    }

    private void onLayoutStateSelected(int position) {
        if (position != StatefulLayout.LayoutState.CONTENT.index) {
            llSecondRow.setVisibility(View.VISIBLE);
            if (position == StatefulLayout.LayoutState.ERROR.index) {
                tilTitle.setVisibility(View.VISIBLE);
                cbDisplayImage.setVisibility(View.VISIBLE);
                cbDisplayRetryButton.setVisibility(View.VISIBLE);
            } else {
                tilTitle.setVisibility(View.GONE);
                cbDisplayRetryButton.setVisibility(View.GONE);
                if (position == StatefulLayout.LayoutState.EMPTY.index) {
                    cbDisplayImage.setVisibility(View.VISIBLE);
                } else {
                    cbDisplayImage.setVisibility(View.GONE);
                }
            }
        } else {
            llSecondRow.setVisibility(View.GONE);
        }

        switch (position) {
            case StatefulLayout.EMPTY_STATE_INDEX:
                showEmpty(cbDisplayImage.isChecked());
                break;
            case StatefulLayout.LOADING_STATE_INDEX:
                String message;
                if (etMessage.getText() != null && !etMessage.getText().toString().isEmpty()) {
                    message = etMessage.getText().toString();
                } else {
                    message = getString(R.string.loading);
                }
                statefulLayout.applyStateConfig(loadingStateConfig.withMessage(message));
                break;
            case StatefulLayout.ERROR_STATE_INDEX:
                showError(cbDisplayImage.isChecked(), cbDisplayRetryButton.isChecked());
                break;
            default:
                statefulLayout.applyStateConfig(contentStateConfig);
                break;
        }
    }

    private void showEmpty(boolean displayImage) {
        Drawable image = null;
        String message;
        if (displayImage) {
            image = getResources().getDrawable(R.drawable.empty_icon);
        }
        if (etMessage.getText() != null && !etMessage.getText().toString().isEmpty()) {
            message = etMessage.getText().toString();
        } else {
            message = getString(R.string.empty);
        }
        statefulLayout.applyStateConfig(emptyStateConfig
                .withImage(image)
                .withMessage(message));
    }

    private void showError(boolean displayImage, boolean displayRetryButton) {
        String title = null;
        String message = null;
        Drawable errorImage = null;
        if (displayImage) {
            errorImage = getResources().getDrawable(R.drawable.error_icon);
        }
        if (etTitle.getText() != null && !etTitle.getText().toString().isEmpty()) {
            title = etTitle.getText().toString();
        }
        if (etMessage.getText() != null && !etMessage.getText().toString().isEmpty()) {
            message = etMessage.getText().toString();
        } else {
            message = getString(R.string.error);
        }
        if (spLayoutState.getSelectedItemPosition() == StatefulLayout.LayoutState.ERROR.index) {
            StatefulLayout.DefaultStateConfig configuredErrorStateConfig =
                    new StatefulLayout.DefaultStateConfig(StatefulLayout.LayoutState.ERROR)
                            .withImage(errorImage)
                            .withTitle(title)
                            .withMessage(message);
            if (displayRetryButton) {
                configuredErrorStateConfig = configuredErrorStateConfig.withButton(getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.retrying), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            statefulLayout.applyStateConfig(configuredErrorStateConfig);
        }
    }

}
