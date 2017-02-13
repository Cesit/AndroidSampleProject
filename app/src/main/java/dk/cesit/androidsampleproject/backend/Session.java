package dk.cesit.androidsampleproject.backend;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dk.cesit.androidsampleproject.models.Item;

/**
 * Created by Cesit on 10/02/2017.<br>
 * Through Session, objects can access the latest known state of the app.<br>
 * Session reads the state from {@link Cache} which can only be changed by {@link BackendService}.<br>
 * Cache + Session could be replaced by a database like SQLite or Realm.<br>
 */

public class Session {

    private static final String TAG = Session.class.getSimpleName();

    private static Session sInstance;

    public static Session getInstance() {
        if(sInstance == null) {
            sInstance = new Session();
        }
        return sInstance;
    }

    public @NonNull List<Item> getCachedItems() {
        List<Item> items = Cache.getInstance().mItems;
        if(items == null) {
            Log.d(TAG, "Items cache was empty!");
            return new ArrayList<>();
        }
        return items;
    }

    public @Nullable Item getCachedItem(String id) {
        List<Item> items = getCachedItems();
        for(Item item : items) {
            Log.d(TAG, item.id+" == "+id);
            if(item.id.equals(id)) {
                return item;
            }
        }
        return null;
    }
}
