package com.penzgtuar;

import android.content.Context;
import android.content.res.Resources;

import rajawali.Object3D;
import rajawali.materials.textures.TextureManager;
import rajawali.math.Quaternion;
import rajawali.math.vector.Vector3;
import rajawali.parser.LoaderOBJ;

public class EntityObject {
    private Object3D mEntity;
    private double initScale = 1;

    public EntityObject(String entname, String meshname, Context mContext, TextureManager mTextureManager){
    	Resources res = mContext.getResources();
    	int meshID = res.getIdentifier(meshname, "raw", mContext.getPackageName());
    	try {
	    	LoaderOBJ meshObjParser = new LoaderOBJ(mContext.getResources(),
					mTextureManager, meshID);
			meshObjParser.parse();
			mEntity = (Object3D) meshObjParser.getParsedObject();
			mEntity.setName(entname);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void processFoundMarker(Vector3 pos, Quaternion orient){
    	mEntity.setPosition(pos);
    	mEntity.setOrientation(orient);
    	mEntity.setVisible(true);
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
    
    public double getInitScale(){
    	return initScale;
    }
}
