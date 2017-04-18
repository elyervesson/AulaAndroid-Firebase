package br.com.thiengo.thiengocalopsitafbexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.thiengo.thiengocalopsitafbexample.domain.User;

public class LoginActivity extends CommonActivity {

    //private DatabaseReference firebase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private User userOBJ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        //firebase = LibraryClass.getFirebase();
        initViews();
        verifyUserLogged();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        // ...
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        // ...
    }

    protected void initViews(){
        email = (AutoCompleteTextView) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
    }

    protected void initUser(){
        userOBJ = new User();
        userOBJ.setEmail( email.getText().toString() );
        userOBJ.setPassword( password.getText().toString() );
        userOBJ.generateCryptPassword();
    }

    public void callSignUp(View view){
        Intent intent = new Intent( this, SignUpActivity.class );
        startActivity(intent);
    }

    public void sendLoginData( View view ){
        openProgressBar();
        initUser();
        verifyLogin();
    }


    private void verifyUserLogged(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged: signed_in:" + user.getUid());

                    callMainActivity();
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged: signed_out");

                    initUser();
                    if( !userOBJ.getTokenSP(LoginActivity.this).isEmpty() ){
                        mAuth.signInWithEmailAndPassword(userOBJ.getEmail(), userOBJ.getPassword())
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Log.d("TAG", "signInWithEmail:onComplete: " + task.isSuccessful());
                                        callMainActivity();

                                        // If sign in fails, display a message to the user. If sign in succeeds
                                        // the auth state listener will be notified and logic to handle the
                                        // signed in user can be handled in the listener.
                                        // if (!task.isSuccessful()) {
                                        //     Log.w("TAG", "signInWithEmail:failed", task.getException());
                                        //     Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed,
                                        //             Toast.LENGTH_SHORT).show();
                                        // }

                                        // ...

                                    }
                                });

                    }
                }
                // ...
            }
        };
        //if( firebase.getAuth() != null ){
        //    callMainActivity();
        //}
        //else{
        //    initUser();
        //
        //    if( !user.getTokenSP(this).isEmpty() ){
        //        firebase.authWithPassword(
        //            "password",
        //            user.getTokenSP(this),
        //            new Firebase.AuthResultHandler() {
        //                @Override
        //                public void onAuthenticated(AuthData authData) {
        //                    user.saveTokenSP( LoginActivity.this, authData.getToken() );
        //                    callMainActivity();
        //                }
        //
        //                @Override
        //                public void onAuthenticationError(FirebaseError firebaseError) {}
        //            }
        //        );
        //    }
        //}
    }

    private void callMainActivity(){
        Intent intent = new Intent( this, MainActivity.class );
        startActivity(intent);
        finish();
    }


    private void verifyLogin(){
        mAuth.signInWithEmailAndPassword(userOBJ.getEmail(), userOBJ.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG", "signInWithEmail:onComplete: " + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "signInWithEmail: failed !!!");

                            showSnackbar( task.getException().getMessage() );
                            closeProgressBar();
                        }

                        if (task.isSuccessful()) {
                            Log.w("TAG", "signInWithEmail: got success !!!");

                            // TODO: 18/04/17 alterar o token default por um valido
                            userOBJ.saveTokenSP( LoginActivity.this, "authData.getToken()" );
                            closeProgressBar();
                            callMainActivity();
                        }

                        // ...
                    }
                });

        //firebase.authWithPassword(
        //    user.getEmail(),
        //    user.getPassword(),
        //    new Firebase.AuthResultHandler() {
        //        @Override
        //        public void onAuthenticated(AuthData authData) {
        //            // LoginActivity.this : necessita pegar o contexto pois esta dentro de uma classe anonima
        //            user.saveTokenSP( LoginActivity.this, authData.getToken() );
        //            closeProgressBar();
        //            callMainActivity();
        //        }
        //
        //        @Override
        //        public void onAuthenticationError(FirebaseError firebaseError) {
        //            showSnackbar( firebaseError.getMessage() );
        //            closeProgressBar();
        //        }
        //    }
        //);
    }
}
