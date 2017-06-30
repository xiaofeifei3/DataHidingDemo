package com.chenqihong.datahidingdemo;

import com.chenqihong.datahidingdemo.plugin.MuStegoPlugin;
import com.chenqihong.datahidingdemo.util.CommonUtil;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenqihong on 2017/6/30.
 */

public class MuStego {
    private MuStegoPlugin plugin = null;

    public MuStego(MuStegoPlugin plugin){
        if(null == plugin){
            return;
        }

        this.plugin = plugin;
    }

    public byte[] embedData(byte[] msg, String msgFileName, byte[] cover, String coverFileName,
                            String stegoFileName){
        if(!this.plugin.getPurpose().contains( MuStegoPlugin.Purpose.DATA_HIDING)){
            return null;
        }

        try{
            return this.plugin.embedData(msg, msgFileName, cover, coverFileName, stegoFileName);
        }catch (Exception e){
            return null;
        }
    }

    public byte[] embedData(String msg, File coverFile, String stegoFileName){
        if(!this.plugin.getPurpose().contains(MuStegoPlugin.Purpose.DATA_HIDING)){
            return null;
        }
        String filename = null;

        try{
            return embedData(msg.getBytes(), filename,
                    coverFile == null? null: CommonUtil.getFileBytes(coverFile),
                    coverFile == null? null:coverFile.getName(), stegoFileName);
        }catch (Exception e){
            return null;
        }
    }

    public byte[] embedMark(byte[] sig, String sigFileName,
                            byte[] cover, String coverFileName, String stegoFileName){
        if(!this.plugin.getPurpose().contains(MuStegoPlugin.Purpose.WATERMARKING)){
            return null;
        }

        try{
            return this.plugin.embedData(sig, sigFileName, cover, coverFileName, stegoFileName);
        }catch (Exception e){
            return null;
        }
    }


    /**
     * Method to extract the message data from stego data
     *
     * @param stegoData Stego data from which the message needs to be extracted
     * @param stegoFileName Name of the stego file
     * @return Extracted message (List's first element is filename and second element is the message as byte array)
     */
    public List<?> extractData(byte[] stegoData, String stegoFileName ){
        if (!this.plugin.getPurpose().contains(MuStegoPlugin.Purpose.DATA_HIDING)) {
            return null;
        }

        byte[] msg = null;
        List<Object> output = new ArrayList<Object>();

        try {
            // Add file name as first element of output list
            //output.add(this.plugin.extractMsgFileName(stegoData, stegoFileName));
            msg = this.plugin.extractData(stegoData, stegoFileName, null);

            // Add message as second element of output list
            output.add(msg);
        } catch (Exception osEx) {
            return null;
        }

        return output;
    }

    /**
     * Method to extract the message data from stego data (alternate API)
     *
     * @param stegoFile Stego file from which message needs to be extracted
     * @return Extracted message (List's first element is filename and second element is the message as byte array)
     */
    public List<?> extractData(File stegoFile){
        if (!this.plugin.getPurpose().contains(MuStegoPlugin.Purpose.DATA_HIDING)) {
            return null;
        }

        return extractData(CommonUtil.getFileBytes(stegoFile), stegoFile.getName());
    }

    /**
     * Method to extract the watermark data from stego data
     *
     * @param stegoData Stego data from which the watermark needs to be extracted
     * @param stegoFileName Name of the stego file
     * @param origSigData Original signature data
     * @return Extracted watermark
     */
    public byte[] extractMark(byte[] stegoData, String stegoFileName, byte[] origSigData)  {
        if (!this.plugin.getPurpose().contains(MuStegoPlugin.Purpose.DATA_HIDING)) {
            return null;
        }

        return this.plugin.extractData(stegoData, stegoFileName, origSigData);
    }

    /**
     * Method to extract the watermark data from stego data (alternate API)
     *
     * @param stegoFile Stego file from which watermark needs to be extracted
     * @param origSigFile Original signature file
     * @return Extracted watermark
     */
    public byte[] extractMark(File stegoFile, File origSigFile)  {
        if (!this.plugin.getPurpose().contains(MuStegoPlugin.Purpose.DATA_HIDING)) {
            return null;
        }

        return extractMark(CommonUtil.getFileBytes(stegoFile), stegoFile.getName(), CommonUtil.getFileBytes(origSigFile));
    }

    /**
     * Method to check the correlation for the given image and the original signature
     *
     * @param stegoData Stego data containing the watermark
     * @param stegoFileName Name of the stego file
     * @param origSigData Original signature data
     * @return Correlation
     */
    public double checkMark(byte[] stegoData, String stegoFileName, byte[] origSigData) {
        if (!this.plugin.getPurpose().contains(MuStegoPlugin.Purpose.DATA_HIDING)) {
            return 0.0;
        }

        return this.plugin.checkMark(stegoData, stegoFileName, origSigData);
    }

    /**
     * Method to check the correlation for the given image and the original signature (alternate API)
     *
     * @param stegoFile Stego file from which watermark needs to be extracted
     * @param origSigFile Original signature file
     * @return Correlation
     */
    public double checkMark(File stegoFile, File origSigFile) {
        if (!this.plugin.getPurpose().contains(MuStegoPlugin.Purpose.DATA_HIDING)) {
            return 0.0;
        }

        double correl = checkMark(CommonUtil.getFileBytes(stegoFile), stegoFile.getName(), CommonUtil.getFileBytes(origSigFile));
        if (Double.isNaN(correl)) {
            correl = 0.0;
        }
        return correl;
    }

    /**
     * Method to generate the signature data using the given plugin
     *
     * @return Signature data
     */
    public byte[] generateSignature() {
        if (!this.plugin.getPurpose().contains(MuStegoPlugin.Purpose.WATERMARKING)) {
            return null;
        }

        return this.plugin.generateSignature();
    }

    /**
     * Method to get difference between original cover file and the stegged file
     *
     * @param stegoData Stego data containing the embedded data
     * @param stegoFileName Name of the stego file
     * @param coverData Original cover data
     * @param coverFileName Name of the cover file
     * @param diffFileName Name of the output difference file
     * @return Difference data
     */
    public byte[] getDiff(byte[] stegoData, String stegoFileName, byte[] coverData, String coverFileName, String diffFileName) {
        return this.plugin.getDiff(stegoData, stegoFileName, coverData, coverFileName, diffFileName);
    }

    /**
     * Method to get difference between original cover file and the stegged file
     *
     * @param stegoFile Stego file containing the embedded data
     * @param coverFile Original cover file
     * @param diffFileName Name of the output difference file
     * @return Difference data
     */
    public byte[] getDiff(File stegoFile, File coverFile, String diffFileName) {
        return getDiff(CommonUtil.getFileBytes(stegoFile), stegoFile.getName(), CommonUtil.getFileBytes(coverFile), coverFile.getName(),
                diffFileName);
    }



}
