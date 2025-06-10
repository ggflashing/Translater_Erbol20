package com.example.translater_erbol20.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "mmm")

public class DualModels {

    @PrimaryKey(autoGenerate = true)
    long id;

    @ColumnInfo(name = "from_text")
    String fromText;

    @ColumnInfo(name = "resultText")
    String resultText;

    public DualModels(String fromText, String resultText) {
        this.fromText = fromText;
        this.resultText = resultText;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFromText() {
        return fromText;
    }

    public void setFromText(String fromText) {
        this.fromText = fromText;
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }
}
