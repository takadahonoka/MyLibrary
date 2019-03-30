package local.hal.st32.android.mylibrary60143;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

public class FullDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("確認");
        builder.setMessage("この商品を削除してもよろしいですか?");
        builder.setPositiveButton("削除" , new DialogButtonClickListener());
        builder.setNegativeButton("キャンセル" , new DialogButtonClickListener());
        AlertDialog dialog = builder.create();
        return dialog;
    }

    /**
     * ダイアログのボタンが押された時の処理が記述されたメンバクラス。
     */
    private class DialogButtonClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog , int which){
            Activity parent = getActivity();
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    Bundle bundle = getArguments();
                    String strId = bundle.getString("id");
                    int _idNo = Integer.parseInt(strId);
                    DatabaseHelper helper = new DatabaseHelper(parent);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    try{
                        DataAccess.delete(db , _idNo);
                    }
                    catch (Exception ex){
                        Log.e("ERROR" , ex.toString());
                    }
                    finally {
                        db.close();
                    }
                    Toast.makeText(parent , "削除されました。" , Toast.LENGTH_SHORT).show();
                    parent.finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    }
}
