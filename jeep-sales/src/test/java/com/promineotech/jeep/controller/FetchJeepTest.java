package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import com.promineotech.jeep.controller.support.BaseTest;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;
import org.springframework.http.HttpMethod;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) 
@ActiveProfiles("test")
@Sql(scripts = {
    "classpath:flyway/migrations/V1.0_Jeep_Schema.sql",
    "classpath:flyway/migration/V1.1_Jeep_Data.sql"},
    config = @SqlConfig(encoding = "utf-8"))

class FetchJeepTest extends BaseTest {

  @Test
  void testThatJeepsAreReturnedWhenAValidModelAndTrimAreSupplied() {
    //Given: a valid model, trim, and URI
    JeepModel model = JeepModel.WRANGLER;
    String trim = "Sport";
    String uri = String.format("%s?model=%s&trim=%s", getBaseUri(), model, trim);
    
    //When: a connection is made to the URI
    ResponseEntity<List<Jeep>> response = getRestTemplate().exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
    
    //Then: a list of Jeeps is returned
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    
    //And: the actual list returned is the same as the expected list
    List<Jeep> expected = buildExpected();
    assertThat(response.getBody()).isEqualTo(expected);
  }

  protected List<Jeep> buildExpected() {
    List<Jeep> list = new LinkedList<>();
    
    //@formatter:off
    list.add(Jeep.builder()
        .modelId(JeepModel.WRANGLER)
        .trimLevel("Sport")
        .numDoors(2)
        .wheelSize(17)
        .basePrice(new BigDecimal("28475.00"))
        .build());
    
    list.add(Jeep.builder()
        .modelId(JeepModel.WRANGLER)
        .trimLevel("Sport")
        .numDoors(4)
        .wheelSize(17)
        .basePrice(new BigDecimal("31975.00"))
        .build());
    
    //@formatter:on
    
    return list;
  }

}
