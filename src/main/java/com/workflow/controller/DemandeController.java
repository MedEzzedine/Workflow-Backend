package com.workflow.controller;

import com.workflow.entity.Demanderecu;
import com.workflow.entity.Demande;
import com.workflow.service.ActivityService;
import com.workflow.service.DemandeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/demande")
@CrossOrigin("*")
public class DemandeController {

    @Autowired
    private DemandeService demandeService;


    @PostMapping("/adddemande")
    public Demande adddemande(@RequestBody Demande d, HttpServletRequest request) {

        Demande demande = demandeService.adddemande(d, request);

        return demande;

    }

    @GetMapping("/getdemande")
    public Page<Demande> getalldemande(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size, @RequestParam(required = false) String search, HttpServletRequest request) {
        Pageable paging = PageRequest.of(page, size);

        return demandeService.getdemande(paging, search, request);

    }


    @GetMapping("/GetDemandeRecue")
    public Demanderecu getAll_demande1Byrole(HttpServletRequest request) {
        return demandeService.getdemandeTraitement(request);

    }


    /**
     * @PostMapping("/accept_refus_demande/{id}/{AcceptOrRefus}")
     * @ResponseBody public demande accept_refus_demande (@PathVariable int id ,HttpServletRequest request,@PathVariable Boolean AcceptOrRefus)
     * {
     * <p>
     * return demandeservice.accept_refus_demande(request,id,AcceptOrRefus);
     * }
     **/

    //@GetMapping("/getmydemande")
    //@ResponseBody
    // public List<demande> getAlldemandebyUser (HttpServletRequest request)
    //	{
    //return demandeservice.getAlldemandebyUser(request);
    //	}
    @GetMapping("/getalldemandehistory")
    public Demanderecu getAllDemandeHistory(HttpServletRequest request) {
        return demandeService.getAllDemandeHistory(request);
    }


    @PostMapping("/accept_refus_multidemande/{AcceptOrRefus}/{otherdemande}")
    public List<Demande> accept_refus_multidemande(@RequestBody List<Demande> demandes, HttpServletRequest request, @PathVariable Boolean AcceptOrRefus, @PathVariable boolean otherdemande) {

        return demandeService.accept_refus_multidemande(request, demandes, AcceptOrRefus, otherdemande);
    }


    @GetMapping("/getdemandedetail/{id}")
    public Demande demandedetail(@PathVariable int id) {
        return demandeService.demandedetail(id);
    }


    @GetMapping("/getfinaldemande")
    public List<Demande> getfinaldemande() {
        return demandeService.getfinaldemande();
    }


    @PutMapping("/refusfinal")
    public Demande getfinaldemande(@RequestBody Demande demande) {
        return demandeService.finalrefus(demande);
    }
}


	



