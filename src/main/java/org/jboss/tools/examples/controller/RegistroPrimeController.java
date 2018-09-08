/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.tools.examples.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
//import javax.inject.Named;

import org.jboss.tools.examples.data.MemberRepository;
import org.jboss.tools.examples.model.Member;
import org.jboss.tools.examples.service.MemberRegistration;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://www.cdi-spec.org/faq/#accordion6
@Model
@ManagedBean
@ViewScoped
public class RegistroPrimeController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private MemberRegistration memberRegistration;
    
    @Inject
    private MemberRepository memberRepo;
    
    private java.util.List<Member> memberLista;
    
    private Member newMember;


	@PostConstruct
    public void initNewMember() {
        newMember = new Member();
        consultarTodos();
    }
	
	public void consultarTodos() {
		memberLista=memberRepo.findAllOrderedByName();
	}

    public void register() throws Exception {
        try {
        	
        	if(newMember!=null 
        			&& newMember.getId()!=null) {
        		
        		actualizar();
        		return;
        		
        	}
        	
        	
        	if(newMember==null) {
        		FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error!", "error inesperado");
                facesContext.addMessage(null, m);
                return;
        	}
            memberRegistration.register(newMember);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful");
            facesContext.addMessage(null, m);
            initNewMember();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration unsuccessful");
            facesContext.addMessage(null, m);
        }
    }
    
    public void actualizar() throws Exception {
        try {
        	if(newMember==null && newMember.getId()!=null 
        			&& newMember.getEmail()!=null) {
        		
        		FacesMessage m = new FacesMessage(
        				FacesMessage.SEVERITY_ERROR, 
        				"error!", "error validando ");
                facesContext.addMessage(null, m);
                return;
        	}
        	
            memberRegistration.actualizar(newMember);
            FacesMessage m = new FacesMessage(
            		FacesMessage.SEVERITY_INFO, 
            		"Actualizando!", "Actualizacion successful");
            facesContext.addMessage(null, m);
            initNewMember();
                        
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration unsuccessful");
            facesContext.addMessage(null, m);
        }
    }
    
    /**
     * remover objeto memeber por id
     * @param idAEliminar
     * @throws Exception
     */
    public void remover(Long idAEliminar) throws Exception {
        try {
            memberRegistration.borrar(idAEliminar);
            consultarTodos();
            FacesMessage m = new FacesMessage
            		(FacesMessage.SEVERITY_INFO, "Eliminado!", 
            				"REliminado id: "+idAEliminar);
            
            facesContext.addMessage(null, m);
          
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
            		errorMessage, "Error eliminando un Memeber");
            facesContext.addMessage(null, m);
        }
    }


    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }
    

    public java.util.List<Member> getMemberLista() {
		return memberLista;
	}

	public void setMemberLista(java.util.List<Member> memberLista) {
		this.memberLista = memberLista;
	}
	

	public Member getNewMember() {
		return newMember;
	}

	public void setNewMember(Member newMember) {
		this.newMember = newMember;
	}


}
