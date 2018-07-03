package CallCenterTest;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import CallCenter.*;

public class DispatcherTest {

    private static final int CALL_AMOUNT = 10;
    private static final int MIN_CALL_DURATION = 5;
    private static final int MAX_CALL_DURATION = 10;

    @Test(expected = NullPointerException.class)
    public void creacionEmpleadosNull() {
        new Dispatcher(null);
    }

    

    @Test
    public void llamadasParaEmpleados() throws InterruptedException {
        List<Empleado> employeeList = generarListadoEmpelado();
        Dispatcher dispatcher = new Dispatcher(employeeList);
        dispatcher.start();
        TimeUnit.SECONDS.sleep(1);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(dispatcher);
        TimeUnit.SECONDS.sleep(1);

        generarLista().stream().forEach(call -> {
            dispatcher.dispatch(call);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                fail();
            }
        });

        executorService.awaitTermination(MAX_CALL_DURATION * 2, TimeUnit.SECONDS);
        assertEquals(CALL_AMOUNT, employeeList.stream().mapToInt(employee -> employee.getLlamadasAtendidas().size()).sum());
    }

    private static List<Empleado> generarListadoEmpelado() {
        Empleado operator1 = Empleado.generarOperador();
        Empleado operator2 = Empleado.generarOperador();
        Empleado operator3 = Empleado.generarOperador();
        Empleado operator4 = Empleado.generarOperador();
        Empleado operator5 = Empleado.generarOperador();
        Empleado operator6 = Empleado.generarOperador();
        Empleado supervisor1 = Empleado.generarSupervisor();
        Empleado supervisor2 = Empleado.generarSupervisor();
        Empleado supervisor3 = Empleado.generarSupervisor();
        Empleado director = Empleado.generarDirecto();
        return Arrays.asList(operator1, operator2, operator3, operator4, operator5, operator6,
                supervisor1, supervisor2, supervisor3, director);
    }

    private static List<Llamada> generarLista() {
        return Llamada.listadoLlamadas(CALL_AMOUNT, MIN_CALL_DURATION, MAX_CALL_DURATION);
    }

}
