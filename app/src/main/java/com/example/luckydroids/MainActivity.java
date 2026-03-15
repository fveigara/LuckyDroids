package com.example.luckydroids;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ImageView mSlot1, mSlot2, mSlot3;
    private TextView mGanancias;
    private Button mJugar;
    private RelativeLayout mRelative;

    private Random mRandom;
    private int mIntSlot1, mIntSlot2, mIntSlot3, mIntGanancias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSlot1 = findViewById(R.id.mainActivitySlot1);
        mSlot2 = findViewById(R.id.mainActivitySlot2);
        mSlot3 = findViewById(R.id.mainActivitySlot3);
        mGanancias = findViewById(R.id.mainActivityTvGanancias);
        mJugar = findViewById(R.id.mainActivityBtJugar);
        mRelative = findViewById(R.id.mainActivityRl);

        mRandom = new Random();
        mIntGanancias = 10;
        mJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Has lanzado -1 euro", Toast.LENGTH_SHORT).show();

                mSlot1.setImageResource(R.drawable.animation);
                final AnimationDrawable slot1Anim = (AnimationDrawable) mSlot1.getDrawable();
                slot1Anim.start();

                mSlot2.setImageResource(R.drawable.animation);
                final AnimationDrawable slot2Anim = (AnimationDrawable) mSlot2.getDrawable();
                slot2Anim.start();

                mSlot3.setImageResource(R.drawable.animation);
                final AnimationDrawable slot3Anim = (AnimationDrawable) mSlot3.getDrawable();
                slot3Anim.start();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        slot1Anim.stop();
                        slot2Anim.stop();
                        slot3Anim.stop();

                        ponerImagenes();
                        dineroAcumulado();
                    }
                }, 1000);
            }
        });
    }

    private void ponerImagenes() {
        mIntSlot1 = mRandom.nextInt(5);
        mIntSlot2 = mRandom.nextInt(5);
        mIntSlot3 = mRandom.nextInt(5);

        switch (mIntSlot1) {
            case 0:
                mSlot1.setImageResource(R.drawable.ic_bot);
                break;
            case 1:
                mSlot1.setImageResource(R.drawable.ic_cable);
                break;
            case 2:
                mSlot1.setImageResource(R.drawable.ic_droid);
                break;
            case 3:
                mSlot1.setImageResource(R.drawable.ic_robot);
                break;
            case 4:
                mSlot1.setImageResource(R.drawable.ic_vr);
                break;
        }

        switch (mIntSlot2) {
            case 0:
                mSlot2.setImageResource(R.drawable.ic_bot);
                break;
            case 1:
                mSlot2.setImageResource(R.drawable.ic_cable);
                break;
            case 2:
                mSlot2.setImageResource(R.drawable.ic_droid);
                break;
            case 3:
                mSlot2.setImageResource(R.drawable.ic_robot);
                break;
            case 4:
                mSlot2.setImageResource(R.drawable.ic_vr);
                break;
        }

        switch (mIntSlot3) {
            case 0:
                mSlot3.setImageResource(R.drawable.ic_bot);
                break;
            case 1:
                mSlot3.setImageResource(R.drawable.ic_cable);
                break;
            case 2:
                mSlot3.setImageResource(R.drawable.ic_droid);
                break;
            case 3:
                mSlot3.setImageResource(R.drawable.ic_robot);
                break;
            case 4:
                mSlot3.setImageResource(R.drawable.ic_vr);
                break;
        }

    }

    private void dineroAcumulado() {
        if (mIntSlot1 == mIntSlot2 && mIntSlot2 == mIntSlot3) {
            Snackbar.make(mRelative, "Has ganado 100 euros", Snackbar.LENGTH_SHORT).show();
            mIntGanancias = mIntGanancias + 100;
        } else if ((mIntSlot1 == mIntSlot2 || mIntSlot1 == mIntSlot3 || mIntSlot2 == mIntSlot3)) {
            Snackbar.make(mRelative, "Has ganado 5 euros", Snackbar.LENGTH_SHORT).show();
            mIntGanancias = mIntGanancias + 5;
        }
        mIntGanancias = mIntGanancias - 1;
        mGanancias.setText(String.valueOf(mIntGanancias));
    }
}