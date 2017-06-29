package com.chenqihong.datahidingdemo.hiding.lsb;

import android.graphics.Bitmap;

import com.chenqihong.datahidingdemo.util.ImageHolder;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by chenqihong on 2017/6/29.
 */

public class LSBOutputStream extends OutputStream {


    /**
     * Output Image data
     */
    private ImageHolder image = null;

    /**
     * Number of bits used per color channel
     */
    private int channelBitsUsed = 1;

    /**
     * Length of the data
     */
    private int dataLength = 0;

    /**
     * Name of the source data file
     */
    private String fileName = null;

    /**
     * Current x co-ordinate
     */
    private int x = 0;

    /**
     * Current y co-ordinate
     */
    private int y = 0;

    /**
     * Current bit number to be read
     */
    private int currBit = 0;

    /**
     * Bit set to store three bits per pixel
     */
    private byte[] bitSet = null;

    /**
     * Width of the image
     */
    private int imgWidth = 0;

    /**
     * Height of the image
     */
    private int imgHeight = 0;

    /**
     * Default constructor
     *
     * @param image Source image into which data will be embedded
     * @param dataLength Length of the data that would be written to the image
     * @param fileName Name of the source data file
     */
    public LSBOutputStream(ImageHolder image, int dataLength, String fileName)  {
        if (image == null || image.getImage() == null) {
            return;
        }

        this.dataLength = dataLength;
        this.imgWidth = image.getImage().getWidth();
        this.imgHeight = image.getImage().getHeight();
        Bitmap newImg = Bitmap.createBitmap(this.imgWidth, this.imgHeight, Bitmap.Config.ARGB_8888);
        this.image = new ImageHolder(newImg, image.getMetadata());
        for (int x = 0; x < this.imgWidth; x++) {
            for (int y = 0; y < this.imgHeight; y++) {
                newImg.setPixel(x, y, image.getImage().getPixel(x, y));
            }
        }

        this.channelBitsUsed = 1;
        this.fileName = fileName;
        this.bitSet = new byte[3];
        writeHeader();
    }

    /**
     * Method to write header data to stream
     *
     */
    private void writeHeader(){
        int channelBits = 1;
        int noOfPixels = 0;
        int headerSize = 0;
        LSBDataHeader header = null;

        try {
            noOfPixels = this.imgWidth * this.imgHeight;
            header = new LSBDataHeader(this.dataLength, channelBits, this.fileName);
            headerSize = header.getHeaderSize();

            while (true) {
                if ((noOfPixels * 3 * channelBits) / 8.0 < (headerSize + this.dataLength)) {
                    channelBits++;
                } else {
                    break;
                }
            }

            // Update channelBitsUsed in the header, and write to image
            header.setChannelBitsUsed(channelBits);
            write(header.getHeaderData());

            if (this.currBit != 0) {
                this.currBit = 0;
                writeCurrentBitSet();
                nextPixel();
            }

            this.channelBitsUsed = channelBits;
            this.bitSet = new byte[3 * channelBits];
        } catch (Exception ex) {

        }
    }

    /**
     * Implementation of <code>OutputStream.write(int)</code> method
     *
     * @param data Byte to be written
     * @throws IOException
     */
    @Override
    public void write(int data) throws IOException {
        for (int bit = 0; bit < 8; bit++) {
            this.bitSet[this.currBit] = (byte) ((data >> (7 - bit)) & 1);
            this.currBit++;
            if (this.currBit == this.bitSet.length) {
                this.currBit = 0;
                writeCurrentBitSet();
                nextPixel();
            }
        }
    }

    /**
     * Flushes the stream
     *
     * @throws IOException
     */
    @Override
    public void flush() throws IOException {
        writeCurrentBitSet();
    }

    /**
     * Closes the stream
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        if (this.currBit != 0) {
            for (int i = this.currBit; i < this.bitSet.length; i++) {
                this.bitSet[i] = 0;
            }
            this.currBit = 0;
            writeCurrentBitSet();
            nextPixel();
        }
        super.close();
    }

    /**
     * Get the image containing the embedded data. Ideally, this should be called after the stream is closed.
     *
     * @return Image data
     */
    public ImageHolder getImage() {
        try {
            flush();
        } catch (IOException ioEx) {
            return null;
        }
        return this.image;
    }

    /**
     * Method to write current bit set
     *
     * @throws IOException
     */
    private void writeCurrentBitSet() throws IOException {
        int pixel = 0;
        int offset = 0;
        int mask = 0;
        int maskPerByte = 0;
        int bitOffset = 0;

        if (this.y == this.imgHeight) {
            return;
        }

        maskPerByte = (int) (Math.pow(2, this.channelBitsUsed) - 1);
        mask = (maskPerByte << 16) + (maskPerByte << 8) + maskPerByte;
        pixel = this.image.getImage().getPixel(this.x, this.y) & (0xFFFFFFFF - mask);

        for (int bit = 0; bit < 3; bit++) {
            bitOffset = 0;
            for (int i = 0; i < this.channelBitsUsed; i++) {
                bitOffset = (bitOffset << 1) + this.bitSet[(bit * this.channelBitsUsed) + i];
            }
            offset = (offset << 8) + bitOffset;
        }
        this.image.getImage().setPixel(this.x, this.y, pixel + offset);
    }

    /**
     * Method to move on to next pixel
     */
    private void nextPixel() {
        this.x++;
        if (this.x == this.imgWidth) {
            this.x = 0;
            this.y++;
        }
    }
}
