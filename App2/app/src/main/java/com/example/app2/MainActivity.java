package com.example.app2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Access a Cloud Firestore instance from your Activity

    public static String TAG = "Refrig";
    private ArrayList<Ingredient> arrayList;
    private IngredientsAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        /*array for recycler view*/
        arrayList = new ArrayList<>();
        adapter = new IngredientsAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);

        IncludeData();


    }


    @Override
    protected void onRestart() {
        super.onRestart();
        IncludeData();
    }

    protected void IncludeData() {
        /*get users ingredients*/
        arrayList.clear();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if( user != null) {
            db.collection(user.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Ingredient ingredient = document.toObject(Ingredient.class);
                                    arrayList.add(ingredient);
                                    /*refresh*/
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        else {
            Intent intent = new Intent(MainActivity.this, SigninActivity.class);
            startActivity(intent);
        }
    }

    public void addIng(View v) {
        Intent intents = new Intent(MainActivity.this, BarcodeActivity.class);
        startActivity(intents);
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
                intent = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
