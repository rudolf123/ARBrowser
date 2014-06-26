package com.penzgtuar;

import rajawali.animation.mesh.SkeletalAnimationObject3D;
import rajawali.animation.mesh.SkeletalAnimationSequence;
import rajawali.parser.ParsingException;
import rajawali.parser.md5.LoaderMD5Anim;
import rajawali.parser.md5.LoaderMD5Mesh;
import rajawali.renderer.RajawaliRenderer;

import android.content.res.Resources;

public class AnimatedMeshObject extends ARObject {
	private SkeletalAnimationObject3D mObject;

	public AnimatedMeshObject(String entname, String meshname, String animname,
			RajawaliRenderer mRenderer) {
		super(mRenderer.getContext());
		Resources res = mContext.getResources();
		int meshID = res.getIdentifier(meshname, "raw",
				mContext.getPackageName());
		int animID = res.getIdentifier(animname, "raw",
				mContext.getPackageName());
		try {
			LoaderMD5Mesh meshParser = new LoaderMD5Mesh(mRenderer, meshID);
			meshParser.parse();

			LoaderMD5Anim animParser = new LoaderMD5Anim(animname, mRenderer,
					animID);
			animParser.parse();

			SkeletalAnimationSequence sequence = (SkeletalAnimationSequence) animParser
					.getParsedAnimationSequence();

			mObject = (SkeletalAnimationObject3D) meshParser
					.getParsedAnimationObject();
			mObject.setAnimationSequence(sequence);
			mObject.setScale(.04f);
			mObject.play();
			mEntity = mObject;
			mRenderer.getCurrentScene().addChild(mObject);
		} catch (ParsingException e) {
			e.printStackTrace();
		}
	}
}