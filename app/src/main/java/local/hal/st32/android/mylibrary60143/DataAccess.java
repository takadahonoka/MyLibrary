package local.hal.st32.android.mylibrary60143;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataAccess {

    /**
     * 全データ検索メソッド
     * @param db SQLiteDatabaseオブジェクト
     * @return 検索結果のCursorオブジェクト
     * */
    public static Cursor findAll(SQLiteDatabase db , int no , int sqlwhere){
        String sql = "";
        if(no == 0){
            sql = "SELECT _id , name , product_type , number , deadline_type , deadline , note , image , strftime('%Y年%m月%d日', deadline) AS deadline2 FROM products ORDER BY deadline DESC ;";
        }else{
            sql = "SELECT _id , name , product_type , number , deadline_type , deadline , note , image , strftime('%Y年%m月%d日', deadline) AS deadline2 FROM products WHERE product_type = '"+ sqlwhere +"' ORDER BY deadline DESC ;";
        }
        Cursor cursor = db.rawQuery(sql , null);
        return cursor;
    }

    /**
     * 主キーによる検索
     * @param db SQLiteDatabaseオブジェクト
     * @param id 主キー値
     * @return 主キーに対応するデータを格納したShopオブジェクト。対応するデータが存在しない場合はnull。
     */
    public static Product findByPK(SQLiteDatabase db , int id){
        String sql = "SELECT _id , name , product_type , number , deadline_type , deadline , note , image , strftime('%Y年%m月%d日', deadline) AS deadline2 FROM products WHERE _id =" + id ;
        Cursor cursor = db.rawQuery(sql , null);
        Product result = null;
        if(cursor.moveToFirst()){
            int idxName = cursor.getColumnIndex("name");
            int idxType = cursor.getColumnIndex("product_type");
            int idxNumber = cursor.getColumnIndex("number");
            int idxDeadLineType = cursor.getColumnIndex("deadline_type");
            int idxDeadLine = cursor.getColumnIndex("deadline");
            int idxMemo = cursor.getColumnIndex("note");
            int idxImage = cursor.getColumnIndex("image");

            String name = cursor.getString(idxName);
            String type = cursor.getString(idxType);
            String number = cursor.getString(idxNumber);
            String deadlinetype = cursor.getString(idxDeadLineType);
            String deadline = cursor.getString(idxDeadLine);
            String memo = cursor.getString(idxMemo);
            String image = cursor.getString(idxImage);

            result = new Product();
            result.setProductId(String.valueOf(id));
            result.setProductName(name);
            result.setProductType(type);
            result.setProductNumber(number);
            result.setProductDateType(deadlinetype);
            result.setProductDate(deadline);
            result.setProductMemo(memo);
            result.setProductImage(image);
        }
        return result;
    }

    /**
     * 詳細種別全データ検索メソッド
     * @param db SQLiteDatabaseオブジェクト
     * @return 検索結果のCursorオブジェクト
     * */
    public static ArrayList<Map<String , Object>> findTypeAll(SQLiteDatabase db , int no ){
        String sql = "SELECT _id , name FROM type WHERE type_no = '"+ no +"'  ORDER BY _id DESC ;";

        Cursor cursor = db.rawQuery(sql , null);
        ArrayList<Map<String , Object>> list = new ArrayList<Map<String, Object>>();
        if (cursor.moveToFirst()){
            while (cursor.moveToNext()) {
                int idxName = cursor.getColumnIndex("_id");
                int idxType = cursor.getColumnIndex("name");
                String id = cursor.getString(idxName);
                String name = cursor.getString(idxType);
                Map map = new HashMap<String, Object>();
                map.put("_id", id);
                map.put("name", name);
                map.put("flag", "0");
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 詳細種別全データ検索メソッド
     * @param db SQLiteDatabaseオブジェクト
     * @return 検索結果のCursorオブジェクト
     * */
    public static ArrayList<Map<String , Object>> findType(SQLiteDatabase db , int no , int _id ){
        String sql = "SELECT t._id , t.name FROM type t INNER JOIN productstype p ON t._id = p.type_id WHERE t.type_no = '"+ no +"' AND products_id = '"+ _id +"' ORDER BY t._id DESC ;";

        Cursor cursor = db.rawQuery(sql , null);
        ArrayList<Map<String , Object>> list = new ArrayList<Map<String, Object>>();
        if (cursor.moveToFirst()){
            while (cursor.moveToNext()) {
                int idxName = cursor.getColumnIndex("t._id");
                int idxType = cursor.getColumnIndex("t.name");
                String id = cursor.getString(idxName);
                String name = cursor.getString(idxType);
                Map map = new HashMap<String, Object>();
                map.put("_id", id);
//                map.put("name", name);
//                map.put("flag", "0");
                list.add(map);
            }
        }
        return list;
    }

    /**
     *商品情報を新規登録するメソッド
     * @param db SQLiteDatabaseオブジェクト
     * @return 登録したレコードの主キーの値
     * */
    public static long insert(SQLiteDatabase db, String name , String product_type , int number , int deadline_type , String deadline , String note , String image){
        String sql = "INSERT INTO products (name,product_type,number,deadline_type,deadline,note,image) VALUES (? , ? , ? , ? , ? , ? ,?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1 , name);
        stmt.bindString(2 , product_type);
        stmt.bindString(3 , String.valueOf(number));
        stmt.bindString(4 , String.valueOf(deadline_type));
        stmt.bindString(5 , deadline);
        stmt.bindString(6 , note);
        stmt.bindString(7 , image);
        long id = stmt.executeInsert();
        return id;
    }

    /**
     *商品種別詳細を新規登録するメソッド
     * @param db SQLiteDatabaseオブジェクト
     * @return 登録したレコードの主キーの値
     * */
    public static long insertType(SQLiteDatabase db,String _id , String type_id){
        String sql = "INSERT INTO productstype (_id,type_id) VALUES (?,?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1 , _id);
        stmt.bindString(2 , type_id);
        long id = stmt.executeInsert();
        return id;
    }

    /**
     *商品種別詳細を新規登録するメソッド
     * @param db SQLiteDatabaseオブジェクト
     * @return 登録したレコードの主キーの値
     * */
    public static long insertType2(SQLiteDatabase db,String type_no , String type_id){
        String sql = "INSERT INTO type (type_no,name) VALUES (?,?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1 , type_no);
        stmt.bindString(2 , type_id);
        long id = stmt.executeInsert();
        return id;
    }


    /**
     *商品情報を更新するメソッド
     * @param db SQLiteDatabaseオブジェクト
     * @param id 主キー値
     * @return 更新件数
     * */
    public static int update(SQLiteDatabase db, int id , String name , String product_type , int number , int deadline_type , String deadline , String note , String image){
        String sql = "UPDATE products SET name = ?,product_type = ?,number = ?,deadline_type = ?,deadline = ?,note = ?,image = ? WHERE _id = ?";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1 , name);
        stmt.bindString(2 , product_type);
        stmt.bindString(3 , String.valueOf(number));
        stmt.bindString(4 , String.valueOf(deadline_type));
        stmt.bindString(5 , deadline);
        stmt.bindString(6 , note);
        stmt.bindString(7 , image);
        stmt.bindString(8 , String.valueOf(id));
        int result = stmt.executeUpdateDelete();
        return result;
    }

    /**
     *商品情報を削除するメソッド
     * @param db SQLiteDatabaseオブジェクト
     * @return 登録したレコードの主キーの値
     * */
    public static int delete(SQLiteDatabase db, int id){
        String sql = "DELETE FROM products WHERE _id = ? ";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindLong(1 , id);
        int result = stmt.executeUpdateDelete();
        return result;
    }
}
