package com.penzgtuar;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import com.penzgtuar.penzgtuarbrowser.R;

public class SoundManager {

	private SoundPool sp;
	private MediaPlayer mp = null;
	private HashMap<Integer, Integer> bind_map = new HashMap<Integer, Integer>();
	private HashMap<String, Integer> tracks = new HashMap<String, Integer>();
	private HashMap<String, Integer> samples = new HashMap<String, Integer>();
	private HashMap<String, SoundClip> soundClips = new HashMap<String, SoundClip>();
	private Context context;
	boolean created = false;
	static boolean isPlayingAsset = false;
	static AssetManager assetManager;
	private SoundClip mCurrentSoundClip;

	boolean isUseOpenSL = false;

	public SoundManager(Context c, int max_streams, AssetManager asstMgr,
			boolean useOpenSl) {
		this.context = c;
		isUseOpenSL = useOpenSl;
		assetManager = asstMgr;
		// mp = new MediaPlayer();

		if (isUseOpenSL) {
			createEngine(max_streams);
		} else {
			sp = new SoundPool(max_streams, AudioManager.STREAM_MUSIC, 0);
		}
	}

	public boolean initSoundSamples() {

		Field[] fields = R.raw.class.getFields();
		// loop for every file in raw folder
		for (int count = 0; count < fields.length; count++) {
			try {
				int rid = fields[count].getInt(fields[count]);
				// Use that if you just need the file name
				String filename = fields[count].getName();
				// skip bad streams
				if (filename.startsWith("daftpunk")
						|| filename.startsWith("noise")) {
					continue;
				}
				loadSample(filename, rid);
				// do whatever you need with the in stream
			} catch (Exception e) {
				// log error
			}
		}
		return true;
	}

	public boolean initSoundTracks() {
		// context.getAssets().openFd("track1.mp3");
		tracks.put("track1", 1);
		return true;
	}

	// input - raw data sound
	// return handle on sound
	public int loadSample(String name, int sound) {
		int init_sound = 0;
		if (isUseOpenSL) {
			// try loading a resource into the native code
			AssetFileDescriptor afd = context.getResources().openRawResourceFd(
					sound);
			if (afd != null) {
				FileDescriptor fd = afd.getFileDescriptor();
				int off = (int) afd.getStartOffset();
				int len = (int) afd.getLength();
				init_sound = load(fd, off, len);
			}
		} else {
			init_sound = sp.load(context, sound, 1);
		}
		samples.put(name, init_sound);

		return init_sound;
	}

	// input - numOfFinger and sound_name
	public boolean bindSample(int numOfFinger, String sound_name) {
		bind_map.put(numOfFinger, samples.get(sound_name));
		return true;
	}

	public boolean clearSoundMap() {
		bind_map.clear();
		return true;
	}

	public HashMap<String, Integer> getListOfSounds() {
		return this.samples;
	}

	public void playSample(int numOfFinger, float volume) {
		if (isUseOpenSL) {
			nativePlay(bind_map.get(numOfFinger), volume);
		} else {
			sp.play(bind_map.get(numOfFinger), volume, volume, 1, 0, 1);
		}
	}

	public void playTrack(String name) throws IOException {
		AssetFileDescriptor afd = assetManager.openFd(name);
		mp = new MediaPlayer();
		mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
				afd.getLength());
		mp.prepare();
		mp.start();
	}

	public void stopTrack() {
		if (mp != null) {
			mp.reset();
			mp.release();
			mp = null;
		}
	}

	public void release() {
		if (isUseOpenSL)
			nativeRelease();
	}

	private boolean listAssetFiles(String path) {

		String[] list;
		try {
			list = context.getAssets().list(path);
			if (list.length > 0) {
				// This is a folder
				for (String file : list) {
					if (!listAssetFiles(path + "/" + file))
						return false;
				}
			} else {
				// This is a file
				// TODO: add file name to an array list
			}
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public void initSound(String filename) {
		mCurrentSoundClip = new SoundClip(context, filename);
	}

	public void playSound() {
		if (mCurrentSoundClip != null)
			mCurrentSoundClip.playSound();
	}

	public void stopSound() {
		if (mCurrentSoundClip != null)
			mCurrentSoundClip.stopSound();
	}

	public void pauseSound() {
		if (mCurrentSoundClip != null)
			mCurrentSoundClip.pauseSound();
	}

	public void onDestroy() {

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