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
import android.widget.RelativeLayout;
import android.widget.TabHost;
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
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
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

//        Button new_btn = new Button(new ContextThemeWrapper(this, R.style.Widget_MaterialComponents_Button_OutlinedButton), null, 0);

        Button new_btn = new Button(this);
        new_btn.setText(text);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        imageView = (ImageView) findViewById(R.id.aaa);
        thisContext = this;

//        relativeLayout.addView(aye);
        new_btn.setText(text);
        new_btn.setLayoutParams(
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT)
        );
        new_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v){System.out.println("onLongClick"); return false; }
        });

        new_btn.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {

            }

            @Override
            public void onDoubleClick(View v){
                RelativeLayout.LayoutParams linnear_lay = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT); // высота и
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
                    int sx = (int) (twoVertex[0].getX() +
                            (twoVertex[0].getLayoutParams().width / 2));
                    int sy = (int) (twoVertex[0].getY() +
                            (twoVertex[0].getLayoutParams().height / 2));
                    int ex = (int) (v.getX() + (v.getLayoutParams().width / 2));
                    int ey = (int) (v.getY() + (v.getLayoutParams().height / 2));
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
            public boolean onTouch(View v, MotionEvent event) {
                int a = (int) v.getX();
                String b = Integer.toString(a);
                int c = (int) v.getY();
                String d = Integer.toString(c);
                textView.setText(b + " " + d);
                RelativeLayout.LayoutParams linnear_lay = new RelativeLayout.LayoutParams(
                        new_btn.getWidth(),
                        new_btn.getHeight()); // высота и
                linnear_lay.leftMargin = (int) event.getRawX() -
                        (int) v.getLayoutParams().width / 2; // отступ
                linnear_lay.topMargin = (int) event.getRawY() - 2 * v.getHeight();
                new_btn.setLayoutParams(linnear_lay);
                System.out.println(v.getHeight());
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

    public void Load(View view) {
        System.out.println(String.valueOf(blockLink.getText()).replace("/", "Ю"));
        try {
            getStructure(String.valueOf(blockLink.getText()).replace("/", "Ю"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}