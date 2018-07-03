package CallCenter;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;


/**
 * Models the Employee Domain Objects
 */
public class Empleado implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Empleado.class);	
    private static tipoEmpleado tipoEmpleado;
    private estadoEmpleado estadoEmpleado;
    private ConcurrentLinkedDeque<Llamada> llamdasEntrantes;
    private ConcurrentLinkedDeque<Llamada> llamadasAtendidas;

    public Empleado(tipoEmpleado tipoEmpleado) {	
        Validate.notNull(tipoEmpleado);
        this.tipoEmpleado = tipoEmpleado;
        this.estadoEmpleado = estadoEmpleado.DISPONIBLE;
        this.llamdasEntrantes = new ConcurrentLinkedDeque<Llamada>();
        this.llamadasAtendidas = new ConcurrentLinkedDeque<Llamada>();
    }

    public tipoEmpleado getTipoEmpleado() {
        return tipoEmpleado;
    }

    public synchronized estadoEmpleado getEstadoEmpleado() {
        return estadoEmpleado;
    }

    private synchronized void modificarEmpleado(estadoEmpleado estadoEmpleado) {
        logger.info("Empleado " + Thread.currentThread().getName() + " esta: " + estadoEmpleado);
        this.estadoEmpleado = estadoEmpleado;
    }

    public synchronized List<Llamada> getLlamadasAtendidas() {
        return new ArrayList<Llamada>(llamadasAtendidas);
    }

    /**
     *
     * @param llamada a ser atendidad
     */
    public synchronized void atender(Llamada llamada) {
        logger.info("Empleado " + Thread.currentThread().getName() + " tomo una llamada " + llamada.getDuracion() + " segundos");
        this.llamdasEntrantes.add(llamada);
    }

    public static Empleado generarOperador() {
        return new Empleado(tipoEmpleado.OPERADOR);
    }

    public static Empleado generarSupervisor() {
        return new Empleado(tipoEmpleado.SUPERVISOR);
    }

    public static Empleado generarDirecto() {
        return new Empleado(tipoEmpleado.DIRECTOR);
    }

    /**
     * 
     */
    
    public void run() {
        logger.info("Empleado " + Thread.currentThread().getName() + " Inicia");
	        while (true) {
	            if (!this.llamdasEntrantes.isEmpty()) {
	                Llamada llamada = this.llamdasEntrantes.poll();
	                this.modificarEmpleado(estadoEmpleado.OCUPADO);
	                logger.info("Empleado " + Thread.currentThread().getName() + " esta atendiendo " + llamada.getDuracion() + " segundos");
	                try {
	                    TimeUnit.SECONDS.sleep(llamada.getDuracion());
	                } catch (InterruptedException e) {
	                    logger.error("Empleado " + Thread.currentThread().getName() + " fue interrunpido " + llamada.getDuracion() + " segundos");
	                } finally {
	                    this.modificarEmpleado(estadoEmpleado.DISPONIBLE);
	                }
	                this.llamadasAtendidas.add(llamada);
	                logger.info("Empleado " + Thread.currentThread().getName() + " ah finalizado " + llamada.getDuracion() + " segundos");
	            }
	        }
    }

}
