package com.contemporaryparking.business;

import com.contemporaryparking.data.ParkingBill;
import com.contemporaryparking.data.PricingPolicy;

public interface IDebitService {

  /*
   * Update the price policy which was first time initialized with toll parking configuration.
   *
   * @param pricingPolicy
   * @return true if succeeded, false if it is not valid
   */
  boolean updatePricingPolicy(PricingPolicy pricingPolicy);

  /*
   * Calculate the price for the parking and return the updated parking bill.
   *
   * @param parkingBill
   * @return return parking bill after setting the price
   */
  ParkingBill handleParkingBill(ParkingBill parkingBill);
}
