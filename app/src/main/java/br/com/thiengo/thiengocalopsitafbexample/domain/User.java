package br.com.thiengo.thiengocalopsitafbexample.domain;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

import br.com.thiengo.thiengocalopsitafbexample.domain.util.CryptWithMD5;
import br.com.thiengo.thiengocalopsitafbexample.domain.util.LibraryClass;

public class User {
    public static String TOKEN = "br.com.thiengo.thiengocalopsitafbexample.domain.User.TOKEN";

    private String id;
    private String name;
    private String email;
    private String password;

    /* PARA PODER SALVAR DIRETAMENTE O OBJ USER NO FIREBASE É NECESSARIO UM CONTRUTOR VAZIO, GETS E SETS */
    public User(){}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void generateCryptPassword() {
        password = CryptWithMD5.cryptWithMD5(password);
    }



    public void saveTokenSP(Context context, String token ){
        LibraryClass.saveSP( context, TOKEN, token );
    }

    public String getTokenSP(Context context ){
        String token = LibraryClass.getSP( context, TOKEN );
        return( token );
    }


    public void saveDB(){
        DatabaseReference firebase = LibraryClass.getFirebase();
        firebase = firebase.child("users").child( getId() ); // Criação de um novo nodo user (caso não exista)

        setPassword(null); // Dados que não precisa ser salvo (dados de autenticação)
        setId(null); // Dados que não precisa ser salvo (dados de autenticação)
        firebase.setValue( this );
    }
}
