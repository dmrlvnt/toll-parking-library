package com.contemporaryparking.business.impl;

import com.contemporaryparking.business.IAutoriginService;
import com.contemporaryparking.data.ParkingSlotType;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public final class AutoriginService implements IAutoriginService {

  public ParkingSlotType retrieveParkingSlotType(String plateNumber) {
    Random random = new Random();
    return ParkingSlotType.values()[random.nextInt(ParkingSlotType.values().length)];
  }
}
