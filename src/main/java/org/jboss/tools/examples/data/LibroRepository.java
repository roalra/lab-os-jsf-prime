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
package org.jboss.tools.examples.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.tools.examples.model.Libro;

@ApplicationScoped
public class LibroRepository {

    @Inject
    private EntityManager em;

    public Libro findById(Long id) {
        return em.find(Libro.class, id);
    }

    public Libro findByName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Libro> criteria = cb.createQuery(Libro.class);
        Root<Libro> Libro = criteria.from(Libro.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Libro).where(cb.equal(Libro.get(Libro_.name), email));
        criteria.select(Libro).where(cb.equal(Libro.get("name"), name));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Libro> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Libro> criteria = cb.createQuery(Libro.class);
        Root<Libro> Libro = criteria.from(Libro.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(Libro).orderBy(cb.asc(Libro.get(Libro_.name)));
        criteria.select(Libro).orderBy(cb.asc(Libro.get("name")));
        return em.createQuery(criteria).getResultList();
    }
}
