package com.cesur.valciro.presentation.controller;

import com.cesur.valciro.business.service.JobApplicationService;

public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    public void readArguments(String[] arguments) {
        if (arguments.length == 0 ) {
            return;
        }
        String argument = arguments[0].toLowerCase();
        switch (argument) {
            default -> {
                System.err.println(ConsoleStatus.ERROR + " no se reconoce como un comando.");
            }
        }
    }
}
