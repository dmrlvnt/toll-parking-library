package com.contemporaryparking.business;

import com.contemporaryparking.config.TollParkingConfig;
import com.contemporaryparking.data.ParkingBill;
import com.contemporaryparking.data.ParkingSlot;
import com.contemporaryparking.data.PricingPolicy;
import java.util.Optional;

public interface ITollParkingManager {

  /*
   * Initialize the toll parking with a configuration. The configuration gives total number of
   * parking slots for three different types and a price policy.
   *
   * @param tollParkingConfig
   * @return used configuration
   */
  TollParkingConfig initialize(TollParkingConfig tollParkingConfig);

  /*
   * Update the price policy in debit srvice which was first time initialized with toll parking
   * configuration.
   *
   * @param pricingPolicy
   * @return true if succeeded, false if it is not valid
   */
  boolean updatePricingPolicy(PricingPolicy pricingPolicy);

  /*
   * Get the first available parking slot from the toll parking.
   *
   * @param plateNumber
   * @return parking slot is returned if available
   */
  Optional<ParkingSlot> getParkingSlot(String plateNumber);

  /*
   * The parking slot is freed, and the parking bill is updated and returned.
   *
   * @param plateNumber
   * @return parking bill is returned if exists
   */
  Optional<ParkingBill> leaveParking(String plateNumber);
}
