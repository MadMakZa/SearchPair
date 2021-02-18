package com.example.searchpair;

public class Card {

    private int imageResource;
    private String tagImage;

    public Card(int imageResource, String tagImage) {
        this.imageResource = imageResource;
        this.tagImage = tagImage;
    }

    public String getTagImage() {
        return tagImage;
    }

    public void setTagImage(String tagImage) {
        this.tagImage = tagImage;
    }
    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
