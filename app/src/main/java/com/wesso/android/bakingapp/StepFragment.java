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
import android.widget.ImageView;
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
    private final static String KEY_WINDOW = "com.wesso.android.bakingapp.key_window";
    private final static String KEY_POSITION = "com.wesso.android.bakingapp.key_position";
    private final static String KEY_AUTO_PLAY = "com.wesso.android.bakingapp.autoplay";
    private final static String KEY_STEP = "com.wesso.android.bakingapp.saved_step";
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


        if (savedInstanceState != null) {
            Log.d(TAG, "onCreateView: Retrieving saved data");
            playWhenReady = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            currentWindow = savedInstanceState.getInt(KEY_WINDOW);
            playbackPosition = savedInstanceState.getLong(KEY_POSITION);
            Log.d(TAG, "onCreateView: Playback position " + playbackPosition);
            step = savedInstanceState.getParcelable(KEY_STEP);
        }

        setStepDescription();
        //initializePlayer(Uri.parse(step.getVideoURL()));
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
            Toast toast = Toast.makeText(getActivity(),"Reached the first step", Toast.LENGTH_SHORT);
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
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.cupcake));
            Log.d(TAG, "initializePlayer: playWhenReady " + playWhenReady);
            Log.d(TAG, "initializePlayer: playBackPosition " + playbackPosition);
            Log.d(TAG, "initializePlayer: now seeking");

            mediaSourceHandler(mediaUri);
        }
    }

    private void mediaSourceHandler(Uri mediaUri){
        // Prepare the MediaSource.
        if(!mediaUri.toString().isEmpty()){

            mPlayerView.setUseController(true);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent)).createMediaSource(mediaUri);
            mExoPlayer.seekTo(currentWindow,playbackPosition);
            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.prepare(mediaSource, false, false);

        } else {
            Log.d(TAG, "No URL found");
            mPlayerView.setUseController(false);
        }
    }

    private void releasePlayer() {
        if(mExoPlayer != null) {
            
            updateStartPosition();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void updateStartPosition() {
        if (mExoPlayer != null) {
            playbackPosition = Math.max(0, mExoPlayer.getContentPosition());
            currentWindow = mExoPlayer.getCurrentWindowIndex();
            playWhenReady = mExoPlayer.getPlayWhenReady();

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23){
            initializePlayer(Uri.parse(step.getVideoURL()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        initializePlayer(Uri.parse(step.getVideoURL()));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }


    @Override
    public void onPause() {
        super.onPause();
        if(Util.SDK_INT <= 23) {
            releasePlayer();
        } else if(mExoPlayer != null) {
            mExoPlayer.stop();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if(Util.SDK_INT > 23) {
            releasePlayer();
        }

    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        updateStartPosition();
        outState.putBoolean(KEY_AUTO_PLAY, playWhenReady);

        outState.putInt(KEY_WINDOW, currentWindow);
        outState.putLong(KEY_POSITION, playbackPosition);

        outState.putParcelable(KEY_STEP, step);
    }







}
