package com.Financial_backend.Financial_backend.Service;


import com.Financial_backend.Financial_backend.Dto.Request.PlanTypeRequestDto;
import com.Financial_backend.Financial_backend.Dto.Response.PlanTypeResponseDto;
import com.Financial_backend.Financial_backend.Entity.PlanTypeEntity;
import com.Financial_backend.Financial_backend.Respository.PlanTypeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class PlanTypeService {

    private final PlanTypeRepository planTypeRepository;


    public PlanTypeService(PlanTypeRepository planTypeRepository){
        this.planTypeRepository=planTypeRepository;
    }


   public PlanTypeResponseDto createPlan(PlanTypeRequestDto planTypeRequestDto){

        PlanTypeEntity planTypeEntity = new PlanTypeEntity();

        planTypeEntity.setPlan_type(planTypeRequestDto.getPlanType());

      PlanTypeEntity saved= planTypeRepository.save(planTypeEntity);

        PlanTypeResponseDto planTypeResponseDto = new PlanTypeResponseDto();

        planTypeResponseDto.setPlanType(saved.getPlan_type());

        return planTypeResponseDto;

   }


   // getting all plan types

   public List <PlanTypeResponseDto> getAllPlanTypes(){
        List<PlanTypeEntity> planTypeEntities = planTypeRepository.findAll();

        //convert list<Entity to List<ResponseDto>>
       return planTypeEntities.stream()
               .map(this::convertToResponseDto)
               .toList();
   }

    // ====================== HELPER CONVERSION METHOD ======================
    private PlanTypeResponseDto convertToResponseDto(PlanTypeEntity entity) {
        PlanTypeResponseDto dto = new PlanTypeResponseDto();

        dto.setId(entity.getId());
        dto.setPlanType(entity.getPlan_type());   // Make sure this getter exists in your Entity

        return dto;
    }


}
