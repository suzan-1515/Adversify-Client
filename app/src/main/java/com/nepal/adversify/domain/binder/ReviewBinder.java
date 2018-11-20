package com.nepal.adversify.domain.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahamed.multiviewadapter.ItemBinder;
import com.ahamed.multiviewadapter.ItemViewHolder;
import com.generic.appbase.domain.dto.ReviewInfo;
import com.nepal.adversify.R;

import javax.inject.Inject;

import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import timber.log.Timber;


public class ReviewBinder extends ItemBinder<ReviewInfo, ReviewBinder.ViewHolder> {


    @Inject
    public ReviewBinder() {
    }

    @Override
    public ViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_review, parent, false));
    }

    @Override
    public void bind(ViewHolder holder, ReviewInfo reviewInfo) {
        Timber.d("Attempting to bind review item ");

        if (reviewInfo == null)
            return;

        holder.name.setText(reviewInfo.clientName);
        holder.content.setText(reviewInfo.content);
        holder.ratingBar.setRating(reviewInfo.star);

    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof ReviewInfo;
    }

    @Override
    public int getSpanSize(int maxSpanCount) {
        return 1;
    }

    static class ViewHolder extends ItemViewHolder<ReviewInfo> {

        AppCompatTextView name;
        AppCompatRatingBar ratingBar;
        AppCompatTextView content;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.client_name);
            ratingBar = itemView.findViewById(R.id.ratingbar);
            content = itemView.findViewById(R.id.review);
        }
    }
}
