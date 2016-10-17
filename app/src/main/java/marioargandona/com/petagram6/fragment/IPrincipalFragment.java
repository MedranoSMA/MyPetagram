package marioargandona.com.petagram6.fragment;

import java.util.ArrayList;

import marioargandona.com.petagram6.adapter.MascotaAdapter;
import marioargandona.com.petagram6.entidades.Mascota;

public interface IPrincipalFragment {

    public void generarGridLayout();
    public MascotaAdapter crearAdaptadorListas(ArrayList<Mascota> mascotas);
    public void crearAdaptador(ArrayList<Mascota> mascotas);
    public void inicializarAdaptadorRV(MascotaAdapter adaptador);
    public void actualizaPerfil();

}
