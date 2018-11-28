package hu.gerlotdev.statefullayout.sample;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, MainFragment.newInstance(), MainFragment.TAG);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void setToolbarAsSupportActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
    }
}
