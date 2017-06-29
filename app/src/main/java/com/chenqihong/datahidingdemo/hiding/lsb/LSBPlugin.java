package com.chenqihong.datahidingdemo.hiding.lsb;

import com.chenqihong.datahidingdemo.template.image.DHImagePluginTemplate;
import com.chenqihong.datahidingdemo.util.ImageHolder;
import com.chenqihong.datahidingdemo.util.ImageUtil;

import java.io.IOException;

/**
 * Created by chenqihong on 2017/6/29.
 */

public class LSBPlugin extends DHImagePluginTemplate{
    @Override
    public String getName() {
        return "LSB";
    }

    @Override
    public String getDescription() {
        return "LSB 算法具体实现";
    }
    @Override
    public byte[] generateSignature() {
        return null;
    }

    @Override
    public double getWatermarkCorrelation(byte[] origSigData, byte[] watermarkData) {
        return 0;
    }

    @Override
    public double getHighWatermarkLevel() {
        return 0;
    }

    @Override
    public double getLowWatermarkLevel() {
        return 0;
    }

    @Override
    public byte[] embedData(byte[] msg, String msgFileName, byte[] cover, String coverFileName, String stegoFileName){
        int numOfPixels = 0;
        ImageHolder image = null;
        LSBOutputStream lsbOS = null;

        try {
            // Generate random image, if input image is not provided
            if (cover == null) {
                numOfPixels = (int) (LSBDataHeader.getMaxHeaderSize() * 8 / 3.0);
                numOfPixels += (int) (msg.length * 8 / (3.0 * 8));
                image = ImageUtil.generateRandomImage(numOfPixels);
            } else {
                image = ImageUtil.byteArrayToImage(cover, coverFileName);
            }
            lsbOS = new LSBOutputStream(image, msg.length, msgFileName);
            lsbOS.write(msg);
            lsbOS.close();

            return ImageUtil.imageToByteArray(lsbOS.getImage(), stegoFileName, this);
        } catch (IOException ioEx) {
            return null;
        }
    }

    @Override
    public String extractMsgFileName(byte[] stegoData, String stegoFileName) {
        LSBInputStream lsbIS = null;

        try {
            lsbIS = new LSBInputStream(ImageUtil.byteArrayToImage(stegoData, stegoFileName));
            return lsbIS.getDataHeader().getFileName();
        } finally {
            if (lsbIS != null) {
                try {
                    lsbIS.close();
                } catch (Exception e) {
                    // Ignore
                }
            }
        }
    }

    @Override
    public byte[] extractData(byte[] stegoData, String stegoFileName, byte[] origSigData){
        int bytesRead = 0;
        byte[] data = null;
        LSBDataHeader header = null;
        LSBInputStream lsbIS = null;

        try {
            lsbIS = new LSBInputStream(ImageUtil.byteArrayToImage(stegoData, stegoFileName));
            header = lsbIS.getDataHeader();
            data = new byte[header.getDataLength()];

            bytesRead = lsbIS.read(data, 0, data.length);
            if (bytesRead != data.length) {
                return null;
            }

            return data;
        }catch (Exception ex) {
            return null;
        } finally {
            if (lsbIS != null) {
                try {
                    lsbIS.close();
                } catch (Exception e) {
                    // Ignore
                }
            }
        }
    }

}
