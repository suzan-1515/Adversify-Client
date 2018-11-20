package com.nepal.adversify.domain.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ahamed.multiviewadapter.ItemBinder;
import com.ahamed.multiviewadapter.ItemViewHolder;
import com.bumptech.glide.Glide;
import com.generic.appbase.domain.event.OnItemClickCallback;
import com.nepal.adversify.R;
import com.nepal.adversify.domain.model.MenuModel;

import javax.inject.Inject;

import androidx.appcompat.widget.AppCompatTextView;
import timber.log.Timber;


public class MenuBinder extends ItemBinder<MenuModel, MenuBinder.ViewHolder> {


    private OnItemClickCallback<MenuModel> onItemClickCallback;

    @Inject
    public MenuBinder(OnItemClickCallback<MenuModel> onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public ViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_home_menu, parent, false));
    }

    @Override
    public void bind(ViewHolder holder, MenuModel menuModel) {
        Timber.d("Attempting to bind menu card data to home");

        if (menuModel == null)
            return;

        holder.title.setText(menuModel.title);
        Glide.with(holder.itemView.getContext())
                .load(menuModel.background)
                .into(holder.background);

        holder.itemView.setOnClickListener((v) -> {
            if (onItemClickCallback != null) {
                onItemClickCallback.onItemClick(v, holder.getAdapterPosition(), menuModel);
            }
        });
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof MenuModel;
    }

    @Override
    public int getSpanSize(int maxSpanCount) {
        return 1;
    }

    static class ViewHolder extends ItemViewHolder<MenuModel> {

        AppCompatTextView title;
        ImageView background;

        ViewHolder(View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.img_background);
            title = itemView.findViewById(R.id.title);
        }
    }
}
