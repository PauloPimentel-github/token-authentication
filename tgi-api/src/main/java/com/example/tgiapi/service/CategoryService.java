package com.example.tgiapi.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.tgiapi.event.CreatedEventResource;
import com.example.tgiapi.model.Category;
import com.example.tgiapi.repository.CategoryRepository;


@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	//dispara o evento
	@Autowired
	private ApplicationEventPublisher publisher;
	
	public List<Category> get() {
		return this.categoryRepository.findAll();
	}
	
	public Category getCategoryById(Long categoryId) {
		Category category = this.categoryRepository.findOne(categoryId);
		return category;
	}
	
	public Category post(Category category, HttpServletResponse response) {
		Category categorySaved = this.categoryRepository.save(category);
		publisher.publishEvent(new CreatedEventResource(this, response, categorySaved.getCategoryId()));
		return categorySaved;
	}
	
	public void delete(Long categoryId) {
		this.categoryRepository.delete(categoryId);
	}

	public Category update(Long categoryId, Category category) {
		Category categorySaved = this.categoryRepository.findOne(categoryId);
		if (categorySaved == null) {
			throw new EmptyResultDataAccessException(1);
		}
		BeanUtils.copyProperties(category, categorySaved, "categoryId");
		return this.categoryRepository.save(categorySaved);
	}
}
