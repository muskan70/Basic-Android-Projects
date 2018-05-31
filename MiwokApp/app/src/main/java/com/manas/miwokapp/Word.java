package com.manas.miwokapp;

public class Word {
     private String engWord,hindiWord;
     private int imageId,audioid;
     private boolean Has_IMAGE;

    public Word(String engWord,String hindiWord,int audioid)
    {
         this.engWord=engWord;
         this.hindiWord=hindiWord;
         Has_IMAGE=false;
         this.audioid=audioid;
    }
    public Word(String engWord,String hindiWord ,int imageId,int audioid)
    {
        this.engWord=engWord;
        this.hindiWord=hindiWord;
        this.imageId=imageId;
        this.audioid=audioid;
        Has_IMAGE=true;
    }
    public String getDefaultTranslation(){
         return engWord;
    }
    public String getHindiTranslation(){
        return hindiWord;
    }
    public int getImageResourceId(){return imageId;}
    public int getAudioResourceId(){return audioid;}
    public boolean hasImage()
    { return Has_IMAGE; }
}
