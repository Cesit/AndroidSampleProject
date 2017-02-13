package dk.cesit.androidsampleproject.components.items.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dk.cesit.androidsampleproject.R;
import dk.cesit.androidsampleproject.components.common.OnListElementClickListener;
import dk.cesit.androidsampleproject.models.Item;

/**
 * Created by Cesit on 10/02/2017.
 */

public class ItemViewHolder extends RecyclerView.ViewHolder {

    private int mPosition;

    @BindView(R.id.text_title) TextView mTitleView;

    public ItemViewHolder(ViewGroup parent, final OnListElementClickListener clickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_element, parent, false));
        ButterKnife.bind(this, super.itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onElementClick(mPosition);
            }
        });
    }

    public void bind(int position, Item item) {
        mPosition = position;
        mTitleView.setText(item.title);
    }
}
