package com.example.project2021;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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


import java.util.ArrayList;

public class CovidSavedEntries extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    CovidMyOpener covidMyOpener;

    ArrayList<CovidEntry> dbData = new ArrayList<>();

    private MyListAdapter myAdapter;

    TextView savedListTitle;
    ListView databaseList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_saved_entries);

        savedListTitle = findViewById(R.id.savedListTitle);
        databaseList = findViewById(R.id.savedList);

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




        databaseList.setAdapter(myAdapter = new MyListAdapter());

        fillListView();



        //If you click on a message
        databaseList.setOnItemLongClickListener((parent, view, position, id) -> {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to delete this selection?")


                    //what the Yes button does:
                    .setPositiveButton("Yes", (click, arg) -> {

                        //Remove this entry
                        dbData.remove(position); //Removes from the arrayList

                        covidMyOpener.removeRow(position); //Removes from the database

                        myAdapter.notifyDataSetChanged(); //Notifies the list that a change has occurred

                    })

                    //What the No button does:
                    .setNegativeButton("No", (click, arg) -> {
                    })

                    //Show the dialog
                    .create().show();

            return true;

        }); //End of if you click an item in the listView

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
                message = "You clicked covidmusic task";
                break;

            case R.id.item2:
                message = "You clicked covidrecipe task";
                break;

            case R.id.item3:
                message = "You clicked go to covid login page.";
                //This creates a transition to load CovidQuery
                Intent mainPage = new Intent(this, CovidMainActivity.class);
                startActivity(mainPage);
                break;

            case R.id.item4:
                message = "You clicked go to ticket master";
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
                        .setMessage("This is your saved list. If you wish to delete one, " +
                                "click and hold the one you wish to delete.")

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
                message = "You clicked covidmusic task";
                break;

            case R.id.item2:
                message = "You clicked covidrecipe task";
                break;


            case R.id.item3:
                message = "You clicked go to covid login page";
                Intent loginPage = new Intent(this, CovidMainActivity.class);
                startActivity(loginPage);
                break;
            case R.id.item4:
                message = "You clicked go to ticket master";

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
                        .setMessage("This is your saved list. If you wish to delete one, " +
                                "click and hold the one you wish to delete.")

                        //what the Yes button does:
                        .setPositiveButton("Done", (click, arg) -> { })

                        //Show the dialog
                        .create().show();

                break;


        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        if (message != null)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        return false;
    }






    private void fillListView() {

        Cursor data = covidMyOpener.getData();

        while(data.moveToNext()) {

            String province = data.getString(1);
            String date = data.getString(2);
            String cases = data.getString(3);

            dbData.add( new CovidEntry(province, date, cases) );

        } //End of while loop

        myAdapter.notifyDataSetChanged();

    } //End of fillListView method


    //MyListAdapter class
    private class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return dbData.size();
        } //Size of arrayList

        public ArrayList<CovidEntry> getItem(int position) { return dbData; } //Gets the item

        //Gets the ID for the item
        public long getItemId(int position) {

            return position;

        }

        //The view
        public View getView(int position, View old, ViewGroup parent) {

            View newView;

            LayoutInflater inflater = getLayoutInflater();

            newView = inflater.inflate(R.layout.covid_province, parent, false);

            TextView pText = newView.findViewById(R.id.provinceTextView);
            pText.setText( dbData.get(position).getProv() );

            TextView pDate = newView.findViewById(R.id.provinceDate);
            pDate.setText( dbData.get(position).getDate() );

            TextView pCases = newView.findViewById(R.id.provinceCases);
            String cases = dbData.get(position).getCases();

            pCases.setText( cases );

            return newView;
        }

    } //End of Base adapter class


} //End of class SavedEntries

