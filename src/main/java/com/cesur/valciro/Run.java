package com.cesur.valciro;
import com.cesur.valciro.business.service.JobApplicationService;
import com.cesur.valciro.data.repository.ConfigRepository;
import com.cesur.valciro.data.repository.JobApplicationRepository;
import com.cesur.valciro.presentation.controller.ConsoleStatus;
import com.cesur.valciro.presentation.controller.JobApplicationController;

/**
 * Clase principal de la aplicación.
 * @author Ciro Álvarez Garrido
 * @version v1.0
 */
public class Run {

    /**
     * Función principal de la aplicación. Inicializa todo lo necesario para la ejecución de la aplicación.
     * @param args Argumentos de la línea de comandos.
     * @author Ciro Álvarez Garrido
     * @version v1.0
     */
    public static void main(String[] args) {
        ConfigRepository config = new ConfigRepository("config.txt");
        String file = config.getCSV();
        try {
            JobApplicationRepository repository = new JobApplicationRepository(file);
            JobApplicationService service = new JobApplicationService(repository, config);
            JobApplicationController controller = new JobApplicationController(service);
            controller.readArguments(args);
        } catch (Exception exception) {
            System.out.println("[" + ConsoleStatus.ERROR + "] Fallo crítico al iniciar la aplicación");
        }
    }
}