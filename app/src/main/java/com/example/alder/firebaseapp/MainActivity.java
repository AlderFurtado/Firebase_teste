package com.example.alder.firebaseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    private DatabaseReference firebaseRaiz = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference firebaseUsuarios = firebaseRaiz.child("usuarios");
    private ListView lv_contatos;
    private ArrayAdapter arrayAdapter;
    private ArrayList<String> usuariosArray;
    //private SearchView barra_pesquisa;




    private DatabaseReference usuarioReferencia = firebaseRaiz.child("usuarios");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Instanciando ArrayList usuarios
        usuariosArray = new ArrayList<>();


        //achando ListView no layout
        lv_contatos = (ListView)findViewById(R.id.lv_usuario);
        //achando SeachView no layout
        //barra_pesquisa = (SearchView)findViewById(R.id.barra_pesquisa);
        //barra_pesquisa.setOnQueryTextListener(acharUsuarioLista());

        //Instanciando o ArrayAdapter
        arrayAdapter = new ArrayAdapter(MainActivity.this,
                R.layout.lista_usuarios,usuariosArray);

        lv_contatos.setAdapter(arrayAdapter);

        firebaseUsuarios.addValueEventListener(adicionarLista());




    }




    private SearchView.OnQueryTextListener acharUsuarioLista(){
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        };

        return queryTextListener;
    }





    private ValueEventListener adicionarLista(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //limpando o array de Usuarios para que os usuarios ja existente não sejam carregados novamente
                usuariosArray.clear();

                //adicionando usuarios no arrayAdapter
                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Usuario usuario = dados.getValue(Usuario.class);

                    usuariosArray.add(usuario.getNome());
                }

                Log.i("array String:",""+usuariosArray.get(0)+"\n"+usuariosArray.get(1)+"\n"+usuariosArray.get(2));

                //Fazendo que o array verifique um mudança na estrutura
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        return  valueEventListener;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pesquisa_usuario,menu);
        MenuItem item = menu.findItem(R.id.barra_pesquisa);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                arrayAdapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
