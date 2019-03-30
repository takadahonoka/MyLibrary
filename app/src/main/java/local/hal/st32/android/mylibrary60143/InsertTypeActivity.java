package local.hal.st32.android.mylibrary60143;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class InsertTypeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_type);

        setTitle("新規種別追加");

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //DrawerLayout
        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //レフトナビ本体。
        NavigationView navigationView = findViewById(R.id.nvSideMenuButton);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * 追加ボタンをクリックした時。
     * @param view
     */
    public void onInsertTypeClick(View view){
        //商品種別
        Spinner spProductType = findViewById(R.id.spProductType);
        String productType =  String.valueOf(spProductType.getSelectedItemPosition());
        //詳細種別名
        EditText etTypeName = findViewById(R.id.etTypeName);
        String typeName = etTypeName.getText().toString();

        if(!"".equals(typeName) && !"0".equals(productType)) {

            DatabaseHelper helper = new DatabaseHelper(InsertTypeActivity.this);
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                DataAccess.insertType2(db, productType, typeName);
                Toast.makeText(InsertTypeActivity.this , "登録されました。" , Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Log.e("ERROR", ex.toString());
            } finally {
                db.close();
            }
        }else{
            Toast.makeText(InsertTypeActivity.this , "種別を選択し、詳細種別名を入力してください。" , Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * レフトナビ以外をクリックした時の動き。
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * レフトナビをクリックした時。
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.nav_all:
                Intent intent = new Intent(InsertTypeActivity.this , LibraryListActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_vegetables:
                intent = new Intent(InsertTypeActivity.this , LibraryListActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_meat:
                intent = new Intent(InsertTypeActivity.this , LibraryListActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_fish:
                intent = new Intent(InsertTypeActivity.this , LibraryListActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_seasoning:
                intent = new Intent(InsertTypeActivity.this , LibraryListActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_new_type:
                break;
        }

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //DrawerLayout
        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
