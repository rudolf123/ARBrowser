package com.penzgtuar;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import com.penzgtuar.penzgtuarbrowser.R;

public class SoundManager {

	private SoundPool sp;
	private MediaPlayer mp = null;
	private HashMap<String, MediaPlayer> tracks = new HashMap<String, MediaPlayer>();
	private Context context;
	boolean created = false;
	static boolean isPlayingAsset = false;
	static AssetManager assetManager;
	private SoundClip mCurrentSoundClip;

	boolean isUseOpenSL = false;

	public SoundManager(Context c, boolean useOpenSl) {
		this.context = c;
		isUseOpenSL = useOpenSl;
		assetManager = c.getAssets();

		if (isUseOpenSL) {
			createEngine(10);
		} else {
			sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		}
	}

	public void addTrack(String trackname, String filename) throws IOException {
		if (tracks.containsKey(trackname))
			return;

		AssetFileDescriptor afd = assetManager.openFd(filename);
		mp = new MediaPlayer();
		mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
				afd.getLength());
		mp.prepare();
		mp.setLooping(true);
		mp.start();
		mp.pause();
		tracks.put(trackname, mp);
	}

	public void playTrack(String name) {
		if (tracks.containsKey(name)) {
			if (!tracks.get(name).isPlaying())
				tracks.get(name).start();
		}
	}

	public void pauseTrack(String name) {
		if (tracks.containsKey(name))
			tracks.get(name).pause();
	}

	public void stopTrack(String name) {
		if (tracks.containsKey(name)) {
			tracks.get(name).stop();
			tracks.get(name).reset();
		}
	}

	private void release() {
		if (isUseOpenSL)
			nativeRelease();
	}

	public void onDestroy() {
		release();
		for (HashMap.Entry<String, MediaPlayer> entry : tracks.entrySet()) {
			MediaPlayer mp = entry.getValue();
			if (mp != null) {
				mp.stop();
				mp.release();
				mp = null;
			}

		}
	}

	private static native void createEngine(int maxStreams);

	private static native int load(FileDescriptor fd, int offset, int length);

	private static native int nativePlay(int soundId, float volume);

	private static native int nativeRelease();

	/** Load jni .so on initialization */
	static {
		// System.loadLibrary("native-audio-jni");
		System.loadLibrary("opensl-soundpool");
	}
}