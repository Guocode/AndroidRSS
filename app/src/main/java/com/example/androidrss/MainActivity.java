package com.example.androidrss;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.example.androidrss.Adapter.FeedAdapter;
import com.example.androidrss.Common.HTTPDataHandler;
import com.example.androidrss.Model.RSSObject;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.gson.Gson;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    RSSObject rssObject;
    SwipeRefreshLayout mSwipeLayout;
    // RSS link
    static String RSS_link = "http://rss.sina.com.cn/news/china/focus15.xml";
    private final String RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url=";
    private AlertDialog.Builder listDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("新浪新闻");
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_ly);
        mSwipeLayout.setRefreshing(true);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //这里可以做一下下拉刷新的操作
                loadRSS();

            }
        });

        loadRSS();
    }

    private void loadRSS() {
        AsyncTask<String, Void, String> loadRSSAsync = new AsyncTask<String, Void, String>() {

            //ProgressDialog mDialog = new ProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute() {
               // mDialog.setMessage("Please wait...");
               // mDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                String result;
                HTTPDataHandler http = new HTTPDataHandler();
                result = http.GetHTTPData(params[0]);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                //mDialog.dismiss();
                rssObject = new Gson().fromJson(s, RSSObject.class);
                FeedAdapter adapter = new FeedAdapter(rssObject, getBaseContext());
                ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(adapter);
                alphaAdapter.setInterpolator(new OvershootInterpolator());
                alphaAdapter.setFirstOnly(false);
                recyclerView.setAdapter(new AlphaInAnimationAdapter(alphaAdapter));
                adapter.notifyDataSetChanged();
                if(mSwipeLayout.isRefreshing()){
                    //关闭刷新动画
                    mSwipeLayout.setRefreshing(false);
                }
                Toast.makeText(MainActivity.this,
                        "刷新成功",Toast.LENGTH_SHORT).show();
                mSwipeLayout.setRefreshing(false);
            }
        };

        StringBuilder url_get_data = new StringBuilder(RSS_to_Json_API);
        url_get_data.append(RSS_link);
        loadRSSAsync.execute(url_get_data.toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh)
            //loadRSS();
            showListDialog();
        return true;
    }

    private void showListDialog() {
        final String[] items = { "   新浪新闻","   腾讯新闻","   网易新闻","   新华网新闻" };
        new MaterialDialog.Builder(this).title("请选择RSS源").items(items).itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                // which 下标从0开始
                // ...To-do
                switch (which) {
                    case 0:
                        RSS_link = getString(R.string.sina);
                        toolbar.setTitle("新浪新闻");
                        break;
                    case 1:
                        RSS_link = getString(R.string.qq);
                        toolbar.setTitle("腾讯新闻");
                        break;
                    case 2:
                        RSS_link = getString(R.string.netease);
                        toolbar.setTitle("网易新闻");
                        break;
                    case 3:
                        RSS_link = getString(R.string.xinhua);
                        toolbar.setTitle("新华网新闻");
                        break;
                }
                loadRSS();
            }
        }).show();





//        listDialog =  new AlertDialog.Builder(MainActivity.this);
//        //listDialog.setView(R.layout.dialog);
//        listDialog.setTitle("请选择RSS源");
//        listDialog.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // which 下标从0开始
//                // ...To-do
//                switch (which) {
//                    case 0:
//                        RSS_link = getString(R.string.sina);
//                        toolbar.setTitle("新浪新闻");
//                        break;
//                    case 1:
//                        RSS_link = getString(R.string.qq);
//                        toolbar.setTitle("腾讯新闻");
//                        break;
//                    case 2:
//                        RSS_link = getString(R.string.netease);
//                        toolbar.setTitle("网易新闻");
//                        break;
//                    case 3:
//                        RSS_link = getString(R.string.xinhua);
//                        toolbar.setTitle("新华网新闻");
//                        break;
//                }
//                loadRSS();
//            }
//        });
//        listDialog.show();
    }


}
