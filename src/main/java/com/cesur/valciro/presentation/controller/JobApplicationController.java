package com.cesur.valciro.presentation.controller;

import com.cesur.valciro.business.exception.DuplicateJobApplicationException;
import com.cesur.valciro.business.exception.IdNotFoundException;
import com.cesur.valciro.business.service.JobApplicationService;
import com.cesur.valciro.data.entity.JobApplicationEntity;

import java.util.List;

public class JobApplicationController {
    private final JobApplicationService service;

    private static final String TITLE = "--titulo";
    private static final String STATUS = "--estado";
    private static final String COMPANY = "--empresa";
    private static final String DATE = "--fecha";

    /**
     * Constructor de la clase <code>JobApplicationController</code>.
     *
     * @param service servicio de candidaturas que se utiliza para procesar datos.
     * @author Valentin Leyton Golla
     * @since v1.0
     */
    public JobApplicationController(JobApplicationService service) {
        this.service = service;
    }

    /**
     * Lee los parámetros pasados como argumentos desde la clase <code>main</code> y selecciona el comando específico si cumple con los requisitos.
     *
     * @param arguments lista de argumentos.
     * @author Valentin Leyton Golla
     * @since v1.0
     */
    public void readArguments(String[] arguments) {
        if (arguments.length == 0 ) {
            return;
        }
        String argument = arguments[0].toLowerCase();
        switch (argument) {
            case "listar"-> executeList(arguments);
            case "añadir" -> executeAdd(arguments);
            case "ver" -> executeFind(arguments);
            case "borrar" -> executeDelete(arguments);
            case "importar" -> executeImport(arguments);
            default -> System.err.println("[" + ConsoleStatus.ERROR + "] no se reconoce como un comando.");
        }
    }

    /**
     * Esta función muestra en pantalla todas las candidaturas que coinciden con los filtros, de haberlos.
     *
     * @param arguments lista de argumentos a pasar como filtros,
     *                  puede ser <code>--estado</code> y/o <code>--empresa</code>.
     *                  Ambos pueden ser null, en cuyo caso devolverá todas las candidaturas.
     * @author Valentin Leyton Golla
     * @since v1.0
     */
    private void executeList(String[] arguments) {
        String statFilter = null;
        String compFilter = null;
        try {
            for (int currentArgument = 1; currentArgument < arguments.length; currentArgument++) {
                String argument = arguments[currentArgument];
                if (argument.equalsIgnoreCase(STATUS)) {
                    statFilter = getArgumentValue(arguments, currentArgument, STATUS);
                    currentArgument++;
                } else if (argument.equalsIgnoreCase(COMPANY)) {
                    compFilter = getArgumentValue(arguments, currentArgument, COMPANY);
                    currentArgument++;
                } else {
                    System.err.println("[" + ConsoleStatus.ERROR + "] \"" + argument + "\" no se reconoce como un argumento.");
                    return;
                }
            }
            List<JobApplicationEntity> entities = service.list(statFilter, compFilter);
            if (entities.isEmpty()) {
                System.out.println("[" + ConsoleStatus.ERROR + "] No se han encontrado candidaturas.");
                return;
            }
            for (JobApplicationEntity entity : entities) {
                System.out.printf(
                        "│ Candidatura ID: %s\n│ │ Título: %s\n│ │ Estado: %s\n│ │ Empresa: %s\n│ │ Fecha: %s\n",
                        entity.id(), entity.title(), entity.status(), entity.company(), entity.dateCreated()
                );
            }
        } catch (IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
        }
    }

    /**
     * Esta función ejecuta el comando "add" para añadir una nueva candidatura.
     *
     * @param arguments el array de argumentos de la línea de comandos.
     * @author Valentin Leyton Golla
     * @since v1.0
     */
    private void executeAdd(String[] arguments) {
        String title = null;
        String status = null;
        String company = null;
        String date = null;
        try {
            for (int currentArgument = 1; currentArgument < arguments.length; currentArgument++) {
                String argument = arguments[currentArgument];
                if (argument.equalsIgnoreCase(TITLE)) {
                    title = getArgumentValue(arguments, currentArgument, TITLE);
                    currentArgument++;
                } else if (argument.equalsIgnoreCase(STATUS)) {
                    status = getArgumentValue(arguments, currentArgument, STATUS);
                    currentArgument++;
                } else if (argument.equalsIgnoreCase(COMPANY)) {
                    company = getArgumentValue(arguments, currentArgument, COMPANY);
                    currentArgument++;
                } else if (argument.equalsIgnoreCase(DATE)) {
                    date = getArgumentValue(arguments, currentArgument, DATE);
                    currentArgument++;
                } else {
                    System.err.println("[" + ConsoleStatus.ERROR + "] \"" + argument + "\" no se reconoce como un argumento.");
                    return;
                }
            }
            service.add(title, status, company, date);
            System.out.println("[" + ConsoleStatus.INFO + "] La candidatura se ha añadido correctamente.");
        } catch (IllegalArgumentException | DuplicateJobApplicationException exception) {
            System.err.println("[" + ConsoleStatus.ERROR + "] " + exception.getMessage());
        }
    }

    /**
     * Esta función muestra en pantalla los detalles de una candidatura concreta especificada por un identificador a elección del usuario.
     *
     * @param arguments Los argumentos del comando.
     * @author Ciro Álvarez Garrido
     * @since 1.0
     */
    private void executeFind(String[] arguments) {
        try {
            String id = getArgumentValue(arguments, 0, "ver");
            long parsedId;
            try {
                parsedId = Long.parseLong(id);
            } catch (NumberFormatException exception) {
                System.err.println("[" + ConsoleStatus.ERROR + "] El valor del argumento \"" + id + "\" no es un número válido.");
                return;
            }
            JobApplicationEntity entity = service.find(parsedId);
            System.out.printf(
                    "│ Candidatura ID: %s\n│ │ Título: %s\n│ │ Estado: %s\n│ │ Empresa: %s\n│ │ Fecha: %s\n",
                    entity.id(), entity.title(), entity.status(), entity.company(), entity.dateCreated()
            );
        } catch (IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
        } catch (IdNotFoundException exception) {
            System.err.println("[" + ConsoleStatus.ERROR + "] " + exception.getMessage());
        }
    }

    /**
     * Esta función permite eliminar una candidatura concreta específica del archivo CSV usando un identificador a elección del usuario.
     *
     * @param arguments los argumentos del comando.
     * @author Ciro Álvarez Garrido
     * @since v1.0
     */
    private void executeDelete(String[] arguments) {
        try {
            String id = getArgumentValue(arguments, 0, "borrar");
            long parsedId;
            try {
                parsedId = Long.parseLong(id);
            } catch (NumberFormatException exception) {
                System.err.println("[" + ConsoleStatus.ERROR + "] El valor del argumento \"" + id + "\" no es un número válido.");
                return;
            }
            service.delete(parsedId);
            System.out.println("[" + ConsoleStatus.INFO + "] La candidatura con identificador \"" + parsedId + "\" ha sido eliminada.");
        } catch (IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
        } catch (IdNotFoundException exception) {
            System.err.println("[" + ConsoleStatus.ERROR + "] " + exception.getMessage());
        }
    }

    /**
     * Esta función permite importar un fichero CSV cualquiera con candidaturas.
     *
     * @param arguments los argumentos del comando.
     * @author Ciro Álvarez Garrido
     * @since v1.0
     */
    private void executeImport(String[] arguments) {
        try {
            String fileCSV = getArgumentValue(arguments, 0, "import");
            service.importFile(fileCSV);
            System.out.println("[" + ConsoleStatus.INFO + "] El fichero \"" + fileCSV + "\" se ha importado correctamente.");
        } catch (IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
        } catch (RuntimeException exception) {
            System.err.println("[" + ConsoleStatus.ERROR + "] " + exception.getMessage());
        }
    }

    /**
     * Esta función obtiene el valor que le pertenece a un argumento de la línea de comandos.
     *
     * @param arguments el array de argumentos de la línea de comandos.
     * @param currentArgument el índice del argumento actual.
     * @param argumentName el nombre del argumento.
     * @return el valor del argumento.
     * @throws IllegalArgumentException si el argumento no tiene valor.
     * @author Valentin Leyton Golla
     * @since v1.0
     */
    private String getArgumentValue(String[] arguments, int currentArgument, String argumentName) {
        if (currentArgument + 1 < arguments.length) {
            return arguments[currentArgument + 1];
        }
        throw new IllegalArgumentException("[" + ConsoleStatus.WARNING + "] \"" + argumentName + "\" se ha introducido sin valor.");
    }
}
