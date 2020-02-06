package fr.epsi.atlas.monnaie.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.epsi.atlas.monnaie.dto.TauxDeChangeDto;
import fr.epsi.atlas.monnaie.entity.Monnaie;
import fr.epsi.atlas.monnaie.exception.MonnaieInexistanteException;
import fr.epsi.atlas.monnaie.service.MonnaieService;

@RestController
@RequestMapping(path = "/monnaie")
public class MonnaieController {
	
	@Autowired MonnaieService monnaieService;
	
	@ExceptionHandler(MonnaieInexistanteException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public String handleException(MonnaieInexistanteException e) {
		return "La monnaie n'existe pas";
	}

	@GetMapping("/{codeMonnaie}")
	public Monnaie getByCode(@PathVariable String codeMonnaie) throws MonnaieInexistanteException {
		return monnaieService.getByCode(codeMonnaie);
	}
	
	@GetMapping
	public Iterable<Monnaie> getAll() {
		return monnaieService.getAll();
	}
	
	@PostMapping
	public void create(@RequestBody Monnaie monnaie) {
		monnaieService.create(monnaie);	
	}
	
	@PutMapping("/{codeMonnaie}")
	public Monnaie update(@PathVariable String codeMonnaie, 
							@RequestBody TauxDeChangeDto tauxDeChangeDto) throws MonnaieInexistanteException {
		return monnaieService.modify(codeMonnaie, tauxDeChangeDto.getTauxDeChange());
	}
	
	@DeleteMapping("/monnaie/{codeMonnaie}")
	public void deleteMonnaie(@PathVariable String codeMonnaie) {
		monnaieService.deleteByCode(codeMonnaie);
	}
	
}
