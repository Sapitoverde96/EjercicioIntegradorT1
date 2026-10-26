package com.cesur.valciro.data.entity;

public record JobApplicationEntity(
        Long id,
        JobApplicationStatus status,
        String company,
        String dateCreated
) {
    public JobApplicationEntity(Long id, JobApplicationStatus status, String company, String dateCreated) {
        this.id = id;
        this.status = status;
        this.company = company;
        this.dateCreated = dateCreated;
    }
}
