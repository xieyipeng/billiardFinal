package com.example.a13834598889.billiards.Tool;

/**
 * Created by xieyipeng on 2018/9/24.
 */

public class Sound {
    private String assetPath;
    private String name;
    private Integer soundID;
    public Sound(String assetPath){
        this.assetPath=assetPath;
        String[] components=assetPath.split("/");
        String fileName=components[components.length-1];
        this.name=fileName.replace(".wav","");
    }
    public String getAssetPath(){
        return assetPath;
    }
    public String getName(){
        return name;
    }

    public Integer getSoundID() {
        return soundID;
    }

    public void setSoundID(Integer soundID) {
        this.soundID = soundID;
    }
}
