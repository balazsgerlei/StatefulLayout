package hu.gerlotdev.statefullayout.sample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
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

import hu.gerlotdev.statefullayout.LayoutStateChangeListener;
import hu.gerlotdev.statefullayout.StatefulLayout;

public class MainFragment extends Fragment {

    public static final String TAG = MainFragment.class.getSimpleName();

    public interface MainFragmentListener {

        void setToolbarAsSupportActionBar(Toolbar toolbar);

        void setTitle(String title);

    }

    private MainFragmentListener listener;

    private Toolbar toolbar;
    private StatefulLayout statefulLayout;

    private Spinner spLayoutState;
    private LinearLayout llSecondRow;
    private TextInputLayout tilTitle;
    private TextInputEditText etTitle;
    private CheckBox cbDisplayImage;
    private TextInputEditText etMessage;
    private CheckBox cbDisplayRetryButton;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (MainFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        if (listener != null) {
            listener.setToolbarAsSupportActionBar(toolbar);
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
            public void onLayoutStateChanged(int newLayoutState) {
                if (listener != null) {
                    String title = getResources().getString(R.string.app_name);
                    switch (newLayoutState) {
                        case StatefulLayout.LayoutState.EMPTY:
                            title = title + " - " + getResources().getString(R.string.empty_state);
                            break;
                        case StatefulLayout.LayoutState.LOADING:
                            title = title + " - " + getResources().getString(R.string.loading_state);
                            break;
                        case StatefulLayout.LayoutState.ERROR:
                            title = title + " - " + getResources().getString(R.string.error_state);
                            break;
                        case StatefulLayout.LayoutState.CONTENT:
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
        if (position != StatefulLayout.LayoutState.CONTENT) {
            llSecondRow.setVisibility(View.VISIBLE);
            if (position == StatefulLayout.LayoutState.ERROR) {
                tilTitle.setVisibility(View.VISIBLE);
                cbDisplayImage.setVisibility(View.VISIBLE);
                cbDisplayRetryButton.setVisibility(View.VISIBLE);
            } else {
                tilTitle.setVisibility(View.GONE);
                cbDisplayRetryButton.setVisibility(View.GONE);
                if (position == StatefulLayout.LayoutState.EMPTY) {
                    cbDisplayImage.setVisibility(View.VISIBLE);
                } else {
                    cbDisplayImage.setVisibility(View.GONE);
                }
            }
        } else {
            llSecondRow.setVisibility(View.GONE);
        }

        String message = null;
        switch (position) {
            case StatefulLayout.LayoutState.EMPTY:
                showEmpty(cbDisplayImage.isChecked());
                break;
            case StatefulLayout.LayoutState.LOADING:
                if (etMessage.getText() != null && !etMessage.getText().toString().isEmpty()) {
                    message = etMessage.getText().toString();
                }
                statefulLayout.showLoading(message);
                break;
            case StatefulLayout.LayoutState.ERROR:
                showError(cbDisplayImage.isChecked(), cbDisplayRetryButton.isChecked());
                break;
            default:
                statefulLayout.showContent();
                break;
        }
    }

    private void showEmpty(boolean displayImage) {
        Drawable image = null;
        String message = null;
        if (displayImage) {
            image = getResources().getDrawable(R.drawable.empty_icon);
        }
        if (etMessage.getText() != null && !etMessage.getText().toString().isEmpty()) {
            message = etMessage.getText().toString();
        }
        statefulLayout.showEmpty(image, message);
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
        }
        if (spLayoutState.getSelectedItemPosition() == StatefulLayout.LayoutState.ERROR) {
            if (displayRetryButton) {
                statefulLayout.showErrorWithRetryButton(errorImage, title, message, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.retrying), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                statefulLayout.showError(errorImage, title, message);
            }
        }
    }

}
