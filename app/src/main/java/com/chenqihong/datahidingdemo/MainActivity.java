package com.chenqihong.datahidingdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenqihong.datahidingdemo.hiding.lsb.LSBPlugin;
import com.chenqihong.datahidingdemo.util.ImageHolder;
import com.chenqihong.datahidingdemo.util.ImageUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;

public class MainActivity extends AppCompatActivity implements OnClickListener{
    public static int REQUEST_PICK_IMAGE = 200;
    private EditText mInfoEdit;
    private Button mHideInfoButton;
    private Button mShowInfoButton;
    private ImageView mProcessedImage;
    private TextView mExtractedInfoText;
    private String mSelectedImagePath;
    private MuStego mMuStego;
    private ImageHolder mImage;
    private byte[] mStegoData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        mInfoEdit = (EditText)findViewById(R.id.editText);
        mHideInfoButton = (Button)findViewById(R.id.pickPhoto);
        mShowInfoButton = (Button)findViewById(R.id.pickInfo);
        mProcessedImage = (ImageView)findViewById(R.id.showImage);
        mExtractedInfoText = (TextView)findViewById(R.id.showInfo);
        mHideInfoButton.setOnClickListener(this);
        mShowInfoButton.setOnClickListener(this);
        mMuStego = new MuStego(new LSBPlugin());
    }

    @Override
    public void onClick(View v) {
        if(mHideInfoButton == v){
            if(TextUtils.isEmpty(mInfoEdit.getText())){
                Toast.makeText(this, "隐藏信息不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            mExtractedInfoText.setVisibility(View.GONE);
            mProcessedImage.setVisibility(View.VISIBLE);
            pickPhoto();

        }else if(mShowInfoButton == v){
            mExtractedInfoText.setVisibility(View.VISIBLE);
            mProcessedImage.setVisibility(View.GONE);
            mExtractedInfoText.setText(extractData(mImage.getImage()));
        }
    }

    public void pickPhoto(){
        MultiImageSelector.create().single().start(this, REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK){
            List<String> path = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
            mSelectedImagePath = path.get(0);
            if (null == mSelectedImagePath){
                Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
                return;
            }

            hideData(mSelectedImagePath);
        }
    }

    public void hideData(String imagePath){
        String info = mInfoEdit.getText().toString();
        File coverFile = new File(imagePath);
        String stegoName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/stego.jpeg";
        mStegoData = mMuStego.embedData(info, coverFile, stegoName);
        mImage = ImageUtil.byteArrayToImage(mStegoData, stegoName);
        mProcessedImage.setImageBitmap(mImage.getImage());

    }

    public String extractData(Bitmap bitmap){
        return new String(((byte[])mMuStego.extractData(mStegoData,
                mSelectedImagePath).get(0)));
    }


}
