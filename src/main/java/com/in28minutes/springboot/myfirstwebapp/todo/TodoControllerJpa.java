package com.in28minutes.springboot.myfirstwebapp.todo;


import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("name")
public class TodoControllerJpa {
		
	public TodoControllerJpa(TodoService todoService, TodoRepository todoRepository) {
		super();
		this.todoRepository = todoRepository;
	}
	
	private TodoRepository todoRepository;
	
	// GET LIST
	@RequestMapping("list-todos")
	public String listAllTodos(ModelMap model) {
		String username = getLoggedInUsername(model);
		
		List<Todo> todos = todoRepository.findByUsername(username);
		model.addAttribute("todos", todos);
		
		return "listTodos";
	}
	
	// DELETE
	@RequestMapping("delete-todo")
	public String deleteTodo(@RequestParam int id) {
		// Delete Service
		todoRepository.deleteById(id);
//		todoService.deleteById(id);
		
		return "redirect:list-todos";
	} 
	
	// GET UPDATE
	@RequestMapping(value="update-todo", method=RequestMethod.GET)
	public String updateTodo(@RequestParam int id, ModelMap model) {
		// Delete Service
		Todo todo = todoRepository.findById(id).get();
		model.addAttribute("todo", todo);
		
		return "todo";
	}	
	
	// UPDATE POST
	@RequestMapping(value="update-todo", method=RequestMethod.POST)
	public String updateTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
		
		if(result.hasErrors()) {
			return "todo";
		}
		
		String username = getLoggedInUsername(model);
		todo.setUsername(username);
		todoRepository.save(todo);
//		todoService.updateTodo(todo);
		return "redirect:list-todos";
	}			
	
	// GET ADD-TODOS
	@RequestMapping(value="add-todo", method=RequestMethod.GET)
	public String showNewTodoPage(ModelMap model) {
		
		String username = getLoggedInUsername(model);
		Todo todo = new Todo(0, username, "", LocalDate.now().plusYears(1) ,false);
		model.put("todo", todo);
		
		return "todo";
	}
	
	// POST ADD-TODOS
	@RequestMapping(value="add-todo", method=RequestMethod.POST)
	public String addNewTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
		
		if(result.hasErrors()) {
			return "todo";
		}
		String username = getLoggedInUsername(model);
		todo.setUsername(username);
		todoRepository.save(todo);
//		todoService.addTodo(todo.getUsername(), todo.getDescription(), todo.getTargetDate(), todo.isDone());
		return "redirect:list-todos";
	}
	
	private String getLoggedInUsername(ModelMap model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}	

	
}
 