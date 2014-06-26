package com.penzgtuar;

import javax.microedition.khronos.opengles.GL10;

import rajawali.math.Quaternion;
import rajawali.math.vector.Vector3;
import rajawali.vuforia.RajawaliVuforiaRenderer;
import android.content.Context;

public class MainRenderer extends RajawaliVuforiaRenderer {
	private Context context;
	private SceneManager mSceneMgr = null;

	public MainRenderer(Context context) {
		super(context);
		this.context = context;
	}

	public void onVisibilityChanged(boolean visible) {
		super.onVisibilityChanged(visible);
	}

	public void onSurfaceDestroyed() {
		super.onSurfaceDestroyed();
		mSceneMgr.onSurfaceDestroyed();
	}

	public SceneManager getSceneManager() {
		return mSceneMgr;
	}

	protected void initScene() {
		mSceneMgr = new SceneManager(context, this);
	}

	@Override
	protected void foundFrameMarker(int markerId, Vector3 position,
			Quaternion orientation) {
	}

	@Override
	protected void foundImageMarker(String trackableName, Vector3 position,
			Quaternion orientation) {
		mSceneMgr.foundImageMarker(trackableName, position, orientation);
	}

	@Override
	public void noFrameMarkersFound() {
		mSceneMgr.noFrameMarkersFound();
	}

	public void onDrawFrame(GL10 glUnused) {
		super.onDrawFrame(glUnused);
		mSceneMgr.OnDrawFrame();
	}
}
