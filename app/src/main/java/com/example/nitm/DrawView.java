package com.example.nitm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class DrawView extends View {
    private int sx1;
    private int sy1;
    private int ex1;
    private int ey1;
    private int yyy = 0;
    Paint paint;
    Map<Button, ArrayList> dictionary = new HashMap<Button, ArrayList>();;

    public DrawView(Context context, int sx, int sy, int ex, int ey) {
        super(context);
        ex1 = ex;
        ey1 = ey;
        sx1 = sx;
        sy1 = sy;
        Paint paint = new Paint();
        paint.setColor(Color.GREEN); // установим белый цвет
        paint.setStrokeWidth(5);
//        paint.setStyle(Paint.Style.FILL); // заливаем
        paint.setAntiAlias(true);
    }

    //    @Override
    protected void onDraw(Canvas canvas) {
        Paint pai = new Paint();
        pai.setColor(Color.GREEN); // установим белый цвет
        pai.setStrokeWidth(5);
        pai.setStyle(Paint.Style.FILL); // заливаем
        pai.setAntiAlias(true);
        if (dictionary != null){
            for (Map.Entry<Button, ArrayList> entry : dictionary.entrySet()) {
                ArrayList btns = (ArrayList) entry.getValue();
                int sx = (int) (entry.getKey().getX() +
                        (entry.getKey().getLayoutParams().width / 2));
                int sy = (int) (entry.getKey().getY() +
                        (entry.getKey().getLayoutParams().height / 2));
                for (int i = 0; i < btns.size(); i++) {
                    Button btn = (Button) btns.get(i);
                    int ex = (int) (btn.getX() + (btn.getLayoutParams().width / 2));
                    int ey = (int) (btn.getY() + (btn.getLayoutParams().height / 2));
//                                                   relativeLayout.addView(new DrawView(thisContext, sx, sy, ex, ey));

                    canvas.drawLine(sx, sy, ex, ey, pai);
                }
            }

        }
    }

    public void invalidate(Map<Button, ArrayList> dictionary1) {
        dictionary = dictionary1;
        super.invalidate();
    }
}
