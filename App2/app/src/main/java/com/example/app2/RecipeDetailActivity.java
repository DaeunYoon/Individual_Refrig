package com.example.app2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    public static String TAG = "Recipe";
    private String name;
    private TextView nametv;
    private TextView mintv;

    private LinearLayout ingredientsContainer;
    private LinearLayout stepsContainer;

    private ScrollView scrollView;


    Recipe recipe;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        name = intent.getStringExtra("RECIPE_NAME");

        nametv = findViewById(R.id.name);
        mintv = findViewById(R.id.min);

        nametv.setText(name);

        ingredientsContainer = findViewById(R.id.ingredients_container);
        stepsContainer = findViewById(R.id.steps_container);

        scrollView = findViewById(R.id.parent);

        Log.d(TAG, "data: " + name);

        db.collection("Recipes")
                .whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                recipe = document.toObject(Recipe.class);
                                setText(recipe);
                            }
                        }
                        else {
                            Log.d(TAG, "No such document");
                        }
                    }
                });

    }


    private void setText(Recipe recipe){
        int i = 1;
        scrollView.setVisibility(View.VISIBLE);
        mintv.setText(recipe.getMin() + " min");

        ingredientsContainer.removeAllViews();
        for(String ingredient: recipe.getIngredients()){
            TextView textView = new TextView(this);
            textView.setText(ingredient);
            textView.setTextSize(15);
            textView.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            ingredientsContainer.addView(textView);
        }

        i = 1;
        stepsContainer.removeAllViews();
        for(String step: recipe.getSteps()){
            TextView textView = new TextView(this);
            textView.setText(i++ + " " +step);
            textView.setTextSize(15);
            textView.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            stepsContainer.addView(textView);
        }

    }
}
