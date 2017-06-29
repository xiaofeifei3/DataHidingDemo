package com.chenqihong.datahidingdemo.hiding.lsb;

import com.chenqihong.datahidingdemo.util.CommonUtil;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by chenqihong on 2017/6/29.
 */

public class LSBDataHeader {
    public static final byte[] DATA_STAMP = "MuStego".getBytes();
    public static final byte[] HEADER_VERSION = new byte[]{(byte)2};
    private static final int FIXED_HEADER_LENGTH = 8;
    private static final int CRYPT_ALGO_LENGTH = 8;
    private int dataLength = 0;
    private int channelBitsUsed = 0;
    private byte[] fileName = null;

    public LSBDataHeader(int dataLength, int channelBitsUsed, String fileName) {
        this.dataLength = dataLength;
        this.channelBitsUsed = channelBitsUsed;

        if (fileName == null) {
            this.fileName = new byte[0];
        } else {
            try {
                this.fileName = fileName.getBytes("UTF-8");
            } catch (UnsupportedEncodingException unEx) {
                this.fileName = fileName.getBytes();
            }
        }
    }

    public LSBDataHeader(InputStream dataInStream) {
        int stampLen = 0;
        int versionLen = 0;
        int fileNameLen = 0;
        int channelBits = 0;
        byte[] header = null;
        byte[] cryptAlgo = null;
        byte[] stamp = null;
        byte[] version = null;

        stampLen = DATA_STAMP.length;
        versionLen = HEADER_VERSION.length;
        header = new byte[FIXED_HEADER_LENGTH];
        cryptAlgo = new byte[CRYPT_ALGO_LENGTH];
        stamp = new byte[stampLen];
        version = new byte[versionLen];

        try {
            dataInStream.read(stamp, 0, stampLen);
            if (!(new String(stamp)).equals(new String(DATA_STAMP))) {
                return;
            }

            dataInStream.read(version, 0, versionLen);
            if (!(new String(version)).equals(new String(HEADER_VERSION))) {
                return;
            }

            dataInStream.read(header, 0, FIXED_HEADER_LENGTH);
            this.dataLength = (CommonUtil.byteToInt(header[0]) + (CommonUtil.byteToInt(header[1]) << 8) + (CommonUtil.byteToInt(header[2]) << 16)
                    + (CommonUtil.byteToInt(header[3]) << 32));
            channelBits = header[4];
            fileNameLen = header[5];

            dataInStream.read(cryptAlgo, 0, CRYPT_ALGO_LENGTH);

            if (fileNameLen == 0) {
                this.fileName = new byte[0];
            } else {
                this.fileName = new byte[fileNameLen];
                dataInStream.read(this.fileName, 0, fileNameLen);
            }
        }  catch (Exception ex) {
        }

        this.channelBitsUsed = channelBits;
    }

    /**
     * This method generates the header in the form of byte array based on the parameters provided in the constructor.
     *
     * @return Header data
     */
    public byte[] getHeaderData() {
        byte[] out = null;
        int stampLen = 0;
        int versionLen = 0;
        int currIndex = 0;

        stampLen = DATA_STAMP.length;
        versionLen = HEADER_VERSION.length;
        out = new byte[stampLen + versionLen + FIXED_HEADER_LENGTH + CRYPT_ALGO_LENGTH + this.fileName.length];

        System.arraycopy(DATA_STAMP, 0, out, currIndex, stampLen);
        currIndex += stampLen;

        System.arraycopy(HEADER_VERSION, 0, out, currIndex, versionLen);
        currIndex += versionLen;

        out[currIndex++] = (byte) ((this.dataLength & 0x000000FF));
        out[currIndex++] = (byte) ((this.dataLength & 0x0000FF00) >> 8);
        out[currIndex++] = (byte) ((this.dataLength & 0x00FF0000) >> 16);
        out[currIndex++] = (byte) ((this.dataLength & 0xFF000000) >> 32);
        out[currIndex++] = (byte) this.channelBitsUsed;
        out[currIndex++] = (byte) this.fileName.length;
        out[currIndex++] = (byte) (0);
        out[currIndex++] = (byte) (0);

        System.arraycopy("DES".getBytes(), 0, out, currIndex,
                "DES".getBytes().length);

        currIndex += CRYPT_ALGO_LENGTH;

        if (this.fileName.length > 0) {
            System.arraycopy(this.fileName, 0, out, currIndex, this.fileName.length);
            currIndex += this.fileName.length;
        }

        return out;
    }

    /**
     * Get Method for channelBitsUsed
     *
     * @return channelBitsUsed
     */
    public int getChannelBitsUsed() {
        return this.channelBitsUsed;
    }

    /**
     * Set Method for channelBitsUsed
     *
     * @param channelBitsUsed
     */
    public void setChannelBitsUsed(int channelBitsUsed) {
        this.channelBitsUsed = channelBitsUsed;
    }

    /**
     * Get Method for dataLength
     *
     * @return dataLength
     */
    public int getDataLength() {
        return this.dataLength;
    }

    /**
     * Get Method for fileName
     *
     * @return fileName
     */
    public String getFileName() {
        String name = null;

        try {
            name = new String(this.fileName, "UTF-8");
        } catch (UnsupportedEncodingException unEx) {
            name = new String(this.fileName);
        }
        return name;
    }

    /**
     * Method to get size of the current header
     *
     * @return Header size
     */
    public int getHeaderSize() {
        return DATA_STAMP.length + HEADER_VERSION.length + FIXED_HEADER_LENGTH + CRYPT_ALGO_LENGTH + this.fileName.length;
    }

    /**
     * Method to get the maximum possible size of the header
     *
     * @return Maximum possible header size
     */
    public static int getMaxHeaderSize() {
        // Max file name length assumed to be 256
        return DATA_STAMP.length + HEADER_VERSION.length + FIXED_HEADER_LENGTH + CRYPT_ALGO_LENGTH + 256;
    }
}
