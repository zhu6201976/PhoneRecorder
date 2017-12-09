package com.example.administrator.test.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.test.R;

import java.util.ArrayList;

/**
 * 电话窃听器(来电录音)
 * 2017年12月8日21:19:39
 * 4.1.2模拟器测试没问题
 * 1.因当前4.1.2模拟器无法访问sdcard,需要提前cmd,adb shell,chmod 777 mnt/sdcard,才能保存文件到sdcard
 * 2.其次,录音完毕后欲提取luyin.3gp文件亦需修改权限:chmod 777 mnt/sdcard/luyin.3gp
 * 3.7.0模拟器崩溃,真机测试没问题
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermission();
    }

    private void initPermission() {
        ArrayList<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.RECORD_AUDIO);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        String[] permissions = new String[permissionList.size()];
        permissions = permissionList.toArray(permissions);
        if (permissions.length > 0) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (permissions.length == 3) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "you allow 3 permission", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "you denied the permission", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else if (permissions.length == 2) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "you allow 2 permission", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "you denied the permission", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else if (permissions.length == 1) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "you allow 1 permission", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "you denied the permission", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                break;
        }
    }

    public void startService(View view) {
        startService(new Intent(this, PhoneListenService.class));
        finish();
    }

    public void stopService(View view) {
        stopService(new Intent(this, PhoneListenService.class));
        finish();
    }
}
