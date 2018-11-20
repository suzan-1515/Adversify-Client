package com.nepal.adversify.mapper;

import com.generic.appbase.domain.dto.PayloadData;
import com.generic.appbase.domain.dto.ReviewInfo;
import com.generic.appbase.mapper.Mapper;
import com.nepal.adversify.domain.model.ReviewModel;

import javax.inject.Inject;

public class ReviewModelToReviewInfoMapper implements Mapper<ReviewModel, ReviewInfo> {

    @Inject
    public ReviewModelToReviewInfoMapper() {
    }

    @Override
    public ReviewInfo from(ReviewModel reviewModel) {
        ReviewInfo reviewInfo = new ReviewInfo();
        reviewInfo.clientId = reviewModel.clientId;
        reviewInfo.clientName = reviewModel.clientname;
        reviewInfo.content = reviewModel.review;
        reviewInfo.star = reviewModel.rating;
        reviewInfo.hasFile = false;
        reviewInfo.dataType = PayloadData.MERCHANT_REVIEW_INFO;
        return reviewInfo;
    }

    @Override
    public ReviewModel to(ReviewInfo reviewInfo) {
        ReviewModel reviewModel = new ReviewModel();
        reviewModel.clientId = reviewInfo.clientId;
        reviewModel.clientname = reviewInfo.clientName;
        reviewModel.review = reviewInfo.content;
        reviewModel.rating = reviewInfo.star;
        return reviewModel;
    }
}
