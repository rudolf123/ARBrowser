package com.penzgtuar;

import rajawali.math.Quaternion;
import rajawali.math.vector.Vector3;

public interface FoundImageMarkerListener {
	public void foundImageMarker(String name, Vector3 pos, Quaternion orient);
	public void noFrameMarkersFound();
}
