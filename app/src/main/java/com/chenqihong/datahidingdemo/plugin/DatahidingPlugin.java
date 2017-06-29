package com.chenqihong.datahidingdemo.plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenqihong on 2017/6/28.
 */

public abstract class DatahidingPlugin extends MuStegoPlugin{
    @Override
    public String getName() {
        return "数据隐藏基础插件";
    }

    @Override
    public List<Purpose> getPurpose() {
        List<Purpose> purposes = new ArrayList<>();
        purposes.add(Purpose.DATA_HIDING);
        return purposes;
    }

    @Override
    public String getDescription() {
        return "数据隐藏算法插件，执行各种数据隐藏算法";
    }

    @Override
    public byte[] embedData(byte[] msg, String msgFileName, byte[] cover, String coverFileName, String stegoFileName) {
        return null;
    }

    @Override
    public String extractMsgFileName(byte[] stregoData, String stegoFileName) {
        return null;
    }

    @Override
    public byte[] extractData(byte[] stegoData, String stegoFileName, byte[] origSigData) {
        return null;
    }

    @Override
    public boolean canHandle(byte[] stegoData) {
        try{
            extractMsgFileName(stegoData, "DUMMY");
        }catch (Exception e){
            return false;
        }

        return true;
    }
}
