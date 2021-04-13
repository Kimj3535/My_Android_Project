package com.example.project2021;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CovidQuery extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
//https://api.covid19api.com/country/CANADA/status/confirmed/live?from=2020-10-14T00:00:00Z&to=2020-10-15T00:00:00Z

    //ProgressBar progressBar;

    private ArrayList<CovidEntry> covidList = new ArrayList<>();
    ArrayList<CovidEntry> savedList = new ArrayList<>();

    private MyListAdapter myAdapter;

    CovidMyOpener covidMyOpener;

    String searchQ, query1, query2, query3, query4;
    ListView myList;

    Button search;

    Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_query);

        //Find the id
        myList = findViewById(R.id.covidListView);
        search = findViewById(R.id.refreshList);

        covidMyOpener = new CovidMyOpener(this);

        //This gets the toolbar from the layout
        Toolbar tBar = findViewById(R.id.toolbar);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        setSupportActionBar(tBar); //This makes Android call onCreateOptionsMenu()

        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar,R.string.open, R.string.close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        //progressBar.setVisibility(View.VISIBLE);

        //Part of the API link (part that the user does not alter)
        query1 = "https://api.covid19api.com/country/";
        query2 = "/status/confirmed/live?from=";
        query3 = "T00:00:00Z&to=";
        query4 = "T00:00:00Z";

        //Gets you from MainActivity.java
        Intent intent = getIntent();

        //Obtained from previous intent (covidpage)
        String countryName = intent.getStringExtra("country");
        String startDate = intent.getStringExtra("dateStart");
        String endDate = intent.getStringExtra("dateEnd");


        //Putting together the query for the API needed to search
        searchQ = query1 + countryName + query2 + startDate + query3 + endDate + query4;
        if (countryName == null || countryName.equals("")) {
            searchQ = "https://api.covid19api.com/country/CANADA/status/confirmed/live?from=2020-10-14T00:00:00Z&to=2020-10-15T00:00:00Z";
        }

        myList.setAdapter(myAdapter = new MyListAdapter());



        //If you click on a message
        myList.setOnItemLongClickListener((parent, view, position, id) -> {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to save this selection?")

                    //what the Yes button does:
                    .setPositiveButton("Yes", (click, arg) -> {

                        //Add this element to saved list
                        savedList.add( covidList.get(position) );

                        //Adds this entry to the database
                        addData(position);

                    })

                    //What the No button does:
                    .setNegativeButton("No", (click, arg) -> {
                    })

                    //Show the dialog
                    .create().show();

            return true;

        }); //End of if you click an item in the listView


        //Search button
        search.setOnClickListener(click-> {

            //Two lines of code to get AsyncTask going to retrieve data from a site
            CQuery req = new CQuery(); //Creates a background thread
            req.execute(searchQ); //Type 1
        }); //End of Search button

    } //End of onCreate method



    @Override //Gets called when I used setSupportActionBar() to inflate the menu
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.covid_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.item1:
                message = "You clicked music task";
                break;

            case R.id.item2:
                message = "You clicked recipe task";
                break;

            case R.id.item3:
                message = "You clicked go to covid login page";
                //This creates a transition to load CovidQuery
                Intent mainPage = new Intent(this, MainActivity.class);
                startActivity(mainPage);
                break;
            case R.id.item4:
                View view = findViewById(R.id.item4);
                snackbar = Snackbar.make(view,"You click go to ticket master",Snackbar.LENGTH_LONG);
                snackbar.show();
                break;

            case R.id.item5:
                message = "You clicked go to saved list";
                Intent savedPage = new Intent(this, CovidSavedEntries.class);
                startActivity(savedPage);
                break;

            case R.id.itemHelp:
                message = "You clicked on help";

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Instructions")

                        //Message has the instructions for this covidpage
                        .setMessage("Press the SEARCH button and the data you requested " +
                                "will be displayed here in a list. \n\nIf you wish " +
                                "to save a selection, click and hold a selection to save it to your " +
                                "SAVED LIST.")

                        //what the Yes button does:
                        .setPositiveButton("Done", (click, arg) -> { })

                        //Show the dialog
                        .create().show();

                break;
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        return true;
    }



    // Needed for the OnNavigationItemSelected interface:
    //When someone clicks on an item on the navigation drawer
    public boolean onNavigationItemSelected( MenuItem item) {

        String message = null;

        switch(item.getItemId())
        {
            case R.id.item1:
                message = "You clicked music task";
                break;
            case R.id.item2:
                message = "You clicked recipe task";
                break;
            case R.id.item3:

                message = "You clicked go to covid login page.";
                Intent loginPage = new Intent(this, CovidMainActivity.class);
                startActivity(loginPage);
                break;
            case R.id.item4:
                View view = findViewById(R.id.item4);
                snackbar = Snackbar.make(view,"You click go to ticket master",Snackbar.LENGTH_LONG);
                snackbar.show();
                break;
            case R.id.item5:
                message = "You clicked go to saved list";
                Intent savedPage = new Intent(this, CovidSavedEntries.class);
                startActivity(savedPage);
                break;

            case R.id.itemHelp:
                message = "You clicked on help";

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Instructions")

                        //Message has the instructions for this covidpage
                        .setMessage("Press the SEARCH button and the data you requested " +
                                "will be displayed here in a list. \n\nIf you wish " +
                                "to save a selection, click and hold a selecion to save it to your " +
                                "SAVED LIST.")

                        //what the Yes button does:
                        .setPositiveButton("Done", (click, arg) -> { })

                        //Show the dialog
                        .create().show();

                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        if (message != null)
            Toast.makeText(this,  message, Toast.LENGTH_SHORT).show();
        return false;
    }




    public void addData(int position) {

        boolean isSaved;

        isSaved = covidMyOpener.insertEntry( covidList.get(position).getProv(),
                covidList.get(position).getDate(), covidList.get(position).getCases() );

        if (isSaved) {
            Toast.makeText(CovidQuery.this, getResources().getString(R.string.db_success) , Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(CovidQuery.this, getResources().getString(R.string.db_failure) , Toast.LENGTH_SHORT).show();
        }

    }

    //Type 1, Type 2, Type 3
    public class CQuery extends AsyncTask< String, Integer, ArrayList<CovidEntry> > {

        protected ArrayList<CovidEntry> doInBackground(String... args) {

            try {

                //Create URL object of what server to contact
                URL url = new URL(args[0]);
                //Open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //JSON --------------------
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
                StringBuilder sb = new StringBuilder();

                String line;
                while((line = reader.readLine()) != null) {

                    sb.append(line + "\n");

                }
                String result = sb.toString(); //result is the whole string

                //Convert the string to a JSON Object
                //JSONObject jObject = new JSONObject(result);
                //array                             //the whole string
                JSONArray covidData = new JSONArray(result);

                //Loops through the JSON Array to obtain all the elements (in this case its JSONObjects)
                for(int i = 0; i < covidData.length(); i++) {

                    //This will get the JSON Object at that position in the array
                    JSONObject currentObj = covidData.getJSONObject(i);

                    String covidProvince = currentObj.getString("Province");
                    String covidDate = currentObj.getString("Date");
                    int covidCases = currentObj.getInt("Cases");

                    String covidC = Integer.toString(covidCases);

                    covidList.add( new CovidEntry(covidProvince, covidDate, covidC) );
                }

            }catch (Exception e) { }

            return covidList;
        }

        //(Where you update your GUI)
        //Type 2
        public void onProgressUpdate(Integer ... args) {
            // progressBar.setVisibility(View.VISIBLE);
            //SystemClock.sleep(300);
            // progressBar.setProgress(args[0]);
        }

        //Type 3
        public void onPostExecute(ArrayList<CovidEntry> covidList) {
            //progressBar.setVisibility(View.INVISIBLE);
            //super.onPostExecute(covidList);

            myAdapter.notifyDataSetChanged();

        }
    } //End of CQuery class

    //MyListAdapter class
    private class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return covidList.size();
        } //Size of arrayList

        public ArrayList<CovidEntry> getItem(int position) { return covidList; } //Gets the item

        public long getItemId(int position) {
            return position;
        } //Gets the ID for the item

        //The view
        public View getView(int position, View old, ViewGroup parent) {

            View newView;

            LayoutInflater inflater = getLayoutInflater();

            newView = inflater.inflate(R.layout.covid_province, parent, false);

            TextView pText = newView.findViewById(R.id.provinceTextView);
            pText.setText( covidList.get(position).getProv() );

            TextView pDate = newView.findViewById(R.id.provinceDate);
            pDate.setText( covidList.get(position).getDate() );

            TextView pCases = newView.findViewById(R.id.provinceCases);
            String cases = covidList.get(position).getCases();

            pCases.setText( cases );

            return newView;
        }

    } //End of Base adapter class

} //End of CovidQuery class

