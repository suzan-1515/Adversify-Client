package com.nepal.adversify.ui.discover;

import android.Manifest;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahamed.multiviewadapter.SimpleRecyclerAdapter;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.SimpleColorFilter;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieValueCallback;
import com.generic.appbase.domain.dto.ClientInfo;
import com.generic.appbase.domain.dto.PayloadData;
import com.generic.appbase.domain.dto.PreviewMerchantInfo;
import com.generic.appbase.domain.event.OnItemClickCallback;
import com.generic.appbase.ui.BaseFragment;
import com.generic.appbase.utils.CommonUtils;
import com.generic.appbase.utils.SerializationUtils;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.Payload;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nepal.adversify.R;
import com.nepal.adversify.domain.binder.DiscoverBinder;
import com.nepal.adversify.domain.callback.ConnectionListener;
import com.nepal.adversify.domain.callback.DiscoveryListener;
import com.nepal.adversify.domain.callback.EndpointListener;
import com.nepal.adversify.domain.callback.PayloadListener;
import com.nepal.adversify.service.ConnectionService;
import com.nepal.adversify.ui.detail.DetailFragment;
import com.nepal.adversify.viewmodel.ClientViewModel;
import com.nepal.adversify.viewmodel.ClientViewModelFactory;
import com.nepal.adversify.viewmodel.DiscoverViewModel;
import com.nepal.adversify.viewmodel.DiscoverViewModelFactory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends BaseFragment implements EndpointListener,
        PayloadListener, ConnectionListener, DiscoveryListener, OnItemClickCallback<PreviewMerchantInfo> {

    public static final String EXTRA_DISCOVER_CATEGORY_ID = "param_discover_category_id";
    public static final String EXTRA_DISCOVER_CATEGORY_TITLE = "param_discover_category_title";

    //Injected object
    @Inject
    DiscoverViewModelFactory mDiscoverViewModelFactory;
    @Inject
    DiscoverBinder mDiscoverBinder;
    @Inject
    ClientViewModelFactory mClientViewModelFactory;

    private ConnectionService mConnectionService = ConnectionService.getInstance();

    //View components
    private Toolbar mToolbar;

    //Variables
    private DiscoverViewModel mDiscoverViewModel;
    private ClientViewModel mClientViewModel;

    private SimpleRecyclerAdapter<PreviewMerchantInfo, DiscoverBinder> mDiscoverAdapter;

    private int mCategoryId;


    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments == null) {
            throw new NullPointerException("Category parameter is null");
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mClientViewModel = ViewModelProviders.of(getActivity(), mClientViewModelFactory).get(ClientViewModel.class);
        mDiscoverViewModel = ViewModelProviders.of(getActivity(), mDiscoverViewModelFactory).get(DiscoverViewModel.class);
        observeData();
        loadData();
    }

    @Override
    protected View onViewReady(View view, Bundle savedInstanceState) {
        initToolbar(view);
        initLottie(view);
        initRecyclerView(view);

        return view;
    }

    private void initRecyclerView(View view) {
        EasyRecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mDiscoverAdapter = new SimpleRecyclerAdapter<>(mDiscoverBinder);
        mRecyclerView.setAdapterWithProgress(mDiscoverAdapter);
    }

    private void initToolbar(View view) {
        mToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
        NavigationUI.setupWithNavController(mToolbar, Navigation.findNavController(view));
    }

    private void initLottie(View view) {
        LottieAnimationView mLottieAnimationView = view.findViewById(R.id.animation_view);
        SimpleColorFilter filter = new SimpleColorFilter(
                ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent));
        KeyPath keyPath = new KeyPath("**");
        LottieValueCallback<ColorFilter> callback = new LottieValueCallback<>(filter);
        mLottieAnimationView.addValueCallback(keyPath, LottieProperty.COLOR_FILTER, callback);
    }

    private void loadData() {
        Timber.d("loadData");
        int mCategoryId = Objects.requireNonNull(getArguments()).getInt(EXTRA_DISCOVER_CATEGORY_ID);
        String mCategoryTitle = getArguments().getString(EXTRA_DISCOVER_CATEGORY_TITLE);
        Timber.d("Cat id: %d, title:%s", mCategoryId, mCategoryTitle);
        mDiscoverViewModel.setSearchCategoryInfo(mCategoryId, mCategoryTitle);
    }

    @Override
    public void onEndpointConnected(String endpointId, String endpointName) {
        Timber.d("onEndpointConnected");
        sendProfileInfo(endpointId);
    }

    @Override
    public void onEndpointConnectionRejected(String endpointId, String endpointName) {
        Timber.d("onEndpointConnectionRejected");
    }

    @Override
    public void onEndpointConnectionError(String endpointId, String endpointName) {
        Timber.d("onEndpointConnectionError");
    }

    @Override
    public void onEndpointDisconnected(String endpointId) {
        Timber.d("onEndpointDisconnected");
        mDiscoverViewModel.removeConnectedMerchant(endpointId);
    }

    @Override
    public void onPayloadSent(long payloadId, String endpointId) {
        Timber.d("onPayloadSent to id: %s", endpointId);
    }

    @Override
    public void onFilePayloadReceived(String endpointId, long id, PayloadData payloadData) {
        Timber.d("onFilePayloadReceived to id: %s", endpointId);
        if (payloadData.dataType == PayloadData.MERCHANT_PREVIEW_INFO) {
            PreviewMerchantInfo previewMerchantInfo = (PreviewMerchantInfo) payloadData;
            previewMerchantInfo.previewImage = previewMerchantInfo.fileName;
            Timber.d("Merchant file path: %s", previewMerchantInfo.previewImage);
            mDiscoverViewModel.addConnectedMerchants(endpointId, previewMerchantInfo);
        }
    }

    private void observeData() {
        Timber.d("Observing livedata");
        mDiscoverViewModel.getConnectedMerchantLiveData().observe(this, data -> {
            Timber.d("Total connected merchants: %d", data.size());
            mDiscoverAdapter.getDataManager().clear();
            mDiscoverAdapter.setData(new ArrayList<>(data.values()));
        });
        mDiscoverViewModel.getCategoryTitleMutableLiveData().observe(this, data -> {
            Timber.d("Search category: %s", data);
            mToolbar.setTitle(data);
        });

        mDiscoverViewModel.getCategoryIdMutableLiveData().observe(this, data -> {
            Timber.d("Search category id: %d", data);
            mCategoryId = data;
            requestPermission();
        });

    }

    @Override
    public void onBytePayloadReceived(String endpointId, long id, Object obj) {
        Timber.d("onBytePayloadReceived to id: %s", endpointId);
        PayloadData payloadData = (PayloadData) obj;
        if (payloadData.dataType == PayloadData.MERCHANT_PREVIEW_INFO) {
            PreviewMerchantInfo previewMerchantInfo = (PreviewMerchantInfo) payloadData;
            updateDistance(endpointId, previewMerchantInfo);
            mDiscoverViewModel.addConnectedMerchants(endpointId, previewMerchantInfo);

        }

    }

    private void sendProfileInfo(String endpointId) {
        Timber.d("sendProfileInfo to id: %s", endpointId);
        ClientInfo clientInfo = mClientViewModel.getClientInfo();
        if (clientInfo.hasFile) {
            try {
                ParcelFileDescriptor pfd = getContext().getContentResolver().openFileDescriptor(Uri.parse(clientInfo.avatar), "r");
                Payload payloadImage = Payload.fromFile(pfd);
                clientInfo.fileId = payloadImage.getId();
                Timber.d("Profile file name: %s", clientInfo.fileName);

                mConnectionService.getPayloadHandler().sendPayload(endpointId, Payload.fromBytes(
                        Objects.requireNonNull(SerializationUtils.serializeToByteArray(
                                clientInfo
                        ))
                ));
                mConnectionService.getPayloadHandler().sendPayload(endpointId, payloadImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            mConnectionService.getPayloadHandler().sendPayload(endpointId, Payload.fromBytes(
                    Objects.requireNonNull(SerializationUtils.serializeToByteArray(
                            clientInfo
                    ))
            ));
        }

    }

    @Override
    public void onEndpointLost(String endpointId) {
        Timber.d("onEndpointLost");
        mConnectionService.getConnectionsClient().disconnectFromEndpoint(endpointId);
    }

    @Override
    public void onEndpointFound(String endpointId, DiscoveredEndpointInfo discoveredEndpointInfo) {
        Timber.d("onEndpointFound");
        ClientInfo clientInfo = mClientViewModel.getClientLiveData().getValue();
        mConnectionService.requestConnection(endpointId, clientInfo, mCategoryId);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_discover;
    }

    private void requestPermission() {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE

                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Timber.d("onPermissionGranted");
                            mConnectionService.startDiscovery();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onItemClick(View v, int position, PreviewMerchantInfo merchantInfo) {
        Timber.d("onItemClick: position:%d", position);

        String endpointId = null;
        TreeMap<String, PreviewMerchantInfo> value = mDiscoverViewModel.getConnectedMerchantLiveData().getValue();
        for (Map.Entry entry : value.entrySet()) {
            if (Objects.equals(entry.getValue(), merchantInfo)) {
                endpointId = (String) entry.getKey();
                break;
            }
        }

        if (endpointId == null) {
            showToast("Connection lost with merchant!");
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(DetailFragment.EXTRA_DETAIL_MERCHANT_INFO, merchantInfo);
        bundle.putString(DetailFragment.EXTRA_MERCHANT_ENDPOINT, endpointId);


        Navigation.findNavController(v).navigate(R.id.action_discoverFragment_to_detailFragment, bundle);
    }

    @Override
    public void onConnectionSuccessful(String endpointId, ClientInfo clientInfo) {
        Timber.d("onConnectionSuccessful- merchant id: %s", endpointId);
    }

    @Override
    public void onConnectionFailed(Exception e, String endpointId, ClientInfo clientInfo) {
        Timber.e("e");
        mConnectionService.getConnectionsClient().disconnectFromEndpoint(endpointId);
    }

    @Override
    public void onDiscoveryStarted() {
        Timber.d("onDiscoveryStarted");
    }

    @Override
    public void onDiscoveryFailed(Exception e) {
        Timber.e(e, "onDiscoveryFailed");
    }

    @Override
    public void onStart() {
        super.onStart();
        mConnectionService.setConnectionListener(this);
        mConnectionService.setDiscoveryListener(this);
        mConnectionService.setEndpointListener(this);
        mConnectionService.setPayloadListener(this);
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
    public void onDestroy() {
        Timber.d("onDestroy");
        mDiscoverViewModel.clearConnectedEndpoint();
        mConnectionService.disconnect();
        mDiscoverViewModel.getCategoryIdMutableLiveData().setValue(null);
        mDiscoverViewModel.getCategoryTitleMutableLiveData().setValue(null);
        setArguments(null);
        super.onDestroy();
    }


}
