package dk.cesit.androidsampleproject.backend;

import java.util.List;

import dk.cesit.androidsampleproject.models.Item;

/**
 * Created by Cesit on 10/02/2017.
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
