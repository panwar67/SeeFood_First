package com.example.nikhil.fitfatsecret;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikhil.fitfatsecret.FatSecretImplementation.FatSecretSearch;
import com.example.nikhil.fitfatsecret.FatSecretImplementation.FatSecretGet;
import com.example.nikhil.fitfatsecret.FatSecretImplementation.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProgressBar mProgressMore, mProgressSearch;
    private FatSecretSearch mFatSecretSearch;
    private FatSecretGet mFatSecretGet;
    TextView fats, carbs, cals, protien;
    String brand;
    private ArrayList<Item> mItem;
    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFatSecretSearch = new FatSecretSearch(); // method.search
        mFatSecretGet = new FatSecretGet();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tag = getIntent().getStringExtra("food_name");
        //button = (Button) findViewById(R.id.button);
        mItem = new ArrayList<>();

        searchFood(tag,1);

        fats = (TextView)findViewById(R.id.fat);
        cals = (TextView)findViewById(R.id.calorie);
        protien = (TextView)findViewById(R.id.protien);
        carbs = (TextView)findViewById(R.id.carbs);

    }


    private void searchFood(final String item, final int page_num) {
        new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                Log.d("Works","Starting to work");
            }

            @Override
            protected String doInBackground(String... arg0) {
                JSONObject food = mFatSecretSearch.searchFood(item, page_num);
                JSONArray FOODS_ARRAY;
                try {
                    if (food != null) {
                        FOODS_ARRAY = food.getJSONArray("food");
                        if (FOODS_ARRAY != null) {
                            for (int i = 0; i < 1; i++) {
                                JSONObject food_items = FOODS_ARRAY.optJSONObject(i);
                                String food_name = food_items.getString("food_name");
                                String food_description = food_items.getString("food_description");
                                String[] row = food_description.split("-");
                                String id = food_items.getString("food_type");
                                if (id.equals("Brand")) {
                                    brand = food_items.getString("brand_name");
                                }
                                if (id.equals("Generic")) {
                                    brand = "Generic";
                                }
                                String food_id = food_items.getString("food_id");
                                mItem.add(new Item(food_name, row[1].substring(1),
                                        "" + brand, food_id));
                            }
                        }
                    }
                } catch (JSONException exception) {
                    return "Error";
                }
                return "";
            }

            @Override
            protected void onPostExecute(String result) {
                xyz();

                super.onPostExecute(result);
                if (result.equals("Error"))
                    Toast.makeText(getApplicationContext(), "No Items Containing Your Search", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    public void xyz(){

        Log.d("okok"," " +mItem);
        getFood(Long.parseLong(mItem.get(0).getID()));
    }



    private void getFood(final long id) {
        new AsyncTask<String, JSONObject, JSONObject>() {
            @Override
            protected JSONObject doInBackground(String... arg0) {

                JSONObject foodGet = mFatSecretGet.getFood(id);
                try {
                    if (foodGet != null) {
                        String food_name = foodGet.getString("food_name");
                        JSONObject servings = foodGet.getJSONObject("servings");

                        JSONObject serving = servings.getJSONObject("serving");
                        String calories = serving.getString("calories");
                        String carbohydrate = serving.getString("carbohydrate");
                        String protein = serving.getString("protein");
                        String fat = serving.getString("fat");
                        String serving_description = serving.getString("serving_description");
                        Log.e("serving_description", serving_description);
                        /**
                         * Displays results in the LogCat
                         */
                        Log.e("food_name", food_name);
                        Log.e("calories", calories);
                        Log.e("carbohydrate", carbohydrate);
                        Log.e("protein", protein);
                        Log.e("fat", fat);
                        Display_Details(foodGet);
                        //cals.setText("Carbohydrates : "+carbohydrate);
                    }

                } catch (JSONException exception) {
                    return foodGet;
                }
                return foodGet;
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                super.onPostExecute(result);
                try {
                    Display_Details(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result.equals("Error"))
                    Toast.makeText(getApplicationContext(), "No Items Containing Your Search", Toast.LENGTH_SHORT).show();

            }
        }.execute();
    }


    public void Display_Details(final JSONObject data) throws JSONException {



        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                String food_name = null;
                try {
                    food_name = data.getString("food_name");
                    JSONObject servings = data.getJSONObject("servings");
                    JSONObject serving = servings.getJSONObject("serving");
                    String calories = serving.getString("calories");
                    String carbohydrate = serving.getString("carbohydrate");
                    String protein = serving.getString("protein");
                    String fat = serving.getString("fat");
                    String serving_description = serving.getString("serving_description");
                    Log.e("serving_description", serving_description);
                    /**
                     * Displays results in the LogCat
                     */
                    Log.e("food_name", food_name);
                    Log.e("calories", calories);
                    Log.e("carbohydrate", carbohydrate);
                    Log.e("protein", protein);
                    Log.e("fat", fat);
                    fats.setText("Fats : "+fat);
                    cals.setText("Calories : "+calories);
                    protien.setText("Protien : "+protein);
                    carbs.setText("Carbohydrates : "+carbohydrate);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });


    }
}