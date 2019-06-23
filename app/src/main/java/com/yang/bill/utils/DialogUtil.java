package com.yang.bill.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;


import com.yang.bill.R;

/**
 * Created by Administrator on 2017/11/9.
 */

public class DialogUtil {
    private static Dialog dialog;
    public static void showReqPermissionDialog(final Context context) {
       AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.go_to_Application_Management_to_turn_on_permissions);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.determine, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                context.startActivity(intent);
            }
        });
        builder.show();
    }

//    public static void showMyInformationDialog(final Context context) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setMessage(R.string.please_verify_the_real_name);
//        builder.setPositiveButton(R.string.determine, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Intent intent = new Intent(context, MyInformationActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                BaseApplication.getContext().startActivity(intent);
//            }
//        });
//        builder.show();
//    }

//    public static void showGotoLoginDialog(final Context context) {
//        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
//        builder.setMessage(R.string.login_timeout_please_log_in_again);
//        builder.setNegativeButton(R.string.cancel, null);
//        builder.setPositiveButton(R.string.determine, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                //删除极光推送别名和标签
//                JPushInterface.deleteAlias(context,1);
//                Set<String> set = new HashSet<>();
//                set.add(SPUtils.getInstance().getInt(Constants.USER_STATE)+"");
//                JPushInterface.deleteTags(context,1,set);
//                CommonUtil.clearData();
//                Intent intent = new Intent(context, LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }
//        });
//        builder.show();
//    }

    public static void showWaittingDialog(Context context){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_waitting);
        dialog.setCancelable(false);
        dialog.show();
    }


    public static void cancelWaittingDialog(){
        if(dialog != null){
            dialog.dismiss();
        }
    }

}
