package dk.cesit.androidsampleproject.backend;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import dk.cesit.androidsampleproject.backend.endpoints.Endpoints;
import dk.cesit.androidsampleproject.backend.endpoints.MockEndpoints;
import dk.cesit.androidsampleproject.models.Item;
import dk.cesit.androidsampleproject.util.Result;

/**
 * Created by Cesit on 10/02/2017.
 */

public class BackendService extends IntentService {

    private static final String TAG = BackendService.class.getSimpleName();

    //region Static variables and methods.

    private static final String ACTION_REQUEST = "BackendService.ACTION_REQUEST";
    private static final String ARG_REQUEST_ID = "BackendService.ARG_REQUEST_ID";
    private static final String ARG_TYPE = "BackendService.ARG_TYPE";

    public static final String ACTION_RESULT = "BackendService.ACTION_RESULT";
    public static final String EXTRA_REQUEST_ID = "BackendService.EXTRA_REQUEST_ID";
    public static final String EXTRA_REQUEST_TYPE = "BackendService.EXTRA_REQUEST_TYPE";
    public static final String EXTRA_ERROR_MESSAGE = "BackendService.EXTRA_ERROR_MESSAGE";

    public enum RequestType {
        GET_ITEMS, CREATE_ITEM
    }

    // A map of request ids and objects needed for those requests.
    private static volatile HashMap<String, Object> sResourceMap = new HashMap<>();

    // Starts the IntentService.
    private static String start(Context context, RequestType type, @Nullable Object object) {
        String requestId = UUID.randomUUID().toString();
        Intent intent = new Intent(context, BackendService.class);
        intent.setAction(ACTION_REQUEST);
        intent.putExtra(ARG_REQUEST_ID, requestId);
        intent.putExtra(ARG_TYPE, type.name());
        if(object != null) {
            sResourceMap.put(requestId, object);
        }
        context.startService(intent);
        return requestId;
    }

    public static String requestItems(Context context) {
        return start(context, RequestType.GET_ITEMS, null);
    }

    public static String createItem(Context context, Item item) {
        return start(context, RequestType.CREATE_ITEM, item);
    }

    public static @Nullable BackendResult parseResultIntent(Intent intent) {
        String action = intent.getAction();
        if(action == null || !action.equals(ACTION_RESULT)) {
            return null;
        }
        return new BackendResult(intent);
    }

    //endregion

    //region Instance variables and methods.

    private static Endpoints sEndpoints = new MockEndpoints();;

    public BackendService() {
        super(BackendService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String action = intent.getAction();
        if(action == null || !action.equals(ACTION_REQUEST)) {
            Log.e(TAG, "Intent action was not ACTION_REQUEST");
            return;
        }

        String requestId = intent.getStringExtra(ARG_REQUEST_ID);
        if(requestId == null) {
            Log.e(TAG, "Intent missing ARG_REQUEST_ID");
            return;
        }

        String rawRequestType = intent.getStringExtra(ARG_TYPE);
        if(rawRequestType == null) {
            Log.e(TAG, "Intent missing ARG_TYPE");
            return;
        }

        RequestType type = null;
        try {
            type = RequestType.valueOf(rawRequestType);
        } catch (Exception e) {
            Log.e(TAG, "Could not parse ARG_TYPE: "+rawRequestType);
        }
        if(type == null) {
            return;
        }

        switch (type) {
            case GET_ITEMS:
                loadItems(requestId);
                break;
            case CREATE_ITEM:
                createItem(requestId);
                break;
        }
    }

    private void loadItems(final String requestId) {
        sEndpoints.getItems(new Result<List<Item>>() {
            @Override
            public void onSuccess(List<Item> content) {
                Cache.getInstance().mItems = content;
                broadcastSuccess(requestId, RequestType.GET_ITEMS);
            }

            @Override
            public void onFailure(String message) {
                broadcastFailure(requestId, RequestType.GET_ITEMS, message);
            }
        });
    }

    private void createItem(final String requestId) {
        Object resource = sResourceMap.remove(requestId);
        if(resource == null || !(resource instanceof Item)) {
            broadcastFailure(requestId, RequestType.CREATE_ITEM, "missing resource");
            return;
        }
        sEndpoints.createItem(((Item) resource), new Result<Item>() {
            @Override
            public void onSuccess(Item content) {
                if(Cache.getInstance().mItems == null) {
                    Cache.getInstance().mItems = new ArrayList<Item>();
                }
                Cache.getInstance().mItems.add(content);
                broadcastSuccess(requestId, RequestType.CREATE_ITEM);
            }

            @Override
            public void onFailure(String message) {
                broadcastFailure(requestId, RequestType.CREATE_ITEM, message);
            }
        });
    }

    private void broadcastSuccess(String requestId, RequestType type) {
        Intent intent = new Intent(ACTION_RESULT);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_REQUEST_TYPE, type.name());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastFailure(String requestId, RequestType type, String message) {
        Intent intent = new Intent(ACTION_RESULT);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        intent.putExtra(EXTRA_REQUEST_TYPE, type.name());
        intent.putExtra(EXTRA_ERROR_MESSAGE, message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    //endregion
}
