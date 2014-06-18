package com.penzgtuar;

import android.content.Context;
import android.content.res.Resources;

import rajawali.Object3D;
import rajawali.materials.Material;
import rajawali.materials.textures.AnimatedGIFTexture;
import rajawali.materials.textures.TextureManager;
import rajawali.materials.textures.ATexture.TextureException;
import rajawali.math.Quaternion;
import rajawali.math.vector.Vector3;
import rajawali.parser.LoaderOBJ;

public class GifObject {
    private Object3D mEntity;
    private double initScale = 1;
    private AnimatedGIFTexture mGifTexture;
    
    public GifObject(String entname, String gifname, Context mContext, TextureManager mTextureManager){
    	final Material material = new Material();
    	Resources res1 = mContext.getResources();
    	int meshID = res1.getIdentifier("plane_obj", "raw", mContext.getPackageName());
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
    	Resources res = mContext.getResources();
    	int gifID = res.getIdentifier(gifname, "drawable", mContext.getPackageName());
		try {
			mGifTexture = new AnimatedGIFTexture(entname, gifID);
			material.addTexture(mGifTexture);
			material.setColorInfluence(0);
			mGifTexture.rewind();
		} catch (TextureException e) {
			e.printStackTrace();
		}
    }
    
    public void update(){
		if (mGifTexture != null) {
			try {
				mGifTexture.update();
			} catch (TextureException e) {
				e.printStackTrace();
			}
		}
    }
    
    public void processFoundMarker(Vector3 pos, Quaternion orient){
    	mEntity.setPosition(pos);
    	mEntity.setOrientation(orient);
    	mEntity.setVisible(true);
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