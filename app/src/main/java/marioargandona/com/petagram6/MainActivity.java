package marioargandona.com.petagram6;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import marioargandona.com.petagram6.adapter.MascotaAdapter;
import marioargandona.com.petagram6.adapter.PageAdapter;
import marioargandona.com.petagram6.entidades.Mascota;
import marioargandona.com.petagram6.fragment.PrincipalFragment;
import marioargandona.com.petagram6.fragment.RecyclerViewFragment;
import marioargandona.com.petagram6.restApi.ConstantesRestApi;
import marioargandona.com.petagram6.restApi.EndpointsApi;
import marioargandona.com.petagram6.restApi.adapter.RestApiAdapter;
import marioargandona.com.petagram6.restApi.model.UsuarioResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public ArrayList<Mascota> mascotas;
    public RecyclerView listaMascotas;
    public MascotaAdapter adapter;
    public Integer likesRecibidos = 0;
    public TextView tvLikes;
    public TextView tvNombreMascota;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar     = (Toolbar)findViewById(R.id.toolbar);
        tabLayout   = (TabLayout) findViewById(R.id.tabLayout);
        viewPager   = (ViewPager) findViewById(R.id.viewPager);
        setUpViewPager();

        if(toolbar != null)
        {
            setSupportActionBar(toolbar);
        }

        Bundle extras       = getIntent().getExtras();

        if(extras != null)
        {
            String usuario          = extras.getString("usuario");

            if(!usuario.equals(""))
            {
                Toast.makeText(this, usuario, Toast.LENGTH_SHORT).show();
                ConstantesRestApi.usuario = usuario;
            }
        }



        if(ConstantesRestApi.usuario == null)
        {
            SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
            String verifica = prefe.getString("usuario","");

            if(!verifica.equals(""))
            {
                ConstantesRestApi.usuario = verifica;
            }
        }

          }

    private ArrayList<Fragment> agregarFragments()
    {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new RecyclerViewFragment());
        fragments.add(new PrincipalFragment());

        return fragments;
    }

    private void setUpViewPager()
    {
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager() , agregarFragments()));
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.dog_house);
        tabLayout.getTabAt(1).setIcon(R.drawable.yeardog);
    }


    private void iniciaListaMascotas()
    {
        mascotas = new ArrayList<Mascota>();

    }



    private void iniciaAdapterMascotas()
    {
        adapter = new MascotaAdapter(mascotas,this);
        listaMascotas.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_opciones , menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.mContacto:
                Intent intentContacto = new Intent(getApplicationContext() , ContactoActivity.class);
                startActivity(intentContacto);
                break;

            case R.id.mAcercaDe:
                Intent intentAcercaDe = new Intent(getApplicationContext() , AcercaDe.class);
                startActivity(intentAcercaDe);
                break;
            case R.id.mConfigurarCuenta:
                Intent intentConfigurarCuenta = new Intent(getApplicationContext() , ConfigurarCuenta.class);
                startActivity(intentConfigurarCuenta);
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    public void enviarToken()
    {
        String id_dispositivo = FirebaseInstanceId.getInstance().getToken();

        enviarIdDispositivoRegistro(id_dispositivo , ConstantesRestApi.usuario);
    }


    private void enviarTokenRegistro(String token)
    {
        Log.d("TOKEN" , token);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
        Call<UsuarioResponse> usuarioResponseCall = endpointsApi.registrarTokenID(token);

        usuarioResponseCall.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                UsuarioResponse usuarioResponse = response.body();
                Log.d("ID FIREBASE" , usuarioResponse.getId());
                Log.d("USUARIO_FIREBASE" , usuarioResponse.getToken());
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {

            }
        });
    }



    private void enviarIdDispositivoRegistro(String id_dispositivo , String id_usuario_instagram)
    {
        Log.d("ID_DISPOSITIVO" , id_dispositivo);
        Log.d("ID_USUARIO_INSTAGRAM" , id_usuario_instagram);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.registrarUsuario();
        Call<UsuarioResponse> usuarioResponseCall = endpointsApi.registrarUsuario(id_dispositivo, id_usuario_instagram);


        usuarioResponseCall.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                UsuarioResponse usuarioResponse = response.body();
                Log.d("ID FIREBASE" , usuarioResponse.getId());
                Log.d("ID_DISPOSITIVO" , usuarioResponse.getId_dispositivo());
                Log.d("ID_USUARIO_INSTAGRAM" , usuarioResponse.getId_usuario_instagram());
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {

            }
        });
    }



    private void recibirNotificaciones()
    {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKEN" , token);
    }
}
