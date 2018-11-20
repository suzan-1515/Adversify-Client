package com.nepal.adversify.ui.detail;


import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import com.ahamed.multiviewadapter.SimpleRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.generic.appbase.domain.dto.ClientInfo;
import com.generic.appbase.domain.dto.DetailMerchantInfo;
import com.generic.appbase.domain.dto.PayloadData;
import com.generic.appbase.domain.dto.PreviewMerchantInfo;
import com.generic.appbase.domain.dto.RequestPayload;
import com.generic.appbase.domain.dto.ReviewInfo;
import com.generic.appbase.ui.BaseFragment;
import com.generic.appbase.utils.CommonUtils;
import com.generic.appbase.utils.SerializationUtils;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.nepal.adversify.R;
import com.nepal.adversify.domain.binder.ReviewBinder;
import com.nepal.adversify.domain.callback.PayloadListener;
import com.nepal.adversify.domain.model.ReviewModel;
import com.nepal.adversify.mapper.ReviewModelToReviewInfoMapper;
import com.nepal.adversify.service.ConnectionService;
import com.nepal.adversify.ui.review.ReviewFragment;
import com.nepal.adversify.viewmodel.ClientViewModel;
import com.nepal.adversify.viewmodel.ClientViewModelFactory;
import com.nepal.adversify.viewmodel.DetailViewModel;
import com.nepal.adversify.viewmodel.DetailViewModelFactory;
import com.nepal.adversify.viewmodel.DiscoverViewModel;
import com.nepal.adversify.viewmodel.DiscoverViewModelFactory;
import com.xw.repo.VectorCompatTextView;

import java.util.Arrays;
import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends BaseFragment implements PayloadListener {


    public static final String EXTRA_DETAIL_MERCHANT_INFO = "param_detail_merchant_info";
    public static final String EXTRA_MERCHANT_ENDPOINT = "param_merchant_endpoint";

    @Inject
    DetailViewModelFactory mDetailViewModelFactory;
    @Inject
    DiscoverViewModelFactory mDiscoverViewModelFactory;
    @Inject
    ClientViewModelFactory mClientViewModelFactory;
    @Inject
    ReviewBinder mReviewBinder;
    @Inject
    ReviewModelToReviewInfoMapper fromRmToRi;


    private AppCompatTextView mTitleTextView;
    private RatingBar mRatingBar;
    private AppCompatTextView mAddressTextView;
    private Chip mDisChip;
    private VectorCompatTextView mContactTextView;
    private VectorCompatTextView mWebsiteTextView;
    private AppCompatTextView mSundayTextView;
    private AppCompatTextView mMondayTextView;
    private AppCompatTextView mTuesdayTextView;
    private AppCompatTextView mWednesdayTextView;
    private AppCompatTextView mThursdayTextView;
    private AppCompatTextView mFridayTextView;
    private AppCompatTextView mSaturdayTextView;
    private AppCompatTextView mDescriptionTextView;
    private View mDiscountContainer;
    private AppCompatTextView mDiscountTitleTextView;
    private AppCompatTextView mDiscountDescriptionTextView;
    private View mOfferContainer;
    private AppCompatTextView mOfferTitleTextView;
    private AppCompatTextView mOfferDescriptionTextView;
    private EasyRecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private MaterialButton mPostReviewButton;
    private AppCompatImageView mPreviewImageView;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private DetailViewModel mDetailViewModel;
    private ClientViewModel mClientViewModel;
    private DiscoverViewModel mDiscoverViewModel;
    private ConnectionService mConnectionService = ConnectionService.getInstance();
    private SimpleRecyclerAdapter<ReviewInfo, ReviewBinder> mReviewAdapter;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mClientViewModel = ViewModelProviders.of(getActivity(), mClientViewModelFactory).get(ClientViewModel.class);
        mDetailViewModel = ViewModelProviders.of(this, mDetailViewModelFactory).get(DetailViewModel.class);
        mDiscoverViewModel = ViewModelProviders.of(getActivity(), mDiscoverViewModelFactory).get(DiscoverViewModel.class);
        observeData();
        loadData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments == null) {
            throw new NullPointerException("Merchant parameter is null");
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected View onViewReady(View view, Bundle savedInstanceState) {
        initToolbar(view);
        initViews(view);
        initRecyclerView(view);

        return view;
    }

    private void initRecyclerView(View view) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.getRecyclerView().setNestedScrollingEnabled(false);
        mReviewAdapter = new SimpleRecyclerAdapter<>(mReviewBinder);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecor);
    }

    private void initViews(View view) {

        mProgressBar = view.findViewById(R.id.progress);
        mPreviewImageView = view.findViewById(R.id.toolbar_image);

        mTitleTextView = view.findViewById(R.id.title);
        mRatingBar = view.findViewById(R.id.ratingbar);
        mAddressTextView = view.findViewById(R.id.address);
        mDisChip = view.findViewById(R.id.distance);
        mContactTextView = view.findViewById(R.id.contact);
        mWebsiteTextView = view.findViewById(R.id.website);

        mDiscountContainer = view.findViewById(R.id.discount_container);
        mDiscountTitleTextView = view.findViewById(R.id.discount_title);
        mDiscountDescriptionTextView = view.findViewById(R.id.discount_desc);
        mOfferContainer = view.findViewById(R.id.offer_container);
        mOfferTitleTextView = view.findViewById(R.id.offer_title);
        mOfferDescriptionTextView = view.findViewById(R.id.offer_desc);

        mSundayTextView = view.findViewById(R.id.sunday);
        mMondayTextView = view.findViewById(R.id.monday);
        mTuesdayTextView = view.findViewById(R.id.tuesday);
        mWednesdayTextView = view.findViewById(R.id.wednesday);
        mThursdayTextView = view.findViewById(R.id.thursday);
        mFridayTextView = view.findViewById(R.id.friday);
        mSaturdayTextView = view.findViewById(R.id.saturday);

        mDescriptionTextView = view.findViewById(R.id.description);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mPostReviewButton = view.findViewById(R.id.review_button);
        mPostReviewButton.setOnClickListener((v) -> {
            ReviewFragment reviewFragment = ReviewFragment.newInstance(mClientViewModel.getClientInfo());
            reviewFragment.registerActionCallback((v1, reviewModel) -> {
                postReview(reviewModel);
            });
            reviewFragment.show(getFragmentManager(), "Post review");
        });
    }

    private void initToolbar(View view) {
        Toolbar mToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
        NavigationUI.setupWithNavController(mToolbar, Navigation.findNavController(view));

        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = view.findViewById(R.id.appbar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                    collapsingToolbarLayout.setTitle(Objects.requireNonNull(mDetailViewModel.getMerchantPreviewInfo().getValue()).title);
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_detail;
    }

    private void observeData() {
        Timber.d("Observing livedata");
        mDetailViewModel.getConnectedMerchantId().observe(this, data -> {
            if (data != null) {
                Timber.d("Connected endpoint: %s", data);
                requestMerchantDetailInfo(data);
            }
        });
        mDetailViewModel.getMerchantPreviewInfo().observe(this, data -> {
            if (data != null) {
                Timber.d("Connected merchant title: %s", data.title);
                fillPreviewInfo(data);
            }

        });
        mDetailViewModel.getMerchantDetailInfo().observe(this, data -> {
            if (data != null) {
                Timber.d("Detail merchant title: %s", data.title);
                fillDetailInfo(data);
                mDetailViewModel.getMerchantDetailInfo().setValue(null);
            }
        });
    }

    private void fillDetailInfo(DetailMerchantInfo data) {
        if (!TextUtils.isEmpty(data.previewImage)) {
            mPreviewImageView.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(data.previewImage)
                    .into(mPreviewImageView);
        }

        mTitleTextView.setText(data.title);
        mRatingBar.setRating(data.rating);
        mAddressTextView.setText(data.address);

        mDisChip.setText(data.distance);
        mContactTextView.setText(data.contact);
        mWebsiteTextView.setText(TextUtils.isEmpty(data.website) ? "N/A" : data.website);

        if (data.discountInfo != null && !TextUtils.isEmpty(data.discountInfo.title)) {
            mDiscountContainer.setVisibility(View.VISIBLE);
            mDiscountTitleTextView.setText(data.discountInfo.title);
            mDiscountDescriptionTextView.setText(data.discountInfo.description);
        } else {
            mDiscountContainer.setVisibility(View.GONE);
        }

        if (data.specialOfferInfo != null && !TextUtils.isEmpty(data.specialOfferInfo.title)) {
            mOfferContainer.setVisibility(View.VISIBLE);
            mOfferTitleTextView.setText(data.specialOfferInfo.title);
            mOfferDescriptionTextView.setText(data.specialOfferInfo.description);
        } else {
            mOfferContainer.setVisibility(View.GONE);
        }

        mSundayTextView.setText(data.openingInfo.sunday);
        mMondayTextView.setText(data.openingInfo.monday);
        mTuesdayTextView.setText(data.openingInfo.tuesday);
        mWednesdayTextView.setText(data.openingInfo.wednesday);
        mThursdayTextView.setText(data.openingInfo.thursday);
        mFridayTextView.setText(data.openingInfo.friday);
        mSaturdayTextView.setText(data.openingInfo.saturday);

        mDescriptionTextView.setText(data.description);

        mRecyclerView.setAdapter(mReviewAdapter);
        mReviewAdapter.setData(Arrays.asList(data.reviewInfos));

    }

    private void fillPreviewInfo(PreviewMerchantInfo data) {
        if (!TextUtils.isEmpty(data.previewImage)) {
            mPreviewImageView.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(data.previewImage)
                    .into(mPreviewImageView);
        } else {
            mPreviewImageView.setVisibility(View.GONE);
        }
        mTitleTextView.setText(data.title);
        mRatingBar.setRating(data.rating);
        mDisChip.setText(data.distance);
        mAddressTextView.setText(data.address);
        mContactTextView.setText(data.contact);

    }

    private void loadData() {
        Timber.d("loadData");
        String endpointId = getArguments().getString(EXTRA_MERCHANT_ENDPOINT);
        PreviewMerchantInfo merchantInfo = (PreviewMerchantInfo) getArguments().getSerializable(EXTRA_DETAIL_MERCHANT_INFO);
        mDetailViewModel.setMerchantEndpoint(endpointId);
        mDetailViewModel.setPreviewInfo(merchantInfo);

    }

    private void requestMerchantDetailInfo(String endpointId) {
        mProgressBar.setVisibility(View.VISIBLE);
        PayloadData payloadData = new RequestPayload();
        payloadData.dataType = PayloadData.MERCHANT_DETAIL_INFO;
        mConnectionService.getPayloadHandler().sendPayload(endpointId, Payload.fromBytes(
                Objects.requireNonNull(SerializationUtils.serializeToByteArray(payloadData))
        ));
    }


    @Override
    public void onPayloadSent(long payloadId, String endpointId) {
        Timber.d("onPayloadSent");
    }

    @Override
    public void onFilePayloadReceived(String endpointId, long id, PayloadData data) {
        Timber.d("onFilePayloadReceived");
        if (data.dataType == PayloadData.MERCHANT_DETAIL_INFO) {
            DetailMerchantInfo merchantInfo = (DetailMerchantInfo) data;
            merchantInfo.previewImage = merchantInfo.fileName;
            mDetailViewModel.getMerchantDetailInfo().setValue(merchantInfo);
            mProgressBar.setVisibility(View.GONE);
        } else if (data.dataType == PayloadData.MERCHANT_PREVIEW_INFO) {
            PreviewMerchantInfo previewMerchantInfo = (PreviewMerchantInfo) data;
            previewMerchantInfo.previewImage = previewMerchantInfo.fileName;
            Timber.d("Merchant file path: %s", previewMerchantInfo.previewImage);
            mDetailViewModel.updateDetailInfoImage(previewMerchantInfo);
            mDiscoverViewModel.addConnectedMerchants(endpointId, previewMerchantInfo);
        }
    }

    @Override
    public void onBytePayloadReceived(String endpointId, long id, Object object) {
        Timber.d("onBytePayloadReceived");
        PayloadData data = (PayloadData) object;
        if (data.dataType == PayloadData.MERCHANT_DETAIL_INFO) {
            DetailMerchantInfo merchantInfo = (DetailMerchantInfo) data;
            updateDistance(endpointId, merchantInfo);
            mDetailViewModel.getMerchantDetailInfo().setValue(merchantInfo);
            mProgressBar.setVisibility(View.GONE);
        } else if (data.dataType == PayloadData.MERCHANT_PREVIEW_INFO) {
            PreviewMerchantInfo previewMerchantInfo = (PreviewMerchantInfo) data;
            updateDistance(endpointId, previewMerchantInfo);
            mDiscoverViewModel.addConnectedMerchants(endpointId, previewMerchantInfo);
        }
    }

    private void postReview(ReviewModel reviewModel) {
        Timber.d("postReview");
        String endpointId = mDetailViewModel.getConnectedMerchantId().getValue();
        ReviewInfo reviewInfo = fromRmToRi.from(reviewModel);
        if (reviewInfo.dataType == PayloadData.MERCHANT_REVIEW_INFO) {
            mConnectionService.getPayloadHandler().sendPayload(endpointId,
                    Payload.fromBytes(Objects.requireNonNull(SerializationUtils.serializeToByteArray(reviewInfo))));
            showToast("Review submitted. It will be shortly posted.");
        }
    }

    private void updateDistance(String endpointId, PreviewMerchantInfo previewMerchantInfo) {
        ClientInfo clientInfo = mClientViewModel.getClientLiveData().getValue();
        if (clientInfo != null) {
            int distance = (int) CommonUtils.calculateDistance(clientInfo.location, previewMerchantInfo.location);
            previewMerchantInfo.distance = String.format(
                    getContext().getString(R.string.distance_surfix),
                    distance);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mConnectionService.setPayloadListener(this);
    }

}
