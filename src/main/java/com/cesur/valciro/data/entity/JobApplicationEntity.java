package com.cesur.valciro.data.entity;

public record JobApplicationEntity(
        Long id,
        String title,
        JobApplicationStatus status,
        String company,
        String dateCreated
) {
    public JobApplicationEntity(Long id, String title, JobApplicationStatus status, String company, String dateCreated) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.company = company;
        this.dateCreated = dateCreated;
    }
}
