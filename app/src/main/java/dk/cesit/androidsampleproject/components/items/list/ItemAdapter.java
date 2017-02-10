package dk.cesit.androidsampleproject.components.items.list;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import dk.cesit.androidsampleproject.components.common.OnListElementClickListener;
import dk.cesit.androidsampleproject.models.Item;

/**
 * Created by Cesit on 10/02/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    public interface Callbacks {
        void onItemClicked(int position, Item item);
    }

    private Callbacks mCallbacks;
    private List<Item> mItems;

    private final OnListElementClickListener mClickListener = new OnListElementClickListener() {
        @Override
        public void onElementClick(int position) {
            Item item = mItems.get(position);
            if(mCallbacks != null) {
                mCallbacks.onItemClicked(position, item);
            }
        }

        @Override
        public boolean onElementLongClick(int position) {
            return false;
        }
    };

    public ItemAdapter(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    public void setItems(List<Item> items) {
        this.mItems = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(parent, mClickListener);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bind(position, mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }
}
