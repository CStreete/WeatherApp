package com.cameron.weatherapp.modals.met;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


public class Summary {

    @JsonProperty("symbol_code")
    private String symbolCode;

    @JsonProperty("symbol_code")
    public String getSymbolCode() {
        return symbolCode;
    }

    @JsonProperty("symbol_code")
    public void setSymbolCode(String symbolCode) {
        this.symbolCode = symbolCode;
    }

}
