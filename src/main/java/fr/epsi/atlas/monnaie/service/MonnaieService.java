package fr.epsi.atlas.monnaie.service;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.epsi.atlas.monnaie.entity.Monnaie;
import fr.epsi.atlas.monnaie.exception.MonnaieInexistanteException;
import fr.epsi.atlas.monnaie.repository.MonnaieRepository;

@Service
public class MonnaieService {

	@Autowired
	private MonnaieRepository monnaieRepository;
	
	public Monnaie getByCode(String codeMonnaie) throws MonnaieInexistanteException {
		Optional<Monnaie> optionalMonnaie = monnaieRepository.findById(codeMonnaie);
		if (!optionalMonnaie.isPresent()) {
			throw new MonnaieInexistanteException();
		}
		return optionalMonnaie.get();
	}
	
	public Iterable<Monnaie> getAll() {
		return monnaieRepository.findAll();
	}
	
	public void create(Monnaie monnaie) {
		monnaieRepository.save(monnaie);
	}
	
	@Transactional
	public void deleteByCode(String codeMonnaie) {
		monnaieRepository.deleteById(codeMonnaie);
		//Monnaie monnaie = monnaieRepository.findById(codeMonnaie).get();
		//monnaieRepository.delete(monnaie);
	}

	@Transactional
	public Monnaie modify(String codeMonnaie, BigDecimal tauxDeChange) throws MonnaieInexistanteException {
		
		Iterable<Monnaie> listMonnaie = monnaieRepository.findAll();
		Iterator<Monnaie> it = listMonnaie.iterator();
		while (it.hasNext()) {
			Monnaie tempMonnaie = it.next();
			if (tempMonnaie.getCode() == codeMonnaie) {
				tempMonnaie.setTauxDeChange(tauxDeChange);
				return tempMonnaie;
			}
		}
		Monnaie monnaie = new Monnaie();
		monnaie.setCode(codeMonnaie);
		monnaie.setTauxDeChange(tauxDeChange);
		this.create(monnaie);
		return monnaie;
	}
}
