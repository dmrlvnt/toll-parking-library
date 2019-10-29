package com.contemporaryparking.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc // need this in Spring Boot test
public class ITestTollParkingController {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private ITollParkingManager tollParkingManager;

  @MockBean
  private IDebitService debitService;

  @MockBean
  private IAutoriginService autoriginService;

  @MockBean
  private ParkingSlotRepository parkingSlotRepository;

  @MockBean
  private ParkingBillRepository parkingBillRepository;

  @Test
  public void testEndpoints() throws Exception {
    // prepare
    PricingPolicy pricingPolicy = new PricingPolicy(5, 1.2);
    PricingPolicy newPricingPolicy = new PricingPolicy(7, 1.0);
    TollParkingConfig tollParkingConfig = new TollParkingConfig(10, 10, 10, pricingPolicy);
    String plateNumber = "AB 123 CD";
    ParkingSlot parkingSlot = new ParkingSlot(ParkingSlotType.ELECTRIC_CAR_50KW, false);
    parkingSlot.setId(1L);
    Optional<ParkingSlot> optionalParkingSlot = Optional.of(parkingSlot);
    ParkingBill parkingBill =
        new ParkingBill("AB 123 CD", parkingSlot, LocalDateTime.of(2012, 7, 1, 12, 0));
    parkingBill.setId(1L);
    parkingBill.setEnd(LocalDateTime.of(2012, 7, 1, 13, 45));
    parkingBill.setPrice(7.1);
    Optional<ParkingBill> optionalParkingBill = Optional.of(parkingBill);
    doReturn(tollParkingConfig).when(tollParkingManager).initialize(any());
    doReturn(true).when(tollParkingManager).updatePricingPolicy(any());
    doReturn(optionalParkingSlot).when(tollParkingManager).getParkingSlot(plateNumber);
    doReturn(optionalParkingBill).when(tollParkingManager).leaveParking(plateNumber);

    // execute
    // initialize
    mvc.perform(
        MockMvcRequestBuilders.post("/initialize")
            .content(toJson(tollParkingConfig))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfStandardParkingSlot", is(10)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElectricCar20KWParkingSlot", is(10)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElectricCar50KWParkingSlot", is(10)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.pricingPolicy.fixedAmount", is(5.0)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.pricingPolicy.hourPrice", is(1.2)));
    // update pricing policy
    mvc.perform(
        MockMvcRequestBuilders.put("/pricingpolicy")
            .content(toJson(newPricingPolicy))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.fixedAmount", is(7.0)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.hourPrice", is(1.0)));
    // enter parking
    mvc.perform(
        MockMvcRequestBuilders.get("/enterparking/AB 123 CD")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.free", is(false)));
    // leave parking
    mvc.perform(
        MockMvcRequestBuilders.get("/leaveparking/AB 123 CD")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.plateNumber", is("AB 123 CD")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(7.1)));
  }

  private byte[] toJson(Object object) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return mapper.writeValueAsBytes(object);
  }
}
