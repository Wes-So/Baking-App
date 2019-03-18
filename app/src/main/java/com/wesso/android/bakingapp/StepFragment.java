package com.wesso.android.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.wesso.android.bakingapp.data.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment {

    private static final String EXTRA_STEP = "com.wesso.android.bakingapp.step";
    private static final String TAG = "Step Fragment";
    private SimpleExoPlayer mExoPlayer;
    private Step step;
    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady;

    @BindView(R.id.short_description) TextView mShortDescription;
    @BindView(R.id.long_description)  TextView mLongDescription;
    @BindView(R.id.video_player) PlayerView mPlayerView;



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
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.cupcake));
        mShortDescription.setText(step.getShortDescription());
        mLongDescription.setText(step.getDescription());

        // Initialize the player.
        initializePlayer(Uri.parse(step.getVideoURL()));
        return view;
    }


    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getActivity()),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());

            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(currentWindow,playbackPosition);


            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "Baking-App");
            MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent)).createMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource, true, false);
        }
    }




    private void releasePlayer() {
        if(mExoPlayer != null) {
            playbackPosition = mExoPlayer.getContentPosition();
            currentWindow = mExoPlayer.getCurrentWindowIndex();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }


    public void adjustExoPlayer(Configuration newConfig) {
        // Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //First Hide other objects (listview or recyclerview), better hide them using Gone.
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=params.MATCH_PARENT;
            mPlayerView.setLayoutParams(params);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //unhide your objects here.
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=600;
            mPlayerView.setLayoutParams(params);
        }
    }
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        Log.d(TAG, "onConfigurationChanged: ");
//        // Checking the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            //First Hide other objects (listview or recyclerview), better hide them using Gone.
//            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPlayerView.getLayoutParams();
//            params.width=params.MATCH_PARENT;
//            params.height=params.MATCH_PARENT;
//            mPlayerView.setLayoutParams(params);
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            //unhide your objects here.
//            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPlayerView.getLayoutParams();
//            params.width=params.MATCH_PARENT;
//            params.height=600;
//            mPlayerView.setLayoutParams(params);
//        }
//    }

}
