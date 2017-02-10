package dk.cesit.androidsampleproject.components.items.detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import dk.cesit.androidsampleproject.backend.BackendResult;
import dk.cesit.androidsampleproject.backend.BackendService;
import dk.cesit.androidsampleproject.backend.Session;
import dk.cesit.androidsampleproject.models.Item;

/**
 * Created by Cesit on 10/02/2017.
 */

public class ItemDetailViewModel {

    private static final String TAG = ItemDetailViewModel.class.getSimpleName();

    public interface StateListener {
        public @NonNull Context getContext();
        public void onStartLoading();
        public void onItemLoaded(Item item);
        public void onError(String message);
    }

    private StateListener mListener;
    private @NonNull String mItemId;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BackendResult result = BackendService.parseResultIntent(intent);

            if(result == null) { Log.w(TAG, "Received a null result"); return; }

            if(result.errorMessage != null) {
                mListener.onError(result.errorMessage);
                return;
            }

            if(result.requestType == BackendService.RequestType.GET_ITEMS) {
                Item item = Session.getInstance().getCachedItem(mItemId);
                if(item == null) {
                    mListener.onError("Could not load item");
                    return;
                }
                mListener.onItemLoaded(item);
            }
            else {
                Log.d(TAG, "Received unknown result type: "+result.requestType);
            }
        }
    };

    public ItemDetailViewModel(@NonNull String itemId) {
        mItemId = itemId;
    }

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
        Item item = Session.getInstance().getCachedItem(mItemId);
        if(!allowCachedState || item == null) {
            mListener.onStartLoading();
            BackendService.requestItems(context);
        }
        else {
            mListener.onItemLoaded(item);
        }
    }
}
