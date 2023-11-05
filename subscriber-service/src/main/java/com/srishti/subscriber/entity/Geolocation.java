package com.srishti.subscriber.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Geolocation {

    private double latitude;

    private double longitude;
}
