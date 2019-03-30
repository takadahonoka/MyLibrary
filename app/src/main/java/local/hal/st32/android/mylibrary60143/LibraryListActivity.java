package local.hal.st32.android.mylibrary60143;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Calendar;

public class LibraryListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    static final int MODE_INSERT = 1;
    static final int MODE_EDIT = 2;
    private int MODE_PAGE = 0;
    private ListView _lvLibraryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_list);

        setTitle("食材残量リスト");

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

        _lvLibraryList = findViewById(R.id.lvLibraryList);
        _lvLibraryList.setOnItemClickListener(new ListItemClickListener());

        String[] from = {"name","deadline2"};
        int[] to = {R.id.tvRowTitle,R.id.tvRowDate};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(LibraryListActivity.this,R.layout.row,null,from,to,0);
        adapter.setViewBinder(new CustomViewBinder());
        _lvLibraryList.setAdapter(adapter);

    }

    @Override
    public void onResume(){
        super.onResume();
        setNewCursor();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.library_list_temple, menu);
        return true;
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

        String title = "";

        switch (id){
            case R.id.nav_all:
                MODE_PAGE = 0;
                title = "全件";
                setNewCursor();
                break;
            case R.id.nav_vegetables:
                MODE_PAGE = 1;
                title = "野菜";
                setNewCursor();
                break;
            case R.id.nav_meat:
                MODE_PAGE = 2;
                title = "肉";
                setNewCursor();
                break;
            case R.id.nav_fish:
                MODE_PAGE = 3;
                title = "魚";
                setNewCursor();
                break;
            case R.id.nav_seasoning:
                MODE_PAGE = 4;
                title = "調味料";
                setNewCursor();
                break;
            case R.id.nav_new_type:
                Intent intent = new Intent(LibraryListActivity.this,InsertTypeActivity.class);
                startActivity(intent);
                break;
        }

        //ツールバー(レイアウトを変更可)。
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(title);

        //DrawerLayout
        DrawerLayout drawer = findViewById(R.id.dlMainContent);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * カーソルアダプタ内のカーソルを更新するメソッド。
     */
    private void setNewCursor(){

        DatabaseHelper helper = new DatabaseHelper(LibraryListActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        int no = 0;
        if(MODE_PAGE != 0){ no = 1; }
        Cursor cursor = DataAccess.findAll(db,no,MODE_PAGE);
        SimpleCursorAdapter adapter = (SimpleCursorAdapter)_lvLibraryList.getAdapter();
        adapter.changeCursor(cursor);
    }

    /**
     * リストがクリックされた時のリスナクラス。
     * */
    private class ListItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent , View view , int position , long id){
            Cursor item = (Cursor)parent.getItemAtPosition(position);
            int idxId = item.getColumnIndex("_id");
            int idNo = item.getInt(idxId);

            Intent intent = new Intent(LibraryListActivity.this , LibraryEditActivity.class);
            intent.putExtra("mode" , MODE_EDIT);
            intent.putExtra("idNo" , idNo);
            startActivity(intent);
        }
    }

    /**
     * 新規ボタンをクリックした時。
     * @param view
     */
    public void onNewButtonClick(View view){
        Intent intent = new Intent(LibraryListActivity.this , LibraryEditActivity.class);
        intent.putExtra("mode" , MODE_INSERT);
        startActivity(intent);
    }

    /**
     * リストビューのカスタムビューバインダークラス。
     */
    private class CustomViewBinder implements SimpleCursorAdapter.ViewBinder{

        @Override
        public boolean setViewValue(View view,Cursor cursor,int columnIndex) {

            int viewId = view.getId();

            switch (viewId) {
                case R.id.tvRowTitle:
                    TextView tvRowTitle = (TextView) view;
                    String roeTitle = cursor.getString(columnIndex);
                    tvRowTitle.setText(roeTitle);
                    return true;
                case R.id.tvRowDate:
                    TextView tvRowdate = (TextView) view;
                    String rowDate = cursor.getString(columnIndex);
                    tvRowdate.setText(rowDate);
                    return true;
            }
            return false;
        }
    }
}
