package com.cesur.valciro.presentation.controller;

import com.cesur.valciro.business.service.JobApplicationService;
import com.cesur.valciro.data.entity.JobApplicationEntity;

import java.util.List;

public class JobApplicationController {
    private final JobApplicationService service;

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
            case "listar"-> {
                executeList(arguments);
            }
            default -> {
                System.err.println(ConsoleStatus.ERROR + " no se reconoce como un comando.");
            }
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
        for (int arg = 1; arg < arguments.length; arg++) {
            String argument = arguments[arg];
            if (argument.equalsIgnoreCase("--estado")) {
                if (arg + 1 < arguments.length) {
                    statFilter = arguments[arg + 1];
                    arg++;
                } else {
                    System.err.println(ConsoleStatus.WARNING + " \"--estado\" se ha introducido sin valor.");
                    return;
                }
            } else if (argument.equalsIgnoreCase("--empresa")) {
                if (arg + 1 < arguments.length) {
                    compFilter = arguments[arg + 1];
                    arg++;
                } else {
                    System.err.println(ConsoleStatus.WARNING + " \"--empresa\" se ha introducido sin valor.");
                    return;
                }
            } else {
                System.err.println(ConsoleStatus.ERROR + " no se reconoce como un argumento.");
                return;
            }
        }
        List<JobApplicationEntity> entities = service.list(statFilter, compFilter);
        if (entities.isEmpty()) {
            System.out.println(ConsoleStatus.WARNING + " No se han encontrado candidaturas.");
            return;
        }
        for (JobApplicationEntity entity : entities) {
            System.out.printf(
                    "╭ Candidatura ID: %s\n│ ╭ Título: %s\n│ │ Estado: %s\n│ │ Empresa: %s\n│ ╰ Fecha: %s\n",
                    entity.id(), entity.title(), entity.status(), entity.company(), entity.dateCreated()
            );
        }
    }
}
