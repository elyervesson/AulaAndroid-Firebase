package br.com.thiengo.thiengocalopsitafbexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.google.firebase.database.DatabaseReference;

import br.com.thiengo.thiengocalopsitafbexample.domain.util.LibraryClass;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void logout(View view){
        DatabaseReference firebase = LibraryClass.getFirebase();
        //firebase.unauth();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
