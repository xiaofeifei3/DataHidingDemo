package com.chenqihong.datahidingdemo.template.image;

import com.chenqihong.datahidingdemo.plugin.WatermarkingPlugin;
import com.chenqihong.datahidingdemo.util.ImageHolder;
import com.chenqihong.datahidingdemo.util.ImageUtil;

/**
 * Created by chenqihong on 2017/6/28.
 */

public abstract class WMImagePluginTemplate extends WatermarkingPlugin {
    @Override
    public final byte[] getDiff(byte[] stegoData, String stegoFileName,
                                   byte[] coverData, String coverFileName, String diffFileName){
        ImageHolder stegoImage = null;
        ImageHolder coverImage = null;
        ImageHolder diffImage = null;

        stegoImage = ImageUtil.byteArrayToImage(stegoData, stegoFileName);
        coverImage = ImageUtil.byteArrayToImage(coverData, coverFileName);
        diffImage = ImageUtil.getDiffImage(stegoImage, coverImage);

        return ImageUtil.imageToByteArray(diffImage, diffFileName, this);
    }
}
