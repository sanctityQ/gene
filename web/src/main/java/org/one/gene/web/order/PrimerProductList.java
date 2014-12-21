package org.one.gene.web.order;

import java.util.ArrayList;
import java.util.List;

import org.one.gene.domain.entity.PrimerProduct;

public class PrimerProductList {

	private List<PrimerProduct> primerProducts = new ArrayList<PrimerProduct>();

	public List<PrimerProduct> getPrimerProducts() {
		return primerProducts;
	}

	public void setPrimerProducts(List<PrimerProduct> primerProducts) {
		this.primerProducts = primerProducts;
	}
	
}
