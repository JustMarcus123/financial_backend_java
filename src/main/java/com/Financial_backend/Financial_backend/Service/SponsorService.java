package com.Financial_backend.Financial_backend.Service;

import com.Financial_backend.Financial_backend.Dto.Request.SponsorRequestDto;
import com.Financial_backend.Financial_backend.Dto.Response.SponsorResponseDto;
import com.Financial_backend.Financial_backend.Entity.SponsorEntity;
import com.Financial_backend.Financial_backend.Respository.SponsorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SponsorService {

    private final SponsorRepository sponsorRepository;


    public SponsorService (SponsorRepository sponsorRepository){
        this.sponsorRepository = sponsorRepository;
    }


    // create new sponsor

    //1. create method declaration
    public SponsorResponseDto createSponsor(SponsorRequestDto sponsorRequestDto){


        /// for creating and saving new data to the db
        SponsorEntity sponsorEntity = new SponsorEntity();

      sponsorEntity.setCompany_name(sponsorRequestDto.getCompany_name());
      sponsorEntity.setEin(sponsorRequestDto.getEin());
      sponsorEntity.setPrimaryContactName(sponsorRequestDto.getPrimaryContactName());
      sponsorEntity.setPrimaryContactEmail(sponsorRequestDto.getPrimaryContactEmail());
      sponsorEntity.setPrimary_contact_phone(sponsorRequestDto.getPrimary_contact_phone());
      sponsorEntity.setAddressLine1(sponsorRequestDto.getAddressLine1());
      sponsorEntity.setCity(sponsorRequestDto.getCity());
      sponsorEntity.setState(sponsorRequestDto.getState());
      sponsorEntity.setZipcode(sponsorRequestDto.getZipcode());
      sponsorEntity.setCountry(sponsorRequestDto.getCountry());
      sponsorEntity.setPlan_type(sponsorRequestDto.getPlan_type());
      sponsorEntity.setMatch_formula(sponsorRequestDto.getMatch_formula());
      sponsorEntity.setVesting_schedule(sponsorRequestDto.getVesting_schedule());
      sponsorEntity.setSafe_harbour_plan(sponsorRequestDto.getSafe_harbour_plan());
      sponsorEntity.setPlan_start_date(sponsorRequestDto.getPlan_start_date());

      SponsorEntity saved = sponsorRepository.save(sponsorEntity);


      //for sending the data to the response dto
      SponsorResponseDto sponsorResponseDto = new SponsorResponseDto();

      sponsorResponseDto.setCompany_name(saved.getCompany_name());
      sponsorResponseDto.setEin(saved.getEin());
      sponsorResponseDto.setPrimaryContactName(saved.getPrimaryContactName());
      sponsorResponseDto.setPrimaryContactEmail(saved.getPrimaryContactEmail());
      sponsorResponseDto.setPrimary_contact_phone(saved.getPrimary_contact_phone());
      sponsorResponseDto.setAddressLine1(saved.getAddressLine1());
      sponsorResponseDto.setCity(saved.getCity());
      sponsorResponseDto.setState(saved.getState());
      sponsorResponseDto.setZipcode(saved.getZipcode());
      sponsorResponseDto.setCountry(saved.getCountry());

      sponsorResponseDto.setPlan_type(saved.getPlan_type());
      sponsorResponseDto.setMatch_formula(saved.getMatch_formula());
      sponsorResponseDto.setVesting_schedule(saved.getVesting_schedule());
      sponsorResponseDto.setSafe_harbour_plan(saved.getSafe_harbour_plan());
      sponsorResponseDto.setPlan_start_date(saved.getPlan_start_date());

      return sponsorResponseDto;

    }


    //let's fetch the sponsors

  public List <SponsorResponseDto> getAllSponsor(){

      List<SponsorEntity> sponsorEntities = sponsorRepository.findAll();

      //convert list <Entity to List> <ResponseDto>

    return sponsorEntities.stream()
            .map(this::convertToResponseDto)
            .toList();
  }

  //====== helper conversion method

  private SponsorResponseDto convertToResponseDto(SponsorEntity sponsorEntity){
      SponsorResponseDto sponsorResponseDto = new SponsorResponseDto();

      sponsorResponseDto.setCompany_name(sponsorEntity.getCompany_name());
      sponsorResponseDto.setCompany_name(sponsorEntity.getCompany_name());
      sponsorResponseDto.setEin(sponsorEntity.getEin());
      sponsorResponseDto.setPrimaryContactName(sponsorEntity.getPrimaryContactName());
      sponsorResponseDto.setPrimaryContactEmail(sponsorEntity.getPrimaryContactEmail());
      sponsorResponseDto.setPrimary_contact_phone(sponsorEntity.getPrimary_contact_phone());
      sponsorResponseDto.setAddressLine1(sponsorEntity.getAddressLine1());
      sponsorResponseDto.setCity(sponsorEntity.getCity());
      sponsorResponseDto.setState(sponsorEntity.getState());
      sponsorResponseDto.setZipcode(sponsorEntity.getZipcode());
      sponsorResponseDto.setCountry(sponsorEntity.getCountry());

      sponsorResponseDto.setPlan_type(sponsorEntity.getPlan_type());
      sponsorResponseDto.setMatch_formula(sponsorEntity.getMatch_formula());
      sponsorResponseDto.setVesting_schedule(sponsorEntity.getVesting_schedule());
      sponsorResponseDto.setSafe_harbour_plan(sponsorEntity.getSafe_harbour_plan());
      sponsorResponseDto.setPlan_start_date(sponsorEntity.getPlan_start_date());

      return sponsorResponseDto;
  }



}
