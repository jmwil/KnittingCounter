package com.jimmywilcox.knittingcounter;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditorActivity extends AppCompatActivity {


    private static final int ROW_REQUEST_CODE = 1002;
    private String action;
    private EditText editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        editor = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(CounterProvider.CONTENT_ITEM_TYPE);

        if (uri == null){
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.new_project));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home: // always the same - back button
                finishEditing();
                break;
        }

        return true;
    }

    private void finishEditing() {
        String projectName = editor.getText().toString().trim();

        switch (action) {
            case Intent.ACTION_INSERT:
                if (projectName.length() == 0) {
                    setResult (RESULT_CANCELED);
                } else {
                    insertCounter(projectName);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivityForResult(intent, ROW_REQUEST_CODE);
                }
        }
        finish();
    }

    private void insertCounter(String projectName) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COUNTER_NAME, projectName);
        getContentResolver().insert(CounterProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }


    public void cancelProject(View view) {
        onBackPressed();
        finish();
    }

    public void openRowActivityForNewCounter(View view) {
        finishEditing();
        }
}

