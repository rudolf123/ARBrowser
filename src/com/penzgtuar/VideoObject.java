package com.penzgtuar;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;

import rajawali.Object3D;
import rajawali.materials.Material;
import rajawali.materials.textures.TextureManager;
import rajawali.materials.textures.ATexture.TextureException;
import rajawali.materials.textures.VideoTexture;
import rajawali.parser.LoaderOBJ;

public class VideoObject extends ARObject {
	private VideoTexture mVideoTexture;
	private MediaPlayer mMediaPlayer;

	public VideoObject(String entname, String videoname, Context mContext,
			TextureManager mTextureManager) {
		super(mContext);
		Resources res = mContext.getResources();
		int videoID = res.getIdentifier(videoname, "raw",
				mContext.getPackageName());
		mMediaPlayer = MediaPlayer.create(mContext, videoID);
		mMediaPlayer.setLooping(true);
		mVideoTexture = new VideoTexture(entname, mMediaPlayer);
		Material material = new Material();
		material.setColorInfluence(0);
		try {
			material.addTexture(mVideoTexture);
		} catch (TextureException e) {
			e.printStackTrace();
		}
		int meshID = res.getIdentifier("plane_obj", "raw",
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

		mMediaPlayer.start();
	}

	public void pause() {
		if (mMediaPlayer != null)
			mMediaPlayer.pause();
	}

	public void play() {
		if (!mMediaPlayer.isPlaying())
			mMediaPlayer.start();
	}

	@Override
	public void update() {
		if (mVideoTexture != null)
			mVideoTexture.update();
	}

	public void processStop() {
		mEntity.getMaterial().unbindTextures();
		mMediaPlayer.stop();
		mMediaPlayer.release();
	}

	public void processPause(boolean pause) {
		if (pause)
			if (mMediaPlayer != null && mMediaPlayer.isPlaying())
				mMediaPlayer.pause();
			else if (mMediaPlayer != null)
				mMediaPlayer.start();
	}
}
