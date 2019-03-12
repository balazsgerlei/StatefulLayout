package hu.gerlotdev.statefullayout.sample;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;

import hu.gerlotdev.statefullayout.sample.custom.CustomLayoutFragment;
import hu.gerlotdev.statefullayout.sample.simple.SimpleFragment;

public class SimpleActivity extends AppCompatActivity
        implements NavigationFragment.NavigationFragmentListener,
        SimpleFragment.SimpleFragmentListener,
        CustomLayoutFragment.CustomLayoutFragmentListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, NavigationFragment.newInstance(), NavigationFragment.TAG)
                    .commit();
        }
    }

    @Override
    public void setToolbarAsSupportActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
    }

    @Override
    public void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void showSimple() {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(SimpleFragment.TAG)
                .replace(R.id.fragment_container, SimpleFragment.newInstance(), SimpleFragment.TAG)
                .commit();
    }

    @Override
    public void showCustom() {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(CustomLayoutFragment.TAG)
                .replace(R.id.fragment_container, CustomLayoutFragment.newInstance(), CustomLayoutFragment.TAG)
                .commit();
    }


}
