package org.skanarakis.practice.reactiveclient.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.SerializedName;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
@JsonSerialize(as = ImmutableCar.class)
@JsonDeserialize(as = ImmutableCar.class)
public abstract class Car {

    @SerializedName(value = "model", alternate = "model")
    public abstract String model();
    @SerializedName(value = "year", alternate = "year")
    public abstract int year();
}
