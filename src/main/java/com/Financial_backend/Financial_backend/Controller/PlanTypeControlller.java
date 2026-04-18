package com.Financial_backend.Financial_backend.Controller;

import com.Financial_backend.Financial_backend.Dto.Request.PlanTypeRequestDto;
import com.Financial_backend.Financial_backend.Dto.Response.PlanTypeResponseDto;
import com.Financial_backend.Financial_backend.Service.PlanTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planType")
public class PlanTypeControlller {

    private final PlanTypeService planTypeService;

    public PlanTypeControlller(PlanTypeService planTypeService){
        this.planTypeService = planTypeService;
    }

    @PostMapping("/create")
    public ResponseEntity<PlanTypeResponseDto> create(@RequestBody PlanTypeRequestDto planTypeRequestDto){
        PlanTypeResponseDto response = planTypeService.createPlan(planTypeRequestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get")
    public ResponseEntity<List<PlanTypeResponseDto>> getAllPlanTypes(){
       System.out.println("request for fetching the plan type");
        List<PlanTypeResponseDto> planTypeResponseDto = planTypeService.getAllPlanTypes();
        return  ResponseEntity.ok(planTypeResponseDto);
    }

}
