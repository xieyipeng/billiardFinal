package com.example.a13834598889.billiards.Tool;


/**
 * Created by xieyipeng on 2018/9/24.
 */

public class SoundViewModel{
    private Sound sound;
    private BeatBox beatBox;

    public SoundViewModel(BeatBox beatBox) {
        this.beatBox = beatBox;
    }


    public Sound getSound() {
        return sound;
    }

    public void onButtonClicked() {
        beatBox.play(sound);
    }
}
