package com.draconra.bakingapp.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.draconra.bakingapp.R;
import com.draconra.bakingapp.util.helper.MediaPlayerHelper;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VideoFragment extends Fragment {

    @BindView(R.id.exoView)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.videoProgress)
    ProgressBar mProgressBar;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.thumbnail)
    ImageView thumbnail;
    @BindView(R.id.descriptionText)
    TextView descriptionText;
    @BindView(R.id.previous)
    FloatingActionButton prev;
    @BindView(R.id.next)
    FloatingActionButton next;
    @BindView(R.id.baseLayout)
    RelativeLayout baseLayout;
    Unbinder unbinder;

    private String mDescription;
    private String mVideoURL, thumbnailUrl;
    private int pos, size = 0;
    private SimpleExoPlayer mPlayer;

    VideoClickListener mCallback;

    public static final String AUTOPLAY = "autoplay";
    public static final String CURRENT_WINDOW_INDEX = "current_window_index";
    public static final String PLAYBACK_POSITION = "playback_position";

    private boolean autoPlay = false;

    // used to remember the playback position
    private int currentWindow;
    private long playbackPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_video, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION, 0);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX, 0);
            autoPlay = savedInstanceState.getBoolean(AUTOPLAY, false);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            mDescription = bundle.getString("description");
            mVideoURL = bundle.getString("videoURL");
            thumbnailUrl = bundle.getString("thumbnailUrl");
            pos = bundle.getInt("position");
            size = bundle.getInt("size");
        } else {
            mDescription = getContext().getResources().getString(R.string.app_name);
            mVideoURL = "";
            thumbnailUrl = "";
        }

        if (pos == 0)
            prev.setVisibility(View.INVISIBLE);
        if (pos == size - 1)
            next.setVisibility(View.INVISIBLE);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPreviousSelected(pos - 1);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onNextSelected(pos + 1);
            }
        });


        if (thumbnail != null && descriptionText != null) {
            Glide.with(getContext()).load(thumbnailUrl).into(thumbnail);
            descriptionText.setText(mDescription);
        }

        mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector(), new DefaultLoadControl());
        mPlayerView.setPlayer(mPlayer);
        mPlayer.setPlayWhenReady(autoPlay);

        mPlayer.seekTo(currentWindow, playbackPosition);
        Uri videoUri = Uri.parse(mVideoURL);
        MediaSource mediaSource = MediaPlayerHelper.buildMediaSource(getActivity(),videoUri);
        mPlayer.prepare(mediaSource);

        if (mPlayer.isLoading()) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mPlayer == null) {
            outState.putLong(PLAYBACK_POSITION, playbackPosition);
            outState.putInt(CURRENT_WINDOW_INDEX, currentWindow);
            outState.putBoolean(AUTOPLAY, autoPlay);
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            // save the player state before releasing its resources
            playbackPosition = mPlayer.getCurrentPosition();
            currentWindow = mPlayer.getCurrentWindowIndex();
            autoPlay = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface VideoClickListener {
        void onNextSelected(int position);

        void onPreviousSelected(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof VideoClickListener) {
            mCallback = (VideoClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement VideoClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

}
