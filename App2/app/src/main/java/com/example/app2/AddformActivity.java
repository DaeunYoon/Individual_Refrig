package com.example.app2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class AddformActivity extends AppCompatActivity {

    EditText name;
    EditText amount;
    Spinner cate;
    FirebaseFirestore db;
    public static String TAG = "Refrig";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addform);
        db = FirebaseFirestore.getInstance();

        name = findViewById(R.id.name);
        amount = findViewById(R.id.amount);
        cate = findViewById(R.id.category);
    }

    public void CancelClick(View V) {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void SaveClick(View v) {

        Log.d("get", name.getText().toString() + " " + amount.getText().toString());

       if(name.getText().toString().matches("") || amount.getText().toString().matches("") ) {
            Toast.makeText(this, "Put all information", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> data = new HashMap<>();
        //put the data in Map
        data.put("name",name.getText().toString());
        if(cate.getSelectedItem().toString().equals("gram")) {
            data.put("unit","g");
        }
        else {
            data.put("unit","n");
        }
        data.put("amount",Integer.parseInt(amount.getText().toString()));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if( user != null) {
            db.collection(user.getEmail()).document(name.getText().toString())
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(AddformActivity.this, AddformActivity.class);
                            setResult(RESULT_OK, intent);
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Intent intent = new Intent(AddformActivity.this, AddformActivity.class);
                            setResult(RESULT_CANCELED, intent);
                            Log.w(TAG, "Error writing document", e);
                            finish();
                        }
                    });
        }
    }
}
