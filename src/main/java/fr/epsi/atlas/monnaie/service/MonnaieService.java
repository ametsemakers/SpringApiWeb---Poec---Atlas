package fr.epsi.atlas.monnaie.service;

import java.math.BigDecimal;
// import java.util.Iterator;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.epsi.atlas.monnaie.entity.Monnaie;
import fr.epsi.atlas.monnaie.entity.Montant;
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
	
	@Transactional
	public Monnaie create(String codeMonnaie, BigDecimal tauxDeChange) {
		Monnaie monnaie = new Monnaie(codeMonnaie, tauxDeChange);
		return monnaieRepository.save(monnaie);
	}
	
	@Transactional
	public void deleteByCode(String codeMonnaie) {
		monnaieRepository.deleteById(codeMonnaie);
		//Monnaie monnaie = monnaieRepository.findById(codeMonnaie).get();
		//monnaieRepository.delete(monnaie);
	}

	@Transactional
	public Monnaie modify(String codeMonnaie, BigDecimal tauxDeChange) throws MonnaieInexistanteException{	
		try {
			Monnaie mmonnaie = this.getByCode(codeMonnaie);
			mmonnaie.setTauxDeChange(tauxDeChange);
			return mmonnaie;
			
		} catch (MonnaieInexistanteException e) {
			throw new MonnaieInexistanteException();
		}
	}
	
	public Montant convert(Montant montant, Monnaie monnaie) {
			montant.setMontant(montant.getMontant().multiply(monnaie.getTauxDeChange()));
			return montant;
	}
}
