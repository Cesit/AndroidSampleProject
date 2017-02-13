package dk.cesit.androidsampleproject.components.items.list;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.List;

import dk.cesit.androidsampleproject.backend.BackendResult;
import dk.cesit.androidsampleproject.backend.BackendService;
import dk.cesit.androidsampleproject.backend.Session;
import dk.cesit.androidsampleproject.components.items.detail.ItemDetailViewModel;
import dk.cesit.androidsampleproject.models.Item;

/**
 * Created by Cesit on 10/02/2017.
 */

public class ItemListViewModel {

    private static final String TAG = ItemListViewModel.class.getSimpleName();

    public interface StateListener {
        public @NonNull Context getContext();
        public void onStartLoading();
        public void onItemsLoaded(List<Item> items);
        public void onError(String message);
    }

    private StateListener mListener;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BackendResult result = BackendService.parseResultIntent(intent);

            if(result == null) { Log.w(TAG, "Received a null result"); return; }

            if(result.errorMessage != null) {
                mListener.onError(result.errorMessage);
                return;
            }

            if(result.requestType == BackendService.RequestType.GET_ITEMS ||
                    result.requestType == BackendService.RequestType.CREATE_ITEM) {
                mListener.onItemsLoaded(Session.getInstance().getCachedItems());
            }
            else {
                Log.d(TAG, "Received unknown result type: "+result.requestType);
            }
        }
    };

    public void setStateListener(StateListener listener) {
        if(mListener != null) {
            LocalBroadcastManager.getInstance(mListener.getContext())
                    .unregisterReceiver(mBroadcastReceiver);
        }

        if(listener != null) {
            LocalBroadcastManager.getInstance(listener.getContext())
                    .registerReceiver(mBroadcastReceiver, new IntentFilter(BackendService.ACTION_RESULT));
        }

        mListener = listener;
    }

    // Will try to load state.
    // Listen for changes with StateListener.
    // Will do nothing if StateListener is not set.
    public void load(Context context, boolean allowCachedState) {
        if(mListener == null) { Log.w(TAG, "Called load without setting StateListener"); return; }

        // Look for cached result.
        List<Item> items = Session.getInstance().getCachedItems();
        if(!allowCachedState || items.size() == 0) {
            mListener.onStartLoading();
            BackendService.requestItems(context);
        }
        else {
            mListener.onItemsLoaded(items);
        }
    }

    public void createItem(Context context, String title) {
        Item item = new Item("new", title);
        BackendService.createItem(context, item);
    }
}
