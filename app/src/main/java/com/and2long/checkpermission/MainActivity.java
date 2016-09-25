package com.and2long.checkpermission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;
    private static final int MY_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.mainActivity);
        Button callPhone = (Button) findViewById(R.id.callPhone);
        callPhone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.callPhone:
                //检查权限
                checkPermissionsAndDoNext(Manifest.permission.CALL_PHONE, MY_PERMISSIONS_REQUEST);
                break;
        }
    }

    private void call() {
        String TELL = "10086";
        intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + TELL));
        startActivity(intent);
    }

    /**
     * 检查是否具有权限
     *
     * @param permission
     * @return
     */
    public void checkPermissionsAndDoNext(String permission, int request) {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this,
                permission)) {
            //具有权限，执行操作
            call();
        } else {
            //没有权限
            //如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
            //如果用户之前拒绝权限的时候勾选了对话框中"Don’t ask again"的选项,那么这个方法会返回false.
            //如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    permission)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        request);
            } else {
                //提示用户需要权限
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.help)
                        .setCancelable(false)
                        .setMessage(R.string.message_need_permission)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNeutralButton(R.string.setting, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //进入设置，让用户手动授权
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .create()
                        .show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //已授权
                    call();

                } else {
                    //没有授权

                }
                break;


        }
    }
}
