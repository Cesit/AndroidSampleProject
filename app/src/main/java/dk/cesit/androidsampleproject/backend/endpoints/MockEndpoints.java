package dk.cesit.androidsampleproject.backend.endpoints;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import dk.cesit.androidsampleproject.models.Item;
import dk.cesit.androidsampleproject.util.Result;

/**
 * Created by Cesit on 10/02/2017.
 */

public class MockEndpoints implements Endpoints {

    private static int MOCK_DELAY = 2000;

    private Timer mMockDelayTimer = new Timer();

    private List<Item> mItems;

    public MockEndpoints() {
        List<Item> items = new ArrayList<Item>();
        items.add(new Item(UUID.randomUUID().toString(), "Item 1"));
        items.add(new Item(UUID.randomUUID().toString(), "Item 2"));
        items.add(new Item(UUID.randomUUID().toString(), "Item 3"));
        items.add(new Item(UUID.randomUUID().toString(), "Item 4"));
        mItems = items;
    }

    @Override
    public void getItems(final Result<List<Item>> result) {
        this.execute(new TimerTask() {
            @Override
            public void run() {
                result.onSuccess(mItems);
            }
        });
    }

    @Override
    public void createItem(Item item, Result<Item> result) {
        if(didEncounterRandomError()) {
            result.onFailure("A random error occurred!");
            return;
        }

        Item createdItem = new Item(UUID.randomUUID().toString(), item.title);
        mItems.add(createdItem);
        result.onSuccess(createdItem);
    }

    private void execute(TimerTask task) {
        mMockDelayTimer.schedule(task, MOCK_DELAY);
    }

    private boolean didEncounterRandomError() {
        return new Random().nextInt(3) + 1 > 1;
    }
}
