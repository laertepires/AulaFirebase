package thiagocury.eti.br.aulafirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tela1 extends AppCompatActivity {

    //Widgets
    private EditText etNome;
    private EditText etIdade;
    private Button btnOK;
    private ListView lvPessoas;
    //Objeto
    private Pessoa p;
    //Array + Adapter
    private ArrayAdapter<Pessoa> adapter;
    private ArrayList<Pessoa> pessoas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela1);

        //Referencias
        etNome = (EditText) findViewById(R.id.t1_et_nome);
        etIdade = (EditText) findViewById(R.id.t1_et_idade);
        btnOK = (Button) findViewById(R.id.t1_btn_ok);
        lvPessoas = (ListView) findViewById(R.id.t1_lv_pessoas);

        //Array + Adapters
        pessoas = new ArrayList<>(); //vazio!
        adapter = new ArrayAdapter<Pessoa>(
                        Tela1.this,
                        android.R.layout.simple_list_item_1,
                        pessoas);
        lvPessoas.setAdapter(adapter);

        //Firebase
        FirebaseApp.initializeApp(Tela1.this);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference banco = db.getReference("contatos");

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p = new Pessoa();
                p.setNome(etNome.getText().toString());
                p.setIdade(
                        Integer.parseInt(etIdade.getText().toString()));
                //Enviar(empurrar) para o firebase
                banco.push().setValue(p);
                Toast.makeText(
                        getBaseContext(),
                        "Pessoa cadastrado com sucesso!",
                        Toast.LENGTH_LONG).show();
            }//fecha onclick
        });//fecha listener


        banco.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                pessoas.clear(); //Joinha do Macgyver
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Pessoa p = data.getValue(Pessoa.class);
                    pessoas.add(p);
                }//fecha for
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }//fecha onCreate
}//fecha classe
