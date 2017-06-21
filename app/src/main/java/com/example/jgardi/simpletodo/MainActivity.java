package com.example.jgardi.simpletodo;

import android.content.Intent;
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

import static com.example.jgardi.simpletodo.EditItemActivity.EDIT_REQUEST_CODE;
import static com.example.jgardi.simpletodo.EditItemActivity.ITEM_POSITION;
import static com.example.jgardi.simpletodo.EditItemActivity.ITEM_TEXT;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getName();

    List<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lvItems = (ListView) findViewById(R.id.lvItems);
        items = readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);


        setupListViewListener();
    }


    public void onEtNewItemClicked(View v) {
        // obtain a reference to the EditText created with the layout
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeToDateFile(items);

        // display a notification to the user
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();

        Log.i(TAG, "Added item " + itemText);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // EDIT_REQUEST_CODE defined with constants
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            int position = data.getExtras().getInt(ITEM_POSITION, 0);
            items.set(position, updatedItem);
            itemsAdapter.notifyDataSetChanged();
            writeToDateFile(items);

            // notify the user the operation completed OK
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
        }
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


        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // first parameter is the context, second is the class of the activity to launch
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                // put "extras" into the bundle for access in the edit activity
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);

                // brings up the edit activity with the expectation of a result
                startActivityForResult(i, EDIT_REQUEST_CODE);
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
