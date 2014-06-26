package com.penzgtuar;

import com.penzgtuar.penzgtuarbrowser.R;

import rajawali.util.RajLog;
import rajawali.vuforia.RajawaliVuforiaActivity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnTouchListener;

public class MainActivity extends RajawaliVuforiaActivity implements
		OnTouchListener {
	private MainRenderer mRenderer;
	private RajawaliVuforiaActivity mUILayout;
	private TextView mDebugLabel;
	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		useCloudRecognition(false);   
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setGravity(Gravity.CENTER);
		
		ImageView logoView = new ImageView(this);
		logoView.setImageResource(R.drawable.rajawali_vuforia);
		ll.addView(logoView);
		
		addContentView(ll, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());
		
		startVuforia();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		mScaleDetector.onTouchEvent(event);
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			/*HashMap<String, Integer> map = ((MainRenderer) mRenderer)
					.getVisibleObjects();
			if (map.get("formula") == 1) {
				Intent browserIntent = new Intent(this, GifActivity.class);
				this.startActivity(browserIntent);
			}
			if (map.get("elborlogo") == 1) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("http://www.electropribor-penza.ru/"));
				this.startActivity(browserIntent);
			}
			if (map.get("mushroom") == 1) {
				try {
					mSoungMgr.playTrack("track1.mp3");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}*/

			break;
		case MotionEvent.ACTION_MOVE:
			mDebugLabel.setText("ACTION_MOVE");
			break;
		case MotionEvent.ACTION_UP:
			mDebugLabel.setText("ACTION_UP");
			break;
		}
		
//		if (mRenderer.getSceneManager().getCurrentObject() != null)
//			mRenderer.getSceneManager().getCurrentObject().setScale(mScaleFactor*mRenderer.getSceneManager().getCurrentObject().getInitScale());
//		if (mRenderer.getSceneManager().getCurrentGifObject() != null)
//			mRenderer.getSceneManager().getCurrentGifObject().setScale(mScaleFactor*mRenderer.getSceneManager().getCurrentGifObject().getInitScale());
//		if (mRenderer.getSceneManager().getCurrentVideoObject() != null)
//			mRenderer.getSceneManager().getCurrentVideoObject().setScale(mScaleFactor*mRenderer.getSceneManager().getCurrentVideoObject().getInitScale());
		return true;
	}

	@Override
	protected void setupTracker() {
		int result = initTracker(TRACKER_TYPE_MARKER);
		if (result == 1) {
			result = initTracker(TRACKER_TYPE_IMAGE);
			if (result == 1) {
				super.setupTracker();
			} else {
				RajLog.e("Couldn't initialize image tracker.");
			}
		} else {
			RajLog.e("Couldn't initialize marker tracker.");
		}
	}

	@Override
	protected void initApplicationAR() {
		super.initApplicationAR();
		
		createImageMarker("rudolf_pnz_db.xml");
	}

	@Override
	protected void initRajawali() {
		super.initRajawali();
		mRenderer = new MainRenderer(this);
		mRenderer.setSurfaceView(mSurfaceView);
		super.setRenderer(mRenderer);
		mSurfaceView.setOnTouchListener(this);

		mDebugLabel = new TextView(this);
		mDebugLabel.setText("DebugMode");

		mUILayout = this;
		mUILayout.addContentView(mDebugLabel, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}
	
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
		    mScaleFactor *= detector.getScaleFactor();
		
		    // Don't let the object get too small or too large.
		    mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

		    return true;
		}
	}
}
