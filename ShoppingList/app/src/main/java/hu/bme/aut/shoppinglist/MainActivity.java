package hu.bme.aut.shoppinglist;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import hu.bme.aut.shoppinglist.adapter.ShoppingAdapter;
import hu.bme.aut.shoppinglist.data.ShoppingItem;
import hu.bme.aut.shoppinglist.data.ShoppingListDatabase;
import hu.bme.aut.shoppinglist.fragments.EditShoppingItemDialogFragment;
import hu.bme.aut.shoppinglist.fragments.NewShoppingItemDialogFragment;

public class MainActivity extends AppCompatActivity
        implements NewShoppingItemDialogFragment.NewShoppingItemDialogListener,
        ShoppingAdapter.ShoppingItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private ShoppingAdapter adapter;

    private ShoppingListDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NewShoppingItemDialogFragment().show(getSupportFragmentManager(), NewShoppingItemDialogFragment.TAG);
            }
        });

        database = Room.databaseBuilder(
                getApplicationContext(),
                ShoppingListDatabase.class,
                "shopping-list"
        ).build();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initRecyclerView();

    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.MainRecyclerView);
        adapter = new ShoppingAdapter(this);
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<ShoppingItem>>() {

            @Override
            protected List<ShoppingItem> doInBackground(Void... voids) {
                return database.shoppingItemDao().getAll();
            }

            @Override
            protected void onPostExecute(List<ShoppingItem> shoppingItems) {
                adapter.update(shoppingItems);
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemChanged(final ShoppingItem item) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                database.shoppingItemDao().update(item);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                Log.d("MainActivity", "ShoppingItem update was successful");
            }
        }.execute();
    }


    @Override
    public void onShoppingItemCreated(final ShoppingItem newItem) {
        new AsyncTask<Void, Void, ShoppingItem>() {

            @Override
            protected ShoppingItem doInBackground(Void... voids) {
                database.shoppingItemDao().insert(newItem);
                return newItem;
            }

            @Override
            protected void onPostExecute(ShoppingItem shoppingItem) {
                adapter.addItem(shoppingItem);
            }
        }.execute();
    }


    public void onItemDeleted(final ShoppingItem item) {
        new AsyncTask<Void, Void, ShoppingItem>() {

            @Override
            protected ShoppingItem doInBackground(Void... voids) {
                database.shoppingItemDao().deleteItem(item);
                return item;
            }

            @Override
            protected void onPostExecute(ShoppingItem shoppingItem) {
                adapter.deleteItem(shoppingItem);
            }
        }.execute();
    }


    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item_diag) {
            Intent statIntent = new Intent(MainActivity.this, StatActivity.class);
            startActivity(statIntent);
        } else if (id == R.id.menu_item_items) {
            Intent itemsIntent = new Intent(MainActivity.this, ItemsActivity.class);
            startActivity(itemsIntent);
        } else if (id == R.id.menu_item_loan) {
            Intent loanIntent = new Intent(MainActivity.this, LoanActivity.class);
            startActivity(loanIntent);
        } else if (id == R.id.menu_item_sum) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /*public void openEditDialogFragment(ShoppingItem item,int pos){
        EditShoppingItemDialogFragment e=new EditShoppingItemDialogFragment();
        FragmentManager fm  = getSupportFragmentManager();
        e.show(fm, EditShoppingItemDialogFragment.TAG);
        e.modPos=pos;
        e.modItem=item;
    }

    @Override
    public void onShoppingItemEdited(ShoppingItem editItem) {

        adapter.updateItem(editItem);
    }*/
}


