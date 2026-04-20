package com.Financial_backend.Financial_backend.Controller;

import com.Financial_backend.Financial_backend.Dto.Request.PlanTypeRequestDto;
import com.Financial_backend.Financial_backend.Dto.Request.SponsorRequestDto;
import com.Financial_backend.Financial_backend.Dto.Response.SponsorResponseDto;
import com.Financial_backend.Financial_backend.Service.SponsorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sponsor")
public class SponsorController {

    private final SponsorService sponsorService;

    public SponsorController(SponsorService sponsorService){
        this.sponsorService = sponsorService;
    }

    @PostMapping("/create")
    public ResponseEntity<SponsorResponseDto> create(
            @RequestBody SponsorRequestDto sponsorRequestDto
            ){
        SponsorResponseDto responseDto = sponsorService.createSponsor(sponsorRequestDto);

        return ResponseEntity.ok(responseDto);

    }

    //fetching the sponsors
    @GetMapping("/allsponsor")

    public  ResponseEntity<List<SponsorResponseDto>> getallSponsor(){
        System.out.println("requesting for fetching all sponsors");
        List<SponsorResponseDto> sponsorResponseDtos = sponsorService.getAllSponsor();

        return  ResponseEntity.ok(sponsorResponseDtos);
    }


    //updating the sponsor
    @PutMapping("/update_sponsor/{id}")

    public ResponseEntity<SponsorResponseDto> updateSponsor(
            @PathVariable Long id,
            @RequestBody SponsorRequestDto sponsorRequestDto
    ){
        SponsorResponseDto sponsorResponseDto = sponsorService.updateSponsor(id, sponsorRequestDto);

        return ResponseEntity.ok(sponsorResponseDto);

    }


}
