package dk.cesit.androidsampleproject.backend.endpoints;

import java.util.List;

import dk.cesit.androidsampleproject.models.Item;
import dk.cesit.androidsampleproject.util.Result;

/**
 * Created by Cesit on 10/02/2017.
 */

public interface Endpoints {
    public void getItems(Result<List<Item>> result);
    public void createItem(Item item, Result<Item> result);
}
