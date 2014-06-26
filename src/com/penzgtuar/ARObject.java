package com.penzgtuar;

import android.content.Context;
import rajawali.Object3D;
import rajawali.math.Quaternion;
import rajawali.math.vector.Vector3;

public class ARObject {
	protected Object3D mEntity = null;
	protected double initScale = 1;
	protected Context mContext;

	ARObject(Context context) {
		mContext = context;
	}

	protected void processFoundMarker(Vector3 pos, Quaternion orient) {
		mEntity.setPosition(pos);
		mEntity.setOrientation(orient);
		mEntity.setVisible(true);
	}

	protected void setScale(double scale) {
		mEntity.setScale(scale);
	}

	protected void processLostMarker() {
		mEntity.setVisible(false);
	}

	protected Object3D getRajawaliObject() {
		return mEntity;
	}

	protected void setInitScale(double scale) {
		initScale = scale;
		mEntity.setScale(scale);
	}

	protected double getInitScale() {
		return initScale;
	}

	protected void update() {

	}

	protected void onDestroy() {

	}
}