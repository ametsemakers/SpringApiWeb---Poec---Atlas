package fr.epsi.atlas.monnaie.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Monnaie {

	/**
	 * le code ISO-4217
	 */
	@Id
	@Column(length = 3)
	@NotNull()
	private String code;
	@NotNull(message = "Le taux de change doit être positif")
	@Min(value = 0, message = "Le taux de change doit être positif")
	private BigDecimal tauxDeChange;
	
	public Monnaie() {}
	
	public Monnaie(String code, BigDecimal tauxDeChange) {
		this.code = code;
		this.tauxDeChange = tauxDeChange;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public BigDecimal getTauxDeChange() {
		return tauxDeChange;
	}
	public void setTauxDeChange(BigDecimal tauxDeChange) {
		this.tauxDeChange = tauxDeChange;
	}
	
	public BigDecimal convert(BigDecimal montant) {
		return montant.multiply(this.tauxDeChange);
	}
}
