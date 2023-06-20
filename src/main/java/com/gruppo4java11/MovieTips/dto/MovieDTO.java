package com.gruppo4java11.MovieTips.dto;

public class MovieDTO {
    private String title;
    private Integer id;

    public MovieDTO(String title, Integer id){
        this.title = title;
        this.id = id;
    }

    public MovieDTO(){

    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
