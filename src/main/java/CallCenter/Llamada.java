package CallCenter;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class Llamada {

    private Integer duracion;

    /**
     * Constructor 
     * @param 
     */
    public Llamada(Integer durationInSeconds) {
        Validate.notNull(durationInSeconds);
        Validate.isTrue(durationInSeconds >= 0);
        this.duracion = durationInSeconds;
    }

    /**
     * Constructor
     * @return
     */
    public Integer getDuracion() {
        return duracion;
    }

    /**
     * Metodo que genera una llamada
     *
     * @param minina minima catntidad de segundos en los cuales se ejecuta la llamada
     * @param maxima maxima duracion de segundos en las cuales se ejecuta la llamada
     * @return una nueva llamada
     */
    public static Llamada generarLlamada(Integer minina, Integer maxima) {
        Validate.isTrue(maxima >= minina && minina >= 0);
        return new Llamada(ThreadLocalRandom.current().nextInt(minina, maxima + 1));
    }

    /**
     * Agrega una nueva llamada
     *
     * @param cantidad de llamadas generadas
     * @param minima  duracion 
     * @param maxima  duracion 
     * @return A new list of random calls each with a random duration value between minimum and maximum duration
     */
    public static List<Llamada> listadoLlamadas(Integer cantidad, Integer minima, Integer maxima) {
        Validate.isTrue(cantidad >= 0);
        List<Llamada> lista = new ArrayList<Llamada>();
        for (int i = 0; i < cantidad; i++) {
            lista.add(generarLlamada(minima, maxima));
        }
        return lista;
    }

}
