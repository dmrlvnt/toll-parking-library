package com.contemporaryparking.business.impl;

import com.contemporaryparking.business.IAutoriginService;
import com.contemporaryparking.business.IDebitService;
import com.contemporaryparking.business.ITollParkingManager;
import com.contemporaryparking.config.TollParkingConfig;
import com.contemporaryparking.data.ParkingBill;
import com.contemporaryparking.data.ParkingSlot;
import com.contemporaryparking.data.ParkingSlotType;
import com.contemporaryparking.data.PricingPolicy;
import com.contemporaryparking.repository.ParkingBillRepository;
import com.contemporaryparking.repository.ParkingSlotRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TollParkingManager implements ITollParkingManager {

  private IDebitService debitService;

  private IAutoriginService autoriginService;

  private ParkingSlotRepository parkingSlotRepository;

  private ParkingBillRepository parkingBillRepository;

  private boolean initialized = false;

  @Autowired
  public TollParkingManager(
      IDebitService debitService,
      IAutoriginService autoriginService,
      ParkingSlotRepository parkingSlotRepository,
      ParkingBillRepository parkingBillRepository) {
    this.debitService = debitService;
    this.autoriginService = autoriginService;
    this.parkingSlotRepository = parkingSlotRepository;
    this.parkingBillRepository = parkingBillRepository;
  }

  public TollParkingConfig initialize(TollParkingConfig tollParkingConfig) {
    if (!initialized) {
      int numberOfStandardParkingSlot = tollParkingConfig.getNumberOfStandardParkingSlot();
      int numberOfElectricCar20KWParkingSlot =
          tollParkingConfig.getNumberOfElectricCar20KWParkingSlot();
      int numberOfElectricCar50KWParkingSlot =
          tollParkingConfig.getNumberOfElectricCar50KWParkingSlot();
      IntStream.rangeClosed(1, numberOfStandardParkingSlot)
          .forEach(
              i -> parkingSlotRepository.save(new ParkingSlot(ParkingSlotType.STANDARD, true)));
      IntStream.rangeClosed(1, numberOfElectricCar20KWParkingSlot)
          .forEach(
              i ->
                  parkingSlotRepository.save(
                      new ParkingSlot(ParkingSlotType.ELECTRIC_CAR_20KW, true)));
      IntStream.rangeClosed(1, numberOfElectricCar50KWParkingSlot)
          .forEach(
              i ->
                  parkingSlotRepository.save(
                      new ParkingSlot(ParkingSlotType.ELECTRIC_CAR_50KW, true)));

      debitService.updatePricingPolicy(tollParkingConfig.getPricingPolicy());
      initialized = true;

      return tollParkingConfig;
    }

    return null;
  }

  public boolean updatePricingPolicy(PricingPolicy pricingPolicy) {
    return debitService.updatePricingPolicy(pricingPolicy);
  }

  public synchronized Optional<ParkingSlot> getParkingSlot(String plateNumber) {
    // no check on same plate number
    ParkingSlotType parkingSlotType = autoriginService.retrieveParkingSlotType(plateNumber);
    Optional<ParkingSlot> firstParkingSlot =
        parkingSlotRepository.findAll().stream()
            .filter(ps -> ps.getParkingSlotType().equals(parkingSlotType) && ps.isFree())
            .findFirst();
    if (firstParkingSlot.isPresent()) {
      ParkingSlot parkingSlot = firstParkingSlot.get();
      parkingSlot.setFree(false);
      parkingSlotRepository.save(parkingSlot);
      parkingBillRepository.save(new ParkingBill(plateNumber, parkingSlot, LocalDateTime.now()));
    }

    return firstParkingSlot;
  }

  public synchronized Optional<ParkingBill> leaveParking(String plateNumber) {
    Optional<ParkingBill> firstParkingBill =
        parkingBillRepository.findAll().stream()
            .filter(pb -> pb.getPlateNumber().equals(plateNumber) && pb.getEnd() == null)
            .findFirst();
    if (firstParkingBill.isPresent()) {
      ParkingBill parkingBill = firstParkingBill.get();
      parkingBill.setEnd(LocalDateTime.now());
      parkingSlotRepository.findById(parkingBill.getParkingSlot().getId()).get().setFree(true);
      debitService.handleParkingBill(parkingBill);
    }

    return firstParkingBill;
  }
}
