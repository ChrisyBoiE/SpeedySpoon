package nje.gamf.speedyspoon.Models;

public class Status {
    private String description;
    private String status;

    public Status() {
    }

    public Status(String description, String status) {
        this.description = description;
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
