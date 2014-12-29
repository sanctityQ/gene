package org.one.gene.domain;


import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerProductValue;

public interface CalculatePrimerValue {

    PrimerProductValue create(PrimerProduct primerProduct);
}
