package com.nepal.adversify.ui.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.generic.appbase.domain.event.OnItemClickCallback;
import com.generic.appbase.ui.BaseFragment;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.nepal.adversify.R;
import com.nepal.adversify.data.prefs.PreferenceHelper;
import com.nepal.adversify.domain.adapter.HomeAdapter;
import com.nepal.adversify.domain.model.HomeModel;
import com.nepal.adversify.domain.model.MenuModel;
import com.nepal.adversify.ui.discover.DiscoverFragment;
import com.nepal.adversify.viewmodel.ClientViewModel;
import com.nepal.adversify.viewmodel.ClientViewModelFactory;
import com.nepal.adversify.viewmodel.HomeViewModel;
import com.nepal.adversify.viewmodel.HomeViewModelFactory;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements OnItemClickCallback<MenuModel> {

    @Inject
    HomeViewModelFactory mHomeViewModelFactory;
    @Inject
    ClientViewModelFactory mClientViewModelFactory;
    @Inject
    HomeAdapter mHomeAdapter;
    @Inject
    PreferenceHelper mPrefs;

    //VIews
    private AppCompatTextView mClientNameTextView;
    private CircularImageView mAvatar;

    private ClientViewModel mClientViewModel;
    private HomeViewModel mHomeViewModel;
    private Observer<HomeModel> homeModelObserver = (data) -> {
        if (data != null) {
            Timber.d("Total menus: %d", data.getMenuModels().size());
            mHomeAdapter.setData(data);
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_home;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mClientViewModel = ViewModelProviders.of(getActivity(), mClientViewModelFactory).get(ClientViewModel.class);
        mHomeViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), mHomeViewModelFactory).get(HomeViewModel.class);
        observeData();
        loadData();
    }

    @Override
    protected View onViewReady(View view, Bundle savedInstanceState) {

        Toolbar mToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
        NavigationUI.setupWithNavController(mToolbar, Navigation.findNavController(view));

        mClientNameTextView = view.findViewById(R.id.name);
        mAvatar = view.findViewById(R.id.avatar);

        mAvatar.setOnClickListener((v) -> {
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_profileFragment);
        });

        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mHomeAdapter.setSpanCount(2);
        gridLayoutManager.setSpanCount(2);
        gridLayoutManager.setSpanSizeLookup(mHomeAdapter.getSpanSizeLookup());

        mRecyclerView.setAdapter(mHomeAdapter);

        if (mPrefs.isHomeFirstLoad()) {
            Timber.d("Home first load");
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileSetupFragment);
            return view;
        }

        return view;
    }

    private void loadData() {
        Timber.d("loadData");
        mHomeViewModel.loadHomeMenus();
        mClientViewModel.loadClientData();
    }

    private void observeData() {
        Timber.d("Observing livedata");
        mHomeViewModel.getHomeModelLiveData().observeForever(homeModelObserver);
        mClientViewModel.getClientLiveData().observe(this, data -> {
            if (data != null) {
                Timber.d("Client display name: %s, avatar:%s", data.name, data.avatar);
                mClientNameTextView.setText(data.name);
                if (!TextUtils.isEmpty(data.avatar)) {
                    Glide.with(getContext())
                            .load(data.avatar)
                            .into(mAvatar);
                }
            }
        });
    }

    @Override
    public void onItemClick(View v, int position, MenuModel menuModel) {
        Timber.d("onItemClick: position %d: id-%d, title:%s", position, menuModel.id, menuModel.title);
        Bundle bundle = new Bundle();
        bundle.putInt(DiscoverFragment.EXTRA_DISCOVER_CATEGORY_ID, menuModel.id);
        bundle.putString(DiscoverFragment.EXTRA_DISCOVER_CATEGORY_TITLE, menuModel.title);

        Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_discoverFragment, bundle);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
