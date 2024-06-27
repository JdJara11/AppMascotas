package co.com.cesde.appmascotas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference().child("usuarios");
    Button btnRegistro;
    TextView volver;
    EditText nombreUsuario, numeroUsuario, ubicacionUsuario, emailUsuario, passwordUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegistro = findViewById(R.id.btn_Registro);
        nombreUsuario = findViewById(R.id.nombre_registro);
        numeroUsuario = findViewById(R.id.telefono_registro);
        ubicacionUsuario = findViewById(R.id.ubicacion_registro);
        emailUsuario = findViewById(R.id.correo_registro);
        passwordUsuario = findViewById(R.id.password_registro);
        volver = findViewById(R.id.cerrar_sesion_registro);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAlInicio();
            }
        });
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }
    public void registrarUsuario(){
        /*Revisar que no funciona el email no lo toma*/
        String nameUsuario = nombreUsuario.getText().toString();
        DatabaseReference nuevoUsuario = reference.child(nameUsuario);

        String correoUsuario = emailUsuario.getText().toString();
        nuevoUsuario.child("Correo Usuario").setValue(correoUsuario);

        String numberUsuario = numeroUsuario.getText().toString();
        nuevoUsuario.child("Número Usuario").setValue(numberUsuario);

        String locationUsuario = ubicacionUsuario.getText().toString();
        nuevoUsuario.child("Ubicación Usuario").setValue(locationUsuario);

        String passUsuario = passwordUsuario.getText().toString();
        nuevoUsuario.child("Contraseña Usuario").setValue(passUsuario);

        Toast.makeText(getApplicationContext(),"Usuario registrado",Toast.LENGTH_LONG).show();

        limpiar();
    }
    public void irAlInicio(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void limpiar(){
        nombreUsuario.setText("");
        numeroUsuario.setText("");
        ubicacionUsuario.setText("");
        emailUsuario.setText("");
        passwordUsuario.setText("");
    }
}