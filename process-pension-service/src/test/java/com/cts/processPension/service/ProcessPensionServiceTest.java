package com.cts.processPension.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.text.ParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.cts.processPension.client.PensionerDetailClient;
import com.cts.processPension.model.Bank;
import com.cts.processPension.model.PensionDetail;
import com.cts.processPension.model.PensionerDetail;
import com.cts.processPension.model.PensionerInput;
import com.cts.processPension.util.DateUtil;

/**
 * 
 * Class to test Service class functionality for process pension micro-service
 *
 */
@SpringBootTest
class ProcessPensionServiceTest {

	@Autowired
	ProcessPensionService processPensionService;

	@MockBean
	PensionerDetailClient pensionerDetailClient;

	@Test
	void testCheckDetailsForCorrectPensionerInputForSelfPensionType() throws ParseException {
		PensionerInput input = new PensionerInput("Shubham", DateUtil.parseDate("23-11-1996"), "ASDFG3456",
				"123456789012", "self");
		Bank bank = new Bank("ICICI", 456678, "public");

		PensionerDetail details = new PensionerDetail("Shubham", DateUtil.parseDate("23-11-1996"), "ASDFG3456", 100000,
				10000, "self", bank);

		assertTrue(processPensionService.checkdetails(input, details));

	}

	@Test
	void testCheckDetailsForCorrectPensionerInputForFamilyPensionType() throws ParseException {
		PensionerInput input = new PensionerInput("Shubham", DateUtil.parseDate("23-11-1996"), "ASDFG3456",
				"123456789012", "family");
		Bank bank = new Bank("ICICI", 456678, "public");

		PensionerDetail details = new PensionerDetail("Shubham", DateUtil.parseDate("23-11-1996"), "ASDFG3456", 100000,
				10000, "family", bank);

		assertTrue(processPensionService.checkdetails(input, details));
	}

	@Test
	void testCheckDetailsForIncorrectPensionerInputForSelf() throws ParseException {
		PensionerInput input = new PensionerInput("Shubham", DateUtil.parseDate("23-11-1996"), "ASDFG3908",
				"123456789012", "self");
		Bank bank = new Bank("ICICI", 456678, "public");
		PensionerDetail details = new PensionerDetail("Shubham", DateUtil.parseDate("23-11-1996"), "ASDFG3456", 100000,
				10000, "self", bank);

		assertFalse(processPensionService.checkdetails(input, details));
	}

	@Test
	void testCheckDetailsForIncorrectPensionerInputForFamily() throws ParseException {
		PensionerInput input = new PensionerInput("Shubham", DateUtil.parseDate("23-11-1996"), "ASDFG3908",
				"123456789012", "family");
		Bank bank = new Bank("ICICI", 456678, "public");
		PensionerDetail details = new PensionerDetail("Shubham", DateUtil.parseDate("23-11-1996"), "ASDFG3456", 100000,
				10000, "family", bank);

		assertFalse(processPensionService.checkdetails(input, details));

	}

	@Test
	void testGettingPensionDetailByPassingPensionerDetalisForSelfPensionType() throws ParseException {
		Bank bank = new Bank("ICICI", 456678, "public");
		PensionerDetail details = new PensionerDetail("Shubham", DateUtil.parseDate("23-11-1996"), "ASDFG3456", 100000,
				10000, "self", bank);

		PensionDetail actualDetail = processPensionService.calculatePensionAmount(details);

		assertEquals(90000, actualDetail.getPensionAmount());
	}

	@Test
	void testGettingPensionDetailByPassingPensionerDetalisForFamilyPensionType() throws ParseException {
		Bank bank = new Bank("ICICI", 456678, "public");
		PensionerDetail details = new PensionerDetail("Shubham", DateUtil.parseDate("23-11-1996"), "ASDFG3456", 100000,
				10000, "family", bank);

		PensionDetail actualDetail = processPensionService.calculatePensionAmount(details);

		assertEquals(60000, actualDetail.getPensionAmount());
	}

	/**
	 * @author Shubham Nawani
	 * @throws ParseException
	 */
	@DisplayName("Method to test getPensionDetails() method")
	@Test
	void testGetPensionDetails() throws ParseException {
		PensionerInput pensionerInput = new PensionerInput("Shubham", DateUtil.parseDate("23-11-1996"), "ASDFG3456",
				"123456789012", "family");

		Bank bank = new Bank("ICICI", 456678, "public");

		PensionerDetail details_family = new PensionerDetail("Shubham", DateUtil.parseDate("23-11-1996"), "ASDFG3456",
				100000, 10000, "family", bank);

		// mock the feign client
		when(pensionerDetailClient.getPensionerDetailByAadhaar(pensionerInput.getAadhaarNumber()))
				.thenReturn(details_family);

		// get the actual result
		PensionDetail pensionDetailFamily = processPensionService.getPensionDetails(pensionerInput);

		// test cases
		assertEquals(60000, pensionDetailFamily.getPensionAmount());
		assertNotNull(pensionDetailFamily);
	}
	
	/**
	 * @author Shubham Nawani
	 * @throws ParseException
	 */
	@DisplayName("Method to test getPensionDetails() method")
	@Test
	void testGetPensionDetails_forSelf() throws ParseException {
		PensionerInput pensionerInput = new PensionerInput("Shubham", DateUtil.parseDate("23-11-1996"), "ASDFG3456",
				"123456789012", "self");

		Bank bank = new Bank("ICICI", 456678, "public");

		PensionerDetail details_family = new PensionerDetail("Shubham", DateUtil.parseDate("23-11-1996"), "ASDFG3456",
				100000, 10000, "self", bank);

		// mock the feign client
		when(pensionerDetailClient.getPensionerDetailByAadhaar(pensionerInput.getAadhaarNumber()))
				.thenReturn(details_family);

		// get the actual result
		PensionDetail pensionDetailFamily = processPensionService.getPensionDetails(pensionerInput);

		// test cases
		assertEquals(90000, pensionDetailFamily.getPensionAmount());
		assertNotNull(pensionDetailFamily);
	}
}