package fr.epsi.atlas.monnaie.web;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.util.UriComponentsBuilder;

import fr.epsi.atlas.monnaie.dto.TauxDeChangeDto;
import fr.epsi.atlas.monnaie.entity.Monnaie;
import fr.epsi.atlas.monnaie.entity.Montant;
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
	public ResponseEntity<Monnaie> create(@RequestBody Monnaie monnaie,
						UriComponentsBuilder uriBuilder) {
		try {
			monnaieService.getByCode(monnaie.getCode());
		} catch (MonnaieInexistanteException e) {
			
			monnaieService.create(monnaie.getCode(), monnaie.getTauxDeChange());
			URI uri = uriBuilder.path("/monnaie/{codeMonnaie}").buildAndExpand(monnaie.getCode()).toUri();
			return ResponseEntity.created(uri).body(monnaie);
		}
		return ResponseEntity.status(409).body(monnaie);
		
	}
	
	@PostMapping("/{codeMonnaie}/EUR")
	public ResponseEntity<Montant> convert(@RequestBody Montant montant,
											@PathVariable String codeMonnaie) {
		// à redifinir sans créer le montant en base (vire Montant)
		try {
			Monnaie monnaie = monnaieService.getByCode(codeMonnaie);
			
			montant = monnaieService.convert(montant, monnaie);
			return ResponseEntity.status(200).body(montant);
			
		} catch (MonnaieInexistanteException e) {
			return ResponseEntity.status(400).body(montant);
		}
			
	}
	
	@PutMapping("/{codeMonnaie}")
	public ResponseEntity<Monnaie> update(@PathVariable String codeMonnaie, 
							@RequestBody TauxDeChangeDto tauxDeChangeDto,
							UriComponentsBuilder uriBuilder) {
		
		try {
			Monnaie monnaie = monnaieService.modify(codeMonnaie, tauxDeChangeDto.getTauxDeChange());
			
			Link lien = WebMvcLinkBuilder.linkTo(MonnaieController.class, codeMonnaie)
										.withRel("self")
										.withTitle("Mon super lien");
			return ResponseEntity.ok().header("Link", lien.toString()).body(monnaie);
		} catch (MonnaieInexistanteException e) {
			Monnaie monnaie = monnaieService.create(codeMonnaie, tauxDeChangeDto.getTauxDeChange());
			URI uri = uriBuilder.path("/monnaie/{codeMonnaie}").buildAndExpand(monnaie.getCode()).toUri();
			return ResponseEntity.created(uri).body(monnaie);
		}
		
	}
	
	@DeleteMapping("/monnaie/{codeMonnaie}")
	public void deleteMonnaie(@PathVariable String codeMonnaie) {
		monnaieService.deleteByCode(codeMonnaie);
	}
	
}
