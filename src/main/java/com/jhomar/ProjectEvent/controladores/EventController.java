package com.jhomar.ProjectEvent.controladores;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jhomar.ProjectEvent.modelos.Event;
import com.jhomar.ProjectEvent.modelos.Message;
import com.jhomar.ProjectEvent.modelos.State;
import com.jhomar.ProjectEvent.modelos.User;
import com.jhomar.ProjectEvent.servicios.AppService;

@Controller
@RequestMapping("/events")
public class EventController {
	
	@Autowired
	private AppService servicio;
	
	@PostMapping("/create")
	public String create_event(@Valid @ModelAttribute("event") Event event,
							   BindingResult result, Model model, HttpSession session) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session");
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		if(result.hasErrors()) {
			
			User myUser = servicio.find_user(currentUser.getId());
			
			model.addAttribute("states", State.States);
			model.addAttribute("user", myUser);
			
			return "dashboard.jsp";
		}
		
		servicio.save_event(event);
		return "redirect:/dashboard";
	}
	
	@DeleteMapping("/delete/{event_id}")
	public String delete_event(@PathVariable("event_id") Long event_id,
								HttpSession session) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session");
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		Event evento = servicio.find_event(event_id);
		
		//Creamos una validación
		if(evento == null || !evento.getPlanner().getId().equals(currentUser.getId())) {
			return "redirect:/dashboard";
		}
		
		servicio.delete_event(event_id);
		
		return "redirect:/dashboard";
		
		
	}
	
	//PASO 1 para EDITAR
	@GetMapping("/edit/{event_id}")
	public String edit_event(@PathVariable("event_id") Long event_id, 
							HttpSession session,
							Model model) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session");
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		Event evento = servicio.find_event(event_id);
		
		if(evento == null || !evento.getPlanner().getId().equals(currentUser.getId())) {
			return "redirect:/dashboard";
		}
		
		model.addAttribute("evento",evento);
		model.addAttribute("states", State.States);
		
		return "editEvent.jsp";
	}
	//PASO 2 PARA EDITAR
	@PutMapping("/update")
	public String update_event(@Valid @ModelAttribute("evento") Event evento,
								BindingResult result, HttpSession session,
								Model model) {
		if(result.hasErrors()) {
			model.addAttribute("states", State.States);
			return "editEvent.jsp";
		}
		
		//Ubicamos nuestro evento a través de su id
		Event thisEvent = servicio.find_event(evento.getId());
		//thisEvent -> Evento seleccionado para Editar
		
		//evento-> Evento Editado enviado del Formulario
		
		//Quiero que me agreges a mi nueva Lista de Usuarios la Lista del anterior Evento
		evento.setAttendees(thisEvent.getAttendees());
		
		//Guardamos nuestro evento editado
		servicio.save_event(evento);
		
		
		return "redirect:/dashboard";
		
	}
	
	//RELACIÓN MANY TO MANY =================
	@GetMapping("/remove/{event_id}")
	public String remove_event(@PathVariable("event_id") Long event_id, 
								HttpSession session) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session");
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		servicio.remove_event_user(currentUser.getId(), event_id);
		
		return "redirect:/dashboard";
		
	}
	
	@GetMapping("/join/{event_id}")
	public String join_event(@PathVariable("event_id") Long event_id,
							HttpSession session) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session");
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		servicio.save_event_user(currentUser.getId(), event_id);
		
		return "redirect:/dashboard";
	}
	
	//===========================
	
	@GetMapping("/{event_id}")
	public String show_event(@PathVariable("event_id") Long event_id,
							HttpSession session,
							Model model,
							@ModelAttribute("message") Message message) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session");
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		Event event_edit = servicio.find_event(event_id);
		model.addAttribute("evento", event_edit);
		
		return "showEvent.jsp";
		
	}
	
	@PostMapping("/message")
	public String message(@Valid @ModelAttribute("message") Message message, 
							BindingResult result,
							HttpSession session, Model model) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session");
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		//
		Long event_id=message.getEvent().getId();
		Event event_edit= servicio.find_event(event_id);
		
		if(result.hasErrors()) {
			model.addAttribute("evento", event_edit);
			return "showEvent.jsp";
		}
		
		servicio.save_message(message);
		
		return "redirect:/events/"+event_id;
		
		
		
		
	}
	
	
	
	
	
	
	
	
}
