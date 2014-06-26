package com.penzgtuar;

import android.content.Context;
import android.content.res.Resources;

import rajawali.Object3D;
import rajawali.materials.Material;
import rajawali.materials.textures.AnimatedGIFTexture;
import rajawali.materials.textures.TextureManager;
import rajawali.materials.textures.ATexture.TextureException;
import rajawali.parser.LoaderOBJ;

public class GifObject extends ARObject {
	private AnimatedGIFTexture mGifTexture;

	public GifObject(String entname, String gifname, Context mContext,
			TextureManager mTextureManager) {
		super(mContext);
		final Material material = new Material();
		Resources res1 = mContext.getResources();
		int meshID = res1.getIdentifier("plane_obj", "raw",
				mContext.getPackageName());
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
		int gifID = res.getIdentifier(gifname, "drawable",
				mContext.getPackageName());
		try {
			mGifTexture = new AnimatedGIFTexture(entname, gifID);
			material.addTexture(mGifTexture);
			material.setColorInfluence(0);
			mGifTexture.rewind();
		} catch (TextureException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		if (mGifTexture != null) {
			try {
				mGifTexture.update();
			} catch (TextureException e) {
				e.printStackTrace();
			}
		}
	}
}
