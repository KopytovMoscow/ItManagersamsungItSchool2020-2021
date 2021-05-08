package com.example.nitm;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout;
    private Canvas canvas;
    private TextView textView;
    EditText blockName;
    ImageView imageView;
    Button[] twoVertex = new Button[2];
    Map<Button, ArrayList> dictionary = new HashMap<Button, ArrayList>();
    Context thisContext;
    private DrawView vertexView;
    OkHttpClient client = new OkHttpClient();

    public int getStructure(String link) throws IOException {
        String url = "http://192.168.1.44:443/";
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
                            System.out.println("1111111111111111111111111111111111111111111111111111111111111111111111");
                            System.out.println(myResponse);
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
        try {
            getStructure("123");
        } catch (IOException e) {
            e.printStackTrace();
        }


//        String aw[] = new String[] {"1", "2", "3"};
//        String bw[][] = new String[][] {{"1", "3"}, {"1", "2"}, {"2", "3"}};
//        loadTheStructure(new String[]{"1", "2", "3"}, bw);

    }

    protected Button BlockInitialized(String text){
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
                    twoVertex[0].setBackgroundColor(Color.GRAY);
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
                linnear_lay.leftMargin = (int) event.getRawX() - 200; // отступ
                linnear_lay.topMargin = (int) event.getRawY() - 300; // отступ
                new_btn.setLayoutParams(linnear_lay);
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

    protected void loadTheStructure(String[] v, String[][] e){
        Map<String, Button> bb = new HashMap<String, Button>();
        for (int i=0; i < v.length; i++){
            System.out.println(v[i]);
            Button a11 = BlockInitialized(v[i]);
//            bb.put(, new Button(this));
            bb.put(v[i], (Button) a11);}
        for (int i=0; i < e.length; i++){
            ArrayList l = new ArrayList();

            l.add(bb.get(e[i][1]));
            System.out.println(e[i][0] + " 888888888888888 " + e[i][1]);
            dictionary.put((Button) bb.get(e[i][0]), (ArrayList) l);
        }
    }

    protected void alternativeStructureBuilder(Map<String, List<String>> gitStructure){
        System.out.println(gitStructure);
    }

    public void Load(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Важное сообщение!")
                .setMessage("Закройте окно!")
                .setCancelable(false)
                .setNegativeButton("ОК, иду на балкон",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}