package cn.com.zhihetech.online.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.easemob.chat.EMChatManager;
import com.easemob.easeui.widget.EaseImageView;
import com.easemob.util.PathUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.xutils.common.util.FileUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import cn.com.zhihetech.online.R;
import cn.com.zhihetech.online.bean.EMUserInfo;
import cn.com.zhihetech.online.bean.ImgInfo;
import cn.com.zhihetech.online.bean.ShowImageInfo;
import cn.com.zhihetech.online.bean.User;
import cn.com.zhihetech.online.core.ZhiheApplication;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.common.ResponseStateCode;
import cn.com.zhihetech.online.core.db.DBUtils;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.SimpleCallback;
import cn.com.zhihetech.online.core.util.ImageLoader;
import cn.com.zhihetech.online.core.util.StringUtils;
import cn.com.zhihetech.online.model.QiniuModel;
import cn.com.zhihetech.online.model.UserModel;

/**
 * Created by ShenYunjie on 2016/3/11.
 */
@ContentView(R.layout.activity_user_header_modify)
public class UserHeaderModifyActivity extends BaseActivity {

    public static final String MODIFY_USER_HEADER_SUCCESS_ACTION = "MODIFY_USER_HEADER_SUCCESS_ACTION";
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL_IMAGE = 3;

    @ViewInject(R.id.user_header_img)
    private EaseImageView userHeaderImg;

    private ProgressDialog progressDialog;

    private User user;
    private File cameraFile;
    private String upToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = ZhiheApplication.getInstance().getUser();
        ImageLoader.disPlayImage(userHeaderImg, user.getPortrait());
        userHeaderImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ShowImageInfo> imageInfos = new ArrayList<>();
                ShowImageInfo imageInfo = new ShowImageInfo(user.getPortrait().getUrl(), "");
                imageInfo.setShowDesc(false);
                imageInfos.add(imageInfo);

                Intent intent = new Intent(v.getContext(), ShowBigImageActivity.class);
                intent.putExtra(ShowBigImageActivity.IMAGE_LIST_KEY, imageInfos);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
                if (cameraFile != null && cameraFile.exists()) {
                    uploadImgByPath(cameraFile.getAbsolutePath());
                }
            } else if (requestCode == REQUEST_CODE_LOCAL_IMAGE) { // 发送本地图片
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        uploadImgByPath(getPicPathByURI(selectedImage));
                    }
                }
            }
        }
    }

    /**
     * 根据文件路径上传图片
     *
     * @param filePath
     */
    private void uploadImgByPath(final String filePath) {
        progressDialog = ProgressDialog.show(this, "", "正在上传...");
        new QiniuModel().getUploadToken(new SimpleCallback() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                upToken = jsonObject.getString("uptoken");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showMsg("头像上传失败，请重试！");
                upToken = null;
                progressDialog.dismiss();
            }

            @Override
            public void onFinished() {
                if (!StringUtils.isEmpty(upToken)) {
                    qiniuUpload(filePath, upToken);
                }
            }
        });
    }

    private void qiniuUpload(String filePath, String upToken) {
        UploadManager uploadManager = new UploadManager();
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        if (filePath.indexOf(".") != -1) {
            String suffix = filePath.substring(filePath.lastIndexOf("."), filePath.length());
            fileName += suffix;
        }
        uploadManager.put(filePath, fileName, upToken, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, org.json.JSONObject response) {
                ImgInfo imgInfo = JSONObject.parseObject(response.toString(), ImgInfo.class);
                changHeader(user.getUserId(), imgInfo);
            }
        }, null);
    }

    private void changHeader(String userId, final ImgInfo imgInfo) {
        new UserModel().changeHeader(new ObjectCallback<ResponseMessage>() {
            @Override
            public void onObject(ResponseMessage data) {
                if (data.getCode() != ResponseStateCode.SUCCESS) {
                    showMsg(data.getMsg());
                    return;
                }
                ImageLoader.disPlayImage(userHeaderImg, imgInfo);
                user.setPortrait(imgInfo);
                saveUserInfo(user);
                Intent intent = new Intent(MODIFY_USER_HEADER_SUCCESS_ACTION);
                sendBroadcast(intent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showMsg("修改头像失败");
            }

            @Override
            public void onFinished() {
                progressDialog.dismiss();
            }
        }, userId, imgInfo.getImgInfoId());
    }

    protected void saveUserInfo(User user) {
        DBUtils db = new DBUtils();
        EMUserInfo info = new EMUserInfo(user.getEMUserId(), user.getUserName(), user.getPortrait().getUrl(), user.getUserId(), ZhiheApplication.COMMON_USER_TYPE);
        try {
            db.saveUserInfo(info);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Event({R.id.pick_image_view, R.id.pick_camera_view})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.pick_image_view:
                selectPicFromLocal();  // 图库选择图片
                break;
            case R.id.pick_camera_view:
                selectPicFromCamera(); // 拍照
                break;
        }
    }

    /**
     * 根据图库图片uri获取图片绝对路径
     *
     * @param selectedImage
     */
    protected String getPicPathByURI(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(this, com.easemob.easeui.R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return null;
            }
            return picturePath;
        }
        File file = new File(selectedImage.getPath());
        if (!file.exists()) {
            Toast toast = Toast.makeText(this, com.easemob.easeui.R.string.cant_find_pictures, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return null;

        }
        return file.getAbsolutePath();
    }

    /**
     * 照相获取图片
     */
    protected void selectPicFromCamera() {
        if (!FileUtil.existsSdcard()) {
            showMsg("SD卡不存在，不能拍照");
            return;
        }

        cameraFile = new File(PathUtil.getInstance().getImagePath(), EMChatManager.getInstance().getCurrentUser()
                + System.currentTimeMillis() + ".jpg");
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }

    /**
     * 从图库获取图片
     */
    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL_IMAGE);
    }
}
