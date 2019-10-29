package com.contemporaryparking.business;

import com.contemporaryparking.business.impl.AutoriginService;
import com.contemporaryparking.data.ParkingSlotType;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAutoriginService {

  private IAutoriginService autoriginService = new AutoriginService();

  @Test
  public void testRetrieveParkingSlotType() {
    // prepare
    String plateNumber = "AB 123 CD";

    // execute
    ParkingSlotType parkingSlotType = autoriginService.retrieveParkingSlotType(plateNumber);

    // asserts
    Assert.assertNotNull("Never null is returned by mocked service", parkingSlotType);
    Assert.assertTrue(
        "Parking slot type is existing in the types",
        Arrays.asList(ParkingSlotType.values()).contains(parkingSlotType));
  }
}
