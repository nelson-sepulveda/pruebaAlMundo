package CallCenter;

import java.util.Collection;

/**
 * Models different strategies on which is the next Employee available to work
 */
public interface atender {

    /**
	 * Finds next available employee
	 *
	 * @param employeeList List of working employees
	 * @return Next available employee to take on a task, or null if all employees are busy
	 */
	Empleado findEmployee(Collection<Empleado> employeeList);

	/**
     * Finds next available employee
     *
     * @param employeeList List of working employees
     * @return Next available employee to take on a task, or null if all employees are busy
     */
    Empleado buscarEmpleado(Collection<Empleado> employeeList);

}
