package com.contemporaryparking.business;

import com.contemporaryparking.business.impl.DebitService;
import com.contemporaryparking.business.impl.TollParkingManager;
import com.contemporaryparking.config.TollParkingConfig;
import com.contemporaryparking.data.ParkingBill;
import com.contemporaryparking.data.ParkingSlot;
import com.contemporaryparking.data.ParkingSlotType;
import com.contemporaryparking.data.PricingPolicy;
import com.contemporaryparking.repository.ParkingBillRepository;
import com.contemporaryparking.repository.ParkingSlotRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTollParkingManager {

  @Mock
  private IAutoriginService autoriginService;

  @Mock
  private ParkingSlotRepository parkingSlotRepository;

  @Mock
  private ParkingBillRepository parkingBillRepository;

  private ITollParkingManager tollParkingManager;

  private IDebitService debitService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    Mockito.when(autoriginService.retrieveParkingSlotType(Mockito.anyString()))
        .thenReturn(ParkingSlotType.ELECTRIC_CAR_50KW);
    ParkingSlot parkingSlot = new ParkingSlot(ParkingSlotType.ELECTRIC_CAR_50KW, true);
    parkingSlot.setId(0L);
    Mockito.when(parkingSlotRepository.findAll())
        .thenReturn(Arrays.asList(new ParkingSlot(ParkingSlotType.ELECTRIC_CAR_50KW, true)));
    Mockito.when(parkingSlotRepository.findById(0L)).thenReturn(Optional.of(parkingSlot));
    Mockito.when(parkingBillRepository.findAll())
        .thenReturn(Arrays.asList(new ParkingBill("AB 123 CD", parkingSlot, LocalDateTime.now())));
    debitService = new DebitService(parkingBillRepository);
    tollParkingManager =
        new TollParkingManager(
            debitService, autoriginService, parkingSlotRepository, parkingBillRepository);
  }

  @Test
  public void testInitialize() {
    // prepare
    PricingPolicy pricingPolicy = new PricingPolicy(5, 1.2);
    TollParkingConfig tollParkingConfig = new TollParkingConfig(10, 10, 10, pricingPolicy);
    String plateNumber = "AB 123 CD";

    // execute
    TollParkingConfig returnedTollParkingConfig = tollParkingManager.initialize(tollParkingConfig);
    Optional<ParkingSlot> parkingSlot = tollParkingManager.getParkingSlot(plateNumber);
    // assert
    Assert.assertNotNull("Returned Toll Parking Config is not null", returnedTollParkingConfig);
    Assert.assertEquals(
        "Same Toll Parking Config is returned", tollParkingConfig, returnedTollParkingConfig);
    Assert.assertNotNull(
        "A parking slot is always returned, since none is occupied", parkingSlot.get());
    Assert.assertFalse(
        "The parking slot is not free anymore, since none is occupied", parkingSlot.get().isFree());

    // execute
    Optional<ParkingBill> parkingBill = tollParkingManager.leaveParking(plateNumber);
    Assert.assertNotNull("A parking bill is returned", parkingBill.get());
    Assert.assertEquals(
        "The parking bill is for the correct car", parkingBill.get().getPlateNumber(), plateNumber);
    Assert.assertEquals(
        "The parking bill is for the correct parking slot",
        parkingSlot.get().getId(),
        parkingBill.get().getParkingSlot().getId());
  }

  @Test
  public void testUpdatePricingPolicy_ValidPricingPolicy() {
    // prepare
    PricingPolicy pricingPolicy = new PricingPolicy(5, 1.2);

    // execute
    boolean updated = tollParkingManager.updatePricingPolicy(pricingPolicy);

    // assert
    Assert.assertTrue("Pricing policy has been updated", updated);
  }

  @Test
  public void testUpdatePricingPolicy_InvalidPricingPolicy() {
    // prepare
    PricingPolicy pricingPolicy = new PricingPolicy(0.0, 0.0);

    // execute
    boolean updated = tollParkingManager.updatePricingPolicy(pricingPolicy);

    // assert
    Assert.assertFalse("Pricing policy has been updated", updated);
  }
}
