package dk.cesit.androidsampleproject.components.items.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import dk.cesit.androidsampleproject.R;
import dk.cesit.androidsampleproject.models.Item;

/**
 * Created by Cesit on 10/02/2017.
 */

public class ItemDetailActivity extends AppCompatActivity implements ItemDetailViewModel.StateListener {

    private static final String TAG = ItemDetailActivity.class.getSimpleName();

    private static final String ARG_ID = "ItemDetailActivity.ARG_ID";

    public static void launch(Context context, String itemId) {
        Intent intent = new Intent(context, ItemDetailActivity.class);
        intent.putExtra(ARG_ID, itemId);
        context.startActivity(intent);
    }

    private String mItemId;

    private ItemDetailViewModel mViewModel;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.text_id) TextView mIdView;
    @BindView(R.id.text_title) TextView mTitleView;

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail_activity);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(null);

        mItemId = getIntent().getStringExtra(ARG_ID);
        if(mItemId == null) { Log.e(TAG, "Missing ARG_ID"); return; }

        mViewModel = new ItemDetailViewModel(mItemId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.setStateListener(this);
        mViewModel.load(this, true);
    }

    @Override
    protected void onPause() {
        mViewModel.setStateListener(null);
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //region Interfaces

    @NonNull
    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onStartLoading() {
        mIdView.setText(null);
        mTitleView.setText(R.string.loading);
        mActionBar.setTitle(null);
    }

    @Override
    public void onItemLoaded(Item item) {
        mIdView.setText(item.id);
        mTitleView.setText(item.title);
        mActionBar.setTitle(item.title);
    }

    @Override
    public void onError(String message) {
        mIdView.setText(null);
        mTitleView.setText(message);
        mActionBar.setTitle(null);
    }

    //endregion
}
