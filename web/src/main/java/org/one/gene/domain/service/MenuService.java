package org.one.gene.domain.service;

import java.util.List;

import org.one.gene.domain.entity.Menu;
import org.one.gene.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class MenuService {
	
	@Autowired
	private MenuRepository menuRepository;

	public List<Menu> getAllMenu() {
		return menuRepository.getAllMenu();
	}
	  
}
