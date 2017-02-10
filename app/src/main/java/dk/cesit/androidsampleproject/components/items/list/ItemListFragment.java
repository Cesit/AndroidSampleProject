package dk.cesit.androidsampleproject.components.items.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import dk.cesit.androidsampleproject.R;
import dk.cesit.androidsampleproject.components.items.detail.ItemDetailActivity;
import dk.cesit.androidsampleproject.components.items.detail.ItemDetailViewModel;
import dk.cesit.androidsampleproject.models.Item;

/**
 * Created by Cesit on 10/02/2017.
 */

public class ItemListFragment extends Fragment implements ItemAdapter.Callbacks, SwipeRefreshLayout.OnRefreshListener, ItemListViewModel.StateListener {

    public static ItemListFragment newInstance() {
        return new ItemListFragment();
    }

    private ItemListViewModel mViewModel;
    private ItemAdapter mAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_list_fragment, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ItemListViewModel();

        mAdapter = new ItemAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mViewModel.setStateListener(this);
        mViewModel.load(getActivity(), true);
    }

    @Override
    public void onPause() {
        mSwipeRefreshLayout.setOnRefreshListener(null);
        mViewModel.setStateListener(null);
        super.onPause();
    }

    //region Interfaces

    @Override
    public void onItemClicked(int position, Item item) {
        ItemDetailActivity.launch(getActivity(), item.id);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartLoading() {
        Toast.makeText(getActivity(), R.string.loading, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemsLoaded(List<Item> items) {
        mAdapter.setItems(items);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    //endregion
}
