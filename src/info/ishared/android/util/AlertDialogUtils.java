package info.ishared.android.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * User: Lee
 * Date: 11-9-19
 * Time: 下午2:37
 */
public class AlertDialogUtils {
    public interface Executor {
        void execute();
    }

    /**
     * @param context
     * @param message
     * @param executor
     * @return
     */
    public static void showYesNoDiaLog(Context context, String message, final Executor executor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false).setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                executor.execute();
            }
        }).setNegativeButton("否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).show();
    }

    public static void showConfirmDiaLog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(true).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).show();
    }


}
