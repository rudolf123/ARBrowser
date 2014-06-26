package com.penzgtuar;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;

public class SoundClip {
	private MediaPlayer mediaPlayer = null;

	SoundClip(Context context, String fileName) {
		Resources res = context.getResources();
		int soundID = res.getIdentifier(fileName, "sounds",
				context.getPackageName());
		mediaPlayer = MediaPlayer.create(context, soundID);
	}

	protected void playSound() {
		if (mediaPlayer != null)
			if (!mediaPlayer.isPlaying())
				mediaPlayer.start();
	}

	protected void pauseSound() {
		if (mediaPlayer != null)
			mediaPlayer.pause();
	}

	protected void stopSound() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			releaseMediaPlayer();
		}
	}

	protected void releaseMediaPlayer() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
}