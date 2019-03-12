package hu.gerlotdev.statefullayout.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NavigationFragment extends Fragment {

    public static final String TAG = NavigationFragment.class.getSimpleName();

    public interface NavigationFragmentListener {

        void setToolbarAsSupportActionBar(Toolbar toolbar);

        void setTitle(String title);

        void showSimple();

        void showCustom();

    }

    private NavigationFragmentListener listener;

    private Toolbar toolbar;
    private Button simpleBtn;
    private Button customBtn;

    public static NavigationFragment newInstance() {
        return new NavigationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listener.setTitle(getString(R.string.app_name));

        toolbar = view.findViewById(R.id.toolbar);
        if (listener != null) {
            listener.setToolbarAsSupportActionBar(toolbar);
        }

        simpleBtn = view.findViewById(R.id.simpleBtn);
        simpleBtn.setOnClickListener(simpleBtnClickListener);

        customBtn = view.findViewById(R.id.customBtn);
        customBtn.setOnClickListener(customBtnClickListener);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (NavigationFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    View.OnClickListener simpleBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.showSimple();
            }
        }
    };

    View.OnClickListener customBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.showCustom();
            }
        }
    };

}
