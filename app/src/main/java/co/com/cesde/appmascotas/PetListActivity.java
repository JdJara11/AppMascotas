package co.com.cesde.appmascotas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;


import java.util.ArrayList;
import java.util.List;

public class PetListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PetAdapter adapter;
    private List<Pet> petList;
    private DatabaseReference reference, reference2;
    TextView volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);

        volver = findViewById(R.id.cerrar_sesion_pet);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        petList = new ArrayList<>();
        adapter = new PetAdapter(petList);
        recyclerView.setAdapter(adapter);

        reference = FirebaseDatabase.getInstance().getReference("anuncios");
        /*reference2 = FirebaseDatabase.getInstance().getReference("anuncios2");*/

        loadPets();

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAlHome();
            }
        });
    }

    private void loadPets() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                petList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String numero = snapshot.getKey();
                    String tipo = snapshot.child("Tipo").getValue(String.class);
                    String raza = snapshot.child("Raza").getValue(String.class);
                    String tamano = snapshot.child("Tamaño").getValue(String.class);
                    String descripcion = snapshot.child("Descripción").getValue(String.class);
                    String collar = snapshot.child("Collar").getValue(String.class);
                    double latitud = snapshot.child("Latitud").getValue(Double.class);
                    double longitud = snapshot.child("Longitud").getValue(Double.class);
                    Pet pet = new Pet(tipo, raza, tamano, descripcion, collar, numero, latitud, longitud);
                    petList.add(pet);
                    Log.d("PetListActivity", "Pet added: " + pet.getTipo());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PetListActivity", "Database error: " + databaseError.getMessage());
            }
        });
    }

    public static class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

        private List<Pet> petList;

        public PetAdapter(List<Pet> petList) {
            this.petList = petList;
        }

        @NonNull
        @Override
        public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet, parent, false);
            return new PetViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
            Pet pet = petList.get(position);
            holder.tvTipo.setText(pet.getTipo());
            holder.tvRaza.setText(pet.getRaza());
            holder.tvTamano.setText(pet.getTamano());
            holder.tvDescripcion.setText(pet.getDescripcion());
            holder.tvCollar.setText(pet.getCollar());
            holder.tvNumero.setText(pet.getNumero());


            holder.mapView.setTileSource(TileSourceFactory.MAPNIK);
            /*holder.mapView.setBuiltInZoomControls(true);*/
            holder.mapView.setMultiTouchControls(true);

            GeoPoint startPoint = new GeoPoint(pet.getLatitud(), pet.getLongitud());
            holder.mapView.getController().setZoom(15.0);
            holder.mapView.getController().setCenter(startPoint);

            Marker marker = new Marker(holder.mapView);
            marker.setPosition(startPoint);
            marker.setTitle("Ubicación de la mascota");
            holder.mapView.getOverlays().clear();
            holder.mapView.getOverlays().add(marker);
            holder.mapView.invalidate();
        }


        @Override
        public int getItemCount() {
            return petList.size();
        }

        public static class PetViewHolder extends RecyclerView.ViewHolder {
            TextView tvTipo, tvRaza, tvTamano, tvDescripcion, tvCollar, tvNumero;
            MapView mapView;

            public PetViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTipo = itemView.findViewById(R.id.tvTipo);
                tvRaza = itemView.findViewById(R.id.tvRaza);
                tvTamano = itemView.findViewById(R.id.tvTamano);
                tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
                tvCollar = itemView.findViewById(R.id.tvCollar);
                tvNumero = itemView.findViewById(R.id.tvNumero);
                mapView = itemView.findViewById(R.id.map2);
            }
        }
    }
    public void irAlHome(){
        Intent intent = new Intent(this, UserHome.class);
        startActivity(intent);
    }
}
