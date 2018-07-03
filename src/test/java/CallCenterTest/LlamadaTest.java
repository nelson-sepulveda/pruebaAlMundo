package CallCenterTest;

import org.junit.Test;

import CallCenter.*;

public class LlamadaTest {

    
    @Test(expected = NullPointerException.class)
    public void creacionParametrosNulos() {
        new Llamada(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parametrosInvalidos() {
        Llamada.generarLlamada(-1, 1);
    }



}
