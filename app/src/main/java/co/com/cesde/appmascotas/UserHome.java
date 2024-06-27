package co.com.cesde.appmascotas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserHome extends AppCompatActivity {

    TextView cerrarSesion;
    Button animalEncontrado,animalPerdido,casosActivos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        cerrarSesion = findViewById(R.id.cerrar_sesion);
        animalEncontrado = findViewById(R.id.animal_encontrado);
        animalPerdido = findViewById(R.id.animal_perdido);
        casosActivos = findViewById(R.id.casos_activos);

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAlInicio();
            }
        });
        animalEncontrado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAEncontreMascota();
            }
        });
        casosActivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irACasosActivos();
            }
        });
        animalPerdido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irABuscarMascota();
            }
        });
    }

    public void irAlInicio(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void irAEncontreMascota(){
        Intent intent = new Intent(this, FoundPet.class);
        startActivity(intent);
    }
    public void irACasosActivos(){
        Intent intent = new Intent(this, PetListActivity.class);
        startActivity(intent);
    }
    public void irABuscarMascota(){
        Intent intent = new Intent(this, SearchPet.class);
        startActivity(intent);
    }
}