package com.example.citysimulator;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class StatusFragment extends Fragment
{
    // private class fields
    private TextView cityName;
    private TextView time;
    private TextView money;
    private TextView population;
    private TextView income;
    private TextView employment;
    private TextView temperature;

    private Button timeButton;
    private Button detailsButton;
    private Button demolishButton;
    private Button resetButton;

    private boolean demolishPressed = false;
    private boolean detailsPressed = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle) {
        View view = inflater.inflate(R.layout.status_fragment, ui, false);

        cityName = (TextView) view.findViewById(R.id.cityName);
        time = (TextView) view.findViewById(R.id.time);
        money = (TextView) view.findViewById(R.id.money);
        population = (TextView) view.findViewById(R.id.population);
        income = (TextView) view.findViewById(R.id.income);
        employment = (TextView) view.findViewById(R.id.employment);
        temperature = (TextView) view.findViewById(R.id.temperature);
        timeButton = (Button) view.findViewById(R.id.timeButton);
        demolishButton = (Button) view.findViewById(R.id.demolishButton);
        detailsButton = (Button) view.findViewById(R.id.detailsButton);
        resetButton = (Button) view.findViewById(R.id.resetButton);

        // time button onClick
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // increment time by 1 and contains calculations for employment, population,
                GameData.get().increaseTime(getContext());
                GameData.get().addGameData(); // add to gameData database
                updateStats();
            }
        });

        // demolish button onClick
        demolishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // if demolish pressed is true, set demolish pressed to true and sets colour of text to red
                if (!demolishPressed) {
                    demolishPressed = true;
                    demolishButton.setTextColor(Color.RED); // sets text to red if activating demolish mode
                }
                else // set demolish pressed back to false and set ctext colour to dark grey
                {
                    demolishPressed = false;
                    demolishButton.setTextColor(Color.DKGRAY); // sets text back to default dark grey when toggled off
                }
            }
        });

        // detailsButton onClick
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                detailsPressed = true; // sets details pressed to be true
            }
        });

        // resetButton onClick
        resetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GameData.get().deleteDatabase(); // deletes databases
                GameData.get().resetGameData(); // sets instance of gameData to be null

                // starts up main activity again through a new intent
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

                // send toast message saying restarting game
                Toast toast = Toast.makeText(getContext(), "Restarting Game", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        updateStats();
        return view;
    }

    // method to display the temperature
    // code obtained from this youtube video https://www.youtube.com/watch?v=8-7Ip6xum6E&fbclid
    public void getTemperature() {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=perth&appid=bfc56e0da5ed0508995b6b2859cff63d&units=metric";

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String temp;
                    JSONObject main_object = response.getJSONObject("main");
                    temp = String.valueOf(main_object.getDouble("temp"));
                    temperature.setText("Temperature: " + temp + "C");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue rq = Volley.newRequestQueue(getContext());
        rq.add(jor);
    }

    // put temp method in here
    public void updateStats()
    {
        // truncates to 3 decimal places
        DecimalFormat numFormat = new DecimalFormat("#.###");

        cityName.setText(GameData.get().getCityName());
        time.setText("Time: " + GameData.get().getGameTime());
        money.setText("$: " + GameData.get().getMoney());
        population.setText("Population: " + GameData.get().getPopulation());
        income.setText("Income: $" + GameData.get().getIncome());
        employment.setText("Employment: " + numFormat.format(GameData.get().getEmployment()) + "%");
        getTemperature(); // calls the method to show the temperature
    }

    // GETTERS
    public boolean demolishPressed() {
        return demolishPressed;
    }

    public boolean detailsPressed() {
        return detailsPressed;
    }

    // SETTERS
    public void setDetailsPressed(boolean detailsPressed) {
        this.detailsPressed = detailsPressed;
    }
}
