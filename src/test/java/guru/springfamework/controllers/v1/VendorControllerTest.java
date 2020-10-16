package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.domain.Vendor;
import guru.springfamework.services.VendorService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static guru.springfamework.controllers.v1.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VendorControllerTest {

    @Mock
    VendorService vendorService;

    @InjectMocks
    VendorController vendorController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(vendorController).build();
    }

    @Test
    public void getAllVendors() throws Exception{

        VendorDTO vendor1 = new VendorDTO();
        vendor1.setId(1L);
        vendor1.setName("Nuts for Nuts Company");
        vendor1.setUrl("/api/v1/vendors/1");

        VendorDTO vendor2 = new VendorDTO();
        vendor2.setId(2L);
        vendor2.setName("Exotic Fruits Company");
        vendor2.setUrl("/api/v1/vendors/2");

        when(vendorService.getAllVendors()).thenReturn(Arrays.asList(vendor1,vendor2));

        mockMvc.perform(get("/api/v1/vendors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendors", hasSize(2)));
    }

    @Test
    public void getVendorById() throws Exception {
        VendorDTO vendor1 = new VendorDTO();
        vendor1.setId(1L);
        vendor1.setName("Nuts for Nuts Company");
        vendor1.setUrl("/api/v1/vendors/1");

        when(vendorService.getVendorById(anyLong())).thenReturn(vendor1);

        mockMvc.perform(get("/api/v1/vendors/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name" , equalTo("Nuts for Nuts Company")));
    }

    @Test
    public void createNewVendor() throws Exception {

        VendorDTO savedVendor = new VendorDTO();
        savedVendor.setId(1L);
        savedVendor.setName("Nuts for Nuts Company");
        savedVendor.setUrl("/api/v1/vendors/1");

        VendorDTO returnVendor = new VendorDTO();
        returnVendor.setId(savedVendor.getId());
        returnVendor.setName(savedVendor.getName());
        returnVendor.setUrl(savedVendor.getUrl());


        when(vendorService.createNewVendor(savedVendor)).thenReturn(returnVendor);

        mockMvc.perform(post("/api/v1/vendors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(savedVendor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name",equalTo("Nuts for Nuts Company")))
                .andExpect(jsonPath("$.vendor_url",equalTo("/api/v1/vendors/1")));
    }

    @Test
    public void testUpdateVendor() throws Exception {

        VendorDTO vendor = new VendorDTO();
        vendor.setName("Exotic Fruits Company");

        VendorDTO returnDTO = new VendorDTO();
        returnDTO.setName(vendor.getName());
        returnDTO.setId(1L);
        returnDTO.setUrl("/api/v1/vendors/1");

        when(vendorService.saveVendorByDTO(anyLong(),any(VendorDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(put("/api/v1/vendors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Exotic Fruits Company")))
                .andExpect(jsonPath("$.vendor_url", equalTo("/api/v1/vendors/1")));

    }

    @Test
    public void testPatchVendor() throws Exception {
        VendorDTO vendor = new VendorDTO();
        vendor.setName("Exotic Fruits Company");

        VendorDTO returnDTO = new VendorDTO();
        returnDTO.setName(vendor.getName());
        returnDTO.setUrl("/api/v1/vendors/1");

        when(vendorService.patchVendorByDTO(anyLong(),any(VendorDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(patch("/api/v1/vendors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Exotic Fruits Company")))
                .andExpect(jsonPath("$.vendor_url", equalTo("/api/v1/vendors/1")));

    }

    @Test
    public void deleteVendor() throws Exception {
        mockMvc.perform(delete("/api/v1/vendors/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(vendorService).deleteVendorById(anyLong());
    }
}