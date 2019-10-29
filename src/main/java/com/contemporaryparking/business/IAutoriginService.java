package com.contemporaryparking.business;

import com.contemporaryparking.data.ParkingSlotType;

public interface IAutoriginService {

  /*
   * Search the type of the car by using its plate number and return a parking slot type.
   *
   * @param plateNumber
   * @return return an appropriate parking slot type
   */
  ParkingSlotType retrieveParkingSlotType(String plateNumber);
}
