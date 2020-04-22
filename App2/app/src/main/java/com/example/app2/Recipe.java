package com.example.app2;
import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String name;
    private int min;
    private List<String> ingredients;
    private List<String> steps;

    public Recipe(){
        name = "";
        min = 0;
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();
    }

    public Recipe(String name, int min, List<String> ingredients, List<String> steps){
        this.name = name;
        this.min = min;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public String getName()
    {
        return name;
    }

    public int getMin() {
        return min;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int compare(ArrayList<Ingredient> a) {
        int gap = 0;

        for(int i = 0; i < ingredients.size(); i++) {
            int check = 0;

            for(int j = 0; j < a.size() ; j++) {
                if(ingredients.get(i).equals(a.get(j).getName())) {
                    check = 1;
                }
            }
            if(check == 0) {
                gap++;
            }
        }
        System.out.println(gap);
        return gap;
    }
}
