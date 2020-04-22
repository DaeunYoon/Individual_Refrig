package com.example.app2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    public static String TAG = "Refrig";
    EditText name;
    EditText amount;
    Spinner cate;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String iname = intent.getStringExtra("INGREDIENT_NAME");
        int iamount = intent.getIntExtra("INGREDIENT_AMOUNT", 0);
        String iunit = intent.getStringExtra("INGREDIENT_UNIT");

        name = findViewById(R.id.name);
        amount = findViewById(R.id.amount);
        cate = findViewById(R.id.category);


        name.setText(iname);
        amount.setText(Integer.toString(iamount));

        if(iunit.matches("n"))
            cate.setSelection(1);
        else
            cate.setSelection(0);
    }

    public void CancelClick(View V) {
        Toast.makeText(this, "CANCELED", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void SaveClick(View V) {
        if(amount.getText().toString().matches("") ) {
            Toast.makeText(this, "Put all information", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
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
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                check(1);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                                check(2);
                                finish();
                            }
                        });
            }


        }
    }

    void check(int ch) {
        if(ch == 1) {
            Toast.makeText(EditActivity.this, "Change saved", Toast.LENGTH_SHORT).show();
            finish();
        }
        else if (ch == 2) {
            Toast.makeText(EditActivity.this, "Error occurred, Change not saved", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void DeleteClick(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if( user != null) {
            db.collection(user.getEmail()).document(name.getText().toString())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });
        }
        else {
            Intent intent = new Intent(EditActivity.this, SigninActivity.class);
            startActivity(intent);
        }
        finish();
    }


}
