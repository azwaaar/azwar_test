package com.azwar.test.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "result_model")
public class ResultModel implements Serializable {

    @PrimaryKey()
    private Integer id;

    private String input;
    private String result;
    private String image;

    public ResultModel() {

    }

    public ResultModel(Integer id, String input, String result, String image) {
        this.id = id;
        this.input = input;
        this.result = result;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ResultModel{" +
                "id=" + id +
                ", input='" + input + '\'' +
                ", result='" + result + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
