package dev.gerlot.statefullayout.sample;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import java.util.Map;

import dev.gerlot.statefullayout.sample.custom.CustomLayoutFragment;
import dev.gerlot.statefullayout.sample.simple.SimpleFragment;

public class MainActivity extends AppCompatActivity
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setToolbarAsSupportActionBar(Toolbar toolbar, boolean displayHome) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(displayHome);
            getSupportActionBar().setDisplayShowHomeEnabled(displayHome);
        }
    }

    @Override
    public void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void showSimple(Map<String, View> sharedElements) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (String key : sharedElements.keySet()) {
            final View sharedElement = sharedElements.get(key);
            if (sharedElement != null) {
                fragmentTransaction.addSharedElement(sharedElement, key);
            }
        }
        fragmentTransaction
                .addToBackStack(SimpleFragment.TAG)
                .replace(R.id.fragment_container, SimpleFragment.newInstance(), SimpleFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void showCustom(Map<String, View> sharedElements) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (String key : sharedElements.keySet()) {
            final View sharedElement = sharedElements.get(key);
            if (sharedElement != null) {
                fragmentTransaction.addSharedElement(sharedElement, key);
            }
        }
        fragmentTransaction
                .addToBackStack(CustomLayoutFragment.TAG)
                .replace(R.id.fragment_container, CustomLayoutFragment.newInstance(), CustomLayoutFragment.TAG);
        fragmentTransaction.commit();
    }


}
