	package com.shinkansen.touchcolor.soundmanager;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;

import com.shinkansen.touchcolor.constant.Constant;

public class SoundManager {
	 
    private SoundPool mSoundPool;
    private HashMap<Integer, Integer> mSoundPoolMap;
    private AudioManager mAudioManager;
    private Context mContext;
    private static SoundManager soundInstance;
    private boolean isTurnOffSound = false;
    
    public static synchronized SoundManager getInstance() {
		if (soundInstance == null){
			soundInstance = new SoundManager();
		}
		return soundInstance;
	}
    public SoundManager() {
       
    }
    public void initSoundBackground(Context ct) {
    	mContext = ct;
    	mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        mSoundPoolMap = new HashMap<Integer, Integer>();
        mAudioManager = (AudioManager) mContext
                .getSystemService(Context.AUDIO_SERVICE);
        addSound(1, Constant.SOUND_ID[0]);
	}
    public void initSound(Context ct) {
    	
        for (int i = 1; i < Constant.SOUND_ID.length; i++) {
			addSound(i+1, Constant.SOUND_ID[i]);
		}
    }
   
    public void addSound(int index, int soundID) {
        mSoundPoolMap.put(index, mSoundPool.load(mContext, soundID, 1));
    }
 
    public void playSound(int index) {
       if (!isTurnOffSound){
    	   int streamVolume = mAudioManager
                   .getStreamVolume(AudioManager.STREAM_MUSIC);
           mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume,
                   1, 0, 1f);
       }
    }
 
    public void playLoopedSound(int index) {
 
        if (!isTurnOffSound){
        	int streamVolume = mAudioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC);
            mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume,
                    1, -1, 1f);
        }
    }
    public void setTurnOffSound(boolean turnOff) {
		isTurnOffSound = turnOff;
	}
}