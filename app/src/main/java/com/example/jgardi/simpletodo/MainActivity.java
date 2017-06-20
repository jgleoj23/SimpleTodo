package com.example.jgardi.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getName();

    List<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // obtain a reference to the ListView created with the layout
        lvItems = (ListView) findViewById(R.id.lvItems);

        items = readItems();
        // initialize the adapter using the items list
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        // wire the adapter to the view
        lvItems.setAdapter(itemsAdapter);


        setupListViewListener();
    }


    public void onEtNewItemClicked(View v) {
        // obtain a reference to the EditText created with the layout
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);

        String itemText = etNewItem.getText().toString();
        // add the item to the list via the adapter
        itemsAdapter.add(itemText);

        etNewItem.setText("");

        writeToDateFile(items);

        // display a notification to the user
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();

        Log.i(TAG, "Added item " + itemText);
    }


    //region private methods

    private void setupListViewListener() {

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "Removing item " + items.get(position));

                items.remove(position);

                itemsAdapter.notifyDataSetChanged();

                writeToDateFile(items);

                // return true to tell the framework that the long click was consumed
                return true;
            }
        });
    }


    private List<String> readItems() {
        try {
            File dataFile = getDataFile();
            if (dataFile.exists()) {
                return new ArrayList<>(FileUtils.readLines(dataFile, Charset.defaultCharset()));
            } else {
                return new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();

            return new ArrayList<>();
        }
    }


    private void writeToDateFile(List<String> items) {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    //endregion
}
