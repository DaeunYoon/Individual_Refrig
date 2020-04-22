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
import android.widget.RadioButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {

    public static String TAG = "Recipe";
    private ArrayList<Recipe> arrayList;
    private ArrayList<Ingredient> ingr = new ArrayList<Ingredient>();
    private RecipeAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    FirebaseFirestore db;
    int menu = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        /*array for recycler view*/
        arrayList = new ArrayList<>();
        adapter = new RecipeAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);

        getUserIng();
        IncludeData();
    }

    protected void getUserIng() {

        ingr.clear();
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
                                    ingr.add(ingredient);
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        else {
            Intent intent = new Intent(RecipeActivity.this, SigninActivity.class);
            startActivity(intent);
        }
    }


    protected void IncludeData() {
        Log.d(TAG, "call");

        arrayList.clear();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if( user != null) {
            db.collection("Recipes")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Recipe recipe = document.toObject(Recipe.class);
                                    int gap = recipe.compare(ingr);
                                    if(menu < 3 && menu == gap) {
                                        arrayList.add(recipe);
                                        adapter.notifyDataSetChanged();
                                    }
                                    else if (menu == 3 && gap >= 3){
                                        arrayList.add(recipe);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
            adapter.notifyDataSetChanged();
        }
        else {
            Intent intent = new Intent(RecipeActivity.this, SigninActivity.class);
            startActivity(intent);
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
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radio:
                if (checked)
                    menu = 0;
                    break;
            case R.id.radio1:
                if (checked)
                    menu = 1;
                break;
            case R.id.radio2:
                if (checked)
                    menu = 2;
                break;
            case R.id.radio3:
                if (checked)
                    menu = 3;
                break;
        }

        IncludeData();
    }
}
