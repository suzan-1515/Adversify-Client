package com.nepal.adversify.ui.review;


import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.generic.appbase.domain.dto.ClientInfo;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.nepal.adversify.R;
import com.nepal.adversify.domain.model.ReviewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends BottomSheetDialogFragment {

    public static final String EXTRA_PARAM_CLIENT_INFO = "extra_param_client_info";

    private OnActionCallBack callBack;

    public static ReviewFragment newInstance(final ClientInfo clientInfo) {
        ReviewFragment reviewFragment = new ReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_PARAM_CLIENT_INFO, clientInfo);
        reviewFragment.setArguments(bundle);
        return reviewFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.fragment_post_review, container, false);
        Bundle bundle = getArguments();
        if (bundle == null) {
            dismiss();
            return contentView;
        }

        ClientInfo clientInfo = (ClientInfo) bundle.getSerializable(EXTRA_PARAM_CLIENT_INFO);
        if (clientInfo == null) {
            dismiss();
            return contentView;
        }

        Timber.d("Client name:%s, avatar:%s", clientInfo.name, clientInfo.avatar);

        //Get the content View
        CircularImageView avatar = contentView.findViewById(R.id.avatar);
        AppCompatRatingBar ratingBar = contentView.findViewById(R.id.ratingbar);
        TextInputEditText reviewInputText = contentView.findViewById(R.id.input_review);
        MaterialButton cancelButton = contentView.findViewById(R.id.cancel_button);
        MaterialButton postButton = contentView.findViewById(R.id.post_button);

        if (!TextUtils.isEmpty(clientInfo.avatar)) {
            Glide.with(contentView.getContext())
                    .load(clientInfo.avatar)
                    .into(avatar);
        }

        cancelButton.setOnClickListener((v) -> {
            this.dismiss();
        });
        postButton.setOnClickListener((v) -> {

            ReviewModel reviewModel = new ReviewModel();
            reviewModel.clientId = clientInfo.id;
            reviewModel.clientname = clientInfo.name;
            reviewModel.rating = (int) ratingBar.getRating();
            reviewModel.review = reviewInputText.getEditableText().toString().trim();
            if (callBack != null) {
                callBack.onPostButtonClicked(v, reviewModel);
            }
            this.dismiss();
        });

        return contentView;
    }

    public void registerActionCallback(OnActionCallBack callBack) {
        this.callBack = callBack;
    }

    public interface OnActionCallBack {
        void onPostButtonClicked(View v, ReviewModel reviewModel);
    }

}
