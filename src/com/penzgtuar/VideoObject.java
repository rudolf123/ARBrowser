package com.penzgtuar;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;

import rajawali.Object3D;
import rajawali.materials.Material;
import rajawali.materials.textures.TextureManager;
import rajawali.materials.textures.ATexture.TextureException;
import rajawali.materials.textures.VideoTexture;
import rajawali.math.Quaternion;
import rajawali.math.vector.Vector3;
import rajawali.parser.LoaderOBJ;

public class VideoObject {
    private Object3D mEntity;
    private double initScale = 1;
    private VideoTexture mVideoTexture;
    private MediaPlayer mMediaPlayer;

    
    public VideoObject(String entname, String videoname, Context mContext, TextureManager mTextureManager){
    	Resources res = mContext.getResources();
    	int videoID = res.getIdentifier(videoname, "raw", mContext.getPackageName());
    	mMediaPlayer = MediaPlayer.create(mContext,
    			videoID);
		mMediaPlayer.setLooping(true);
		mVideoTexture = new VideoTexture(entname, mMediaPlayer);
		Material material = new Material();
		material.setColorInfluence(0);
		try {
			material.addTexture(mVideoTexture);
		} catch (TextureException e) {
			e.printStackTrace();
		}
    	int meshID = res.getIdentifier("plane_obj", "raw", mContext.getPackageName());
    	try {
	    	LoaderOBJ meshObjParser = new LoaderOBJ(mContext.getResources(),
					mTextureManager, meshID);
			meshObjParser.parse();
			mEntity = (Object3D) meshObjParser.getParsedObject();
			mEntity.setName(entname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mEntity.setMaterial(material);
		
		mMediaPlayer.start();
    }
    
    public void update(){
		if (mVideoTexture != null) 
			mVideoTexture.update();
    }
    
    public void processFoundMarker(Vector3 pos, Quaternion orient){
    	mEntity.setPosition(pos);
    	mEntity.setOrientation(orient);
    	mEntity.setVisible(true);
    }
    
    public void processStop(){
    	mEntity.getMaterial().unbindTextures();
    	mVideoTexture.shouldRecycle(true);
    	mEntity.setMaterial(null);
    	mMediaPlayer.stop();
		mMediaPlayer.release();
    }
    
    public void processPause(boolean pause){
		if (pause)
			if (mMediaPlayer != null && mMediaPlayer.isPlaying())
				mMediaPlayer.pause();
			else if (mMediaPlayer != null)
				mMediaPlayer.start();
    }
    
    public double getInitScale(){
    	return initScale;
    }
    
    public void setScale(double scale){
    	mEntity.setScale(scale);
    }
    
    public void processLostMarker(){
    	mEntity.setVisible(false);
    }
    
    public Object3D getRajawaliObject(){
    	return mEntity;
    }
    
    public void setInitScale(double scale){
    	initScale = scale;
    	mEntity.setScale(scale);
    }
}
