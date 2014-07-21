package com.penzgtuar;

import com.penzgtuar.penzgtuarbrowser.R;
import com.qualcomm.vuforia.CameraDevice;
import com.qualcomm.vuforia.HINT;
import com.qualcomm.vuforia.Vuforia;

import rajawali.util.RajLog;
import rajawali.vuforia.RajawaliVuforiaActivity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
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

		addContentView(ll, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());

		startVuforia();

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		super.onTouchEvent(event);
		mScaleDetector.onTouchEvent(event);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// add action on touch
			break;
		}

		// action on
		if (mRenderer.getSceneManager().getCurrentARObject() != null)
			mRenderer
					.getSceneManager()
					.getCurrentARObject()
					.setScale(
							mScaleFactor
									* mRenderer.getSceneManager()
											.getCurrentARObject()
											.getInitScale());

		return true;
	}

	@Override
	public void onDestroy() {
		System.out.println("onDestroyActivityOccured ");
		if (mRenderer != null)
			mRenderer.getSceneManager().processVideoThreadStop();
		super.onDestroy();
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
		mDebugLabel.setText("");

		mUILayout = this;
		mUILayout.addContentView(mDebugLabel, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}

	private class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mScaleFactor *= detector.getScaleFactor();

			// Don't let the object get too small or too large.
			mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

			return true;
		}
	}
}
