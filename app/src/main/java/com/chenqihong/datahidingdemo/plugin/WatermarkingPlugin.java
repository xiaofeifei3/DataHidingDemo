package com.chenqihong.datahidingdemo.plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenqihong on 2017/6/28.
 */

public abstract class WatermarkingPlugin extends MuStegoPlugin{
    @Override
    public String getName() {
        return "水印基础插件";
    }

    @Override
    public List<Purpose> getPurpose() {
        List<Purpose> purposes = new ArrayList<>();
        purposes.add(Purpose.WATERMARKING);
        return purposes;
    }

    @Override
    public String extractMsgFileName(byte[] stregoData, String stegoFileName) {
        return null;
    }

    @Override
    public String getDescription() {
        return "水印基础插件，执行各种水印签名算法";
    }

    @Override
    public double getHighWatermarkLevel() {
        return 0.5f;
    }

    @Override
    public double getLowWatermarkLevel() {
        return 0.2f;
    }


    @Override
    public boolean canHandle(byte[] stegoData) {
        return false;
    }
}
