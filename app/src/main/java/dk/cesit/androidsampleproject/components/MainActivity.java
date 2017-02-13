package dk.cesit.androidsampleproject.components;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import dk.cesit.androidsampleproject.R;
import dk.cesit.androidsampleproject.components.items.list.ItemListFragment;
import dk.cesit.androidsampleproject.components.profile.ProfileFragment;

/**
 * Created by Cesit on 10/02/2017.
 */

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String STATE_CURRENT_TAB = "CurrentTab";

    @BindView(R.id.fragment_pager) ViewPager mFragmentViewPager;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.action_button) FloatingActionButton mActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_acitivity);
        ButterKnife.bind(this);

        if(savedInstanceState != null) {
            mFragmentViewPager.setCurrentItem(savedInstanceState.getInt(STATE_CURRENT_TAB));
        }

        MainFragmentsAdapter adapter = new MainFragmentsAdapter(getSupportFragmentManager());
        adapter.addFragment(ItemListFragment.newInstance(), getString(R.string.tab_items));
        adapter.addFragment(ProfileFragment.newInstance(), getString(R.string.tab_profile));

        mFragmentViewPager.setPageMargin(10);
        mFragmentViewPager.setPageMarginDrawable(R.color.separator);
        mFragmentViewPager.setAdapter(adapter);
        mFragmentViewPager.addOnPageChangeListener(this);
        mTabLayout.setupWithViewPager(mFragmentViewPager);

        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_cupcake);
        mTabLayout.getTabAt(0).getIcon().setTintList(getResources().getColorStateList(R.color.tab_color));
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_profile);
        mTabLayout.getTabAt(1).getIcon().setTintList(getResources().getColorStateList(R.color.tab_color));

        mActionButton.setOnClickListener(this);
    }

    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(STATE_CURRENT_TAB, mFragmentViewPager.getCurrentItem());
        super.onSaveInstanceState(bundle);
    }

    //region Interfaces

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mActionButton.show();
                break;
            case 1:
                mActionButton.hide();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) { }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onClick(View v) {
        int position = mTabLayout.getSelectedTabPosition();
        switch (position) {
            case 0:
                Toast.makeText(this, "Create new item!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //endregion
}
