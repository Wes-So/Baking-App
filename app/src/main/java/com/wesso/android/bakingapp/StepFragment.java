package com.wesso.android.bakingapp;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment {

    private static final String EXTRA_STEP = "com.wesso.android.bakingapp.step";
    private static final String EXTRA_STEPS = "com.wesso.android.bakingapp.steps";
    private static final String TAG = "Step Fragment";
    private SimpleExoPlayer mExoPlayer;
    private Step step;
    private List<Step> mSteps;
    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady;
    private int mTotalSteps;
    private String  userAgent;

    @BindView(R.id.short_description) TextView mShortDescription;
    @BindView(R.id.long_description)  TextView mLongDescription;
    @BindView(R.id.video_player) PlayerView mPlayerView;
    @BindView(R.id.previous_btn) Button mPreviousButton;
    @BindView(R.id.next_btn) Button mNextButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        step = Objects.requireNonNull(getArguments()).getParcelable(EXTRA_STEP);
        mSteps = getArguments().getParcelableArrayList(EXTRA_STEPS);
        mTotalSteps = Objects.requireNonNull(mSteps).size();
        userAgent = Util.getUserAgent(getActivity(), "Baking-App");
        Log.d(TAG, "Step Name: " + step.getShortDescription());
    }


    public static StepFragment newInstance(Step step, ArrayList<Step> steps){
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_STEP,step);
        args.putParcelableArrayList(EXTRA_STEPS,steps);

        StepFragment fragment = new StepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step,container, false);
        ButterKnife.bind(this, view);
        setStepDescription();

        // Initialize the player.
        initializePlayer(Uri.parse(step.getVideoURL()));

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePreviousVideo();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNextVideo();
            }
        });

        return view;
    }

    private void setStepDescription(){
        mShortDescription.setText(step.getShortDescription());
        mLongDescription.setText(step.getDescription());
    }

    private void handlePreviousVideo(){
        int currentId = step.getId();
        int prevId = currentId - 1;

        if(prevId > -1){
            pageHandler(prevId);
        } else {
            Toast toast = Toast.makeText(getActivity(),"Reached the last step", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    private void handleNextVideo() {
        int currentId = step.getId();
        int nextId = currentId + 1;
        if(nextId < mTotalSteps){
            pageHandler(nextId);
        } else {
            Toast toast = Toast.makeText(getActivity(),"Reached the last step", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void pageHandler(int id){
        step = mSteps.get(id);
        Log.d(TAG, "handleNextVideo: " + step.getDescription());
        Log.d(TAG, "Number of steps received: " + mTotalSteps);
        setStepDescription();
        if(mExoPlayer != null) {
            mediaSourceHandler(Uri.parse(step.getVideoURL()));
        } else {
            initializePlayer(Uri.parse(step.getVideoURL()));
        }
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getActivity()),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());

            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(currentWindow,playbackPosition);

            mediaSourceHandler(mediaUri);
        }
    }

    private void mediaSourceHandler(Uri mediaUri){
        // Prepare the MediaSource.
        if(!mediaUri.toString().isEmpty()){
//            mPlayerView.setVisibility(View.VISIBLE);
            mPlayerView.showController();
            MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent)).createMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource, true, false);
        } else {
            Log.d(TAG, "No URL found");
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), R.drawable.cupcake));
            mPlayerView.hideController();
//            mPlayerView.setVisibility(View.INVISIBLE);
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






}
