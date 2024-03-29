package dev.gerlot.statefullayout.sample.custom;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.transition.AutoTransition;

import com.google.android.material.appbar.AppBarLayout;

import dev.gerlot.statefullayout.sample.R;

public class CustomLayoutFragment extends Fragment {

    public static final String TAG = CustomLayoutFragment.class.getSimpleName();

    public interface CustomLayoutFragmentListener {

        void setToolbarAsSupportActionBar(Toolbar toolbar, boolean displayHome);

        void setTitle(String title);

    }

    private CustomLayoutFragmentListener listener;

    private AppBarLayout appBarLayout;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(new AutoTransition());
            setSharedElementReturnTransition(new AutoTransition());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_custom_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appBarLayout = view.findViewById(R.id.appBarLayout);

        toolbar = view.findViewById(R.id.toolbar);
        if (listener != null) {
            listener.setToolbarAsSupportActionBar(toolbar, true);
        }

    }
}
