package com.jimmywilcox.knittingcounter;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class RowActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int ROW_REQUEST_CODE = 1002;
    private String nameFilter;
    private TextView rowNumber;
    private FloatingActionButton subtractButton;
    private FloatingActionButton addButton;
    private Integer oldRow;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_row);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initializing the views
        TextView projectName = (TextView) findViewById(R.id.projectName);
        rowNumber = (TextView) findViewById(R.id.rowNumber);
        subtractButton = (FloatingActionButton) findViewById(R.id.fabSubract);
        addButton = (FloatingActionButton) findViewById(R.id.fabAdd);
        subtractButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        //row = 0;

        rowNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 60);


        Uri uri = getIntent().getParcelableExtra(CounterProvider.CONTENT_ITEM_TYPE);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle("New Project");
        } else if () {

        } else {
            action = Intent.ACTION_EDIT;
            nameFilter = DBOpenHelper.COUNTER_ID + "=" + uri.getLastPathSegment(); // Query where clause

            Cursor cursor = getContentResolver().query(uri,
                    DBOpenHelper.ALL_COLUMNS, nameFilter, null, null);

            cursor.moveToFirst();
            String dbName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COUNTER_NAME));
            oldRow = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.COUNTER_ROW));
            rowNumber.setText(Integer.toString(oldRow));
            rowNumber.requestFocus();
            projectName.setText(dbName);
            projectName.requestFocus();
        }
    }


    @Override
    public void onClick(View v) {
        if (v == addButton) {
            int row = Integer.parseInt(rowNumber.getText().toString());
            row++;
            rowNumber.setText(Integer.toString(row));
        }
        if (v == subtractButton) {
            int row = Integer.parseInt(rowNumber.getText().toString());
            row--;
            rowNumber.setText(Integer.toString(row));
        }
    }
    private void finishCounting() {
        rowNumber = (TextView) findViewById(R.id.rowNumber);
        String editedRow = rowNumber.getText().toString();
        int row = Integer.parseInt(editedRow);
        switch (action) {
            case Intent.ACTION_INSERT:
                if (row == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    insertRow(row);
                }
                break;
            case Intent.ACTION_EDIT:
                if (oldRow.equals(rowNumber)) {
                    setResult(RESULT_CANCELED);
                } else {
                    updateRow(row);
                }
        }
        finish();
    }

    private void insertRow(Integer row) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COUNTER_ROW, row);
        getContentResolver().insert(CounterProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    private void updateRow(Integer row) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COUNTER_ROW, row);
        getContentResolver().update(CounterProvider.CONTENT_URI, values, nameFilter, null);
        Toast.makeText(this, R.string.rows_updated, Toast.LENGTH_SHORT).show();

        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finishCounting();
        setResult(RESULT_OK);
        Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, ROW_REQUEST_CODE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       if (action.equals(Intent.ACTION_EDIT)) {
           getMenuInflater().inflate(R.menu.menu_row, menu);
       }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home: // This is the back button in the options menu!
                finishCounting();
                break;
            case (R.id.action_delete):
                deleteCounter();
                break;
            }
        return true;
        }

    private void deleteCounter() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {

                            getContentResolver().delete(
                                    CounterProvider.CONTENT_URI, nameFilter, null
                            );

                            Toast.makeText(RowActivity.this,
                                    getString(R.string.counter_deleted),
                                    Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }
}