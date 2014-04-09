package com.videoscale.sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bgxt.surfaceviewdemo.R;

import com.videoscale.tools.VideoViewScale;
import com.videoscale.tools.VideoViewScale.ViewSize;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends Activity implements SurfaceHolder.Callback {
	MediaPlayer player;
	SurfaceView surface;
	SurfaceHolder surfaceHolder;
	Button mPlay, mPause, mStop,mZoomIn,mZoomOut;
	VideoViewScale videoViewScale;
	FrameLayout surfacelayout;
	private final int INI_WIDTH = 480;
	private final int INI_HIGHT = 330;
	private final int DST_WIDTH = 160;
	private final int DST_HIGHT = 110;

	private final int DST_LEFT_MARGIN = 0;
	private final int DST_TOP_MARGIN = 0;
	private final int DST_RIGHT_MARGIN = 0;
	private final int DST_BOTTOM_MARGIN = 0;
	private final int SCALE_TIME = 1000;

	ViewSize iniSize = new ViewSize(INI_WIDTH, INI_HIGHT);
	ViewSize dstSise = new ViewSize(DST_WIDTH, DST_HIGHT);
	Rect marginRect = new Rect(DST_LEFT_MARGIN, DST_TOP_MARGIN,
			DST_RIGHT_MARGIN, DST_BOTTOM_MARGIN);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mPlay = (Button) findViewById(R.id.button1);
		mPause = (Button) findViewById(R.id.button2);
		mStop = (Button) findViewById(R.id.button3);
		mZoomIn = (Button) findViewById(R.id.button4);
		mZoomOut = (Button) findViewById(R.id.button5);
		surfacelayout = (FrameLayout) findViewById(R.id.surfacelayout);

		surface = (SurfaceView) findViewById(R.id.surface);
		surfaceHolder = surface.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		initScaleInfo(this, surfacelayout, surface, iniSize, dstSise,
				marginRect);

		mPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				player.start();
			}
		});
		mPause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				player.pause();
				Log.w("test", "position " + player.getCurrentPosition());
				// saveMyBitmap("snapshot",bmp);

			}
		});
		mStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				player.stop();
			}
		});
		
		mZoomIn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				player.pause();
				Log.w("test", "position " + player.getCurrentPosition());
				Bitmap bmp = getVideoFrame("/sdcard/mtv.mp4",
						player.getCurrentPosition(), player);
				videoViewScale.smaller(bmp, SCALE_TIME);

			}
		});
		
		mZoomOut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				player.pause();
				Log.w("test", "position " + player.getCurrentPosition());
				Bitmap bmp = getVideoFrame("/sdcard/mtv.mp4",
						player.getCurrentPosition(), player);
				videoViewScale.larger(bmp, SCALE_TIME);

			}
		});
		
		
	}
	
	
	

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// 必须在surface创建后才能初始化MediaPlayer,否则不会显示图像
		player = new MediaPlayer();
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		player.setDisplay(surfaceHolder);
		// 设置显示视频显示在SurfaceView上
		try {
			player.setDataSource("/sdcard/mtv.mp4");
			player.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {

	}
	
	
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (player.isPlaying()) {
			player.stop();
		}
		player.release();
	}

	@SuppressLint("NewApi")
	public static Bitmap getVideoFrame(String path, int position,
			MediaPlayer mediaPlayer) {

		Bitmap bmp = null;
		// android 9及其以上版本可以使用该方法
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(path);
			String timeString = retriever
					.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
			long titalTime = Long.parseLong(timeString) * 1000;
			long videoPosition = 0;
			int duration = mediaPlayer.getDuration();
			// 通过这个计算出想截取的画面所在的时间
			videoPosition = titalTime * position / duration;
			if (videoPosition > 0) {
				bmp = retriever.getFrameAtTime(videoPosition,
						MediaMetadataRetriever.OPTION_CLOSEST);
			}
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		} catch (RuntimeException ex) {
			ex.printStackTrace();
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException ex) {
			}
		}
		return bmp;
	}

	public void saveMyBitmap(String bitName, Bitmap mBitmap) {
		File f = new File("/sdcard/" + bitName + ".png");
		try {
			f.createNewFile();
		} catch (IOException e) {
			Log.e("test", "");
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initScaleInfo(Activity a, FrameLayout flCamera,
			SurfaceView surfaceview, ViewSize iniSize, ViewSize dstSise,
			Rect marginRect) {

		videoViewScale = new VideoViewScale(a, flCamera, surfaceview, iniSize,
				dstSise, marginRect);

	}

}