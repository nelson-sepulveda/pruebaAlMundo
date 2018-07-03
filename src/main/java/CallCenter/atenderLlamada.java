package CallCenter;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * DefaultCallAttendStrategy
 * <p>
 * This strategy returns the first available operator employee.
 * If all operator employees are busy, it returns the first available supervisor employee.
 * If all supervisor employees are busy, it returns the first available director employee.
 * If all employees are busy, it returns null.
 */
public class atenderLlamada implements atender {

    private static final Logger logger = LoggerFactory.getLogger(atenderLlamada.class);

    
    public Empleado findEmployee(Collection<Empleado> employeeList) {
		return buscarEmpleado(employeeList);
	}


	public Empleado buscarEmpleado(Collection<Empleado> lista) {
        Validate.notNull(lista);
        List<Empleado> availableEmployees = lista.stream().filter(e -> e.getEstadoEmpleado() == estadoEmpleado.DISPONIBLE).collect(Collectors.toList());
        logger.info("Operadores disponible: " + availableEmployees.size());
        Optional<Empleado> empleado = availableEmployees.stream().filter(e -> e.getTipoEmpleado() == tipoEmpleado.OPERADOR).findAny();
        if (!empleado.isPresent()) {
            logger.info("Operadores no disponibles");
            empleado = availableEmployees.stream().filter(e -> e.getTipoEmpleado() == tipoEmpleado.SUPERVISOR).findAny();
            if (!empleado.isPresent()) {
                logger.info("Supervisore no diponibles");
                empleado = availableEmployees.stream().filter(e -> e.getTipoEmpleado() == tipoEmpleado.DIRECTOR).findAny();
                if (!empleado.isPresent()) {
                    logger.info("Directores no disponibles");
                    return null;
                }
            }
        }
        logger.info("Tipo Empleado " + empleado.get().getTipoEmpleado() + " ");
        return empleado.get();
    }

}
