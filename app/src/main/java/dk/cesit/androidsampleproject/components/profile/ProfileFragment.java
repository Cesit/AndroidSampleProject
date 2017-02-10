package dk.cesit.androidsampleproject.components.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import dk.cesit.androidsampleproject.R;
import dk.cesit.androidsampleproject.components.items.list.ItemListFragment;

/**
 * Created by Cesit on 10/02/2017.
 */

public class ProfileFragment extends Fragment {

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
