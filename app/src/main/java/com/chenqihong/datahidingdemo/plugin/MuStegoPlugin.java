package com.chenqihong.datahidingdemo.plugin;

import java.util.List;

/**
 * Created by chenqihong on 2017/6/28.
 */

public abstract class MuStegoPlugin {

    public enum Purpose{
        DATA_HIDING,

        WATERMARKING
    }

    public abstract String getName();

    public abstract List<Purpose> getPurpose();

    public abstract String getDescription();

    public abstract byte[] embedData(byte[] msg, String msgFileName,
                                     byte[] cover, String coverFileName, String stegoFileName);

    public abstract String extractMsgFileName(byte[] stregoData, String stegoFileName);

    public abstract byte[] extractData(byte[] stegoData, String stegoFileName, byte[] origSigData);

    public abstract byte[] generateSignature();

    public final double checkMark(byte[] stegoData, String stegoFileName, byte[] origSigData){
        return getWatermarkCorrelation(origSigData, extractData(stegoData, stegoFileName, origSigData));
    }

    public abstract double getWatermarkCorrelation(byte[] origSigData, byte[] watermarkData);

    public abstract double getHighWatermarkLevel();

    public abstract double getLowWatermarkLevel();

    public abstract byte[] getDiff(byte[] stegoData, String stegoFileName,
                                   byte[] coverData, String coverFileName, String diffFileName);

    public abstract boolean canHandle(byte[] stegoData);

}
