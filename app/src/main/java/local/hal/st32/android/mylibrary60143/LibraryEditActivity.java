package local.hal.st32.android.mylibrary60143;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LibraryEditActivity extends AppCompatActivity {

    private int _mode = LibraryListActivity.MODE_INSERT;
    private static final int REQUEST_GALLERY = 0;
    private final static int RESULT_CAMERA = 1001;
    private final static int REQUEST_PERMISSION = 1002;

    private ImageView imageView;
    private Uri cameraUri;
    private String filePath = "";

    private int _id = 0;
    private Product p;
    private List<Map<String, Object>> _typeList = new ArrayList<Map<String, Object>>();

    private Calendar cal = Calendar.getInstance();
    private int nowYear = cal.get(Calendar.YEAR);
    private int nowMonth = cal.get(Calendar.MONTH);
    private int nowDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_edit);

        setTitle("食材残量詳細");

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Intent intent = getIntent();
        _mode = intent.getIntExtra("mode", 1);

        ImageView cameraButton = findViewById(R.id.ivProductImage);
        imageView = findViewById(R.id.ivProductImage);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            //画像をクリックした時。
            @Override
            public void onClick(View v) {
                // Android 6, API 23以上でパーミッシンの確認
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermission();
                }
                else {
                    cameraIntent();
                }
            }
        });// アクションバーに前画面に戻る機能をつける
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        this.p = new Product();
        p.setProductImage(String.valueOf(R.drawable.icon));

        if(_mode == 1) {
            //新規登録時
            EditText etProductNumber = findViewById(R.id.etProductNumber);
            etProductNumber.setText("0");

            EditText etProductDate = findViewById(R.id.etProductDate);
            etProductDate.setText(nowYear + "年" + (nowMonth+1) + "月" + nowDayOfMonth + "日");

            ImageView ivProductImage = findViewById(R.id.ivProductImage);
            ivProductImage.setImageResource(R.drawable.icon);

        }else{
            //変更時
            _id = intent.getIntExtra("idNo" , 1);

            DatabaseHelper helper = new DatabaseHelper(LibraryEditActivity.this);
            SQLiteDatabase db = helper.getWritableDatabase();
            this.p = new Product();
            try {
                this.p = DataAccess.findByPK(db, _id);
            } catch (Exception ex) {
                Log.e("ERROR", ex.toString());
            } finally {
                db.close();
            }
            //商品名
            EditText etProductName = findViewById(R.id.etProductName);
            etProductName.setText(p.getProductName());
            //商品種別
            Spinner spProductType = findViewById(R.id.spProductType);
            String productType = p.getProductType();
            spProductType.setSelection(Integer.parseInt(productType));
            //商品個数
            EditText etProductNumber = findViewById(R.id.etProductNumber);
            etProductNumber.setText(p.getProductNumber());
            //期限種別
            Switch swDateType = findViewById(R.id.swDateType);
            if(Integer.parseInt(p.getProductDateType()) == 1){
                swDateType.setChecked(true);
            }else{
                swDateType.setChecked(false);
            }
            //期限
            EditText etProductDate = findViewById(R.id.etProductDate);
            DataConversion dc = new DataConversion();
            etProductDate.setText(dc.getDataConversion02(p.getProductDate()));
            //メモ
            EditText etProductMemo = findViewById(R.id.etProductMemo);
            etProductMemo.setText(p.getProductMemo());
            //写真
            ImageView ivImage = findViewById(R.id.ivProductImage);
            ivImage.setImageURI(Uri.parse(p.getProductImage()));
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        //写真
        ImageView ivImage = findViewById(R.id.ivProductImage);
        if(String.valueOf(R.drawable.icon).equals(p.getProductImage())){
            ivImage.setImageResource(R.drawable.icon);
        }else if ("".equals(p.getProductImage())){
            ivImage.setImageResource(R.drawable.icon);
        }else {
            ivImage.setImageURI(Uri.parse(p.getProductImage()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        if (_mode == LibraryListActivity.MODE_INSERT) {
            inflater.inflate(R.menu.library_edit_insert, menu);
        }else{
            inflater.inflate(R.menu.library_edit_update, menu);
        }
        return true;
    }

    /**
     * -のボタンがクリックされた時。
     */
    public void onNumberMinusClick(View view){
        EditText etProductNumber = findViewById(R.id.etProductNumber);
        int productNumber = Integer.parseInt(etProductNumber.getText().toString())-1;
        if(productNumber<0){
            productNumber = 0;
        }
        etProductNumber.setText(String.valueOf(productNumber));
    }

    /**
     * +のボタンがクリックされた時。
     */
    public void onNumberPlusClick(View view){
        EditText etProductNumber = findViewById(R.id.etProductNumber);
        int productNumber = Integer.parseInt(etProductNumber.getText().toString())+1;
        etProductNumber.setText(String.valueOf(productNumber));
    }

    /**
     * 日付選択ダイアログ表示ボタンが押された時のイベント処理用メソッド。
     */
    public void onProductDateClick(View view){

        EditText etProductDate = findViewById(R.id.etProductDate);
        String productDate = etProductDate.getText().toString();

        //データ変換用クラス。
        DataConversion dc = new DataConversion();
        productDate = dc.getDataConversion01(productDate);

        DatePickerDialog dialog = new DatePickerDialog(LibraryEditActivity.this, new DatePickerDialogDateSetListener() , Integer.parseInt(dc.getDataConversion03(productDate)) , Integer.parseInt(dc.getDataConversion04(productDate))-1 , Integer.parseInt(dc.getDataConversion05(productDate)));
        dialog.show();
    }

    /**
     * 日付選択ダイアログの、完了ボタンが押された時の処理が記述されたメンバクラス。
     */
    private class DatePickerDialogDateSetListener implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePicker view , int year , int monthOfYear , int dayOfMonth){

            monthOfYear = monthOfYear + 1;
            String strMonth = String.valueOf(monthOfYear);
            String strDate = String.valueOf(dayOfMonth);

            if(monthOfYear < 10 ){
                strMonth = "0" + strMonth;
            }
            if(dayOfMonth < 10 ){
                strDate = "0" + strDate;
            }

            EditText etProductDate = findViewById(R.id.etProductDate);
            etProductDate.setText(year + "年" + strMonth + "月" + strDate + "日");

        }
    }

    /**
     * アクションバー。
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.btnNewSave:
                this.p = new Product();
                //商品名
                EditText etProductName = findViewById(R.id.etProductName);
                p.setProductName(etProductName.getText().toString());
                //商品種別
                Spinner spProductType = findViewById(R.id.spProductType);
                p.setProductType(String.valueOf(spProductType.getSelectedItemPosition()));
                //商品個数
                EditText etProductNumber = findViewById(R.id.etProductNumber);
                p.setProductNumber(etProductNumber.getText().toString());
                //期限種別
                Switch swDateType = findViewById(R.id.swDateType);
                String dateType = "";
                if(swDateType.isChecked()){
                    //ON
                    dateType = "1";
                }else{
                    //OFF
                    dateType = "0";
                }
                p.setProductDateType(dateType);
                //期限
                EditText etProductDate = findViewById(R.id.etProductDate);
                DataConversion dc = new DataConversion();
                p.setProductDate(dc.getDataConversion01(etProductDate.getText().toString()));
                //メモ
                EditText etProductMemo = findViewById(R.id.etProductMemo);
                p.setProductMemo(etProductMemo.getText().toString());
                //写真
                p.setProductImage(filePath);

                if(p.getErrorFlag()){
                    DatabaseHelper helper = new DatabaseHelper(LibraryEditActivity.this);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    try {
                        long productId = DataAccess.insert(db, p.getProductName(), p.getProductType(), Integer.parseInt(p.getProductNumber()), Integer.parseInt(p.getProductDateType()), p.getProductDate(), p.getProductMemo() , p.getProductImage());
                        if(_typeList == null) {
                            for (int i=0; i<_typeList.size();i++) {
                                Map map = _typeList.get(i);
                                if ("1".equals(map.get("flag"))) {
                                    DataAccess.insertType2(db, String.valueOf(productId), String.valueOf(map.get("id")));
                                }
                            }
                        }
                    }
                    catch (Exception ex){
                        Log.e("ERROR" , ex.toString());
                    }
                    finally {
                        db.close();
                    }
                    finish();
                    Toast.makeText(LibraryEditActivity.this , "登録されました。" , Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LibraryEditActivity.this , "商品名は入力して下さい。" , Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.btnSave:
                this.p = new Product();
                //商品名
                etProductName = findViewById(R.id.etProductName);
                p.setProductName(etProductName.getText().toString());
                //商品種別
                spProductType = findViewById(R.id.spProductType);
                p.setProductType(String.valueOf(spProductType.getSelectedItemPosition()));
                //商品個数
                etProductNumber = findViewById(R.id.etProductNumber);
                p.setProductNumber(etProductNumber.getText().toString());
                //期限種別
                swDateType = findViewById(R.id.swDateType);
                dateType = "";
                if(swDateType.isChecked()){
                    //ON
                    dateType = "1";
                }else{
                    //OFF
                    dateType = "0";
                }
                p.setProductDateType(dateType);
                //期限
                etProductDate = findViewById(R.id.etProductDate);
                dc = new DataConversion();
                p.setProductDate(dc.getDataConversion01(etProductDate.getText().toString()));
                //メモ
                etProductMemo = findViewById(R.id.etProductMemo);
                p.setProductMemo(etProductMemo.getText().toString());
                //写真
                p.setProductImage(filePath);

                if(p.getErrorFlag()){
                    DatabaseHelper helper = new DatabaseHelper(LibraryEditActivity.this);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    try {
                        long productId = DataAccess.update(db, _id , p.getProductName(), p.getProductType(), Integer.parseInt(p.getProductNumber()), Integer.parseInt(p.getProductDateType()), p.getProductDate(), p.getProductMemo() , p.getProductImage());
                        Toast.makeText(LibraryEditActivity.this , "保存されました。" , Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception ex){
                        Log.e("ERROR" , ex.toString());
                    }
                    finally {
                        db.close();
                    }
                    finish();
                }else{
                    Toast.makeText(LibraryEditActivity.this , "商品名は入力して下さい。" , Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.btnDelete:
                Bundle extras = new Bundle();
                extras.putString("id" , String.valueOf(_id));
                FullDialogFragment dialog = new FullDialogFragment();
                FragmentManager manager = getSupportFragmentManager();
                dialog.show(manager,"FullDialogFragment");
                dialog.setArguments(extras);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //カメラ
    private void cameraIntent(){
        Log.d("debug","cameraIntent()");

        // 保存先のフォルダーをカメラに指定した場合
        File cameraFolder = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM),"Camera");


        // 保存ファイル名
        String fileName = new SimpleDateFormat(
                "yyyyMMddHHmmss", Locale.US).format(new Date());
        filePath = String.format("%s/%s.jpg", cameraFolder.getPath(),fileName);
        Log.d("debug","filePath:"+filePath);
        p.setProductImage(filePath);

        // capture画像のファイルパス
        File cameraFile = new File(filePath);
        cameraUri = FileProvider.getUriForFile(
                LibraryEditActivity.this,
                getApplicationContext().getPackageName() + ".fileprovider",
                cameraFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(intent, RESULT_CAMERA);

        Log.d("debug","startActivityForResult()");
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent intent) {
        if (requestCode == RESULT_CAMERA) {

            if(cameraUri != null){
                imageView.setImageURI(cameraUri);

                registerDatabase(filePath);
            }
            else{
                Log.d("debug","cameraUri == null");
            }
        }
    }
    // アンドロイドのデータベースへ登録する
    private void registerDatabase(String file) {
        ContentValues contentValues = new ContentValues();
        ContentResolver contentResolver = LibraryEditActivity.this.getContentResolver();
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        contentValues.put("_data", file);
        contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
//        p.setProductImage(file);
    }

    // Runtime Permission check
    private void checkPermission(){
        // 既に許可している
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED){
            cameraIntent();
        }
        // 拒否していた場合
        else{
            requestPermission();
        }
    }

    // 許可を求める
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) { ActivityCompat.requestPermissions(LibraryEditActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        } else {
            Toast toast = Toast.makeText(this,
                    "許可されないとアプリが実行できません",
                    Toast.LENGTH_SHORT);
            toast.show();

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                    REQUEST_PERMISSION);
        }
    }

    // 結果の受け取り
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d("debug","onRequestPermissionsResult()");

        if (requestCode == REQUEST_PERMISSION) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraIntent();

            } else {
                // それでも拒否された時の対応
                Toast toast = Toast.makeText(this,
                        "これ以上なにもできません", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    /**
     * 種別詳細のボタンがクリックされた時。
     * @param view
     */
    public void onProductTypeDetailsClick(final View view) {

        // 対象ボタン
        final Button button = (Button)view;
        // 候補リスト
        DatabaseHelper helper = new DatabaseHelper(LibraryEditActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
        try {
            list = DataAccess.findTypeAll(db, 1);
            if(!"0".equals(_id)) {
                list2 = DataAccess.findType(db, 1, _id);
            }
        } catch (Exception ex) {
            Log.e("ERROR", ex.toString());
        } finally {
            db.close();
        }
        this._typeList = list;
        String[] typeList = null;
        typeList = new String[list.size()];
        for (int i=0; i<list.size(); i++){
            typeList[i] = list.get(i).get("name").toString();
        }
        final String[] likeArray = typeList;

        // 選択中の候補を取得
        final String buttonText = button.getText().toString();
        String[] textArray = buttonText.split("、");

        // 選択リスト
        boolean[] checkArray = new boolean[likeArray.length];
        for (int i = 0; i < likeArray.length; i++) {
            checkArray[i] = false;
            for (String data : textArray) {
                if (likeArray[i].equals(data)) {
                    checkArray[i] = true;
                    break;
                }
//                if(list2.get(i).get("id").toString().equals(list.get(i).get("id").toString())){
//                    checkArray[i] = true;
//                    break;
//                }
            }
        }

        // ダイアログを生成
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        // 選択イベント
        dialog.setMultiChoiceItems(likeArray, checkArray, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dia, int value, boolean isChecked) {

                String text = button.getText().toString();

                // 選択された場合
                if (isChecked) {
                    // ボタンの表示に追加
                    button.setText(text + ("".equals(text) ? "" : "、") + likeArray[value]);
                    Map map = new HashMap<String , Object>();
                    map.put("flag" , "1");
                    _typeList.set(value , map);
                } else {
                    // ボタンの表示から削除
                    if (text.indexOf(likeArray[value] + "、") >= 0) {
                        button.setText(text.replace(likeArray[value] + "、", ""));
                    } else if (text.indexOf("、" + likeArray[value]) >= 0) {
                        button.setText(text.replace("、" + likeArray[value], ""));
                    } else {
                        button.setText(text.replace(likeArray[value], ""));
                    }
                    Map map = new HashMap<String , Object>();
                    map.put("flag" , "0");
                    _typeList.set(value , map);
                }
            }
        });

        dialog.setPositiveButton("決 定", null);
        dialog.setNeutralButton("クリア", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int value) {
                button.setText("");
                // 再表示
                onProductTypeDetailsClick(view);
            }
        });

        dialog.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int value) {
                // 選択前の状態に戻す
                button.setText(buttonText);
            }
        });
        dialog.show();
    }

}
