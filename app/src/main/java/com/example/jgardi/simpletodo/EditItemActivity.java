package com.example.jgardi.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    /**
     * a numeric code to identify the edit activity
     */
    public static final int EDIT_REQUEST_CODE = 20;


    // keys used for passing data between activities
    public static final String ITEM_TEXT = "itemText";
    public static final String ITEM_POSITION = "itemPosition";


    /**
     * text field containing updated item description
     */
    EditText etItemText;
    /**
     * the position of the item in the list of items
     */
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);


        etItemText = (EditText) findViewById(R.id.etItemText);
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));

        position = getIntent().getIntExtra(ITEM_POSITION, 0);

        getSupportActionBar().setTitle("Edit Item");
    }


    public void onSaveItem(View v) {
        Intent data = new Intent();

        data.putExtra(ITEM_TEXT, etItemText.getText().toString());
        data.putExtra(ITEM_POSITION, position); // ints work too
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish();
    }
}
