package com.jhomar.ProjectEvent.controladores;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.jhomar.ProjectEvent.modelos.Event;
import com.jhomar.ProjectEvent.modelos.LoginUser;
import com.jhomar.ProjectEvent.modelos.State;
import com.jhomar.ProjectEvent.modelos.User;
import com.jhomar.ProjectEvent.servicios.AppService;

@Controller
public class UserController {
	//Importamos el Servicio
	@Autowired
	private AppService servicio;
	
	@GetMapping("/")
	public String index(@ModelAttribute("nuevoUsuario") User nuevoUsuario,
						@ModelAttribute("nuevoLogin") LoginUser nuevoLogin,
						Model model) {
		
		//Agregamos los opciones de State a nuestro formulario
		model.addAttribute("states", State.States);
		
		return "index.jsp";
	}
	
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("nuevoUsuario") User nuevoUsuario,
						   BindingResult result, Model model, HttpSession session) {
		
		servicio.register(nuevoUsuario, result);
		if(result.hasErrors()) {
			System.out.println(result);
			model.addAttribute("nuevoLogin", new LoginUser());
			model.addAttribute("states", State.States);
			return "index.jsp";
		}
		
		session.setAttribute("user_session", nuevoUsuario);
		return "redirect:/dashboard";
		
	}
	
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("nuevoLogin") LoginUser nuevoLogin,
						BindingResult result, Model model, HttpSession session) {
							
		User user = servicio.login(nuevoLogin, result);
		if(result.hasErrors()) {
			model.addAttribute("nuevoUsuario", new User());
			model.addAttribute("states", State.States);
			return "index.jsp";
		}
		
		session.setAttribute("user_session", user);
		return "redirect:/dashboard";
							
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("user_session");
		return "redirect:/";
	}
	
	@GetMapping("/dashboard")
	public String dashboard(HttpSession session, Model model, @ModelAttribute("event") Event event) {
		/*REVISAMOS SESION*/
		User currentUser = (User)session.getAttribute("user_session");
		if(currentUser == null) {
			return "redirect:/";
		}
		/*REVISAMOS SESION*/
		
		User myUser = servicio.find_user(currentUser.getId());
		model.addAttribute("user", myUser);
		model.addAttribute("states", State.States);
		
		//Lista de Eventos
		//Obtenemos el estado de mi usuario
		String miEstado = currentUser.getState();
		
		//Creamos una lista de Eventos que tengan mi estado
		List<Event> eventos_enmi_estado = servicio.eventos_enmi_estado(miEstado);
		//Creamos una lista de Eventos que no tengan mi estado 
		List<Event> eventos_noenmi_estado = servicio.eventos_noenmi_estado(miEstado);
		
		model.addAttribute("eventos_enmi_estado", eventos_enmi_estado);
		model.addAttribute("eventos_noenmi_estado", eventos_noenmi_estado);
		
		return "dashboard.jsp";
	}
	
	
	
}
