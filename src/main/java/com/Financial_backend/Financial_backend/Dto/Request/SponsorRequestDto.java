package com.Financial_backend.Financial_backend.Dto.Request;

import lombok.Data;

@Data

public class SponsorRequestDto {

    private String company_name;

    private String ein;

    private String plan_type;

    private String match_formula;

    private String vesting_schedule;

    private String safe_harbour_plan;

    private String plan_start_date;

    private String primaryContactName;

    private String primaryContactEmail;

    private  String primary_contact_phone;

    private String addressLine1;

    private String city;

    private String state;

    private String zipcode;



    private String country = "United States";


}

