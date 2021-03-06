package com.example.nitm;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout;
    private Canvas canvas;
    private TextView textView;
    EditText blockName;
    EditText blockLink;
    ImageView imageView;
    Button[] twoVertex = new Button[2];
    Map<Button, ArrayList> dictionary = new HashMap<Button, ArrayList>();
    Context thisContext;
    private DrawView vertexView;
    OkHttpClient client = new OkHttpClient();
    TabHost tabHost;
    private int mX;
    private int mY;

    public static Map<String, String> jsonToMap(String t) throws JSONException {

        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while( keys.hasNext() ){
            String key = (String)keys.next();
            String value = jObject.getString(key);
            map.put(key, value);

        }

        System.out.println("json : "+jObject);
        System.out.println("map : "+map);
        return map;
    }

    public void addGraphToScreen(Map<String, String> graph){
        int lenGraph = graph.keySet().size();
        List<String> mapKeySet = new ArrayList<>(graph.keySet());
        for(int i = 0; i < lenGraph; i++){
            String instantKey = mapKeySet.get(i);
            String child = graph.get(instantKey);
            assert child != null;
            String[] divided = child.split(",");

            Button indexButton = BlockInitialized(instantKey);
//            Button childButton = BlockInitialized(child);
//            List<Button> allChildButtons = [childButton];
//            List<Button> allChildButtons = Collections.singletonList(childButton);
            ArrayList<Button> allChildButtons = new ArrayList<>(Arrays.asList());

            for (String word : divided) {
                System.out.println(word);
                allChildButtons.add(BlockInitialized(word));
            }

            dictionary.put(indexButton, allChildButtons);
        }
    }

    public int getStructure(String link) throws IOException {
        String url = "http://192.168.1.44:443/" + link;
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println("ayeayey aye  aye aye aye aue aye aye");
                                HashMap<String, String> gitMap = (HashMap<String, String>) jsonToMap(myResponse);
                                addGraphToScreen(gitMap);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        textView = (TextView) findViewById(R.id.textView);
        blockName = (EditText) findViewById(R.id.editTextTextPersonName);
        Button[] twoVertex = new Button[2]; // array that contain from 0 to 2 vertex to draw an edge between it
        vertexView = new DrawView(this, 0, 0, 100, 200);
        relativeLayout.addView(vertexView);
        blockLink = (EditText) findViewById(R.id.editTextTextPersonName2);


        // TabHost:
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec tabSpec;
        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("add new block");
        tabSpec.setContent(R.id.add);
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("import from GitHub");
        tabSpec.setContent(R.id.Import);
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("tag3");
        View v = getLayoutInflater().inflate(R.layout.tab_header, null);
        tabSpec.setIndicator("settings");
        tabSpec.setContent(R.id.settings);
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTabByTag("tag2");
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
            }
        });

    }

    protected Button BlockInitialized(String text){
        Button new_btn = new Button(this);
        new_btn.setX(-10);
        new_btn.setX(0);
        new_btn.setBackgroundColor(Color.DKGRAY);
        new_btn.setText(text);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        imageView = (ImageView) findViewById(R.id.aaa);
        thisContext = this;
        new_btn.setText(text);
        RelativeLayout.LayoutParams RL_LP = new RelativeLayout.LayoutParams(200, 200);
        new_btn.setLayoutParams(RL_LP);
        new_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v){System.out.println("onLongClick"); return false;}
        });

        new_btn.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {

            }

            @Override
            public void onDoubleClick(View v){
                System.out.println("onDoubleClickonDoubleClickonDoubleClickonDoubleClickonDoubleClick");
                RelativeLayout.LayoutParams linnear_lay = new RelativeLayout.LayoutParams(
                        200,
                        200); // ???????????? ??
                linnear_lay.leftMargin = (int) new_btn.getX();
                linnear_lay.topMargin = (int) new_btn.getY();
                if (twoVertex[0] == null){
                    twoVertex[0] = (Button) v;
                    v.setBackgroundColor(Color.YELLOW);
                }
                else{
                    List ast = new ArrayList();
                    if ( Arrays.asList(dictionary.keySet()).contains(twoVertex[0])){
                        ast = (List) dictionary.get(twoVertex[0]);
                    }
                    ast.add((Button) v);
                    dictionary.put(twoVertex[0], (ArrayList) ast);
                    vertexView.invalidate(dictionary);
                    twoVertex[0].setBackgroundColor(Color.DKGRAY);
                    twoVertex[0] = null;
                    twoVertex[1] = null;
                }

                new_btn.setLayoutParams(linnear_lay);
            }
        });

        new_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                //?????????????????????? ?????????????????? ?????????? getRawX() ?? getRawY() ????????
                //???????????????????? ???? ?????????????????? ?? ???????????????? ???????????? ????????????????????:
                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    //ACTION_DOWN ?????????????????????? ?????? ?????????????????????????? ?? ????????????,
                    //?????????? ???????????????????????? ?????????????????? ?????????????????? ?????????????????? ??????????????:
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        mX = X - lParams.leftMargin;
                        mY = Y - lParams.topMargin;
                        break;

                    //ACTION_MOVE ???????????????????????? ?????????????????????? ?? ???????????????? ?????????????????????????? ??????????????????, ??????????
                    //???????????????????? ???????????????????? ?? ?????????????????? ??????????, ?????? ?????????????????? ???????????? ?????????? ?????????????????? ???????????????? ?????????????????????????? ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                .getLayoutParams();
                        layoutParams.leftMargin = X - mX;
                        layoutParams.topMargin = Y - mY;
                        layoutParams.rightMargin = -250;
                        layoutParams.bottomMargin = -250;
                        view.setLayoutParams(layoutParams);
                        break;

                }
                vertexView.invalidate(dictionary);
                return false;
            }
        });
        relativeLayout.addView(new_btn);
        return new_btn;

    }

    public void newBlock(View view){
        String name = String.valueOf(blockName.getText());
        BlockInitialized(name);
    }

    public void HideOrShowMenu(View view){
        System.out.println("--HideOrShowMenu--");
    }

    public void deleteBlock(View view){
        if(twoVertex[0] != null)
        {
            System.out.println(dictionary.containsKey(twoVertex[0]));
            dictionary.remove((Button) twoVertex[0]);
            int dictSize = dictionary.keySet().size();
            List<Button> buttonsKey = new ArrayList<Button>(dictionary.keySet());
            for(int i = 0; i < buttonsKey.size(); i++) {
                Button key = buttonsKey.get(i);
                Object o = dictionary.get(key);
                ArrayList al1 = new ArrayList();
                al1 = (ArrayList) o;
                assert al1 != null;
                if(al1.contains(twoVertex[0])){
                    Objects.requireNonNull(dictionary.get(key)).remove(twoVertex[0]);
                }
                System.out.println(al1);
                System.out.println(o.getClass());
            }
            relativeLayout.removeView(twoVertex[0]);
            twoVertex[0] = null;
            vertexView.invalidate(dictionary);
        }
    }

    public void Load(View view) {
        System.out.println(String.valueOf(blockLink.getText()).replace("/", "??"));
        try {
            getStructure(String.valueOf(blockLink.getText()).replace("/", "??"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}