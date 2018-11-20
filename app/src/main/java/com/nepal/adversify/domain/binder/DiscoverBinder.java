package com.nepal.adversify.domain.binder;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ahamed.multiviewadapter.ItemBinder;
import com.ahamed.multiviewadapter.ItemViewHolder;
import com.bumptech.glide.Glide;
import com.generic.appbase.domain.dto.PreviewMerchantInfo;
import com.generic.appbase.domain.event.OnItemClickCallback;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.nepal.adversify.R;
import com.xw.repo.VectorCompatTextView;

import javax.inject.Inject;

import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import timber.log.Timber;


public class DiscoverBinder extends ItemBinder<PreviewMerchantInfo, DiscoverBinder.ViewHolder> {


    private OnItemClickCallback<PreviewMerchantInfo> onItemClickCallback;

    @Inject
    public DiscoverBinder(OnItemClickCallback<PreviewMerchantInfo> onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public ViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_discover, parent, false));
    }

    @Override
    public void bind(ViewHolder holder, PreviewMerchantInfo merchantInfo) {
        Timber.d("Attempting to bind discover card ");

        if (merchantInfo == null)
            return;

        holder.title.setText(merchantInfo.title);
        holder.address.setText(merchantInfo.address);
        holder.ratingBar.setRating(merchantInfo.rating);
        holder.distance.setText(merchantInfo.distance);
        holder.chipGroup.removeAllViews();
        if (!TextUtils.isEmpty(merchantInfo.discount)) {
            Chip discountChip = new Chip(holder.itemView.getContext());
            discountChip.setText(merchantInfo.discount);
            discountChip.setChipBackgroundColorResource(R.color.green);
            discountChip.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorSecondaryText));
            holder.chipGroup.addView(discountChip);
        }

        if (!TextUtils.isEmpty(merchantInfo.specialOffer)) {
            Chip deals = new Chip(holder.itemView.getContext());
            deals.setText(merchantInfo.specialOffer);
            deals.setChipBackgroundColorResource(R.color.green);
            deals.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorSecondaryText));
            holder.chipGroup.addView(deals);
        }

        if (!TextUtils.isEmpty(merchantInfo.previewImage) && merchantInfo.hasFile) {
            Glide.with(holder.itemView.getContext())
                    .load(merchantInfo.previewImage)
                    .into(holder.avatar);
            holder.progressBar.setVisibility(View.GONE);
        } else {
            if (merchantInfo.hasFile)
                holder.progressBar.setVisibility(View.VISIBLE);
            else
                holder.progressBar.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener((v) -> {
            if (onItemClickCallback != null) {
                onItemClickCallback.onItemClick(v, holder.getAdapterPosition(), merchantInfo);
            }
        });
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof PreviewMerchantInfo;
    }

    @Override
    public int getSpanSize(int maxSpanCount) {
        return 1;
    }

    static class ViewHolder extends ItemViewHolder<PreviewMerchantInfo> {

        AppCompatTextView title;
        VectorCompatTextView address;
        AppCompatRatingBar ratingBar;
        ChipGroup chipGroup;
        Chip distance;
        ImageView avatar;
        ProgressBar progressBar;

        ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            title = itemView.findViewById(R.id.title);
            address = itemView.findViewById(R.id.address);
            ratingBar = itemView.findViewById(R.id.ratingbar);
            chipGroup = itemView.findViewById(R.id.chipgroup);
            distance = itemView.findViewById(R.id.distance);
            progressBar = itemView.findViewById(R.id.avatar_progress);
        }
    }
}
