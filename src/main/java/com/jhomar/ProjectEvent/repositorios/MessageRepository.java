package com.jhomar.ProjectEvent.repositorios;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jhomar.ProjectEvent.modelos.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long>{

}
