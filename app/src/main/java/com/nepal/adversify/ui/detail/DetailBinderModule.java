/*
 * *
 *  * All Rights Reserved.
 *  * Created by Suzn on 2018.
 *  * suzanparajuli@gmail.com
 *
 */

package com.nepal.adversify.ui.detail;

import com.generic.appbase.domain.dto.ReviewInfo;
import com.generic.appbase.mapper.Mapper;
import com.nepal.adversify.domain.callback.PayloadListener;
import com.nepal.adversify.domain.model.ReviewModel;
import com.nepal.adversify.mapper.ReviewModelToReviewInfoMapper;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class DetailBinderModule {

    @Binds
    abstract PayloadListener bindsPayloadCallback(DetailFragment discoverFragment);

    @Binds
    abstract Mapper<ReviewModel, ReviewInfo> bindsReviewModelToReviewInfo(ReviewModelToReviewInfoMapper infoMapper);

}
