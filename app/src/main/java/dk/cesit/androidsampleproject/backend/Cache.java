package dk.cesit.androidsampleproject.backend;

import java.util.List;

import dk.cesit.androidsampleproject.models.Item;

/**
 * Created by Cesit on 10/02/2017.<br>
 * Cache contains the latest known state of the app for this user.<br>
 * Only objects in this package can edit the content of Cache (i.e. {@link BackendService}).<br>
 * Other objects in the app can access the cached state through {@link Session}.<br>
 * Cache + Session could be replaced by a database like SQLite or Realm.<br>
 */

class Cache {

    private static Cache sInstance;

    static Cache getInstance() {
        if(sInstance == null) {
            sInstance = new Cache();
        }
        return sInstance;
    }

    volatile List<Item> mItems;
}
