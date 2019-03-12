package hu.gerlotdev.statefullayout.sample.custom;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.gerlotdev.statefullayout.sample.R;

public class CustomLayoutFragment extends Fragment {

    public static final String TAG = CustomLayoutFragment.class.getSimpleName();

    public interface CustomLayoutFragmentListener {

        void setToolbarAsSupportActionBar(Toolbar toolbar);

        void setTitle(String title);

    }

    private CustomLayoutFragmentListener listener;

    private Toolbar toolbar;

    public static CustomLayoutFragment newInstance() {
        return new CustomLayoutFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (CustomLayoutFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_custom_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        if (listener != null) {
            listener.setToolbarAsSupportActionBar(toolbar);
        }

    }
}
