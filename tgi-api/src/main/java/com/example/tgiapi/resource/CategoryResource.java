package com.example.tgiapi.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.tgiapi.model.Category;
import com.example.tgiapi.service.CategoryService;

@RestController
@RequestMapping("/categorias")
public class CategoryResource {
	
	@Autowired
	private CategoryService categoryService;

	/**
	 * <b> Lista: </b> de categorias
	 * @return Category: 
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public List<Category> get() {
		return this.categoryService.get();
	}
	
	/**
	 * * <b> Salvar: </b> uma categoria no banco
	 * @param category
	 * @param response
	 * @return Category: Retorna a categoria que foi salva com status 210 - CREATED
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Category> post(@Valid @RequestBody Category category, HttpServletResponse response) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.categoryService.post(category, response));
	}
	
	/**
	 * <b> Retorna: </b> uma categoria especifica pelo codigo informado ou 404 NO_FOUND caso o codigo seja invalido
	 */
	@GetMapping("/{categoryId}")
	public ResponseEntity<Category> getCategoryById(@PathVariable Long categoryId) {
		Category category = this.categoryService.getCategoryById(categoryId);
		return category != null ? ResponseEntity.ok(category) : ResponseEntity.notFound().build();
	}
	
	/**
	 * <b> Deleta: </b> uma cetegoria e retorna o status 204 - NO_CONTENT
	 * @param categoryId
	 */
	@DeleteMapping("/{categoryId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long categoryId) {
		this.categoryService.delete(categoryId);
	}
	
	/**
	 * Retorna a categoria com os valores atualizados
	 * @param categoryId
	 * @param category
	 * @return Category: atualizada
	 */
	@PutMapping("/{categoryId}")
	public ResponseEntity<Category> put(@PathVariable Long categoryId, @Valid @RequestBody Category category) {
		Category categorySaved = this.categoryService.update(categoryId, category);
		return ResponseEntity.ok(categorySaved);
	}
}
