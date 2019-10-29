package com.contemporaryparking.business.impl;

import com.contemporaryparking.business.IDebitService;
import com.contemporaryparking.data.ParkingBill;
import com.contemporaryparking.data.PricingPolicy;
import com.contemporaryparking.repository.ParkingBillRepository;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DebitService implements IDebitService {

  private PricingPolicy pricingPolicy;

  private ParkingBillRepository parkingBillRepository;

  @Autowired
  public DebitService(ParkingBillRepository parkingBillRepository) {
    this.parkingBillRepository = parkingBillRepository;
  }

  public boolean updatePricingPolicy(PricingPolicy pricingPolicy) {
    if (pricingPolicy.getFixedAmount() >= 0 && pricingPolicy.getHourPrice() > 0) {
      this.pricingPolicy = pricingPolicy;
      return true;
    }

    return false;
  }

  public ParkingBill handleParkingBill(ParkingBill parkingBill) {
    double price =
        pricingPolicy.getFixedAmount() + pricingPolicy.getHourPrice() * parkingHours(parkingBill);
    parkingBill.setPrice(price);

    return parkingBillRepository.save(parkingBill);
  }

  private double parkingHours(ParkingBill parkingBill) {
    return parkingBill.getStart().until(parkingBill.getEnd(), ChronoUnit.MINUTES) / 60.;
  }
}
