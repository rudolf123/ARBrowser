package com.penzgtuar;

import android.content.Context;
import android.content.res.Resources;

import rajawali.Object3D;
import rajawali.materials.textures.TextureManager;
import rajawali.parser.LoaderOBJ;

public class MeshObject extends ARObject {
	public MeshObject(String entname, String meshname, Context mContext,
			TextureManager mTextureManager) {
		super(mContext);
		Resources res = mContext.getResources();
		int meshID = res.getIdentifier(meshname, "raw",
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
	}
}
