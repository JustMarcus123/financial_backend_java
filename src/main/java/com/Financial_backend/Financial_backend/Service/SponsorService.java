package com.Financial_backend.Financial_backend.Service;

import com.Financial_backend.Financial_backend.Dto.Request.SponsorRequestDto;
import com.Financial_backend.Financial_backend.Dto.Response.SponsorResponseDto;
import com.Financial_backend.Financial_backend.Entity.SponsorEntity;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import com.Financial_backend.Financial_backend.Enum.Role;
import com.Financial_backend.Financial_backend.Enum.SponsorStatus;
import com.Financial_backend.Financial_backend.Respository.SponsorRepository;
import com.Financial_backend.Financial_backend.Respository.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SponsorService {

    private final SponsorRepository sponsorRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private  EmailService emailService;

    public SponsorService (SponsorRepository sponsorRepository, UsersRepository usersRepository , PasswordEncoder passwordEncoder, EmailService emailService){
        this.sponsorRepository = sponsorRepository;
        this.usersRepository= usersRepository;
        this.passwordEncoder= passwordEncoder;
        this.emailService= emailService;
    }


    // create new sponsor

                    //1.  method declaration
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

      String enrollCode = sponsorRequestDto.getCompany_name()
              .replaceAll("","")
              .toUpperCase()
              .substring(0, Math.min(10, sponsorRequestDto.getCompany_name().length()))
              +"_" + LocalDateTime.now().getYear()
              +"_" + UUID.randomUUID().toString().substring(0,6).toUpperCase();

      sponsorEntity .setEnrollmentCode(enrollCode);

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

      sponsorResponseDto.setId(sponsorEntity.getId());
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
      sponsorResponseDto.setSponsorStatus(String.valueOf(sponsorEntity.getSponsorStatus())); //in case if the data is not type(string,numbers, int) then we use 'valueof'

      return sponsorResponseDto;
  }



  // updating the sponsor

    public SponsorResponseDto updateSponsor(Long id,   SponsorRequestDto sponsorRequestDto){

       // find this particular requested sponsor through the id
     SponsorEntity existingSponsor = sponsorRepository.findById(id)
             .orElseThrow(()->new RuntimeException("Sponsor not found with if"+ id));


     //update only the field that are provided, we update the data only if it's not null

     if(sponsorRequestDto.getCompany_name() !=null)  {
         existingSponsor.setCompany_name(sponsorRequestDto.getCompany_name());
     }

     if(sponsorRequestDto.getEin() !=null){
         existingSponsor.setEin(sponsorRequestDto.getEin());
     }

        if(sponsorRequestDto.getPlan_type() !=null){
            existingSponsor.setPlan_type(sponsorRequestDto.getPlan_type());
        }
        if(sponsorRequestDto.getMatch_formula() !=null){
            existingSponsor.setMatch_formula(sponsorRequestDto.getMatch_formula());
        }

        if(sponsorRequestDto.getVesting_schedule() !=null){
            existingSponsor.setVesting_schedule(sponsorRequestDto.getVesting_schedule());
        }
        if(sponsorRequestDto.getSafe_harbour_plan() !=null){
            existingSponsor.setSafe_harbour_plan(sponsorRequestDto.getSafe_harbour_plan());
        }
        if(sponsorRequestDto.getPlan_start_date() !=null){
            existingSponsor.setPlan_start_date(sponsorRequestDto.getPlan_start_date());
        }
        if(sponsorRequestDto.getPrimaryContactName() !=null){
            existingSponsor.setPrimaryContactName(sponsorRequestDto.getPrimaryContactName());
        }
        if(sponsorRequestDto.getPrimaryContactEmail() !=null){
            existingSponsor.setPrimaryContactEmail(sponsorRequestDto.getPrimaryContactEmail());
        }
        if(sponsorRequestDto.getPrimary_contact_phone() !=null){
            existingSponsor.setPrimary_contact_phone(sponsorRequestDto.getPrimary_contact_phone());
        }
        if(sponsorRequestDto.getAddressLine1() !=null){
            existingSponsor.setAddressLine1(sponsorRequestDto.getAddressLine1());
        }
        if(sponsorRequestDto.getCity() !=null){
            existingSponsor.setCity(sponsorRequestDto.getCity());
        }
        if(sponsorRequestDto.getState() !=null){
            existingSponsor.setState(sponsorRequestDto.getState());
        }
        if(sponsorRequestDto.getZipcode() !=null){
            existingSponsor.setZipcode(sponsorRequestDto.getZipcode());
        }

        if(sponsorRequestDto.getCountry() !=null){
            existingSponsor.setCountry(sponsorRequestDto.getCountry());
        }

      SponsorEntity sponsorEntity = sponsorRepository.save(existingSponsor);

         //convert entity to response dto manually

        return convertToResponseDto(sponsorEntity);

    }

    @Transactional
    public void activateSponsor(Long sponsorId) {

        // 1. Find the sponsor
        SponsorEntity sponsor = sponsorRepository.findById(sponsorId)
                .orElseThrow(() -> new RuntimeException(
                        "Sponsor not found with id: " + sponsorId));

        // 2. Check it is currently DRAFT
        if (sponsor.getSponsorStatus() != SponsorStatus.DRAFT) {
            throw new RuntimeException(
                    "Sponsor is already " + sponsor.getSponsorStatus() +
                            " — can only activate DRAFT sponsors");
        }

        // 3. Check if employer user already exists for this email
        if (usersRepository.existsByEmail(sponsor.getPrimaryContactEmail())) {
            throw new RuntimeException(
                    "A user already exists with email: " +
                            sponsor.getPrimaryContactEmail());
        }

        // 4. Generate temporary password
        String tempPassword = generateTempPassword();

        // 5. Create the EMPLOYER_ADMIN user
        UsersEntity employerUser = UsersEntity.builder()
                .email(sponsor.getPrimaryContactEmail())
                .password_hash(passwordEncoder.encode(tempPassword))
                .firstName(getFirstName(sponsor.getPrimaryContactName()))
                .lastName(getLastName(sponsor.getPrimaryContactName()))
                .phone(sponsor.getPrimary_contact_phone())
                .role(Role.EMPLOYER_ADMIN)
                .sponsor(sponsor)
                .is_active(true)
                .mfa_enable(false)
                .email_varified(false)
                .build();
        usersRepository.save(employerUser);

        // 6. Update sponsor status to ACTIVE
        sponsor.setSponsorStatus(SponsorStatus.ACTIVE);
        sponsor.setOnboarded_date(LocalDateTime.now());
        sponsor.setUpdatedAt(LocalDateTime.now());
        sponsorRepository.save(sponsor);

        // 7. Send welcome email with temp password
        // NOTE: plain tempPassword sent in email — NOT the hash
        emailService.sendEmployerWelcomeEmail(
                sponsor.getPrimaryContactEmail(),
                sponsor.getPrimaryContactName(),
                sponsor.getCompany_name(),
                tempPassword
        );
    }

    // ── HELPERS ──────────────────────────────────────────────

    private String generateTempPassword() {
        // Generates something like "Temp@A3kP"
        String upper   = "ABCDEFGHJKLMNPQRSTUVWXYZ";
        String lower   = "abcdefghjkmnpqrstuvwxyz";
        String digits  = "23456789";

        return "Temp@"
                + upper.charAt((int)(Math.random() * upper.length()))
                + digits.charAt((int)(Math.random() * digits.length()))
                + lower.charAt((int)(Math.random() * lower.length()))
                + upper.charAt((int)(Math.random() * upper.length()));
    }

    private String generateEnrollmentCode(String companyName) {
        String cleaned = companyName
                .replaceAll("[^a-zA-Z0-9]", "")
                .toUpperCase()
                .substring(0, Math.min(8, companyName.length()));
        String random = UUID.randomUUID()
                .toString()
                .substring(0, 6)
                .toUpperCase();
        return cleaned + "-" + random;
        // e.g. "ACMECORP-A3F2B1"
    }

    private String getFirstName(String fullName) {
        if (fullName == null || !fullName.contains(" ")) return fullName;
        return fullName.substring(0, fullName.indexOf(" "));
    }

    private String getLastName(String fullName) {
        if (fullName == null || !fullName.contains(" ")) return "";
        return fullName.substring(fullName.indexOf(" ") + 1);
    }




}
