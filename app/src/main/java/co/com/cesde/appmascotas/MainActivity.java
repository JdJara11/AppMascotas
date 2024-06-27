package co.com.cesde.appmascotas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference().child("usuarios");
    Button btnIngresar;

    EditText correoInicio;
    EditText passwordInicio;
    TextView registroInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnIngresar = findViewById(R.id.btn_ingresar);
        correoInicio = findViewById(R.id.correo_inicio);
        passwordInicio = findViewById(R.id.password_inicio);
        registroInicio = findViewById(R.id.registro_inicio);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciar();
            }
        });

        registroInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registro();
            }
        });
    }
    public void iniciar(){
        String inputMail = correoInicio.getText().toString().trim();
        String inputPass = passwordInicio.getText().toString().trim();

        if (inputMail.isEmpty() || inputPass.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_LONG).show();
            return;
        }

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String dbMail = snapshot.child("Correo Usuario").getValue(String.class);
                    String dbPass = snapshot.child("Contraseña Usuario").getValue(String.class);

                    if (dbMail.equals(inputMail) && dbPass.equals(inputPass)) {
                        found = true;
                        Intent intent = new Intent(MainActivity.this, UserHome.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
                if (!found) {
                    Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error de base de datos", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void registro(){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}