package co.com.cesde.appmascotas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.events.MapEventsReceiver;

public class FoundPet extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("anuncios");
    MapView mapView;
    MyLocationNewOverlay myLocationOverlay;

    LinearLayout form;
    EditText inputTipo, inputRaza, inputTamano, inputDescripcion, inputCollar, inputNumero;
    RadioGroup radioGroup;
    RadioButton radioButton1, radioButton2, radioButton3;
    Button submitButton;
    Marker currentMarker = null;
    TextView volver;
    GeoPoint currentGeoPoint;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(), getPreferences(MODE_PRIVATE));
        setContentView(R.layout.activity_found_pet);

        mapView = findViewById(R.id.mapa);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        /*mapView.setBuiltInZoomControls(true);*/
        mapView.setMultiTouchControls(true);

        form = findViewById(R.id.form);
        inputTipo = findViewById(R.id.input_tipo);
        inputRaza = findViewById(R.id.input_raza);
        inputTamano = findViewById(R.id.input_tamano);
        inputDescripcion = findViewById(R.id.input_descripcion);
        inputCollar = findViewById(R.id.input_collar);
        inputNumero = findViewById(R.id.input_numero);
        radioGroup = findViewById(R.id.radio_group);
        radioButton1 = findViewById(R.id.radio_button1);
        radioButton2 = findViewById(R.id.radio_button2);
        radioButton3 = findViewById(R.id.radio_button3);
        submitButton = findViewById(R.id.btn_submit);
        volver = findViewById(R.id.cerrar_sesion_found);

        GeoPoint startPoint = new GeoPoint(6.2442, -75.5812); // Medellín coordenadas
        mapView.getController().setZoom(13.0);
        mapView.getController().setCenter(startPoint);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            initializeLocationOverlay();
        }

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAlHome();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnnounce();
            }
        });

        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {

            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                addMarker(p);
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        mapView.getOverlays().add(0, new org.osmdroid.views.overlay.MapEventsOverlay(mapEventsReceiver));
    }

    private void initializeLocationOverlay() {
        myLocationOverlay = new MyLocationNewOverlay(mapView);
        myLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(myLocationOverlay);
    }

    private void submitAnnounce() {
        if (currentGeoPoint != null) {
            String numero = inputNumero.getText().toString();
            DatabaseReference nuevoAnuncio = reference.child(numero);

            String tipo = inputTipo.getText().toString();
            nuevoAnuncio.child("Tipo").setValue(tipo);

            String raza = inputRaza.getText().toString();
            nuevoAnuncio.child("Raza").setValue(raza);

            String tamano = inputTamano.getText().toString();
            nuevoAnuncio.child("Tamaño").setValue(tamano);

            String descripcion = inputDescripcion.getText().toString();
            nuevoAnuncio.child("Descripción").setValue(descripcion);

            String collar = inputCollar.getText().toString();
            nuevoAnuncio.child("Collar").setValue(collar);

            int selectedId = radioGroup.getCheckedRadioButtonId();
            String sexo;
            if (selectedId == radioButton1.getId()) {
                sexo = "Hembra";
            } else if (selectedId == radioButton2.getId()) {
                sexo = "Macho";
            } else {
                sexo = "No lo sé";
            }
            nuevoAnuncio.child("Sexo").setValue(sexo);

            nuevoAnuncio.child("Latitud").setValue(currentGeoPoint.getLatitude());
            nuevoAnuncio.child("Longitud").setValue(currentGeoPoint.getLongitude());

            /*String numero = inputNumero.getText().toString();*/
            nuevoAnuncio.child("Número").setValue(numero);

            Toast.makeText(FoundPet.this, "Anuncio publicado exitosamente", Toast.LENGTH_SHORT).show();
            clearForm();
        } else {
            Toast.makeText(FoundPet.this, "Por favor selecciona una ubicación en el mapa", Toast.LENGTH_SHORT).show();
        }
    }

    private void addMarker(GeoPoint p) {
        if (currentMarker != null) {
            mapView.getOverlays().remove(currentMarker);
            mapView.invalidate();
        }
        Marker marker = new Marker(mapView);
        marker.setPosition(p);
        /*marker.setTitle("Animal Reportado! 🐶🐱");
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);*/
        mapView.getOverlays().add(marker);
        mapView.invalidate();

        currentGeoPoint = p;
        currentMarker = marker;
    }

    private void clearForm() {
        inputTipo.setText("");
        inputRaza.setText("");
        inputTamano.setText("");
        inputDescripcion.setText("");
        inputCollar.setText("");
        inputNumero.setText("");
        radioGroup.clearCheck();
        if (currentMarker != null) {
            mapView.getOverlays().remove(currentMarker);
            mapView.invalidate();
            currentMarker = null;
            currentGeoPoint = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeLocationOverlay();
            } else {
                Toast.makeText(this, "Permisos de Localización necesarios", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.setDestroyMode(true);
    }
    public void irAlHome(){
        Intent intent = new Intent(this, UserHome.class);
        startActivity(intent);
    }
}

