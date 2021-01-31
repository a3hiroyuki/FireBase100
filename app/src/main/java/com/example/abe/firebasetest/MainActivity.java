package com.example.abe.firebasetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Firebase mFirebaseRef;
    private Firebase mChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // クライアントライブラリにコンテキスト(Activity)をセットします。
        Firebase.setAndroidContext(this);

        // Firebaseアプリへの参照を取得します
        mFirebaseRef = new Firebase("https://mytestapp-972cc.firebaseio.com/");

        // 操作対象の参照を取得します
        mChild = mFirebaseRef.child("messages");

        mChild.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.v("abe", snapshot.toString());
            }
            @Override
            public void onCancelled(FirebaseError error) {
            }
        });

        Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
            int num;
            @Override
            public void onClick(View view) {
                Map<String, String> data = new HashMap<String, String>();
                data.put(String.valueOf(num++), "Hello");
                data.put(String.valueOf(num++), "Hello");
                data.put(String.valueOf(num++), "Hello");
                mChild.setValue(data, new Firebase.CompletionListener() {
                    public void onComplete(FirebaseError error, Firebase ref) {
                        Toast.makeText(getApplicationContext(), ref.toString(), Toast.LENGTH_SHORT).show();
                    };
                });
            }
        });
    }

}
