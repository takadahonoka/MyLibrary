package local.hal.st32.android.mylibrary60143;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context){
        super(context , DATABASE_NAME , null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE products (");
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("name TEXT NOT NULL,");
        sb.append("product_type INT,");
        sb.append("number INT,");
        sb.append("deadline_type INT,");
        sb.append("deadline DATE,");
        sb.append("note TEXT,");
        sb.append("image VARCHAR(30)");
        sb.append(");");
        String sql = sb.toString();

        db.execSQL(sql);

        sb = new StringBuilder();
        sb.append("CREATE TABLE productstype (");
        sb.append("products_id INTEGER,");//商品ID
        sb.append("type_id INTEGER");//typeテーブルID
        sb.append(");");
        sql = sb.toString();

        db.execSQL(sql);

        sb = new StringBuilder();
        sb.append("CREATE TABLE type (");
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("type_no INTEGER NOT NULL,");//大まかな種別No(肉、魚・・・)
        sb.append("name TEXT NOT NULL");//種別名。
        sb.append(");");
        sql = sb.toString();

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db , int oldVersion , int newVersion){
    }

}
