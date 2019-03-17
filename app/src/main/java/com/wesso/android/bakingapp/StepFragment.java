package com.wesso.android.bakingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.wesso.android.bakingapp.data.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment {

    private static final String EXTRA_STEP = "com.wesso.android.bakingapp.step";
    private static final String TAG = "Step Fragment";
    private SimpleExoPlayer mExoPlayer;
    private Step step;
    @BindView(R.id.short_description) TextView mShortDescription;
    @BindView(R.id.long_description)  TextView mLongDescription;
    @BindView(R.id.video_player) SimpleExoPlayerView mPlayerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        step = getArguments().getParcelable(EXTRA_STEP);
        Log.d(TAG, "Step Name: " + step.getShortDescription());
    }

    public static StepFragment newInstance(Step step){
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_STEP,step);

        StepFragment fragment = new StepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step,container, false);

        ButterKnife.bind(this, view);
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.cupcake));

        mShortDescription.setText(step.getShortDescription());
        mLongDescription.setText(step.getDescription());

        // Initialize the player.
        initializePlayer(Uri.parse(step.getVideoURL()));
        return view;
    }


    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);
            mPlayerView.setDefaultArtwork(b);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "Baking-App");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

}
