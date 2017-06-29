/*
 * Steganography utility to hide messages into cover files
 * Author: Samir Vaidya (mailto:syvaidya@gmail.com)
 * Copyright (c) 2017 Samir Vaidya
 */
package com.chenqihong.datahidingdemo.util;


import android.graphics.Bitmap;

import com.drew.metadata.Metadata;

/**
 * Class to hold image and its metadata
 */
public class ImageHolder {
    private Bitmap image;
    private Metadata metadata;

    /**
     * Default constructor
     *
     * @param image
     * @param metadata
     */
    public ImageHolder(Bitmap image, Metadata metadata) {
        this.image = image;
        this.metadata = metadata;
    }

    /**
     * Getter method for image
     * 
     * @return image
     */
    public Bitmap getImage() {
        return this.image;
    }

    /**
     * Setter method for image
     * 
     * @param image Value for image to be set
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     * Getter method for metadata
     * 
     * @return metadata
     */
    public Metadata getMetadata() {
        return this.metadata;
    }

    /**
     * Setter method for metadata
     * 
     * @param metadata Value for metadata to be set
     */
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
