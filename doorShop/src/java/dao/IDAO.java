/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface IDAO<T, K> {
    boolean create(T entity);
    T getById(K id);
    List<T> getByName(String name);
    List<T> getAll();
}