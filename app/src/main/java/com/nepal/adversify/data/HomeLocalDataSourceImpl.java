package com.nepal.adversify.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import com.generic.appbase.domain.dto.Category;
import com.nepal.adversify.R;
import com.nepal.adversify.domain.model.HomeModel;
import com.nepal.adversify.domain.model.MenuModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import io.reactivex.Flowable;
import timber.log.Timber;

@Singleton
public class HomeLocalDataSourceImpl implements HomeLocalDataSource {

    private WeakReference<Context> context;

    @Inject
    public HomeLocalDataSourceImpl(Context context) {
        this.context = new WeakReference<>(context);
        Timber.d("Initialised!");
    }

    @SuppressLint("ResourceType")
    @Override
    public LiveData<HomeModel> loadHomeData() {
        Timber.d("Attempting to load from db");
        return LiveDataReactiveStreams.fromPublisher(Flowable.fromCallable(() -> {
            Resources res = context.get().getResources();
            TypedArray homeMenus = res.obtainTypedArray(R.array.home_menus);
            int n = homeMenus.length();

            List<MenuModel> menuModelList = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                int id = homeMenus.getResourceId(i, 0);
                if (id > 0) {
                    TypedArray menuInfo = res.obtainTypedArray(id);
                    String title = menuInfo.getString(0);
                    Drawable background = menuInfo.getDrawable(1);
                    Category category = Category.valueOf(menuInfo.getString(2));

                    MenuModel menuModel = new MenuModel();
                    menuModel.id = i;
                    menuModel.title = title;
                    menuModel.background = background;
                    menuModel.category = category;
                    menuModelList.add(menuModel);

                    menuInfo.recycle();
                }

            }
            homeMenus.recycle();

            HomeModel homeModel = new HomeModel();
            homeModel.setMenuModels(menuModelList);

            return homeModel;
        }));
    }
}
