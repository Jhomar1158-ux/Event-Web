package com.jhomar.ProjectEvent.servicios;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.jhomar.ProjectEvent.modelos.Event;
import com.jhomar.ProjectEvent.modelos.LoginUser;
import com.jhomar.ProjectEvent.modelos.Message;
import com.jhomar.ProjectEvent.modelos.User;
import com.jhomar.ProjectEvent.repositorios.EventRepository;
import com.jhomar.ProjectEvent.repositorios.MessageRepository;
import com.jhomar.ProjectEvent.repositorios.UserRepository;

@Service
public class AppService {
	
	@Autowired
	private UserRepository repositorio_user;
	
	@Autowired
	private MessageRepository repositorio_message;
	
	@Autowired
	private EventRepository repositorio_event;
	
	public User register(User nuevoUsuario, BindingResult result) {
			
			String nuevoEmail = nuevoUsuario.getEmail();
			
			//Revisamos si existe el correo electrónico en BD
			if(repositorio_user.findByEmail(nuevoEmail).isPresent()) {
				result.rejectValue("email", "Unique", "El correo fue ingresado previamente.");
			}
			
			if(! nuevoUsuario.getPassword().equals(nuevoUsuario.getConfirm()) ) {
				result.rejectValue("confirm", "Matches", "Las contraseñas no coiniciden");
			}
			
			if(result.hasErrors()) {
				return null;
			} else {
				//Encriptamos contraseña
				String contra_encr = BCrypt.hashpw(nuevoUsuario.getPassword(), BCrypt.gensalt());
				nuevoUsuario.setPassword(contra_encr);
				//Guardo usuario
				return repositorio_user.save(nuevoUsuario);
			}
			
	}

	public User login(LoginUser nuevoLogin, BindingResult result) {
		
		if(result.hasErrors()) {
			return null;
		}
		
		//Buscamos por correo
		Optional<User> posibleUsuario = repositorio_user.findByEmail(nuevoLogin.getEmail());
		if(!posibleUsuario.isPresent()) {
			result.rejectValue("email", "Unique", "Correo ingresado no existe");
			return null;
		}
		
		User user_login = posibleUsuario.get();
		
		//Comparamos contraseñas encriptadas
		if(! BCrypt.checkpw(nuevoLogin.getPassword(), user_login.getPassword()) ) {
			result.rejectValue("password", "Matches", "Contraseña inválida");
		}
		
		if(result.hasErrors()) {
			return null;
		} else {
			return user_login; 
		}
		
	}
	
	public User find_user(Long id) {
		Optional<User> optionalUser = repositorio_user.findById(id);
        if(optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            return null;
        }
	}
	
	public Event find_event(Long id) {
		Optional<Event> optionalEvent= repositorio_event.findById(id);
        if(optionalEvent.isPresent()) {
            return optionalEvent.get();
        } else {
            return null;
        }
	}
	
	public void delete_event(Long id) {
		repositorio_event.deleteById(id);
	}
	
	public void save_event(Event thisEvent) {
		repositorio_event.save(thisEvent);
	}
	
	//Creamos un método que devuelva una lista de eventos de mi estado
	public List<Event> eventos_enmi_estado(String state) {
		return repositorio_event.findByState(state);
	}
	
	//Creamos un método que devuelva una lista de eventos que no están en mi estado
	public List<Event> eventos_noenmi_estado(String state){
		return repositorio_event.findByStateIsNot(state);
	}
	
	public void remove_event_user(Long user_id, Long event_id) {
		
		User myUser = find_user(user_id);
		Event myEvent = find_event(event_id);
		
		//Removemos myEvent de mi lista de Eventos a los que asistiré
		myUser.getEventsAttending().remove(myEvent);
		repositorio_user.save(myUser);
		
	}
	
	public void save_event_user(Long user_id, Long event_id) {
		User myUser = find_user(user_id);
		Event myEvent = find_event(event_id);
		
		myUser.getEventsAttending().add(myEvent);
		repositorio_user.save(myUser);
	}
	
	public void save_message(Message thisMessage) {
		repositorio_message.save(thisMessage);
	}
	
	
	
}
