package com.example.app2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

public class BarcodeActivity extends AppCompatActivity {

    public static String TAG = "Refrig";
    private static final int ADD = 1;

    private LinearLayout linearLayout;
    private TextView textView;
    private EditText name;
    private EditText amount;
    private Spinner cate;
    private Button button;

    FirebaseFirestore db;
    Ingredient ingredient = null;
    int barcode;
    int ck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        db = FirebaseFirestore.getInstance();

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.addbutton);
        if(ingredient == null) {
            button.setVisibility(View.INVISIBLE);
        }
        linearLayout = findViewById(R.id.linearLayout2);
        name = findViewById(R.id.name);
        amount = findViewById(R.id.amount);
        cate = findViewById(R.id.category);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);

    }

    public void ScanButton(View view) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.initiateScan();
    }

    public void InputButton(View view) {
        Intent intent = new Intent(this, AddformActivity.class);
        startActivityForResult(intent, ADD);
    }

    public void AddButton(View view) {
        Map<String, Object> data = new HashMap<>();

        /*in case database doesn't have information about ingredients*/
        if(ingredient == null) {
            /*get name and input add -> ingredient information will added*/
            if(name.getText().toString().matches("")) {
                Toast.makeText(this, "Input ingredients name.", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                data.put("name", name.getText().toString());
                data.put("barcode", Integer.toString(barcode));
                db.collection("Ingredients").document(name.getText().toString())
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully saved!");
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                                Toast.makeText(BarcodeActivity.this, "Database update failed", Toast.LENGTH_LONG).show();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        });
            }
        }
        else {
            /*get amount and cate -> add*/
            if(amount.getText().toString().matches("")) {
                Toast.makeText(this, "Input ingredients amount.", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                //put the data in Map
                data.put("name",ingredient.getName());
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
                                    Log.d(TAG, "DocumentSnapshot successfully saved!");
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                    Toast.makeText(BarcodeActivity.this, "Database update failed", Toast.LENGTH_LONG).show();
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            });
                }
            }
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "CANCELED", Toast.LENGTH_SHORT).show();
            }
        }
        else if (intentResult != null) {
            if (intentResult.getContents() == null) {
                textView.setText("Failed to scan Barcode");
            }
            else {
                barcode = Integer.parseInt(intentResult.getContents());
                button.setVisibility(View.VISIBLE);
                db.collection("Ingredients")
                        .whereEqualTo("barcode", Integer.toString(barcode))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        ingredient = document.toObject(Ingredient.class);
                                        Log.d(TAG, ingredient + " 1");
                                    }
                                    setText();
                                }
                                else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                    textView.setText("Error occurred.");
                                    button.setVisibility(View.INVISIBLE);
                                }

                            }
                        });
            }

        }
    }

    private void setText(){
        if(ingredient == null) {
            textView.setText("We don't have this barcode information. By add information about this, you can help developing our application !");
            name.setVisibility(View.VISIBLE);
        }
        else{
            name.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
            name.setText(ingredient.getName());
            name.setFocusable(false);
            name.setEnabled(false);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.item1:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.item2:
                intent = new Intent(this, RecipeActivity.class);
                startActivity(intent);
                return true;
            case R.id.item3:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent( this, SigninActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
