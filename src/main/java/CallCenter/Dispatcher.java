package CallCenter;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import CallCenter.atender;
import CallCenter.Empleado;


import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dispatcher implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    public static final Integer MAX_THREADS = 10;
    private Boolean activo;
    private ExecutorService executorService;
    private ConcurrentLinkedDeque<Empleado> empleados;
    private ConcurrentLinkedDeque<Llamada> llamadasEntrantes;
    private atender atender;

    /**
     * Constructor
     * @param empleados
     */
    public Dispatcher(List<Empleado> empleados) {
        this(empleados, new atenderLlamada());
    }
    
    /**
     * Constructor
     * @param empleados
     * @param atender
     */
    public Dispatcher(List<Empleado> empleados, atender atender) {
        Validate.notNull(empleados);
        Validate.notNull(atender);
        this.empleados = new ConcurrentLinkedDeque(empleados);
        this.atender = atender;
        this.llamadasEntrantes = new ConcurrentLinkedDeque<Llamada>();
        this.executorService = Executors.newFixedThreadPool(MAX_THREADS);
    }

    public synchronized void dispatch(Llamada llamada) {
        logger.info("Atendiendo" + llamada.getDuracion() + " segundos");
        this.llamadasEntrantes.add(llamada);
    }

    /**
     * Metodo que inicializa un empleado en un proceso
     */
    public synchronized void start() { 
        this.activo = true;
        for (Empleado empleado : this.empleados) {
            this.executorService.execute(empleado);
        }
    }

    /**
     * Detiene de manera inmediata los procesos del empleado
     */
    public synchronized void stop() {	 
        this.activo = false;
        this.executorService.shutdown();
    }

    public synchronized Boolean getActivo() {
        return activo;
    }

    /**
     * Metodo RUN 
     * Sobreescrito de la interface Runnable
     */
    
    public void run() {
        while (getActivo()) {
            if (this.llamadasEntrantes.isEmpty()) {
                continue;
            } else {
                Empleado empleado = this.atender.buscarEmpleado(this.empleados);
                if (empleado == null) {
                    continue;
                }
                Llamada llamada = this.llamadasEntrantes.poll();
                try {
                    empleado.atender(llamada);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    this.llamadasEntrantes.addFirst(llamada);
                }
            }
        }
    }

}

