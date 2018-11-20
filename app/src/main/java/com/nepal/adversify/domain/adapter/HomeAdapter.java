package com.nepal.adversify.domain.adapter;

import com.ahamed.multiviewadapter.DataListManager;
import com.ahamed.multiviewadapter.RecyclerAdapter;
import com.ahamed.multiviewadapter.util.PayloadProvider;
import com.nepal.adversify.domain.binder.MenuBinder;
import com.nepal.adversify.domain.model.HomeModel;
import com.nepal.adversify.domain.model.MenuModel;

import javax.inject.Inject;

import timber.log.Timber;

public class HomeAdapter extends RecyclerAdapter {

    private DataListManager<MenuModel> menuModelDataListManager;

    @Inject
    public HomeAdapter(MenuBinder menuBinder) {
        menuModelDataListManager = new DataListManager<>(this, new PayloadProvider<MenuModel>() {
            @Override
            public boolean areContentsTheSame(MenuModel oldItem, MenuModel newItem) {
                Timber.d("same content");
                return true;
            }

            @Override
            public Object getChangePayload(MenuModel oldItem, MenuModel newItem) {
                return null;
            }
        });

        addDataManager(menuModelDataListManager);

        registerBinder(menuBinder);
    }

    public void setData(HomeModel homeModel) {
        menuModelDataListManager.set(homeModel.getMenuModels());
    }

}
