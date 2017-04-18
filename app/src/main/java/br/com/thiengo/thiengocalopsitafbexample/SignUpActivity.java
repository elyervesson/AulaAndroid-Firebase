package br.com.thiengo.thiengocalopsitafbexample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;

import java.util.Map;

import br.com.thiengo.thiengocalopsitafbexample.domain.User;
import br.com.thiengo.thiengocalopsitafbexample.domain.util.LibraryClass;

public class SignUpActivity extends CommonActivity {

    private DatabaseReference firebase;
    private FirebaseAuth mAuth;
    private User userOBJ;
    private AutoCompleteTextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebase = LibraryClass.getFirebase();
        mAuth = FirebaseAuth.getInstance();
        initViews();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }

    protected void initViews(){
        name = (AutoCompleteTextView) findViewById(R.id.name);
        email = (AutoCompleteTextView) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.sign_up_progress);
    }

    protected void initUser(){
        userOBJ = new User();
        userOBJ.setName( name.getText().toString() );
        userOBJ.setEmail( email.getText().toString() );
        userOBJ.setPassword( password.getText().toString() );
        userOBJ.generateCryptPassword();
    }

    public void sendSignUpData( View view ){
        openProgressBar();
        initUser();
        saveUser();
    }

    private void saveUser(){
        mAuth.createUserWithEmailAndPassword(userOBJ.getEmail(), userOBJ.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG", "createUserWithEmail:onComplete: " + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "createUser: failed !!!");

                            showSnackbar( task.getException().getMessage() );
                            closeProgressBar();
                        }

                        if (task.isSuccessful()) {
                            userOBJ.setId( "stringObjectMap.get('uid').toString()" ); // Id unico retornado ao criar um novo usuario [16:00]
                            userOBJ.saveDB();
                            //firebase.unauth(); // Desconectar o usuario

                            showToast( "Conta criada com sucesso!" );
                            closeProgressBar();
                            finish(); // Voltar para activity de longin que esta na pilha de actiities
                        }
                        // ...
                    }
                });

        //firebase.createUser(
        //    user.getEmail(),
        //    user.getPassword(),
        //    new Firebase.ValueResultHandler<Map<String, Object>>() {
        //        @Override
        //        public void onSuccess(Map<String, Object> stringObjectMap) {
        //            user.setId( stringObjectMap.get("uid").toString() ); // Id unico retornado ao criar um novo usuario [16:00]
        //            user.saveDB();
        //            firebase.unauth(); // Desconectar o usuario
        //
        //            showToast( "Conta criada com sucesso!" );
        //            closeProgressBar();
        //            finish(); // Voltar para activity de longin que esta na pilha de actiities
        //        }
        //
        //        @Override
        //        public void onError(DatabaseError firebaseError) {
        //            showSnackbar( firebaseError.getMessage() );
        //            closeProgressBar();
        //        }
        //    }
        //);
    }
}
