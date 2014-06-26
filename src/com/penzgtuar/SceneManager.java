package com.penzgtuar;

import java.util.HashMap;

import android.content.Context;

import rajawali.materials.textures.TextureManager;
import rajawali.math.Quaternion;
import rajawali.math.vector.Vector3;
import rajawali.scene.RajawaliScene;

public class SceneManager{
//	private HashMap<String, EntityObject> objects = new HashMap<String, EntityObject>();
	private HashMap<String, GifObject> gifObjects = new HashMap<String, GifObject>();
	private HashMap<String, VideoObject> videoObjects = new HashMap<String, VideoObject>();
	private RajawaliScene mCurrentScene;
//	private EntityObject mCurrentObject = null;
	private GifObject mCurrentGifObject = null;
	private VideoObject mCurrentVideoObject = null;
	private Context context;
	private TextureManager textureManager;
	
	public SceneManager(Context cntxt, TextureManager txtmgr, RajawaliScene scn){
		context = cntxt;
		textureManager = txtmgr;
		mCurrentScene = scn;
	}

    public void setCurrentScene(RajawaliScene scn){
    	mCurrentScene = scn;
    }

//    public EntityObject createObject3DEntity(String name, String meshname){
//    	EntityObject entity = new EntityObject(name, meshname, context, textureManager);
//    	try {
//			mCurrentScene.addChild(entity.getRajawaliObject());
//		} catch (Exception e) {
//	       System.out.print("MyMessage@@@@@@@@@@@@@@@@@" + e.getMessage());
//		}
//		objects.put(name, entity);
//
//    	return entity;
//    }
    
    public GifObject createGifObject(String name, String gifName){
    	GifObject gifobject = new GifObject(name, gifName, context, textureManager);
    	try {
			mCurrentScene.addChild(gifobject.getRajawaliObject());
		} catch (Exception e) {
	       System.out.print("MyMessage@@@@@@@@@@@@@@@@@" + e.getMessage());
		}
    	gifObjects.put(name, gifobject);
    	return gifobject;
    }
    
    public VideoObject createVideoObject(String name, String videoName){
    	VideoObject videoobject = new VideoObject(name, videoName, context, textureManager);
    	try {
			mCurrentScene.addChild(videoobject.getRajawaliObject());
		} catch (Exception e) {
	       System.out.print("MyMessage@@@@@@@@@@@@@@@@@" + e.getMessage());
		}
    	videoObjects.put(name, videoobject);
    	return videoobject;
    }
    
//    public EntityObject getCurrentObject(){
//    	return mCurrentObject;
//    }
    
    public GifObject getCurrentGifObject(){
    	return mCurrentGifObject;
    }
    
    public VideoObject getCurrentVideoObject(){
    	return mCurrentVideoObject;
    }
    
//    private void setCurrentObject(EntityObject obj){
//    	mCurrentObject = obj;
//    }
    
    private void setCurrentGifObject(GifObject obj){
    	mCurrentGifObject = obj;
    }
    
    private void setCurrentVideoObject(VideoObject obj){
    	mCurrentVideoObject = obj;
    }
    
//    public EntityObject getObjectByName(String name){
//    	if (objects.get(name) != null)
//    		return objects.get(name);
//    	
//    	return null;
//    }
    
    public GifObject getGifObjectByName(String name){
    	if (gifObjects.get(name) != null)
    		return gifObjects.get(name);
    	
    	return null;
    }

	public void foundImageMarker(String name, Vector3 pos, Quaternion orient) {
//		if (name.equals("letterg")){
//			EntityObject entity = objects.get(name);
//			if (entity!=null){
//				entity.processFoundMarker(pos, orient);
//		    	setCurrentObject(entity);
//			}
//			else{
//				createObject3DEntity("letterg", "mushrooms_obj").setInitScale(200);
//			}
//		}
		
		if(name.equals("letterc"))
		{
			GifObject entity = gifObjects.get(name);
			if (entity!=null){
				entity.processFoundMarker(pos, orient);
		    	setCurrentGifObject(entity);
			}
			else{
				createGifObject("letterc", "hana").setInitScale(2);
			}
		}
		
		if(name.equals("letterr"))
		{
			VideoObject entity = videoObjects.get(name);
			if (entity!=null){
				entity.processFoundMarker(pos, orient);
		    	setCurrentVideoObject(entity);
		    	//getCurrentVideoObject().processPause(true);
			}
			else{
				setCurrentVideoObject(createVideoObject("letterr", "sintel_trailer_480p"));
			}
		}
	}

	public void noFrameMarkersFound() {
//		if (getCurrentObject() != null){
//			getCurrentObject().getRajawaliObject().setVisible(false);
//			setCurrentObject(null);
//		}
		
		if (getCurrentGifObject() != null){
			getCurrentGifObject().getRajawaliObject().setVisible(false);
			setCurrentGifObject(null);
		}
		
		if (getCurrentVideoObject() != null){
			//getCurrentVideoObject().getRajawaliObject().setVisible(false);
			getCurrentVideoObject().processStop();
			mCurrentScene.removeChild(mCurrentVideoObject.getRajawaliObject());
			setCurrentVideoObject(null);
		}
	}
	
	public void onSurfaceDestroyed(){
		if (mCurrentVideoObject != null)
			mCurrentVideoObject.processStop();
	}
	
	public void OnDrawFrame() {
		if (mCurrentGifObject != null)
			mCurrentGifObject.update();
		if (mCurrentVideoObject != null)
			mCurrentVideoObject.update();
	}
}