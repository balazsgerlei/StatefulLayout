package hu.gerlotdev.statefullayout.sample;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import hu.gerlotdev.statefullayout.StatefulLayout;

public class MainFragment extends Fragment {

    public static final String TAG = MainFragment.class.getSimpleName();

    public interface MainFragmentListener {

        void setToolbarAsSupportActionBar(Toolbar toolbar);

    }

    private MainFragmentListener listener;

    private Toolbar toolbar;
    private StatefulLayout statefulLayout;

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

        statefulLayout = view.findViewById(R.id.statefulLayout);
        statefulLayout.showErrorWithRetryButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), getResources().getString(R.string.retrying), Toast.LENGTH_SHORT).show();
            }
        });
        // statefulLayout.showError("ErrorRRR!");
        /*statefulLayout.setErrorView(R.layout.layout_error);
        statefulLayout.showError();
        statefulLayout.setLoadingView(R.layout.layout_loading);
        statefulLayout.showLoading();
        statefulLayout.setEmptyView(R.layout.layout_empty);
        statefulLayout.showEmpty();*/
    }
}
