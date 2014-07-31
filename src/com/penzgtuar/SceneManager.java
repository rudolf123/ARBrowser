package com.penzgtuar;

import java.util.HashMap;

import android.content.Context;

import rajawali.materials.textures.TextureManager;
import rajawali.math.Quaternion;
import rajawali.math.vector.Vector3;
import rajawali.renderer.RajawaliRenderer;
import rajawali.scene.RajawaliScene;

public class SceneManager {
	private HashMap<String, MeshObject> meshObjects = new HashMap<String, MeshObject>();
	private HashMap<String, GifObject> gifObjects = new HashMap<String, GifObject>();
	private HashMap<String, VideoObject> videoObjects = new HashMap<String, VideoObject>();
	private HashMap<String, AnimatedMeshObject> animatedMeshObjects = new HashMap<String, AnimatedMeshObject>();
	private RajawaliScene mCurrentScene;
	private MeshObject mCurrentMeshObject = null;
	private GifObject mCurrentGifObject = null;
	private VideoObject mCurrentVideoObject = null;
	private AnimatedMeshObject mCurrentAnimatedMeshObject = null;
	private Context context;
	private TextureManager textureManager;
	private ARObject mCurrentARObject;
	private RajawaliRenderer mRenderer;
	private boolean isVideoDestroed = false;

	public SceneManager(Context cntxt, RajawaliRenderer renderer) {
		context = cntxt;
		mRenderer = renderer;
		textureManager = renderer.getTextureManager();
		mCurrentScene = renderer.getCurrentScene();
	}

	public void setCurrentScene(RajawaliScene scn) {
		mCurrentScene = scn;
	}

	public MeshObject createMeshObject(String name, String meshname) {
		MeshObject entity = new MeshObject(name, meshname, context,
				textureManager);
		try {
			mCurrentScene.addChild(entity.getRajawaliObject());
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		meshObjects.put(name, entity);
		setCurrentMeshObject(entity);

		return entity;
	}

	public AnimatedMeshObject createAnimatedMeshObject(String name,
			String meshname, String animname) {
		AnimatedMeshObject entity = new AnimatedMeshObject(name, meshname,
				animname, mRenderer);
		try {
			mCurrentScene.addChild(entity.getRajawaliObject());
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		animatedMeshObjects.put(name, entity);
		setCurrentAnimatedMeshObject(entity);

		return entity;
	}

	public GifObject createGifObject(String name, String gifName) {
		GifObject gifobject = new GifObject(name, gifName, context,
				textureManager);
		try {
			mCurrentScene.addChild(gifobject.getRajawaliObject());
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		gifObjects.put(name, gifobject);
		mCurrentARObject = gifobject;

		return gifobject;
	}

	public VideoObject createVideoObject(String name, String videoName) {
		VideoObject videoobject = new VideoObject(name, videoName, context,
				textureManager);
		try {
			mCurrentScene.addChild(videoobject.getRajawaliObject());
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		videoObjects.put(name, videoobject);
		setCurrentVideoObject(videoobject);

		return videoobject;
	}

	public ARObject getCurrentARObject() {
		return mCurrentARObject;
	}

	public MeshObject getCurrentMeshObject() {
		return mCurrentMeshObject;
	}

	public AnimatedMeshObject getCurrentAnimatedMeshObject() {
		return mCurrentAnimatedMeshObject;
	}

	public GifObject getCurrentGifObject() {
		return mCurrentGifObject;
	}

	public VideoObject getCurrentVideoObject() {
		return mCurrentVideoObject;
	}

	private void setCurrentMeshObject(MeshObject obj) {
		mCurrentARObject = obj;
		mCurrentMeshObject = obj;
	}

	private void setCurrentAnimatedMeshObject(AnimatedMeshObject obj) {
		mCurrentARObject = obj;
		mCurrentAnimatedMeshObject = obj;
	}

	private void setCurrentGifObject(GifObject obj) {
		mCurrentARObject = obj;
		mCurrentGifObject = obj;
	}

	private void setCurrentVideoObject(VideoObject obj) {
		mCurrentARObject = obj;
		mCurrentVideoObject = obj;
	}

	public GifObject getGifObjectByName(String name) {
		if (gifObjects.get(name) != null)
			return gifObjects.get(name);

		return null;
	}

	public void foundImageMarker(String name, Vector3 pos, Quaternion orient) {
		// System.out.println("Found model: " + name);
		if (name.equals("letterg")) {
			MeshObject entity = meshObjects.get(name);
			if (entity != null) {
				entity.processFoundMarker(pos, orient);
				setCurrentMeshObject(entity);
			} else {
				createMeshObject("letterg", "mushrooms_obj").setInitScale(200);
			}
		}

		if (name.equals("formula_")) {
			GifObject entity = gifObjects.get(name);
			if (entity != null) {
				entity.processFoundMarker(pos, orient);
				setCurrentGifObject(entity);
			} else {
				createGifObject("formula_", "hana").setInitScale(2);
			}
		}

		if (name.equals("letterc")) {
			VideoObject entity = videoObjects.get(name);
			if (entity != null) {
				entity.processFoundMarker(pos, orient);
				setCurrentVideoObject(entity);
				entity.play();
			} else {
				setCurrentVideoObject(createVideoObject("letterc",
						"sintel_trailer_480p"));
			}
		}

		if (name.equals("letterr")) {
			AnimatedMeshObject entity = animatedMeshObjects.get(name);
			if (entity != null) {
				entity.processFoundMarker(pos, orient);
				setCurrentAnimatedMeshObject(entity);
			} else {
				setCurrentAnimatedMeshObject(createAnimatedMeshObject(
						"letterr", "boblampclean_mesh", "boblampclean_anim"));
			}
		}

	}

	public void noFrameMarkersFound() {
		if (getCurrentMeshObject() != null) {
			getCurrentMeshObject().getRajawaliObject().setVisible(false);
			setCurrentMeshObject(null);
		}

		if (getCurrentGifObject() != null) {
			getCurrentGifObject().getRajawaliObject().setVisible(false);
			setCurrentGifObject(null);
		}

		if (getCurrentVideoObject() != null) {
			getCurrentVideoObject().getRajawaliObject().setVisible(false);
			getCurrentVideoObject().pause();
			setCurrentVideoObject(null);
		}
	}

	public void processVideoThreadStop() {
		if (mCurrentVideoObject != null) {
			System.out.println("ProcessingStopVideo ");
			mCurrentVideoObject.processStop();
			mCurrentVideoObject = null;
		}
	}

	public void OnDrawFrame() {
		if (mCurrentGifObject != null)
			mCurrentGifObject.update();
		if (mCurrentVideoObject != null)
			mCurrentVideoObject.update();
	}
}