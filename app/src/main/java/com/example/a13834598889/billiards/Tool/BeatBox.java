package com.example.a13834598889.billiards.Tool;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xieyipeng on 2018/9/24.
 */

public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private AssetManager manager;
    private List<Sound> soundList = new ArrayList<>();

    public static final int MAX_SOUNDS=5;
    private SoundPool soundPool;

    public BeatBox(Context context) {
        manager = context.getAssets();
        soundPool=new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC,0);
        loadSounds();
    }

    private void loadSounds() {
        String[] soundNames;
        try {
            soundNames = manager.list(SOUNDS_FOLDER);
            for (String filrname :
                    soundNames) {
                String assetPath = SOUNDS_FOLDER + "/" + filrname;
                Sound sound = new Sound(assetPath);
                load(sound);
                soundList.add(sound);
            }
        } catch (IOException e) {
            Log.e(TAG, "loadSounds: "+e.getMessage() );
        }
    }

    private void load(Sound sound)throws IOException{
        AssetFileDescriptor descriptor=manager.openFd(sound.getAssetPath());
        int soundID=soundPool.load(descriptor,1);
        sound.setSoundID(soundID);
    }

    public void play(Sound sound){
        Integer soundID=sound.getSoundID();
        if (soundID==null){
            return;
        }
        soundPool.play(soundID,1.0f,1.0f,1,0,1.0f);
    }


    public List<Sound> getSoundList() {
        return soundList;
    }

    public void release(){
        soundPool.release();
    }


}
