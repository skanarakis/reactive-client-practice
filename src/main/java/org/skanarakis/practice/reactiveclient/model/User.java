package org.skanarakis.practice.reactiveclient.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.SerializedName;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
@JsonSerialize(as = ImmutableUser.class)
@JsonDeserialize(as = ImmutableUser.class)
public abstract class User {

    @SerializedName(value = "id", alternate = "id")
    public abstract int id();
    @SerializedName(value = "name", alternate = "name")
    public abstract String name();
}
