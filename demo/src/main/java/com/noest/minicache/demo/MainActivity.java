package com.noest.minicache.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.noest.minicache.MiniCache;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    MiniCache miniCache;

    int times = 1000;
    ArrayList<String> keys = new ArrayList<>(times);
    ArrayList<byte[]> values = new ArrayList<>(times);

    TextView tvDisplay;
    EditText etTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.display);
        etTimes = findViewById(R.id.times);

        etTimes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                times = Integer.parseInt(etTimes.getText().toString());
                if (times > keys.size()) {
                    createRandomKV(times);
                }
            }
        });

        String init = MiniCache.init(getCacheDir().getAbsolutePath());

        if (init == null) {
            print("init failed");
        }

        miniCache = MiniCache.getDefaultCache();
        miniCache = MiniCache.getCache("default");

        createRandomKV(times);

    }

    void createRandomKV(int time) {
        keys.clear();
        values.clear();
        for (int i = 0; i < times; i++) {
            keys.add("key: " + i);
            byte[] buff = ("text text text text text " + i).getBytes();
            values.add(buff);
        }
    }

    public void get(View view) {
        long start = System.nanoTime();
        for (int i = 0; i < times; i++) {
            byte[] bytes = miniCache.get(keys.get(i));
            if (!Arrays.equals(bytes, values.get(i))) {
                print("get wrong at: " + i);
                return;
            }
        }
        long end = System.nanoTime();
        print("get coast time: " + (end - start) / 1000000.0f + "ms");
    }

    public void set(View view) {
        long start = System.nanoTime();
        for (int i = 0; i < times; i++) {
            miniCache.put(keys.get(i), values.get(i));
        }
        long end = System.nanoTime();
        print("set coast time: " + (end - start) / 1000000.0f + "ms");
    }

    void print(String msg) {
        Log.d(TAG, msg);
        if (tvDisplay.getText().length() > 2000) {
            tvDisplay.setText("");
        }
        tvDisplay.setText(msg + "\n" + tvDisplay.getText());
    }

    public void load(View view) {
        long start = System.nanoTime();
        miniCache.reload();
        long end = System.nanoTime();
        print("load coast time: " + (end - start) / 1000000.0f + "ms");
    }
}
